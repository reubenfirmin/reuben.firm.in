package rf

import rf.view.Styles
import zoned.framework.routing.RenderMode
import zoned.framework.routing.Router
import zoned.framework.routing.Routes

/** Composition root: the single page instance the route renders. */
object App {
    val page = Page()
}

/** The one route: "/" renders the whole page (FULL_PAGE clears the body and rebuilds). */
object pages : Routes<Page>(App.page, "/") {
    val home = route(mode = RenderMode.FULL_PAGE) { "/" to { render(it) } }
        .title { "Reuben Firmin" }
}

fun main() {
    Styles.inject()
    Router.start(pages)
}
