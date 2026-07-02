package rf

import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.id
import rf.view.*
import rf.view.Deck
import rf.view.DomIds
import rf.view.Effects
import rf.view.contactSection
import rf.view.experienceSection
import rf.view.header
import rf.view.hero
import rf.view.projects
import rf.view.proof
import rf.view.skills
import web.html.HTMLElement
import zoned.framework.routing.Params

/**
 * Builds the whole single-page site under one app-root <div> (which carries the decorative grid
 * glow): fixed header, then hero / proof / work / experience / contact. After the DOM is built
 * synchronously into the body cleared by the FULL_PAGE route, [SectionNavigator] wires scroll nav +
 * scroll-spy and [Effects] wires the client-side polish.
 */
class Page {

    fun TagConsumer<HTMLElement>.render(params: Params) {
        div {
            id = DomIds.APP_ROOT
            header()
            div(classes = DECK) {
                hero()
                proof()
                projects()
                experienceSection()
                skills()
                contactSection()
            }
            div(classes = DECK_METER) {
                div(classes = DECK_METER_TRACK) { div(classes = DECK_METER_FILL) {} }
                div(classes = DECK_DOTS) {
                    DomIds.sections.forEach { div(classes = DECK_DOT) {} }
                }
            }
        }
        Deck.install()
        Effects.installAll()
    }
}
