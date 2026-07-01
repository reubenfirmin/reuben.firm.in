package rf.view

import kotlinx.css.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.css
import zoned.framework.interop.raw

/** Fixed top header: logo + name on the left, a Contact button (scrolls to the contact section)
 *  on the right. The button carries [SectionNavigator.SCROLL_TARGET] so the navigator wires it. */
fun FlowContent.header() {
    header {
        css {
            position = Position.fixed
            top = 0.px; left = 0.px; right = 0.px
            height = 70.px
            display = Display.flex
            alignItems = Align.center
            justifyContent = JustifyContent.spaceBetween
            padding = Padding(0.px, 32.px)
            zIndex = 20
        }
        a {
            href = "#"
            css {
                display = Display.flex
                alignItems = Align.center
                color = Palette.WHITE
            }
            img {
                src = "/img/logo.png"; alt = Content.FIRST_NAME
                css { width = 30.px; raw("height", "auto") }
            }
            span {
                +"${Content.FIRST_NAME} ${Content.LAST_NAME}"
                css {
                    marginLeft = 10.px
                    fontSize = 16.px
                    fontWeight = FontWeight("700")
                    textTransform = TextTransform.uppercase
                }
            }
        }
        button(classes = CssClasses.CTA) {
            +"Contact"
            attributes[SectionNavigator.SCROLL_TARGET] = DomIds.CONTACT.id
            css {
                padding = Padding(8.px, 20.px)
                color = Palette.WHITE
                fontWeight = FontWeight("700")
                textTransform = TextTransform.uppercase
                backgroundColor = Palette.HIGHLIGHT
            }
        }
    }
}
