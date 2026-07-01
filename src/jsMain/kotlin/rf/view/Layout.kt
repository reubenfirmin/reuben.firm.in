package rf.view

import kotlinx.html.*
import zoned.framework.tags.svg

/** A `<section>` with the shared max-width wrapper. [reveal] adds the scroll-reveal marker to the
 *  inner content; [flip] marks the section for the iTunes-style flip-in (off for the hero). Every
 *  card but the last gets a bouncing "next" chevron so people who don't scroll can still advance. */
fun FlowContent.pageSection(sec: DomIds.Section, reveal: Boolean = true, flip: Boolean = true, block: DIV.() -> Unit) {
    val sectionClasses = if (flip) "${CssClasses.SECTION} ${CssClasses.FLIP}" else CssClasses.SECTION
    section(classes = sectionClasses) {
        id = sec.id
        val wrapClasses = if (reveal) "${CssClasses.WRAP} ${CssClasses.REVEAL}" else CssClasses.WRAP
        div(classes = wrapClasses) { block() }
        if (sec != DomIds.sections.last()) {
            button(type = ButtonType.button, classes = CssClasses.DECK_NEXT) {
                attributes["aria-label"] = "Next"
                svg {
                    viewBox = "0 0 24 24"; fill = "none"; stroke = "currentColor"
                    path { d = "M6 9l6 6 6-6"; strokeWidth = "2"; strokeLinecap = "round"; strokeLinejoin = "round" }
                }
            }
        }
    }
}

/** A monospace `// SECTION` eyebrow label (the "// " prefix is added by CSS). */
fun FlowContent.monoLabel(text: String) {
    span(classes = CssClasses.MONO_LABEL) { +text }
}
