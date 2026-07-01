package rf.view

import kotlinx.css.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.css
import zoned.framework.interop.raw

/** Section 02: recent career, as a responsive row of role cards (replaces the old jQuery slider). */
fun FlowContent.experienceSection() {
    section(classes = CssClasses.SECTION) {
        id = DomIds.EXPERIENCE.id
        wrapper {
            h2 {
                +"Recent Career"
                css { fontSize = 30.px; textAlign = TextAlign.center; marginBottom = 40.px }
            }
            div {
                css {
                    display = Display.flex
                    justifyContent = JustifyContent.center
                    flexWrap = FlexWrap.wrap
                    raw("gap", "40px")
                }
                Content.roles.forEach { role ->
                    div(classes = CssClasses.ROLE_CARD) {
                        css {
                            raw("flex", "0 1 240px")
                            textAlign = TextAlign.center
                        }
                        div {
                            css {
                                width = 150.px; height = 150.px
                                raw("margin", "0 auto")
                                borderRadius = 50.pct
                                overflow = Overflow.hidden
                            }
                            img {
                                src = role.image; alt = role.company
                                css { width = 100.pct; height = 100.pct; raw("object-fit", "cover") }
                            }
                        }
                        h3 {
                            +role.company
                            css { marginTop = 16.px; fontSize = 16.px; fontWeight = FontWeight("700"); textTransform = TextTransform.uppercase }
                        }
                        p {
                            +role.body
                            css { color = Palette.MID; marginTop = 8.px }
                        }
                    }
                }
            }
        }
    }
}
