package rf.view

import kotlinx.css.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.css

/** Accent palette cycled across endorsements, so each speaker gets a distinct color (badge + org). */
private val QUOTE_HEX = listOf("#5b74ff", "#a855f7", "#22c55e", "#f59e0b", "#38bdf8", "#f472b6")

private fun monogram(t: Content.Testimonial) = (t.org ?: t.name).first().uppercaseChar().toString()

/** References: a single spotlight card that auto-cycles through every endorsement (the quote types in
 *  word-by-word), a color-coded author rail + progress bar below it, then a band of count-up metrics.
 *  Only one card is ever visible, so the pane fits one deck screen without scrolling. */
fun FlowContent.proof() {
    pageSection(DomIds.PROOF) {
        monoLabel("what people say")
        div(classes = CssClasses.SPOTLIGHT) {
            Content.testimonials.forEachIndexed { i, t ->
                val accent = Color(QUOTE_HEX[i % QUOTE_HEX.size])
                val active = if (i == 0) " ${CssClasses.IS_ACTIVE}" else ""
                div(classes = "${CssClasses.TESTIMONIAL_CARD}$active") {
                    span(classes = CssClasses.TESTIMONIAL_MARK) { css { color = accent }; +"“" }
                    p(classes = CssClasses.TESTIMONIAL_QUOTE) {
                        // Each word is its own inline-block so it can transform on reveal; the space
                        // goes between spans as a plain text node (inline-block trims inner trailing spaces).
                        t.quote.split(" ").forEach { w ->
                            span(classes = CssClasses.TESTIMONIAL_WORD) { +w }
                            +" "
                        }
                    }
                    div(classes = CssClasses.TESTIMONIAL_AUTHOR) {
                        span(classes = CssClasses.TESTIMONIAL_BADGE) {
                            css { color = accent; borderColor = accent }
                            +monogram(t)
                        }
                        div {
                            div(classes = CssClasses.TESTIMONIAL_NAME) { +t.name }
                            div(classes = CssClasses.TESTIMONIAL_META) {
                                +t.title
                                t.org?.let { org -> +", "; span { css { color = accent }; +org } }
                            }
                        }
                    }
                }
            }
        }
        div(classes = CssClasses.SPOTLIGHT_RAIL) {
            Content.testimonials.forEachIndexed { i, t ->
                val active = if (i == 0) " ${CssClasses.IS_ACTIVE}" else ""
                span(classes = "${CssClasses.SPOTLIGHT_CHIP}$active") {
                    attributes[Attrs.ACCENT_HEX] = QUOTE_HEX[i % QUOTE_HEX.size]
                    +monogram(t)
                }
            }
        }
        div(classes = CssClasses.SPOTLIGHT_PROGRESS) { div(classes = CssClasses.SPOTLIGHT_PROGRESS_FILL) {} }
        div(classes = "${CssClasses.METRIC_BAND} ${CssClasses.REVEAL}") {
            Content.metrics.forEach { m ->
                div(classes = CssClasses.METRIC) {
                    div(classes = CssClasses.METRIC_VALUE) {
                        span { attributes[Attrs.COUNT_TO] = m.value.toString(); +"0" }
                        +m.suffix
                    }
                    div(classes = CssClasses.METRIC_LABEL) { +m.label }
                }
            }
        }
    }
}
