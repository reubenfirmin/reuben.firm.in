package rf.view

import kotlinx.css.Color

/** The design system's colours and font stacks. */
object Tokens {

    // Surfaces (near-black, slightly layered).
    val BG = Color("#0a0a0b")
    val BG_ELEV = Color("#111216")
    val CARD = Color("#101116")

    // Text.
    val TEXT = Color("#e9e9ec")
    val MUTED = Color("#9c9fa6")
    val FAINT = Color("#74767d")

    // Accent: the brand's electric blue.
    val ACCENT = Color("#2b47ff")
    val ACCENT_SOFT = Color("#7d90ff")

    // Timeline "pre-history" tier: a warm taupe for the haze years + degree.
    val SLATE = Color("#c99a6a")

    // Structure.
    val LINE = Color("#232428")
    val LINE_SOFT = Color("#191a1e")

    // Font stacks.
    const val SANS = "'Inter', system-ui, -apple-system, sans-serif"
    const val MONO = "'JetBrains Mono', ui-monospace, 'SF Mono', Menlo, monospace"
}
