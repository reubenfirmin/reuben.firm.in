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
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan
import kotlin.random.Random

/** The skills constellation, rendered with three.js: real 3D depth (fog + perspective) and crisp HTML
 *  labels via CSS2DRenderer (always facing the reader). The graph is one group we rotate directly:
 *  drag to spin freely, auto-rotating when idle; the front cluster reveals its labels. A search box
 *  rotates the globe to the strongest-matching cluster.
 *  A toggle swaps this canvas out entirely for a flat DOM chip-grid ([Skills.skills] renders both
 *  mount points; this object owns the show/hide + search wiring for both). */
object SkillsGl {

    private val BG = Tokens.BG.value.removePrefix("#").toInt(16)

    private class DomainGl(
        val nodeMat: MeshBasicMaterial, val lineMat: LineBasicMaterial, val leafLabels: MutableList<LabelGl>,
        val hub: Mesh, val dir: DoubleArray,
    )

    private class LabelGl(val obj: CSS2DObject, val inner: HTMLElement)

    fun init() {
        val stage = document.querySelector(".${SKILLS_STAGE}") as? HTMLElement ?: return
        var w = stage.clientWidth.toDouble()
        var h = stage.clientHeight.toDouble()
        if (w <= 0 || h <= 0) return

        val renderer = webGLRenderer(antialias = true, alpha = true)
        renderer.setPixelRatio(window.devicePixelRatio)
        renderer.setSize(w, h)
        renderer.setClearColor(BG, 0.0)
        renderer.domElement.className = SKILLS_CANVAS
        stage.appendChild(renderer.domElement)

        // Overlay must be click-through so drags reach the canvas.
        val labelRenderer = CSS2DRenderer()
        labelRenderer.setSize(w, h)
        labelRenderer.domElement.className = SKILLS_LABELS
        stage.appendChild(labelRenderer.domElement)

        val scene = Scene()
        val fovDeg = 48.0
        val camera = PerspectiveCamera(fovDeg, w / h, 0.1, 100.0)
        camera.lookAt(0.0, 0.0, 0.0)

        starfield(scene)

        val world = Group()
        scene.add(world)

        val leafGeo = SphereGeometry(0.05, 16, 16)
        val hubGeo = SphereGeometry(0.095, 20, 20)
        val nDomains = Content.skillDomains.size
        val rHub = 1.7; val rLeaf = 2.6; val cap = 0.52
        // Farthest a node (plus its label anchor) can sit from the origin; the camera is pulled back
        // far enough that this always stays inside the frustum, on any container aspect ratio.
        val maxExtent = rLeaf + 0.18
        var camDist = 6.3
        fun fitCamera() {
            // Exact tangent-line distance for a bounding sphere of radius maxExtent to fit inside a
            // half-angle theta cone (the tighter of the vertical/horizontal fov), plus a small margin.
            val halfV = fovDeg * PI / 180.0 / 2.0
            val aspect = w / h
            val theta = if (aspect >= 1.0) halfV else atan(tan(halfV) * aspect)
            camDist = (maxExtent / sin(theta)) * 1.05
            camera.position.set(0.0, 0.0, camDist)
            camera.aspect = aspect
            camera.updateProjectionMatrix()
            // Fog near/far track the camera distance so the sphere fades the same way at any zoom.
            scene.fog = Fog(BG, camDist - 0.8, camDist + 6.7)
        }
        fitCamera()
        val defaultIdx = Content.skillDomains.indexOfFirst { d -> d.skills.any { it.startsWith("Fractional CTO") } }.coerceAtLeast(0)

        val domains = mutableListOf<DomainGl>()
        val allLabels = mutableListOf<LabelGl>()

        // Core "RF" node; shared line origin for every domain's connector.
        val coreMat = MeshBasicMaterial().apply { color = Color("#000000"); transparent = true; opacity = 1.0 }
        val core = Mesh(SphereGeometry(0.2, 24, 24), coreMat)
        world.add(core)
        val coreLabel = makeLabel("RF", SK_CORE_LABEL)
        world.add(coreLabel.obj); allLabels += coreLabel

        // Hubs spread evenly over the sphere (Fibonacci); leaves fan around each hub.
        Content.skillDomains.forEachIndexed { i, d ->
            val hd = fibDir(i, nDomains)
            val hx = hd[0] * rHub; val hy = hd[1] * rHub; val hz = hd[2] * rHub
            val ref = if (abs(hd[1]) < 0.85) doubleArrayOf(0.0, 1.0, 0.0) else doubleArrayOf(1.0, 0.0, 0.0)
            val p = norm(cross(hd, ref)); val q = cross(hd, p)
            val k = d.skills.size

            val nodeMat = MeshBasicMaterial().apply { color = Color(d.color); transparent = true; opacity = 1.0 }
            val lineMat = LineBasicMaterial().apply { color = Color(d.color); transparent = true; opacity = 0.9 }
            val leafLabels = mutableListOf<LabelGl>()

            val hub = Mesh(hubGeo, nodeMat)
            hub.position.set(hx, hy, hz)
            world.add(hub)
            val ends = mutableListOf<Object3D>(core, hub)

            val hubLabel = makeLabel(d.name, SK_HUB_LABEL)
            hubLabel.obj.position.set(hd[0] * (rHub + 0.24), hd[1] * (rHub + 0.24), hd[2] * (rHub + 0.24))
            world.add(hubLabel.obj); allLabels += hubLabel

            d.skills.forEachIndexed { j, skill ->
                val az = j.toDouble() / k * 2.0 * PI + i * 0.7
                val polar = cap + if (j % 2 == 0) 0.14 else -0.05
                val sp = sin(polar); val cpm = cos(polar)
                val dx = hd[0] * cpm + (p[0] * cos(az) + q[0] * sin(az)) * sp
                val dy = hd[1] * cpm + (p[1] * cos(az) + q[1] * sin(az)) * sp
                val dz = hd[2] * cpm + (p[2] * cos(az) + q[2] * sin(az)) * sp

                val leaf = Mesh(leafGeo, nodeMat)
                leaf.position.set(dx * rLeaf, dy * rLeaf, dz * rLeaf)
                world.add(leaf)
                ends += hub; ends += leaf

                val label = makeLabel(skill, SK_LEAF_LABEL)
                label.obj.position.set(dx * (rLeaf + 0.18), dy * (rLeaf + 0.18), dz * (rLeaf + 0.18))
                label.obj.visible = false
                world.add(label.obj); leafLabels += label; allLabels += label
            }

            val lineGeo = BufferGeometry().setFromPoints(ends.map { Vector3(it.position.x, it.position.y, it.position.z) }.toTypedArray())
            world.add(LineSegments(lineGeo, lineMat))
            domains += DomainGl(nodeMat, lineMat, leafLabels, hub, hd)
        }

        fun activate(i: Int) {
            domains.forEachIndexed { j, dg ->
                dg.nodeMat.opacity = if (j == i) 1.0 else 0.28
                dg.lineMat.opacity = if (j == i) 0.9 else 0.14
                dg.leafLabels.forEach { it.obj.visible = j == i }
            }
        }
        var active = defaultIdx
        activate(active)

        var rotX = 0.0; var rotY = 0.0
        var dragging = false; var lastX = 0.0; var lastY = 0.0
        var searching = false; var targetX = 0.0; var targetY = 0.0
        var flat = false

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

        // Toggle swaps the 3D canvas for the flat chip-grid entirely (two sibling panels, one hidden).
        val toggle = document.querySelector(".${SKILLS_TOGGLE}") as? HTMLElement
        val grid = document.querySelector(".${SKILLS_GRID}") as? HTMLElement
        val cards = (grid?.querySelectorAll(".${SKILLS_CARD}")?.let { l -> (0 until l.length).mapNotNull { l.item(it) as? HTMLElement } }) ?: emptyList()
        toggle?.addEventListener("click", { _: Event ->
            flat = !flat
            stage.classList.toggle(SKILLS_HIDDEN, flat)
            grid?.classList?.toggle(SKILLS_HIDDEN, !flat)
            toggle.textContent = if (flat) "3D" else "2D"
        })

        // Search: rotates the globe to the strongest-matching cluster in 3D, or filters/dims the
        // chip-grid's cards in 2D.
        val searchInput = document.querySelector(".${SKILLS_SEARCH_INPUT}") as? org.w3c.dom.HTMLInputElement
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
        fun filterGrid(q: String) {
            cards.forEach { card ->
                val name = card.querySelector(".${SKILLS_CARD_NAME}")?.textContent?.lowercase() ?: ""
                val chips = card.querySelectorAll(".${SKILLS_CHIP}")
                var anyMatch = q.isEmpty() || name.contains(q)
                for (i in 0 until chips.length) {
                    val chip = chips.item(i) as? HTMLElement ?: continue
                    val hit = q.isNotEmpty() && (chip.textContent?.lowercase() ?: "").contains(q)
                    chip.classList.toggle(SKILLS_CHIP_MATCH, hit)
                    if (hit) anyMatch = true
                }
                card.classList.toggle(SKILLS_CARD_DIM, q.isNotEmpty() && !anyMatch)
            }
        }
        searchInput?.addEventListener("input", { _: Event ->
            val q = searchInput.value.trim().lowercase()
            if (flat) { filterGrid(q); return@addEventListener }
            val best = if (q.isEmpty()) -1 else bestDomain(q)
            if (best < 0) { searching = false; return@addEventListener }
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
            if (flat) return@animate

            when {
                dragging -> {}
                searching -> { rotX += (targetX - rotX) * 0.09; rotY += (targetY - rotY) * 0.09 }
                else -> rotY += 0.0022
            }
            world.rotation.set(rotX, rotY, 0.0)

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

            allLabels.forEach { l ->
                l.obj.getWorldPosition(tmp)
                val dx = tmp.x - cam.x; val dy = tmp.y - cam.y; val dz = tmp.z - cam.z
                val near = camDist - maxExtent; val far = camDist + maxExtent
                val t = ((sqrt(dx * dx + dy * dy + dz * dz) - near) / (far - near)).coerceIn(0.0, 1.0)
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
                fitCamera()
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
