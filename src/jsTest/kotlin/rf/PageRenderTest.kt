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
        // The whole page hangs off the app-root div; removing it resets the DOM.
        document.getElementById(DomIds.APP_ROOT)?.remove()
    }

    @Test
    fun rendersAllSections() {
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
    fun headerHasOneNavLinkPerNavSection() {
        renderPage()
        try {
            val items = document.querySelectorAll(".${CssClasses.NAV_LINK}")
            assertEquals(DomIds.navSections.size, items.length, "one nav link per nav section")
        } finally {
            cleanup()
        }
    }

    @Test
    fun featuredProjectLinkIsPresent() {
        renderPage()
        try {
            assertNotNull(
                document.querySelector("a[href='https://the.do.zone']"),
                "The Do Zone project link is present",
            )
        } finally {
            cleanup()
        }
    }

    @Test
    fun metricsHaveCountUpTargets() {
        renderPage()
        try {
            val nums = document.querySelectorAll("[${rf.view.Attrs.COUNT_TO}]")
            assertEquals(Content.metrics.size, nums.length, "one count-up target per metric")
        } finally {
            cleanup()
        }
    }
}
