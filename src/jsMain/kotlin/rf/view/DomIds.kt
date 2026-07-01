package rf.view

/**
 * Centralized element ids, the section registry, and the typed data-attribute names used to wire
 * client behaviour. Nothing in the view builds an id, section id, or attribute name from a raw string.
 */
object DomIds {

    /** A page section. [navLabel] non-null means it also appears as a header nav link. */
    data class Section(val id: String, val navLabel: String?)

    val HERO = Section("hero", null)
    val PROOF = Section("proof", "References")
    val PROJECTS = Section("projects", "Projects")
    val EXPERIENCE = Section("experience", "Experience")
    val SKILLS = Section("skills", "Skills")
    val CONTACT = Section("contact", "Contact")

    /** Order = order on the page and in scroll-spy. */
    val sections = listOf(HERO, PROOF, PROJECTS, EXPERIENCE, SKILLS, CONTACT)

    /** The sections that surface as header nav links. */
    val navSections = sections.filter { it.navLabel != null }

    fun navLinkId(section: Section) = "nav-${section.id}"

    const val APP_ROOT = "app-root"
}

/**
 * Typed names for the `data-*` attributes that mark elements for [Effects] / [SectionNavigator] to
 * enhance. Kept out of raw strings so a rename is compiler-checked at every call site.
 */
object Attrs {
    /** Value = section id to smooth-scroll to on click (nav links, CTAs). */
    const val SCROLL_TARGET = "data-scroll-target"
    /** Presence = element's text should decrypt/scramble into place on load. */
    const val DECRYPT = "data-decrypt"
    /** Value = integer to count up to on reveal (metric numbers). */
    const val COUNT_TO = "data-count-to"
    /** Value = hex accent color for the element (testimonial spotlight chips). */
    const val ACCENT_HEX = "data-accent"
}
