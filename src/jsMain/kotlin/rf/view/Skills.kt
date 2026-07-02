package rf.view

import kotlinx.css.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.css

/** Skills: a three.js constellation of capability domains (default view), with a flat chip-grid
 *  fallback (toggled via [SKILLS_TOGGLE]) that reads like a compact skills matrix. Both mount points
 *  are rendered here; [SkillsGl] builds the 3D scene and owns the toggle/search wiring for both. */
fun FlowContent.skills() {
    pageSection(DomIds.SKILLS) {
        monoLabel("capabilities")
        div(classes = SKILLS_STAGE) {}
        div(classes = "${SKILLS_GRID} ${SKILLS_HIDDEN}") {
            Content.skillDomains.forEach { d ->
                val accent = Color(d.color)
                div(classes = SKILLS_CARD) {
                    div(classes = SKILLS_CARD_NAME) { css { color = accent }; +d.name }
                    div(classes = SKILLS_CHIPS) {
                        d.skills.forEach { skill ->
                            span(classes = SKILLS_CHIP) {
                                css { borderColor = accent.withAlpha(0.4) }
                                +skill
                            }
                        }
                    }
                }
            }
        }
        div(classes = SKILLS_CONTROLS) {
            div(classes = SKILLS_SEARCH) {
                span(classes = SKILLS_SEARCH_PROMPT) { +"›" }
                input(type = InputType.text, classes = SKILLS_SEARCH_INPUT) {
                    placeholder = "search"
                    attributes["autocomplete"] = "off"
                    attributes["spellcheck"] = "false"
                }
            }
            button(type = ButtonType.button, classes = SKILLS_TOGGLE) { +"2D" }
        }
    }
}
