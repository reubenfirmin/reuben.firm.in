package rf.view

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import rf.Content
import zoned.framework.gl.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/** The skills constellation, rendered with three.js: real 3D depth (fog + perspective) and crisp HTML
 *  labels via CSS2DRenderer (always facing the reader). The graph is one group we rotate directly —
 *  drag to spin freely (no pole lock), auto-rotating when idle; the front cluster reveals its labels.
 *  A toggle morphs between the 3D sphere and a flat 2D radial map (positions lerp, lines follow). A
 *  search box rotates the globe to the strongest-matching cluster. */
object SkillsGl {

    private val BG = Tokens.BG.value.removePrefix("#").toInt(16)

    private class DomainGl(
        val nodeMat: MeshBasicMaterial, val lineMat: LineBasicMaterial, val leafLabels: MutableList<LabelGl>,
        val hub: Mesh, val dir: DoubleArray,
    )

    private class LabelGl(val obj: CSS2DObject, val inner: HTMLElement)

    /** A node/label that lerps between its 3D-sphere position and its 2D-flat position by [morph]. */
    private class Morphable(
        val obj: Object3D,
        val sx: Double, val sy: Double, val sz: Double,
        val fx: Double, val fy: Double, val fz: Double,
    )

    /** A domain's connector line + the ordered node endpoints, so its geometry can follow the morph. */
    private class LineGl(val geo: BufferGeometry, val ends: List<Object3D>)

    fun init() {
        val stage = document.querySelector(".${CssClasses.SKILLS_STAGE}") as? HTMLElement ?: return
        var w = stage.clientWidth.toDouble()
        var h = stage.clientHeight.toDouble()
        if (w <= 0 || h <= 0) return

        val renderer = webGLRenderer(antialias = true, alpha = true)
        renderer.setPixelRatio(window.devicePixelRatio)
        renderer.setSize(w, h)
        renderer.setClearColor(BG, 0.0)
        renderer.domElement.className = CssClasses.SKILLS_CANVAS
        stage.appendChild(renderer.domElement)

        // Overlay must be click-through so drags reach the canvas.
        val labelRenderer = CSS2DRenderer()
        labelRenderer.setSize(w, h)
        labelRenderer.domElement.className = CssClasses.SKILLS_LABELS
        stage.appendChild(labelRenderer.domElement)

        val scene = Scene()
        scene.fog = Fog(BG, 5.5, 13.0)
        val camera = PerspectiveCamera(48.0, w / h, 0.1, 100.0)
        camera.position.set(0.0, 0.0, 6.3)
        camera.lookAt(0.0, 0.0, 0.0)

        starfield(scene)

        val world = Group()
        scene.add(world)

        val leafGeo = SphereGeometry(0.05, 16, 16)
        val hubGeo = SphereGeometry(0.095, 20, 20)
        val nDomains = Content.skillDomains.size
        val rHub = 1.7; val rLeaf = 2.6; val cap = 0.52
        val defaultIdx = Content.skillDomains.indexOfFirst { d -> d.skills.any { it.startsWith("Fractional CTO") } }.coerceAtLeast(0)

        // 2D layout is a left-to-right expanding tree: root · domains · leaves across three columns.
        val counts = Content.skillDomains.map { it.skills.size }
        val totalLeaves = counts.sum().coerceAtLeast(1)
        val offsets = IntArray(nDomains).also { var a = 0; for (i in 0 until nDomains) { it[i] = a; a += counts[i] } }
        val yTop = 2.6; val rootX = -2.7; val domainX = -0.7; val leafX = 1.5
        fun slotY(slot: Int) = if (totalLeaves <= 1) 0.0 else yTop - slot * (2.0 * yTop / (totalLeaves - 1))

        val domains = mutableListOf<DomainGl>()
        val allLabels = mutableListOf<LabelGl>()
        val morphs = mutableListOf<Morphable>()
        val lineGroups = mutableListOf<LineGl>()

        // Core (shared line origin; doesn't move between layouts).
        val coreMat = MeshBasicMaterial().apply { color = Color(Tokens.ACCENT_SOFT.value); transparent = true; opacity = 1.0 }
        val core = Mesh(SphereGeometry(0.2, 24, 24), coreMat)
        world.add(core)
        morphs += Morphable(core, 0.0, 0.0, 0.0, rootX, 0.0, 0.0)
        val coreLabel = makeLabel("RF", CssClasses.SK_CORE_LABEL)
        world.add(coreLabel.obj); allLabels += coreLabel
        morphs += Morphable(coreLabel.obj, 0.0, 0.0, 0.0, rootX, 0.26, 0.0)

        // Hubs spread evenly over the sphere (Fibonacci) in 3D, or on a ring in 2D; leaves fan around
        // each hub in both layouts.
        Content.skillDomains.forEachIndexed { i, d ->
            val hd = fibDir(i, nDomains)
            val hx = hd[0] * rHub; val hy = hd[1] * rHub; val hz = hd[2] * rHub
            val ref = if (abs(hd[1]) < 0.85) doubleArrayOf(0.0, 1.0, 0.0) else doubleArrayOf(1.0, 0.0, 0.0)
            val p = norm(cross(hd, ref)); val q = cross(hd, p)
            val k = d.skills.size
            val dY = (slotY(offsets[i]) + slotY(offsets[i] + k - 1)) / 2.0

            val nodeMat = MeshBasicMaterial().apply { color = Color(d.color); transparent = true; opacity = 1.0 }
            val lineMat = LineBasicMaterial().apply { color = Color(d.color); transparent = true; opacity = 0.9 }
            val leafLabels = mutableListOf<LabelGl>()

            val hub = Mesh(hubGeo, nodeMat)
            hub.position.set(hx, hy, hz)
            world.add(hub)
            morphs += Morphable(hub, hx, hy, hz, domainX, dY, 0.0)
            val ends = mutableListOf<Object3D>(core, hub)

            val hubLabel = makeLabel(d.name, CssClasses.SK_HUB_LABEL)
            hubLabel.obj.position.set(hd[0] * (rHub + 0.24), hd[1] * (rHub + 0.24), hd[2] * (rHub + 0.24))
            world.add(hubLabel.obj); allLabels += hubLabel
            morphs += Morphable(hubLabel.obj,
                hd[0] * (rHub + 0.24), hd[1] * (rHub + 0.24), hd[2] * (rHub + 0.24),
                domainX - 0.62, dY, 0.0)

            d.skills.forEachIndexed { j, skill ->
                val az = j.toDouble() / k * 2.0 * PI + i * 0.7
                val polar = cap + if (j % 2 == 0) 0.14 else -0.05
                val sp = sin(polar); val cpm = cos(polar)
                val dx = hd[0] * cpm + (p[0] * cos(az) + q[0] * sin(az)) * sp
                val dy = hd[1] * cpm + (p[1] * cos(az) + q[1] * sin(az)) * sp
                val dz = hd[2] * cpm + (p[2] * cos(az) + q[2] * sin(az)) * sp
                val ly = slotY(offsets[i] + j)

                val leaf = Mesh(leafGeo, nodeMat)
                leaf.position.set(dx * rLeaf, dy * rLeaf, dz * rLeaf)
                world.add(leaf)
                morphs += Morphable(leaf, dx * rLeaf, dy * rLeaf, dz * rLeaf, leafX, ly, 0.0)
                ends += hub; ends += leaf

                val label = makeLabel(skill, CssClasses.SK_LEAF_LABEL)
                label.obj.position.set(dx * (rLeaf + 0.18), dy * (rLeaf + 0.18), dz * (rLeaf + 0.18))
                label.obj.visible = false
                world.add(label.obj); leafLabels += label; allLabels += label
                morphs += Morphable(label.obj,
                    dx * (rLeaf + 0.18), dy * (rLeaf + 0.18), dz * (rLeaf + 0.18),
                    leafX + 0.5, ly, 0.0)
            }

            val lineGeo = BufferGeometry().setFromPoints(ends.map { Vector3(it.position.x, it.position.y, it.position.z) }.toTypedArray())
            world.add(LineSegments(lineGeo, lineMat))
            lineGroups += LineGl(lineGeo, ends)
            domains += DomainGl(nodeMat, lineMat, leafLabels, hub, hd)
        }

        fun activate(i: Int) {
            domains.forEachIndexed { j, dg ->
                dg.nodeMat.opacity = if (j == i) 1.0 else 0.28
                dg.lineMat.opacity = if (j == i) 0.9 else 0.14
                dg.leafLabels.forEach { it.obj.visible = j == i }
            }
        }
        fun showAll() {
            domains.forEach { dg ->
                dg.nodeMat.opacity = 1.0; dg.lineMat.opacity = 0.6
                dg.leafLabels.forEach { it.obj.visible = true }
            }
        }
        var active = defaultIdx
        activate(active)

        var rotX = 0.0; var rotY = 0.0
        var dragging = false; var lastX = 0.0; var lastY = 0.0
        var searching = false; var targetX = 0.0; var targetY = 0.0
        var morph = 0.0; var morphTarget = 0.0; var wasFlat = false

        renderer.domElement.addEventListener("pointerdown", { e ->
            val ev = e as MouseEvent; dragging = true; searching = false
            lastX = ev.clientX.toDouble(); lastY = ev.clientY.toDouble()
        })
        window.addEventListener("pointermove", { e ->
            if (!dragging) return@addEventListener
            val ev = e as MouseEvent
            rotY += (ev.clientX - lastX) * 0.006
            rotX += (ev.clientY - lastY) * 0.006
            lastX = ev.clientX.toDouble(); lastY = ev.clientY.toDouble()
        })
        window.addEventListener("pointerup", { _: Event -> dragging = false })

        // 2D / 3D morph toggle.
        val toggle = document.querySelector(".${CssClasses.SKILLS_TOGGLE}") as? HTMLElement
        toggle?.addEventListener("click", { _: Event ->
            morphTarget = if (morphTarget < 0.5) 1.0 else 0.0
            toggle.textContent = if (morphTarget > 0.5) "3D" else "2D"
        })

        // Search: typing rotates the globe (3D) or focuses (2D) the strongest-matching cluster.
        val searchInput = document.querySelector(".${CssClasses.SKILLS_SEARCH_INPUT}") as? org.w3c.dom.HTMLInputElement
        fun bestDomain(q: String): Int {
            var best = -1; var score = 0
            Content.skillDomains.forEachIndexed { i, dom ->
                var s = 0
                val nm = dom.name.lowercase()
                if (nm.startsWith(q)) s = maxOf(s, 3) else if (nm.contains(q)) s = maxOf(s, 1)
                dom.skills.forEach { sk ->
                    val skl = sk.lowercase()
                    if (skl.startsWith(q)) s = maxOf(s, 4) else if (skl.contains(q)) s = maxOf(s, 2)
                }
                if (s > score) { score = s; best = i }
            }
            return best
        }
        searchInput?.addEventListener("input", { _: Event ->
            val q = searchInput.value.trim().lowercase()
            val best = if (q.isEmpty()) -1 else bestDomain(q)
            if (best < 0) { searching = false; if (morphTarget > 0.5) showAll(); return@addEventListener }
            val d = domains[best].dir
            val baseY = -atan2(d[0], d[2])
            targetY = baseY + round((rotY - baseY) / (2 * PI)) * 2 * PI
            targetX = atan2(d[1], sqrt(d[0] * d[0] + d[2] * d[2]))
            searching = true; active = best; activate(best)
        })

        val cam = camera.position
        val tmp = Vector3()
        fun animate() {
            window.requestAnimationFrame { animate() }
            morph += (morphTarget - morph) * 0.09
            val animating = abs(morphTarget - morph) > 0.0008
            val flat = morphTarget > 0.5

            when {
                flat -> { rotX += (0.0 - rotX) * 0.1; rotY += (0.0 - rotY) * 0.1 }
                dragging -> {}
                searching -> { rotX += (targetX - rotX) * 0.09; rotY += (targetY - rotY) * 0.09 }
                else -> rotY += 0.0022
            }
            world.rotation.set(rotX, rotY, 0.0)

            morphs.forEach { m ->
                m.obj.position.set(m.sx + (m.fx - m.sx) * morph, m.sy + (m.fy - m.sy) * morph, m.sz + (m.fz - m.sz) * morph)
            }
            if (animating) lineGroups.forEach { lg ->
                lg.geo.setFromPoints(lg.ends.map { Vector3(it.position.x, it.position.y, it.position.z) }.toTypedArray())
            }

            if (flat) {
                if (!wasFlat) { showAll(); wasFlat = true }
            } else {
                wasFlat = false
                if (!searching) {
                    var best = Double.MAX_VALUE; var near = active
                    domains.forEachIndexed { i, dg ->
                        dg.hub.getWorldPosition(tmp)
                        val dx = tmp.x - cam.x; val dy = tmp.y - cam.y; val dz = tmp.z - cam.z
                        val d2 = dx * dx + dy * dy + dz * dz
                        if (d2 < best) { best = d2; near = i }
                    }
                    if (near != active) { active = near; activate(near) }
                }
            }

            allLabels.forEach { l ->
                l.obj.getWorldPosition(tmp)
                val dx = tmp.x - cam.x; val dy = tmp.y - cam.y; val dz = tmp.z - cam.z
                val t = ((sqrt(dx * dx + dy * dy + dz * dz) - 3.5) / (9.1 - 3.5)).coerceIn(0.0, 1.0)
                l.inner.setScale(1.18 - t * 0.56)
                l.inner.setOpacity(1.0 - t * 0.62)
            }
            renderer.render(scene, camera)
            labelRenderer.render(scene, camera)
        }
        animate()

        window.addEventListener("resize", { _: Event ->
            w = stage.clientWidth.toDouble(); h = stage.clientHeight.toDouble()
            if (w > 0 && h > 0) {
                camera.aspect = w / h; camera.updateProjectionMatrix()
                renderer.setSize(w, h); labelRenderer.setSize(w, h)
            }
        })
    }

    private fun makeLabel(text: String, cls: String): LabelGl {
        val outer = document.createElement("div") as HTMLElement
        val inner = document.createElement("span") as HTMLElement
        inner.className = cls
        inner.textContent = text
        outer.appendChild(inner)
        return LabelGl(CSS2DObject(outer), inner)
    }

    /** A faint field of stars surrounding the scene (fog-immune so distant ones still show). */
    private fun starfield(scene: Scene) {
        val count = 700
        val positions = FloatArray(count * 3)
        for (s in 0 until count) {
            val u = Random.nextDouble() * 2.0 - 1.0
            val th = Random.nextDouble() * 2.0 * PI
            val r = 24.0 + Random.nextDouble() * 42.0
            val sq = sqrt((1.0 - u * u).coerceAtLeast(0.0))
            positions[s * 3] = (r * sq * cos(th)).toFloat()
            positions[s * 3 + 1] = (r * u).toFloat()
            positions[s * 3 + 2] = (r * sq * sin(th)).toFloat()
        }
        val geo = BufferGeometry().setAttribute("position", Float32BufferAttribute(positions, 3))
        val mat = PointsMaterial().apply {
            size = 0.16; color = Color(Tokens.MUTED.value); transparent = true; opacity = 0.85; sizeAttenuation = true; fog = false
        }
        scene.add(Points(geo, mat))
    }

    private fun cross(a: DoubleArray, b: DoubleArray) = doubleArrayOf(
        a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0]
    )

    private fun norm(a: DoubleArray): DoubleArray {
        val m = sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2])
        return doubleArrayOf(a[0] / m, a[1] / m, a[2] / m)
    }

    private fun fibDir(i: Int, n: Int): DoubleArray {
        val y = 1.0 - (i + 0.5) / n * 2.0
        val r = sqrt((1.0 - y * y).coerceAtLeast(0.0))
        val t = PI * (3.0 - sqrt(5.0)) * i
        return doubleArrayOf(r * cos(t), y, r * sin(t))
    }
}
