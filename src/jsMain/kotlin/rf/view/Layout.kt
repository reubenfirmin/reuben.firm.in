package rf.view

import kotlinx.css.*
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.div
import zoned.framework.interop.css
import zoned.framework.interop.raw

/** A centered, max-width content container shared by every section's inner content. */
fun FlowContent.wrapper(block: DIV.() -> Unit) {
    div {
        css {
            width = 100.pct
            maxWidth = 960.px
            raw("margin", "0 auto")
        }
        block()
    }
}
