package rf.view

import kotlinx.css.*
import kotlinx.html.*
import rf.Content
import zoned.framework.interop.css

/** Hero: a two-column grid. Left = name/positioning headline, a decrypting terminal role line with a
 *  blinking cursor, metric chips, CTAs, and a client pull-quote. Right = the portrait inside a
 *  rotating conic ring, gently floating, with the status badges overlaid. */
fun FlowContent.hero() {
    pageSection(DomIds.HERO, reveal = false, flip = false) {
        div(classes = CssClasses.HERO) {
            div(classes = CssClasses.HERO_GRID) {
                div(classes = CssClasses.HERO_LEFT) {
                    h1(classes = CssClasses.HERO_HEAD) {
                        +Content.HEADLINE_1
                        br {}
                        span { +Content.HEADLINE_2; css { color = Tokens.ACCENT } }
                    }
                    div(classes = CssClasses.TERMINAL) {
                        span { +"> " }
                        span { attributes[Attrs.DECRYPT] = "true"; +Content.TERMINAL_ROLE }
                        span(classes = CssClasses.CURSOR) {}
                    }
                    div(classes = CssClasses.CHIP_ROW) {
                        Content.heroChips.forEach { chip -> span(classes = CssClasses.CHIP) { +chip } }
                    }
                    div(classes = CssClasses.CHIP_ROW) {
                        a(classes = CssClasses.CTA) {
                            attributes[Attrs.SCROLL_TARGET] = DomIds.CONTACT.id
                            +"Get in touch"
                        }
                        a(classes = CssClasses.CTA_GHOST) {
                            href = Content.GITHUB_URL; target = "_blank"
                            +"GitHub"
                        }
                    }
                    p(classes = CssClasses.HERO_QUOTE) {
                        +Content.HERO_QUOTE
                        br {}
                        span(classes = CssClasses.HERO_QUOTE_ROLE) { +Content.HERO_QUOTE_ROLE }
                    }
                }
                div(classes = CssClasses.HERO_MEDIA) {
                    div(classes = CssClasses.HEADSHOT_FRAME) {
                        img(classes = CssClasses.HEADSHOT_IMG) {
                            src = Content.HEADSHOT
                            alt = "${Content.FIRST_NAME} ${Content.LAST_NAME}"
                        }
                    }
                }
            }
        }
    }
}
