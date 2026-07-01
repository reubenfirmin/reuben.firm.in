package rf.view

import kotlinx.css.*
import kotlinx.html.*
import zoned.framework.interop.css
import zoned.framework.interop.raw

/**
 * The fixed left rail: one numbered item (01..NN) per [DomIds.sections]. Items carry
 * [SectionNavigator.SCROLL_TARGET] (click scrolls to the section) and are scroll-spied by
 * [SectionNavigator] to toggle `is-active`. Desktop-only (hidden < 1180px in [Styles]).
 */
fun FlowContent.sideNav() {
    nav {
        id = DomIds.SIDE_NAV
        css {
            position = Position.fixed
            left = 24.px
            top = 50.pct
            raw("transform", "translateY(-50%)")
            display = Display.flex
            flexDirection = FlexDirection.column
            raw("gap", "28px")
            zIndex = 20
        }
        DomIds.sections.forEachIndexed { index, section ->
            div(classes = CssClasses.SIDE_NAV_ITEM) {
                id = DomIds.navItemId(section)
                attributes[SectionNavigator.SCROLL_TARGET] = section.id
                css {
                    display = Display.flex
                    alignItems = Align.center
                    cursor = Cursor.pointer
                }
                span(classes = CssClasses.SIDE_NAV_NUM) {
                    +(index + 1).toString().padStart(2, '0')
                    css { color = Palette.MID; fontSize = 12.px; fontWeight = FontWeight("700") }
                }
                span(classes = CssClasses.SIDE_NAV_LABEL) {
                    +section.navLabel
                    css {
                        marginLeft = 12.px
                        color = Palette.MID
                        fontSize = 14.px
                        fontWeight = FontWeight("300")
                    }
                }
            }
        }
    }
}
