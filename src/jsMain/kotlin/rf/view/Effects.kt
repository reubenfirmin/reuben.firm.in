package rf.view

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.random.Random

/**
 * The site's client-side polish, all queried off typed [CssClasses] / [Attrs] markers: scroll-reveal
 * (+ metric count-up), the hero's decrypt-in text, magnetic CTAs, and card tilt. Everything degrades
 * to a static, final state under `prefers-reduced-motion`.
 */
object Effects {

    private const val GLYPHS = "abcdefghijklmnopqrstuvwxyz/_.:>#*01"

    fun installAll() {
        cycleTerminal()
        spotlight()
        projects()
        timeline()
        // Guarded: the constellation needs WebGL; without it (headless tests, old browsers) the rest
        // of the page must still render.
        try { SkillsGl.init() } catch (e: Throwable) { }
        magnetic()
    }

    // --- Experience timeline accordion: hovering / focusing a role expands its body ---------------
    private fun timeline() {
        val rowNodes = document.getElementsByClassName(CssClasses.TL_ROW)
        val rows = (0 until rowNodes.length)
            .mapNotNull { rowNodes.item(it) as? HTMLElement }
            .filter { it.querySelector(".${CssClasses.TL_DETAIL}") != null }
        if (rows.isEmpty()) return
        // Fractional CTO / CISO is the default-open row; the timeline reverts to it whenever the
        // pointer isn't on it, so panning into the pane always shows that role open.
        val defaultIdx = rows.indexOfFirst { (it.textContent ?: "").contains("Fractional CTO") }.coerceAtLeast(0)
        fun open(i: Int) {
            rows.forEachIndexed { j, r ->
                if (j == i) r.classList.add(CssClasses.IS_ACTIVE) else r.classList.remove(CssClasses.IS_ACTIVE)
            }
        }
        rows.forEachIndexed { i, r -> r.addEventListener("pointerenter", { _: Event -> open(i) }) }
        document.querySelector(".${CssClasses.TIMELINE}")?.addEventListener("pointerleave", { _: Event -> open(defaultIdx) })
        open(defaultIdx)
    }

    // --- Testimonial spotlight: auto-cycle one card at a time; each quote types in word-by-word ----
    private const val SPOTLIGHT_MS = 5200.0

    private fun spotlight() {
        val root = document.querySelector(".${CssClasses.SPOTLIGHT}") as? HTMLElement ?: return
        val cardNodes = root.getElementsByClassName(CssClasses.TESTIMONIAL_CARD)
        val cards = (0 until cardNodes.length).mapNotNull { cardNodes.item(it) as? HTMLElement }
        if (cards.isEmpty()) return
        val chipNodes = document.getElementsByClassName(CssClasses.SPOTLIGHT_CHIP)
        val chips = (0 until chipNodes.length).mapNotNull { chipNodes.item(it) as? HTMLElement }
        val fill = document.querySelector(".${CssClasses.SPOTLIGHT_PROGRESS_FILL}") as? HTMLElement

        // Stagger each card's words so they rise in sequence when the card becomes active.
        for (card in cards) {
            val words = card.getElementsByClassName(CssClasses.TESTIMONIAL_WORD)
            for (k in 0 until words.length) {
                (words.item(k) as? HTMLElement)?.style?.transitionDelay = "${k * 14}ms"
            }
        }

        var index = 0
        var now = 0.0
        var lastAdvance = 0.0
        var prev = 0.0
        var hovered = false

        fun show(i: Int) {
            cards.forEachIndexed { j, c ->
                if (j == i) c.classList.add(CssClasses.IS_ACTIVE) else c.classList.remove(CssClasses.IS_ACTIVE)
            }
            chips.forEachIndexed { j, c ->
                val accent = c.getAttribute(Attrs.ACCENT_HEX) ?: ""
                if (j == i) {
                    c.classList.add(CssClasses.IS_ACTIVE)
                    c.style.backgroundColor = accent
                    c.style.borderColor = accent
                    fill?.style?.backgroundColor = accent
                } else {
                    c.classList.remove(CssClasses.IS_ACTIVE)
                    c.style.backgroundColor = ""
                    c.style.borderColor = ""
                }
            }
            index = i
            lastAdvance = now
        }

        root.addEventListener("pointerenter", { _: Event -> hovered = true })
        root.addEventListener("pointerleave", { _: Event -> hovered = false })
        chips.forEachIndexed { j, chip -> chip.addEventListener("click", { _: Event -> show(j) }) }

        fun frame(ts: Double) {
            now = ts
            if (prev == 0.0) { prev = ts; lastAdvance = ts }
            val dt = ts - prev
            prev = ts
            if (hovered) {
                lastAdvance += dt // freeze the meter while the reader hovers
            } else {
                val elapsed = ts - lastAdvance
                fill?.style?.width = "${(elapsed / SPOTLIGHT_MS * 100.0).coerceAtMost(100.0)}%"
                if (elapsed >= SPOTLIGHT_MS) show((index + 1) % cards.size)
            }
            window.requestAnimationFrame(::frame)
        }
        show(0)
        window.requestAnimationFrame(::frame)
    }

    // --- Hero terminal: decrypt through each role, hold, advance to the next, loop -----------------
    private fun scrambled(target: String, p: Double): String {
        val reveal = (target.length * p).toInt()
        val sb = StringBuilder()
        for (k in target.indices) {
            val ch = target[k]
            if (k < reveal || ch == ' ' || ch == '/') sb.append(ch)
            else sb.append(GLYPHS[Random.nextInt(GLYPHS.length)])
        }
        return sb.toString()
    }

    private fun cycleTerminal() {
        val el = document.querySelector("[${Attrs.DECRYPT}]") as? HTMLElement ?: return
        val roles = rf.Content.terminalRoles
        if (roles.isEmpty()) return
        val scramble = 850.0
        val hold = 1500.0
        var idx = 0
        var phaseStart = -1.0
        fun frame(ts: Double) {
            if (phaseStart < 0.0) phaseStart = ts
            val elapsed = ts - phaseStart
            val current = roles[idx]
            when {
                elapsed < scramble -> el.textContent = scrambled(current, elapsed / scramble)
                elapsed < scramble + hold -> el.textContent = current
                else -> { idx = (idx + 1) % roles.size; phaseStart = ts }
            }
            window.requestAnimationFrame(::frame)
        }
        window.requestAnimationFrame(::frame)
    }

    // --- Magnetic CTAs ----------------------------------------------------------------------------
    private fun magnetic() {
        val els = document.getElementsByClassName(CssClasses.CTA)
        for (i in 0 until els.length) {
            val el = els.item(i) as? HTMLElement ?: continue
            el.addEventListener("pointermove", { e: Event ->
                val ev = e as MouseEvent
                val r = el.getBoundingClientRect()
                val dx = ev.clientX - (r.left + r.width / 2)
                val dy = ev.clientY - (r.top + r.height / 2)
                el.style.transform = "translate(${dx * 0.25}px, ${dy * 0.4}px)"
            })
            el.addEventListener("pointerleave", { _: Event -> el.style.transform = "translate(0px, 0px)" })
        }
    }

    // --- Projects accordion: hovering / focusing a panel expands it, collapsing the others --------
    private fun projects() {
        val panelNodes = document.getElementsByClassName(CssClasses.PROJECT_PANEL)
        val panels = (0 until panelNodes.length).mapNotNull { panelNodes.item(it) as? HTMLElement }
        if (panels.isEmpty()) return

        fun open(i: Int) {
            panels.forEachIndexed { j, p ->
                if (j == i) {
                    p.classList.add(CssClasses.IS_ACTIVE)
                    p.style.borderColor = p.getAttribute(Attrs.ACCENT_HEX) ?: ""
                } else {
                    p.classList.remove(CssClasses.IS_ACTIVE)
                    p.style.borderColor = ""
                }
            }
        }
        panels.forEachIndexed { i, p ->
            p.addEventListener("pointerenter", { _: Event -> open(i) })
            p.addEventListener("focus", { _: Event -> open(i) })
        }
        open(0)
    }
}
