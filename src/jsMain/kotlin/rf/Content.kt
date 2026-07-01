package rf

/**
 * All site copy in one place, so the content can be revised without touching layout/markup.
 * (Ported verbatim from the original jQuery site; refresh the wording here as needed.)
 */
object Content {

    const val FIRST_NAME = "Reuben"
    const val LAST_NAME = "Firmin"
    const val TAGLINE = "Experienced technology leader, entrepreneur, and 10x coder."

    const val EMAIL = "fromsite@flavor8.com"
    const val RESUME_URL = "/reubenfirmin.pdf"
    const val GITHUB_URL = "https://github.com/reubenfirmin/"
    const val LINKEDIN_URL = "https://www.linkedin.com/in/reubenfirmin/"
    const val BOOK_MEETING_URL = "http://reubencal.com"

    const val HEADSHOT = "/img/headshot.png"

    /** The three "what I do" cards under the intro banner. */
    data class Highlight(val title: String, val body: String)
    val highlights = listOf(
        Highlight(
            "Technology Leader",
            "Startup CTO (successful acquisition). VPE, Director Engineering. Hands on / architect."
        ),
        Highlight(
            "10x Coder",
            "Kotlin, Java, TypeScript, SQL, Python, Rust, Golang."
        ),
        Highlight(
            "Data & AI",
            "Databases, Postgres, Sqlite, Redshift, ElasticSearch, ML, NLP, GPT, LLM."
        ),
    )

    /** Recent-career roles. */
    data class Role(val company: String, val image: String, val body: String)
    val roles = listOf(
        Role(
            "4rc.io", "/img/work-alex-nowak.jpg",
            "Founder, 2013-2015. Virtual CTO consulting. Architecture, product design, recruiting, hands on coding."
        ),
        Role(
            "ExecVision", "/img/work-execvision.png",
            "CTO, 2015-2022. Joined after founding, grew through acquisition. Led technology and product."
        ),
        Role(
            "Mediafly", "/img/work-mediafly.png",
            "VPE, 2022-2023. Led integration of acquired products. De facto principal engineer."
        ),
    )

    /** Open source projects. */
    data class Project(val name: String, val url: String)
    val projects = listOf(
        Project("The Do Zone", "http://the.do.zone"),
        Project("sz - a better du", "https://github.com/reubenfirmin/sz"),
        Project("GPT email reply bot", "https://github.com/reubenfirmin/EmailReplyBot"),
        Project("Diycam", "https://github.com/reubenfirmin/diycam"),
    )

    /** Contact links (label to href). */
    data class Link(val label: String, val href: String)
    val contactLinks = listOf(
        Link("LinkedIn", LINKEDIN_URL),
        Link("GitHub", GITHUB_URL),
        Link("Book Meeting", BOOK_MEETING_URL),
        Link("Download Resume", RESUME_URL),
    )
}
