package rf.view

import kotlinx.html.*
import rf.Content

/** Fixed top header: monospace brand (scrolls to top) on the left, nav links + a CTA on the right.
 *  Interactive elements carry [Attrs.SCROLL_TARGET]; [SectionNavigator] wires clicks and scroll-spy. */
fun FlowContent.header() {
    header(classes = HEADER) {
        a(classes = BRAND) {
            attributes[Attrs.SCROLL_TARGET] = DomIds.HERO.id
            +"${Content.FIRST_NAME.lowercase()} ${Content.LAST_NAME.lowercase()}"
        }
        nav(classes = NAV) {
            DomIds.navSections.forEach { sec ->
                a(classes = NAV_LINK) {
                    id = DomIds.navLinkId(sec)
                    attributes[Attrs.SCROLL_TARGET] = sec.id
                    +sec.navLabel.orEmpty()
                }
            }
            a(classes = CTA) {
                attributes[Attrs.SCROLL_TARGET] = DomIds.CONTACT.id
                +"Get in touch"
            }
        }
    }
}
