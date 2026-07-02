package rf.view

import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.backgroundClipText
import zoned.framework.interop.css
import zoned.framework.interop.linearGradient
import zoned.framework.interop.webkitTextFillColor

/** Hero: a two-column grid. Left = name/positioning headline, a decrypting terminal role line with a
 *  blinking cursor, metric chips, CTAs, and a client pull-quote. Right = the portrait inside a
 *  rotating conic ring, gently floating, with the status badges overlaid. */
fun FlowContent.hero() {
    pageSection(DomIds.HERO, reveal = false, flip = false) {
        div(classes = HERO) {
            div(classes = HERO_GRID) {
                div(classes = HERO_LEFT) {
                    h1(classes = HERO_HEAD) {
                        +Content.HEADLINE_1
                        br {}
                        span {
                            +Content.HEADLINE_2
                            css {
                                backgroundImage = linearGradient(direction = 120.deg) {
                                    stop(Tokens.ACCENT); stop(Tokens.ACCENT_SOFT)
                                }
                                backgroundClipText()
                                webkitTextFillColor(Color.transparent)
                            }
                        }
                    }
                    div(classes = TERMINAL) {
                        span { +"> " }
                        span { attributes[Attrs.DECRYPT] = "true"; +Content.TERMINAL_ROLE }
                        span(classes = CURSOR) {}
                    }
                    div(classes = CHIP_ROW) {
                        Content.heroChips.forEach { chip -> span(classes = CHIP) { +chip } }
                    }
                    div(classes = CHIP_ROW) {
                        a(classes = CTA) {
                            attributes[Attrs.SCROLL_TARGET] = DomIds.CONTACT.id
                            +"Get in touch"
                        }
                        a(classes = CTA_GHOST) {
                            href = Content.GITHUB_URL; target = "_blank"
                            +"GitHub"
                        }
                    }
                    p(classes = HERO_QUOTE) {
                        +Content.HERO_QUOTE
                        br {}
                        span(classes = HERO_QUOTE_ROLE) { +Content.HERO_QUOTE_ROLE }
                    }
                }
                div(classes = HERO_MEDIA) {
                    div(classes = HEADSHOT_FRAME) {
                        img(classes = HEADSHOT_IMG) {
                            src = Content.HEADSHOT
                            alt = "${Content.FIRST_NAME} ${Content.LAST_NAME}"
                        }
                    }
                }
            }
        }
    }
}
