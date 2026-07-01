package rf.view

import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.css
import zoned.framework.interop.raw

/** Section 03: open source projects, with a GitHub link and a card per project. */
fun FlowContent.projectsSection() {
    section(classes = CssClasses.SECTION) {
        id = DomIds.PROJECTS.id
        wrapper {
            div {
                css {
                    display = Display.flex
                    alignItems = Align.center
                    justifyContent = JustifyContent.spaceBetween
                    flexWrap = FlexWrap.wrap
                    raw("gap", "24px")
                    marginBottom = 40.px
                }
                h2 {
                    +"Open source projects."
                    css { fontSize = 48.px; fontWeight = FontWeight("900"); lineHeight = LineHeight("1"); maxWidth = 420.px }
                }
                a(classes = CssClasses.ARROW_LINK) {
                    href = Content.GITHUB_URL
                    target = "_blank"
                    css {
                        display = Display.inlineFlex
                        alignItems = Align.center
                        color = Palette.WHITE
                        fontWeight = FontWeight("700")
                        textTransform = TextTransform.uppercase
                    }
                    +"Github"
                    span { +" →"; css { marginLeft = 8.px } }
                }
            }
            div {
                css {
                    display = Display.flex
                    flexWrap = FlexWrap.wrap
                    raw("gap", "20px")
                }
                Content.projects.forEach { project ->
                    a(classes = CssClasses.PROJECT_CARD) {
                        href = project.url
                        target = "_blank"
                        css {
                            display = Display.flex
                            alignItems = Align.center
                            raw("flex", "1 1 220px")
                            minHeight = 90.px
                            padding = Padding(16.px)
                            color = Palette.WHITE
                            raw("border", "4px solid ${Palette.HIGHLIGHT}")
                        }
                        h3 {
                            +project.name
                            css { fontSize = 16.px; textTransform = TextTransform.uppercase }
                        }
                    }
                }
            }
        }
    }
}
