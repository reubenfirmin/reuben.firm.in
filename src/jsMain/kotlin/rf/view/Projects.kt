package rf.view

import kotlinx.css.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.css

/** Accent per project (index number, open border, tagline), cycled from this palette. */
private val PROJECT_HEX = listOf("#5b74ff", "#a855f7", "#2b47ff", "#38bdf8", "#22c55e", "#f472b6")

/** Projects: an expanding-panel accordion. All projects sit side-by-side; hovering one grows it to
 *  reveal tagline / blurb / tags while the others collapse to a slim spine (see Effects.projects).
 *  Fits one deck screen without scrolling; the `zoned` card is flagged as this site's framework. */
fun FlowContent.projects() {
    pageSection(DomIds.PROJECTS) {
        monoLabel("selected projects")
        div(classes = PROJECTS) {
            Content.projects.forEachIndexed { i, project ->
                val hex = PROJECT_HEX[i % PROJECT_HEX.size]
                val accent = Color(hex)
                val open = if (i == 0) " ${IS_ACTIVE}" else ""
                a(classes = "${PROJECT_PANEL}$open") {
                    href = project.url; target = "_blank"
                    attributes[Attrs.ACCENT_HEX] = hex
                    span(classes = PROJECT_INDEX) {
                        css { color = accent }
                        +(i + 1).toString().padStart(2, '0')
                    }
                    div(classes = PROJECT_NAME) { +project.name }
                    div(classes = PROJECT_DETAIL) {
                        div(classes = PROJECT_TAGLINE) { css { color = accent }; +project.tagline }
                        p(classes = PROJECT_BLURB) { +project.blurb }
                        div(classes = TAG_ROW) {
                            project.tags.forEach { tag -> span(classes = TAG) { +tag } }
                        }
                        project.meta?.let { div(classes = CARD_META) { +"↳ $it" } }
                    }
                }
            }
        }
    }
}
