package rf.view

import kotlinx.css.Color

/**
 * The single source of truth for the design system's colours and font stacks. No colour hex or font
 * name appears anywhere else — call sites read these tokens so a palette/typography change is one edit.
 */
object Tokens {

    // Surfaces (near-black, slightly layered).
    val BG = Color("#0a0a0b")
    val BG_ELEV = Color("#111216")
    val CARD = Color("#101116")

    // Text.
    val TEXT = Color("#e9e9ec")
    val MUTED = Color("#8b8e96")
    val FAINT = Color("#5b5e66")

    // Accent: the brand's electric blue.
    val ACCENT = Color("#2b47ff")
    val ACCENT_SOFT = Color("#7d90ff")

    // Timeline "pre-history" tier: the muted slate shared by Catalist, the haze years, and the degree.
    val SLATE = Color("#94a3b8")

    // Structure.
    val LINE = Color("#232428")
    val LINE_SOFT = Color("#191a1e")

    // Font stacks.
    const val SANS = "'Inter', system-ui, -apple-system, sans-serif"
    const val MONO = "'JetBrains Mono', ui-monospace, 'SF Mono', Menlo, monospace"
}
