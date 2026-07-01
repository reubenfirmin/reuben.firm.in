package rf

import kotlinx.html.TagConsumer
import kotlinx.html.div
import rf.view.SectionNavigator
import rf.view.contactSection
import rf.view.experienceSection
import rf.view.header
import rf.view.introSection
import rf.view.projectsSection
import rf.view.sideNav
import web.html.HTMLElement
import zoned.framework.routing.Params

/**
 * Builds the whole single-page site. Everything lives under one app-root <div>: the fixed header
 * and side-nav, then the four scrollable sections. After the DOM is built (synchronously, into the
 * body cleared by the FULL_PAGE route), [SectionNavigator] wires scroll navigation + scroll-spy.
 */
class Page {

    fun TagConsumer<HTMLElement>.render(params: Params) {
        div {
            header()
            sideNav()
            introSection()
            experienceSection()
            projectsSection()
            contactSection()
        }
        SectionNavigator.install()
    }
}
