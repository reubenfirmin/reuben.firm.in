package rf.view

import kotlinx.html.FlowContent
import rf.Content
import zoned.framework.tags.SvgTag
import zoned.framework.tags.svg

/** Classy inline icons as typed SVG. Line icons use `currentColor` for stroke, brand marks for fill,
 *  so the card's color (and its hover shift) flows straight through. */
fun FlowContent.contactIcon(icon: Content.ContactIcon) {
    svg(classes = CssClasses.CONTACT_ICON) {
        viewBox = "0 0 24 24"
        when (icon) {
            Content.ContactIcon.MAIL -> {
                fill = "none"; stroke = "currentColor"
                strokePath("M4 4h16a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z")
                strokePath("m22 7-10 6L2 7")
            }
            Content.ContactIcon.CALENDAR -> {
                fill = "none"; stroke = "currentColor"
                strokePath("M5 4h14a2 2 0 0 1 2 2v13a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z")
                strokePath("M16 2v4"); strokePath("M8 2v4"); strokePath("M3 10h18")
            }
            Content.ContactIcon.GLOBE -> {
                fill = "none"; stroke = "currentColor"
                circle { cx = "12"; cy = "12"; r = "10"; fill = "none"; stroke = "currentColor"; strokeWidth = "1.7" }
                strokePath("M2 12h20")
                strokePath("M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z")
            }
            Content.ContactIcon.LINKEDIN -> {
                fill = "currentColor"
                path {
                    fill = "currentColor"
                    d = "M20.45 20.45h-3.56v-5.57c0-1.33-.03-3.04-1.85-3.04-1.85 0-2.14 1.45-2.14 2.94v5.67H9.34V9h3.42v1.56h.05c.48-.9 1.64-1.85 3.37-1.85 3.6 0 4.27 2.37 4.27 5.45v6.29zM5.34 7.43a2.06 2.06 0 1 1 0-4.13 2.06 2.06 0 0 1 0 4.13zM7.12 20.45H3.56V9h3.56v11.45zM22.22 0H1.77C.79 0 0 .77 0 1.72v20.56C0 23.23.79 24 1.77 24h20.45c.98 0 1.78-.77 1.78-1.72V1.72C24 .77 23.2 0 22.22 0z"
                }
            }
            Content.ContactIcon.GITHUB -> {
                fill = "currentColor"
                path {
                    fill = "currentColor"; fillRule = "evenodd"; clipRule = "evenodd"
                    d = "M12 .5C5.37.5 0 5.87 0 12.5c0 5.3 3.44 9.8 8.21 11.39.6.11.82-.26.82-.58 0-.29-.01-1.04-.02-2.05-3.34.73-4.04-1.61-4.04-1.61-.55-1.39-1.34-1.76-1.34-1.76-1.09-.75.08-.73.08-.73 1.21.09 1.84 1.24 1.84 1.24 1.07 1.84 2.81 1.31 3.5 1 .11-.78.42-1.31.76-1.61-2.67-.3-5.47-1.34-5.47-5.95 0-1.31.47-2.39 1.24-3.23-.13-.31-.54-1.53.12-3.18 0 0 1.01-.32 3.3 1.23a11.5 11.5 0 0 1 6 0c2.29-1.55 3.3-1.23 3.3-1.23.66 1.65.25 2.87.12 3.18.77.84 1.24 1.92 1.24 3.23 0 4.62-2.81 5.65-5.49 5.95.43.37.81 1.1.81 2.22 0 1.61-.01 2.9-.01 3.29 0 .32.22.7.83.58C20.56 22.29 24 17.8 24 12.5 24 5.87 18.63.5 12 .5z"
                }
            }
        }
    }
}

private fun SvgTag.strokePath(data: String) = path {
    d = data; fill = "none"; stroke = "currentColor"
    strokeWidth = "1.7"; strokeLinecap = "round"; strokeLinejoin = "round"
}
