package rf.view

import kotlinx.css.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.css
import zoned.framework.interop.raw

/** Section 04: contact card over a background image (email + social/resume links). */
fun FlowContent.contactSection() {
    section(classes = CssClasses.SECTION) {
        id = DomIds.CONTACT.id
        css {
            raw("background", "url(/img/contact-bg.png) center/cover no-repeat")
            alignItems = Align.center
        }
        div {
            css {
                padding = Padding(45.px)
                textAlign = TextAlign.center
                backgroundColor = Palette.BLACK
                raw("box-shadow", "0 0 30px 0 rgba(0,0,0,.75)")
                minWidth = 240.px
            }
            a {
                href = "mailto:${Content.EMAIL}"
                +"Email Me"
                css {
                    display = Display.block
                    marginBottom = 24.px
                    color = Palette.WHITE
                    fontWeight = FontWeight("700")
                }
            }
            Content.contactLinks.forEach { link ->
                a(classes = CssClasses.CONTACT_LINK) {
                    href = link.href
                    if (link.href.startsWith("http")) target = "_blank"
                    +link.label
                    css {
                        display = Display.block
                        width = 160.px
                        raw("margin", "0 auto 16px auto")
                        padding = Padding(8.px, 0.px)
                        color = Palette.WHITE
                        fontWeight = FontWeight("700")
                        backgroundColor = Palette.HIGHLIGHT
                    }
                }
            }
        }
    }
}
