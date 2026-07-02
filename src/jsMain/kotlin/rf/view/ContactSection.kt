package rf.view

import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.backgroundClipText
import zoned.framework.interop.css
import zoned.framework.interop.linearGradient
import zoned.framework.interop.webkitTextFillColor

/** Contact: a live availability pulse, a gradient heading, and a row of monospace link buttons. */
fun FlowContent.contactSection() {
    pageSection(DomIds.CONTACT) {
        div {
            css { maxWidth = 640.px; margin = Margin(0.px, LinearDimension.auto) }
            monoLabel("contact")
            h2 {
                +"Let's build something."
                css {
                    fontSize = clamp(30.px, 5.vw, 52.px)
                    fontWeight = FontWeight("800")
                    letterSpacing = (-0.02).em
                    marginBottom = 14.px
                    backgroundImage = linearGradient(direction = 120.deg) { stop(Tokens.TEXT); stop(Tokens.ACCENT_SOFT) }
                    backgroundClipText()
                    webkitTextFillColor(Color.transparent)
                }
            }
            p {
                +"Fractional CTO / CISO, currently taking on select engagements."
                css { color = Tokens.MUTED; marginBottom = 32.px }
            }
        }
        div(classes = CONTACT_GRID) {
            Content.contactLinks.forEach { link ->
                val external = link.href.startsWith("http")
                val caption = link.href
                    .removePrefix("mailto:").removePrefix("https://").removePrefix("http://")
                    .removePrefix("www.").removeSuffix("/")
                a(classes = CONTACT_LINK) {
                    href = link.href
                    if (external) target = "_blank"
                    contactIcon(link.icon)
                    div(classes = CONTACT_LINK_LABEL) { +link.label }
                    div(classes = CONTACT_LINK_SUB) { +caption }
                    span(classes = CONTACT_LINK_ARROW) { +(if (external) "↗" else "→") }
                }
            }
        }
    }
}
