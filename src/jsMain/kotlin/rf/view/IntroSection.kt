package rf.view

import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.css
import zoned.framework.interop.raw

/** Section 01: name banner + headshot + the three "what I do" highlight cards. */
fun FlowContent.introSection() {
    section(classes = CssClasses.SECTION) {
        id = DomIds.INTRO.id
        wrapper {
            // Banner: name/tagline/CTA on the left, headshot on the right.
            div {
                css {
                    display = Display.flex
                    alignItems = Align.center
                    justifyContent = JustifyContent.spaceBetween
                    flexWrap = FlexWrap.wrap
                    raw("gap", "32px")
                    paddingBottom = 32.px
                    raw("border-bottom", "2px solid ${Palette.MUTED}")
                }
                div {
                    css { raw("flex", "1 1 320px") }
                    h1 {
                        +Content.FIRST_NAME; br {}; +Content.LAST_NAME
                        css { fontSize = 68.px; fontWeight = FontWeight("900"); lineHeight = LineHeight("1") }
                    }
                    p {
                        +Content.TAGLINE
                        css { color = Palette.MUTED; margin = Margin(16.px, 0.px, 24.px, 0.px); maxWidth = 420.px }
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
                img {
                    src = Content.HEADSHOT; alt = "${Content.FIRST_NAME} ${Content.LAST_NAME}"
                    css { width = 300.px; height = 300.px; raw("object-fit", "cover"); borderRadius = 50.pct }
                }
            }
            // The three highlight cards.
            div {
                css {
                    display = Display.flex
                    justifyContent = JustifyContent.spaceBetween
                    flexWrap = FlexWrap.wrap
                    raw("gap", "24px")
                    paddingTop = 32.px
                }
                Content.highlights.forEach { h ->
                    div(classes = CssClasses.HIGHLIGHT_CARD) {
                        css { raw("flex", "1 1 220px"); maxWidth = 250.px; color = Palette.MUTED }
                        h3 {
                            +h.title
                            css { fontSize = 16.px; textTransform = TextTransform.uppercase; color = Palette.WHITE; marginBottom = 8.px }
                        }
                        p { +h.body }
                    }
                }
            }
        }
    }
}
