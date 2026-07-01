package rf.view

/**
 * Centralized element ids and the section registry — no raw id strings scattered across the view.
 * The [sections] list is the single source of truth for both the scrollable `<section>`s and the
 * side-nav items that scroll-spy against them (see [SideNav] and [SectionNavigator]).
 */
object DomIds {

    /** A page section and its side-nav label. [id] is the section element's DOM id and scroll target. */
    data class Section(val id: String, val navLabel: String)

    val INTRO = Section("intro", "Home")
    val EXPERIENCE = Section("experience", "Experience")
    val PROJECTS = Section("projects", "Projects")
    val CONTACT = Section("contact", "Contact")

    /** Order matters: drives section order on the page and numbering (01..04) in the side-nav. */
    val sections = listOf(INTRO, EXPERIENCE, PROJECTS, CONTACT)

    /** The side-nav item id for a given section (e.g. "nav-intro"). */
    fun navItemId(section: Section) = "nav-${section.id}"

    const val SIDE_NAV = "side-nav"
}
