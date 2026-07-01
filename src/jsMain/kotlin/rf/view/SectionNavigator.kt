package rf.view

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * The only client behaviour: replaces the original jQuery/Hammer navigation. Wires every element
 * carrying [SCROLL_TARGET] to smooth-scroll to its section (smoothness comes from CSS
 * `scroll-behavior: smooth`), and scroll-spies the sections to highlight the matching side-nav item.
 */
object SectionNavigator {

    /** Attribute naming the section id an element should scroll to on click (nav items, CTAs). */
    const val SCROLL_TARGET = "data-scroll-target"

    fun install() {
        val clickables = document.querySelectorAll("[$SCROLL_TARGET]")
        for (i in 0 until clickables.length) {
            val el = clickables.item(i) as? HTMLElement ?: continue
            el.addEventListener("click", { _: Event ->
                el.getAttribute(SCROLL_TARGET)?.let { scrollTo(it) }
            })
        }
        window.addEventListener("scroll", { _: Event -> updateActive() })
        updateActive()
    }

    fun scrollTo(sectionId: String) {
        (document.getElementById(sectionId) as? HTMLElement)?.scrollIntoView()
    }

    /** Mark the side-nav item for the section nearest the top of the viewport as active. */
    private fun updateActive() {
        val threshold = window.innerHeight / 3.0
        var active = DomIds.sections.first()
        for (section in DomIds.sections) {
            val el = document.getElementById(section.id) as? HTMLElement ?: continue
            if (el.getBoundingClientRect().top <= threshold) active = section
        }
        for (section in DomIds.sections) {
            val item = document.getElementById(DomIds.navItemId(section)) ?: continue
            if (section.id == active.id) item.classList.add(CssClasses.IS_ACTIVE)
            else item.classList.remove(CssClasses.IS_ACTIVE)
        }
    }
}
