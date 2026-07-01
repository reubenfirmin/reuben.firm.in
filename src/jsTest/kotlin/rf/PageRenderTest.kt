package rf

import kotlinx.browser.document
import rf.view.CssClasses
import rf.view.DomIds
import zoned.framework.interop.addToBody
import zoned.framework.routing.Params
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PageRenderTest {

    private fun renderPage() {
        addToBody { with(App.page) { render(Params(emptyMap())) } }
    }

    private fun cleanup() {
        // The whole page hangs off one app-root div; removing it (via any section's parent) resets.
        document.getElementById(DomIds.INTRO.id)?.parentElement?.remove()
    }

    @Test
    fun rendersAllFourSections() {
        renderPage()
        try {
            DomIds.sections.forEach { section ->
                assertNotNull(document.getElementById(section.id), "section '${section.id}' is rendered")
            }
        } finally {
            cleanup()
        }
    }

    @Test
    fun sideNavHasOneItemPerSection() {
        renderPage()
        try {
            val items = document.querySelectorAll(".${CssClasses.SIDE_NAV_ITEM}")
            assertEquals(DomIds.sections.size, items.length, "one side-nav item per section")
        } finally {
            cleanup()
        }
    }

    @Test
    fun resumeAndProjectLinksArePresent() {
        renderPage()
        try {
            assertNotNull(
                document.querySelector("a[href='${Content.RESUME_URL}']"),
                "download-resume link points at the PDF",
            )
            assertNotNull(
                document.querySelector("a[href='http://the.do.zone']"),
                "The Do Zone project link is present",
            )
        } finally {
            cleanup()
        }
    }
}
