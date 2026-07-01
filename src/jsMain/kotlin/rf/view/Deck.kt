package rf.view

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.WheelEvent
import kotlin.math.abs

/**
 * The rolodex. Sections are stacked full-screen panels; the current one stays put. Scrolling fills a
 * pressure meter, and at the threshold the next panel flips over the current (a quick rolodex flip).
 * Because there's no page scroll, the deck also drives the per-panel reveal + metric count-up.
 */
object Deck {

    private var panels: List<HTMLElement> = emptyList()
    private var dots: List<HTMLElement> = emptyList()
    private var meterFill: HTMLElement? = null
    private var index = 0
    private var pressure = 0.0
    private var lockUntil = 0.0
    private var now = 0.0
    private val revealed = mutableSetOf<HTMLElement>()
    private val counted = mutableSetOf<HTMLElement>()

    private const val STEP = 300.0      // wheel delta to fill the meter and trigger a flip
    private const val FLIP_MS = 680.0   // lock-out while the flip plays

    fun install() {
        val nodes = document.querySelectorAll(".${CssClasses.DECK} > .${CssClasses.SECTION}")
        panels = (0 until nodes.length).mapNotNull { nodes.item(it) as? HTMLElement }
        if (panels.isEmpty()) return
        val dotNodes = document.querySelectorAll(".${CssClasses.DECK_DOT}")
        dots = (0 until dotNodes.length).mapNotNull { dotNodes.item(it) as? HTMLElement }
        meterFill = document.querySelector(".${CssClasses.DECK_METER_FILL}") as? HTMLElement

        applyLayout()
        activate()

        window.addEventListener("wheel", { e -> onWheel(e as WheelEvent) }, js("({ passive: false })"))
        window.addEventListener("keydown", { e -> onKey(e as KeyboardEvent) })

        // Nav links / CTAs (data-scroll-target) jump the deck to that section.
        val targets = document.querySelectorAll("[${Attrs.SCROLL_TARGET}]")
        for (i in 0 until targets.length) {
            val el = targets.item(i) as? HTMLElement ?: continue
            el.addEventListener("click", { _: Event -> el.getAttribute(Attrs.SCROLL_TARGET)?.let(::goToId) })
        }
        // Per-card "next" chevrons advance the deck for people who don't scroll.
        val nextButtons = document.querySelectorAll(".${CssClasses.DECK_NEXT}")
        for (i in 0 until nextButtons.length) {
            val el = nextButtons.item(i) as? HTMLElement ?: continue
            el.addEventListener("click", { _: Event -> go(1) })
        }
        window.requestAnimationFrame(::loop)
    }

    private fun onWheel(e: WheelEvent) {
        e.preventDefault()
        if (now < lockUntil) return
        pressure = (pressure + e.deltaY).coerceIn(-STEP, STEP)
    }

    private fun onKey(e: KeyboardEvent) {
        when (e.key) {
            "ArrowDown", "PageDown" -> go(1)
            "ArrowUp", "PageUp" -> go(-1)
        }
    }

    private fun goToId(id: String) {
        val target = panels.indexOfFirst { it.id == id }
        if (target >= 0 && target != index) { index = target; pressure = 0.0; lockUntil = now + FLIP_MS; applyLayout(); activate() }
    }

    private fun loop(ts: Double) {
        now = ts
        if (ts >= lockUntil) {
            when {
                pressure >= STEP && index < panels.lastIndex -> go(1)
                pressure <= -STEP && index > 0 -> go(-1)
                else -> { pressure *= 0.90; if (abs(pressure) < 1.0) pressure = 0.0 }
            }
        } else {
            pressure = 0.0
        }
        meterFill?.style?.height = "${(abs(pressure) / STEP * 100.0).coerceAtMost(100.0)}%"
        window.requestAnimationFrame(::loop)
    }

    private fun go(dir: Int) {
        val next = (index + dir).coerceIn(0, panels.lastIndex)
        pressure = 0.0
        if (next == index) return
        index = next
        lockUntil = now + FLIP_MS
        applyLayout()
        activate()
    }

    /**
     * Current panel flat. Everything hinges at the bottom of the screen: upcoming panels wait tilted
     * toward the viewer (they flip in from the front) and past panels fall back away from the viewer.
     */
    private fun applyLayout() {
        for (j in panels.indices) {
            val p = panels[j]
            val offset = j - index
            val deg = when {
                offset == 0 -> 0    // current, flat and facing the viewer
                offset > 0 -> -90   // upcoming: tilted forward, ready to flip in from the front
                else -> 90          // past: fallen back, away from the viewer
            }
            p.style.transform = "rotateX(${deg}deg)"
            p.style.zIndex = (100 - abs(offset)).toString()
        }
    }

    private fun activate() {
        dots.forEachIndexed { i, d ->
            if (i == index) d.classList.add(CssClasses.IS_ACTIVE) else d.classList.remove(CssClasses.IS_ACTIVE)
        }
        val panelId = panels.getOrNull(index)?.id
        for (sec in DomIds.navSections) {
            val item = document.getElementById(DomIds.navLinkId(sec)) ?: continue
            if (sec.id == panelId) item.classList.add(CssClasses.IS_ACTIVE) else item.classList.remove(CssClasses.IS_ACTIVE)
        }
        val panel = panels.getOrNull(index) ?: return
        val reveals = panel.getElementsByClassName(CssClasses.REVEAL)
        for (i in 0 until reveals.length) {
            val el = reveals.item(i) as? HTMLElement ?: continue
            if (revealed.add(el)) el.classList.add(CssClasses.REVEALED)
        }
        val nums = panel.querySelectorAll("[${Attrs.COUNT_TO}]")
        for (i in 0 until nums.length) {
            val n = nums.item(i) as? HTMLElement ?: continue
            if (counted.add(n)) countUp(n)
        }
    }

    private fun countUp(el: HTMLElement) {
        val target = el.getAttribute(Attrs.COUNT_TO)?.toDoubleOrNull() ?: return
        val duration = 1100.0
        var start = -1.0
        fun step(ts: Double) {
            if (start < 0) start = ts
            val p = ((ts - start) / duration).coerceIn(0.0, 1.0)
            val eased = 1 - (1 - p) * (1 - p)
            el.textContent = (target * eased).toInt().toString()
            if (p < 1.0) window.requestAnimationFrame(::step) else el.textContent = target.toInt().toString()
        }
        window.requestAnimationFrame(::step)
    }
}
