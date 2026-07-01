package rf.view

import kotlinx.html.*

/** Skills: a three.js constellation of capability domains. This just renders the mount container;
 *  [SkillsGl] builds the scene, labels, drag-orbit controls, and hover-focus into it. */
fun FlowContent.skills() {
    pageSection(DomIds.SKILLS) {
        monoLabel("capabilities")
        div(classes = CssClasses.SKILLS_STAGE) {
            button(type = ButtonType.button, classes = CssClasses.SKILLS_TOGGLE) { +"2D" }
        }
        div(classes = CssClasses.SKILLS_SEARCH) {
            span(classes = CssClasses.SKILLS_SEARCH_PROMPT) { +"›" }
            input(type = InputType.text, classes = CssClasses.SKILLS_SEARCH_INPUT) {
                placeholder = "grep skills"
                attributes["autocomplete"] = "off"
                attributes["spellcheck"] = "false"
            }
        }
    }
}
