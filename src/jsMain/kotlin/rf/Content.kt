package rf

/** All site copy in one place. */
object Content {

    const val FIRST_NAME = "Reuben"
    const val LAST_NAME = "Firmin"

    // Hero
    const val HEADLINE_1 = "Technology leadership,"
    const val HEADLINE_2 = "hands on the keys."
    const val TERMINAL_ROLE = "fractional cto / ciso"

    /** The hero terminal line decrypts through each of these in turn, then loops. */
    val terminalRoles = listOf(
        "fractional cto / ciso",
        "innovator",
        "entrepreneur",
        "project manager",
        "security & compliance",
        "product manager",
        "builder",
        "maker",
    )
    const val HERO_QUOTE = "“We never would have exited the business without Reuben's technology leadership.”"
    const val HERO_QUOTE_ROLE = "Steve Richard, Co-Founder, ExecVision"

    // Links / contact
    const val EMAIL = "rf@4rc.io"
    const val RESUME_URL = "/reubenfirmin.pdf"
    const val GITHUB_URL = "https://github.com/reubenfirmin"
    const val LINKEDIN_URL = "https://www.linkedin.com/in/reubenfirmin/"
    const val BOOK_URL = "http://reubencal.com"
    const val BUSINESS_URL = "https://4rc.io"

    /** Hero metric chips + the proof-band figures. [value] counts up on reveal; [suffix] is static. */
    data class Metric(val value: Int, val suffix: String, val label: String)
    val metrics = listOf(
        Metric(25, "+", "years in software engineering"),
        Metric(4, "", "C-level titles: CEO, CTO, CISO, chief architect"),
        Metric(2, "", "patents (hardware + data)"),
        Metric(1, "", "acquisition, led as CTO through exit"),
    )

    /** Short status chips shown in the hero. */
    val heroChips = listOf("CTO", "CISO", "25+ yrs", "SOC 2 / ISO 27001", "US citizen", "1099")

    const val HEADSHOT = "/img/headshot.png"

    /** LinkedIn recommendations. */
    data class Testimonial(val quote: String, val name: String, val title: String, val org: String? = null)
    val testimonials = listOf(
        Testimonial(
            "Reuben is an expert engineering leader who oversaw the creation of the ExecVision product, now part of Mediafly. Reuben is an innovator. I look forward to seeing what he does next.",
            "Carson Conant", "Founder & CEO", "Mediafly",
        ),
        Testimonial(
            "We went from deploying once every two weeks to shipping multiple times a week. As a non-technical founder, having someone like Reuben is a lifesaver. He's your guy.",
            "Chaim Schwartz", "CEO", "Finance Lobby",
        ),
        Testimonial(
            "Reuben has filled various roles as our needs evolved, including overseeing the SOC 2 process as our fractional CISO. His versatility and dedication have been invaluable.",
            "Bomee Jung", "Founder", "Cadence OneFive",
        ),
        Testimonial(
            "Reuben strategically managed our SOC 2 certification, streamlining the mundane tasks and minimizing team disruption. We maintained laser focus on product delivery without compromising our compliance requirements.",
            "Francois Huet", "Head of Engineering", "Cadence OneFive",
        ),
        Testimonial(
            "He became my technical cofounder. An architect and a hands-on CTO who can outperform most salespeople on a call. I can't wait to start another company with Reuben.",
            "David Stillman", "CEO", "ExecVision",
        ),
        Testimonial(
            "Reuben quickly understood complicated specifications, created an implementation plan, and single-handedly put together a talented team of developers.",
            "Daniel Schley", "Founder", "York Run",
        ),
        Testimonial(
            "Reuben helped us avoid the costly pitfalls of new companies. We achieved product-market fit with paying customers in less than a year.",
            "El Young", "Co-Founder", "Stream Bean AI",
        ),
        Testimonial(
            "I could not believe how hands-on Reuben could get. He's that person to rely on every step of the way. My first Tech Jefe, and it was life-changing.",
            "Jeffry Luna", "QA Engineer", "ExecVision",
        ),
        Testimonial(
            "We co-filed a patent and led development of a powerful health-research data platform. A rare combination of strong technical chops, solid project management, and good instincts sourcing talent.",
            "Mitch Praver", "CEO", "Devexi (client)",
        ),
        Testimonial(
            "He gave us a comprehensive set of security recommendations tailored to our needs, helped us implement best practices, and ran an engaging security-awareness session. We now feel more secure and better prepared thanks to his guidance.",
            "Lucas Gray", "Head of Engineering", "Alloy Health",
        ),
        Testimonial(
            "He is effective, thorough and quick. He thinks issues through and brings you solutions, not problems. I enthusiastically recommend his work.",
            "Tom Des Jardins", "Director of Digital Products",
        ),
        Testimonial(
            "Great depth and breadth in software engineering, and an incredibly fast study on the business requirements. I would definitely work with him again.",
            "Peter Connolly", "direct report",
        ),
    )

    /** Featured work. [meta], when set, adds a "↳ …" callout on the card. */
    data class Project(
        val name: String,
        val tagline: String,
        val blurb: String,
        val tags: List<String>,
        val url: String,
        val meta: String? = null,
    )
    val projects = listOf(
        Project(
            "The Do Zone", "Private, offline-first task manager",
            "A keyboard-driven \"3D kanban\" PWA: local-first, sync-enabled, and open source. Built on zoned.",
            listOf("Kotlin/JS", "PWA", "Local-first"), "https://github.com/reubenfirmin/thedozone",
        ),
        Project(
            "bui", "Sandbox untrusted code, without the bwrap flags",
            "A TUI to configure and launch bubblewrap sandboxes with network filtering. See the command before it runs. Handy for constraining agents like Claude Code.",
            listOf("Python", "TUI", "Sandboxing"), "https://github.com/reubenfirmin/bubblewrap-tui",
        ),
        Project(
            "TuneConsole", "A music recommender you can actually tune",
            "Learns a transparent, tunable model of your taste from your own library, discovers its distinct \"taste modes\", and drives new-artist discovery, rediscoveries, and auto-generated playlists. A local, fully inspectable alternative to a streaming service's black-box recommender.",
            listOf("Python", "ML", "Recommenders", "HTMX", "Alpine.js"), "https://github.com/reubenfirmin/tuneconsole",
        ),
        Project(
            "zoned", "Typesafe full-stack web apps in Kotlin",
            "SQL to CSS to routes: everything typed Kotlin, no raw strings.",
            listOf("Kotlin", "Framework", "HTMX"), "https://github.com/reubenfirmin/zoned", meta = "this site runs on it",
        ),
        Project(
            "qtools", "What's eating my disk, CPU, or memory?",
            "A small suite of fast, visual Linux CLI tools: dq (disk query) recurses across a thread pool and skips virtual/cross-device mounts; pq (process query) clusters processes by resolved identity, not just executable name. Donut charts on capable terminals, JSON for machines.",
            listOf("Rust"), "https://github.com/reubenfirmin/qtools",
        ),
        Project(
            "wildcams", "Local ML pipeline for wildlife cameras",
            "A CPU-feasible, locally-running ML pipeline that ingests wildlife-camera footage from SD cards and filters it down to real detections.",
            listOf("Python", "ML"), "https://github.com/reubenfirmin/wildcams",
        ),
        Project(
            "reuben.firm.in", "The site you're on",
            "A card-deck single-pager with a draggable WebGL skills globe that morphs into a 2D tree. Typed Kotlin/JS on zoned, three.js, shipped on Cloudflare.",
            listOf("Kotlin/JS", "three.js", "Cloudflare"), "https://github.com/reubenfirmin/reuben.firm.in", meta = "you're looking at it",
        ),
    )

    /** Experience timeline, newest first. Dates/titles from the LinkedIn export. [color] visually
     *  ties an org across entries (both 4rc.io stints share a colour). */
    data class Role(val period: String, val org: String, val title: String, val body: String, val color: String)
    val roles = listOf(
        Role("2023 - now", "4rc.io", "Fractional CTO / CISO",
            "Fractional technology leadership for early-stage startups: architecture, product, security (SOC 2 / ISO 27001), and AI engineering.", "#ff5e3a"),
        Role("2022 - 2023", "Mediafly", "VP Engineering",
            "Joined via the ExecVision acquisition. Integrated the platform into Mediafly and ran technical diligence for a strategic acquisition.", "#ffab2e"),
        Role("2015 - 2022", "ExecVision", "CTO",
            "Built the org and product from prototype to a successful acquisition. Scaled a fully-remote team to 25 engineers; owned SOC 2, architecture, and hands-on code.", "#ff7a1a"),
        Role("2013 - 2015", "4rc.io", "Founder / Virtual CTO",
            "Consulting CTO for early-stage startups: product, architecture, team sourcing, hands-on coding. Three successful engagements, culminating in ExecVision.", "#ff5e3a"),
        Role("2010 - 2013", "Catalist", "Chief Architect",
            "Set technical direction for big-data microtargeting products; managed teams and coded hands-on.", "#f43f5e"),
    )

    /** Education, shown at the foot of the timeline (after the animated gap for the early-career years). */
    val degree = Role("1999", "University of Aberdeen", "BSc (Hons), Computer Science", "Summa cum laude.", "#c99a6a")

    /** Skills constellation: each domain is a hub, its [skills] the leaf nodes. */
    data class SkillDomain(val name: String, val color: String, val skills: List<String>)
    val skillDomains = listOf(
        SkillDomain("Leadership", "#4f7cff", listOf("Fractional CTO / CISO", "Team building", "M&A diligence", "Agile / Scrum")),
        SkillDomain("Architecture", "#2dd4bf", listOf("System design", "Scalability", "REST APIs", "Design patterns")),
        SkillDomain("Security", "#10b981", listOf("SOC 2", "ISO 27001", "Compliance programs")),
        SkillDomain("Languages", "#8b5cf6", listOf("Kotlin", "Python", "Java", "Rust", "TypeScript")),
        SkillDomain("Web", "#ef4444", listOf("CSS", "Svelte", "HTMX", "Tailwind", "React", "Chrome Extensions")),
        SkillDomain("Data", "#f59e0b", listOf("PostgreSQL", "SQLite", "Redis", "Elasticsearch", "Data architecture", "SQL")),
        SkillDomain("AI", "#38bdf8", listOf("AI engineering", "LLM integration", "RAG", "ML pipelines", "Team adoption")),
        SkillDomain("DevOps", "#f472b6", listOf("Cloudflare", "Fly.io", "Render", "Docker", "Podman", "Shell scripting", "AWS", "Linux", "Observability", "GitOps")),
    )

    /** Contact links (label to href), each with a typed icon for the card. */
    enum class ContactIcon { MAIL, LINKEDIN, GITHUB, CALENDAR, GLOBE }
    data class Link(val label: String, val href: String, val icon: ContactIcon)
    val contactLinks = listOf(
        Link("Email", "mailto:$EMAIL", ContactIcon.MAIL),
        Link("LinkedIn", LINKEDIN_URL, ContactIcon.LINKEDIN),
        Link("GitHub", GITHUB_URL, ContactIcon.GITHUB),
        Link("Book a call", BOOK_URL, ContactIcon.CALENDAR),
        Link("4rc.io", BUSINESS_URL, ContactIcon.GLOBE),
    )
}
