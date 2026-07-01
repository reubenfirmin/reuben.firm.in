package rf.view

import kotlinx.css.*
import zoned.framework.interop.raw
import zoned.framework.interop.styleSheet

/** The site's colour palette (from the original site's SASS vars). */
object Palette {
    val WHITE = Color("#fff")
    val BLACK = Color("#0c0c0c")
    val HIGHLIGHT = Color("#0f33ff")
    val MUTED = Color("#282828")
    val MID = Color("#555")
}

/**
 * Shared, selector-based CSS injected once into <head>. Per-element geometry lives in inline `css {}`
 * at the builders' call sites; this file holds only what needs a selector — pseudo-classes, hover /
 * active states, and the responsive media queries. Injected from [rf.main] before the first render.
 */
object Styles {

    fun inject() {
        styleSheet("landing-styles") {
            // Vertical scroll with gentle snapping between full-height sections.
            rule("html") { raw("scroll-snap-type", "y proximity") }
            rule(".${CssClasses.SECTION}") {
                minHeight = 100.vh
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
                position = Position.relative
                raw("scroll-snap-align", "start")
                padding = Padding(80.px, 24.px, 48.px, 24.px)
            }

            // Side-nav: dim by default, blue label + white number when active, white label on hover.
            rule(".${CssClasses.SIDE_NAV_ITEM}") { raw("transition", "color .3s ease-in-out") }
            rule(".${CssClasses.SIDE_NAV_ITEM}:hover .${CssClasses.SIDE_NAV_LABEL}") { color = Palette.WHITE }
            rule(".${CssClasses.SIDE_NAV_ITEM}.${CssClasses.IS_ACTIVE} .${CssClasses.SIDE_NAV_NUM}") { color = Palette.WHITE }
            rule(".${CssClasses.SIDE_NAV_ITEM}.${CssClasses.IS_ACTIVE} .${CssClasses.SIDE_NAV_LABEL}") { color = Palette.HIGHLIGHT }

            // Contact CTA button: solid highlight; dim slightly on hover.
            rule(".${CssClasses.CTA}") { raw("transition", "opacity .2s ease-in-out") }
            rule(".${CssClasses.CTA}:hover") { opacity = 0.85 }

            // Intro highlight cards: muted until hovered.
            rule(".${CssClasses.HIGHLIGHT_CARD}") { raw("transition", "color .2s ease-in-out") }
            rule(".${CssClasses.HIGHLIGHT_CARD}:hover") { color = Palette.WHITE }

            // Role + project cards lift subtly on hover.
            rule(".${CssClasses.ROLE_CARD}") { raw("transition", "transform .2s ease-in-out") }
            rule(".${CssClasses.ROLE_CARD}:hover") { raw("transform", "translateY(-6px)") }
            rule(".${CssClasses.PROJECT_CARD}") { raw("transition", "border-color .2s ease-in-out, color .2s ease-in-out") }
            rule(".${CssClasses.PROJECT_CARD}:hover") {
                color = Palette.WHITE
                raw("border-color", Palette.WHITE.toString())
            }

            rule(".${CssClasses.CONTACT_LINK}") { raw("transition", "opacity .2s ease-in-out") }
            rule(".${CssClasses.CONTACT_LINK}:hover") { opacity = 0.85 }

            // Arrow links nudge the arrow on hover.
            rule(".${CssClasses.ARROW_LINK} svg") { raw("transition", "transform .2s ease-in-out") }
            rule(".${CssClasses.ARROW_LINK}:hover svg") { raw("transform", "translateX(6px)") }

            // Responsive: the fixed side-nav is desktop-only; cards stack on narrow screens.
            raw("""
                @media (max-width: 1180px) {
                  #${DomIds.SIDE_NAV} { display: none; }
                }
                @media (max-width: 767px) {
                  .${CssClasses.HIGHLIGHT_CARD} { max-width: 100% !important; }
                }
            """.trimIndent())
        }
    }
}
