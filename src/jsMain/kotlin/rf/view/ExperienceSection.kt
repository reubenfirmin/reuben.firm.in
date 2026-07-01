package rf.view

import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.css

/** Experience: a compact vertical accordion on a glowing spine. Every role shows as a one-line
 *  header (role · org · period) so the whole arc is visible at once; the active row expands to reveal
 *  its body (see Effects.timeline). Fits one deck screen; the early years are a drifting "haze". */
fun FlowContent.experienceSection() {
    pageSection(DomIds.EXPERIENCE, reveal = false) {
        monoLabel("experience")
        div(classes = CssClasses.TIMELINE) {
            Content.roles.forEachIndexed { i, r -> timelineRow(r, active = i == 0) }
            hazeRow()
            timelineRow(Content.degree, active = false)
        }
    }
}

private fun FlowContent.timelineRow(r: Content.Role, active: Boolean) {
    val c = Color(r.color)
    val cls = "${CssClasses.TL_ROW} ${CssClasses.REVEAL}" + if (active) " ${CssClasses.IS_ACTIVE}" else ""
    div(classes = cls) {
        div(classes = CssClasses.TL_NODE) {
            css { borderColor = c; boxShadow += BoxShadow(c.withAlpha(0.4), 0.px, 0.px, 18.px, (-6).px) }
            span(classes = CssClasses.TL_MONO) { css { color = c }; +monogram(r.org) }
        }
        div(classes = CssClasses.TL_MAIN) {
            div(classes = CssClasses.TL_HEAD) {
                span(classes = CssClasses.TL_ROLE) { +r.title }
                span(classes = CssClasses.TL_WHAT) { css { color = c }; +r.org }
                span(classes = CssClasses.TL_WHEN) { +r.period }
            }
            // Inner wrapper so the grid-rows 0fr→1fr reveal can clip the body cleanly.
            div(classes = CssClasses.TL_DETAIL) { div { p(classes = CssClasses.TL_BODY) { +r.body } } }
        }
    }
}

/** A blurred, drifting "haze of time" standing in for the unshown early-career years (degree → Catalist).
 *  Slate-toned to sit in the timeline's muted "pre-history" tier alongside Catalist and the degree. */
private fun FlowContent.hazeRow() {
    div(classes = "${CssClasses.TL_ROW} ${CssClasses.REVEAL}") {
        div(classes = CssClasses.TL_NODE) {
            css { borderColor = Tokens.SLATE; boxShadow += BoxShadow(Tokens.SLATE.withAlpha(0.3), 0.px, 0.px, 18.px, (-6).px) }
            span(classes = CssClasses.TL_MONO) { css { color = Tokens.SLATE }; +"~" }
        }
        div(classes = CssClasses.TL_MAIN) {
            div(classes = CssClasses.TL_HEAD) {
                span(classes = CssClasses.TL_ROLE) { +"Earlier engineering" }
                span(classes = CssClasses.TL_WHEN) { +"1999 - 2010" }
            }
            div(classes = CssClasses.TL_DETAIL) {
                div { div(classes = CssClasses.HAZE) { repeat(18) { span(classes = CssClasses.HAZE_MOTE) {} } } }
            }
        }
    }
}

private fun monogram(org: String) = when {
    org.startsWith("4rc") -> "4"
    org.startsWith("Mediafly") -> "M"
    org.startsWith("ExecVision") -> "E"
    org.startsWith("Catalist") -> "C"
    org.startsWith("University") -> "A"
    else -> org.take(1)
}
