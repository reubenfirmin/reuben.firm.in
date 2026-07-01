package rf.view

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

/**
 * Smooth section navigation + scroll-spy. Wires every [Attrs.SCROLL_TARGET] element (nav links,
 * CTAs, brand) to scroll to its section, and highlights the header nav link for the section nearest
 * the top. Smoothness comes from CSS `scroll-behavior: smooth`.
 */
object SectionNavigator {

    fun install() {
        val clickables = document.querySelectorAll("[${Attrs.SCROLL_TARGET}]")
        for (i in 0 until clickables.length) {
            val el = clickables.item(i) as? HTMLElement ?: continue
            el.addEventListener("click", { _: Event ->
                el.getAttribute(Attrs.SCROLL_TARGET)?.let { scrollTo(it) }
            })
        }
        window.addEventListener("scroll", { _: Event -> updateActive() })
        updateActive()
    }

    fun scrollTo(sectionId: String) {
        (document.getElementById(sectionId) as? HTMLElement)?.scrollIntoView()
    }

    /** Highlight the nav link whose section is nearest the top of the viewport. */
    private fun updateActive() {
        val threshold = window.innerHeight / 3.0
        var active = DomIds.navSections.firstOrNull() ?: return
        for (sec in DomIds.sections) {
            if (sec.navLabel == null) continue
            val el = document.getElementById(sec.id) as? HTMLElement ?: continue
            if (el.getBoundingClientRect().top <= threshold) active = sec
        }
        for (sec in DomIds.navSections) {
            val item = document.getElementById(DomIds.navLinkId(sec)) ?: continue
            if (sec.id == active.id) item.classList.add(CssClasses.IS_ACTIVE)
            else item.classList.remove(CssClasses.IS_ACTIVE)
        }
    }
}
