package rf.view

import kotlinx.css.*
import kotlinx.css.properties.*
import zoned.framework.interop.*

/** Selector-based CSS injected once into <head>: rules, pseudo-elements, states, keyframes, media queries. */
object Styles {

    fun inject() {
        styleSheet("landing-styles") {
            // Keyframes (typed; referenced by the rules below via animation()).
            val blink = keyframes("rf-blink") {
                at(0.pct, 100.pct) { opacity = 1 }
                at(50.pct) { opacity = 0 }
            }
            val spin = keyframes("rf-spin") { to { transform { rotate(360.deg) } } }
            val floatKf = keyframes("rf-float") {
                at(0.pct, 100.pct) { transform { translateY(0.px) } }
                at(50.pct) { transform { translateY((-10).px) } }
            }
            val mote = keyframes("rf-mote") {
                at(0.pct) { opacity = 0; transform { translateX(0.px); translateY(7.px) } }
                at(50.pct) { opacity = 0.9; transform { translateX(9.px); translateY((-7).px) } }
                at(100.pct) { opacity = 0; transform { translateX(18.px); translateY(7.px) } }
            }
            val nudge = keyframes("rf-nudge") {
                at(0.pct, 100.pct) { transform { translateX((-50).pct); translateY(0.px) } }
                at(50.pct) { transform { translateX((-50).pct); translateY(5.px) } }
            }
            // Experience-tab sizzle: a beacon ring on the active node, a spark of light running down the
            // spine, and a slow breath on the warm glow.
            val beacon = keyframes("rf-beacon") {
                at(0.pct) { opacity = 0.6; transform { scale(0.85) } }
                at(100.pct) { opacity = 0.0; transform { scale(2.4) } }
            }
            val breathe = keyframes("rf-breathe") {
                at(0.pct, 100.pct) { opacity = 0.7 }
                at(50.pct) { opacity = 1.0 }
            }
            val sparkKf = keyframes("rf-spark") {
                at(0.pct) { opacity = 0.0; top = 3.pct }
                at(14.pct) { opacity = 1.0 }
                at(86.pct) { opacity = 1.0 }
                at(100.pct) { opacity = 0.0; top = 97.pct }
            }
            val infinite = IterationCount("infinite")

            // --- Sections & layout ---------------------------------------------------------------
            rule(".${DECK}") {
                position = Position.fixed
                top = 0.px; left = 0.px; right = 0.px; bottom = 0.px
                important("perspective", "1600px")
                overflow = Overflow.hidden
            }
            // Each section is a full-screen, opaque rolodex panel hinged at the bottom of the screen;
            // [Deck] rotates them (old cards fall back, new cards flip in from the front).
            rule(".${SECTION}") {
                position = Position.absolute
                top = 0.px; left = 0.px; right = 0.px; bottom = 0.px
                // Opaque card carrying the blueprint grid (our nod to tech/makers) plus a soft accent
                // glow top-right; BG shows through the gaps between the faint grid lines.
                backgroundColor = Tokens.BG
                val glow = radialGradient(shape = "60% 45%", at = "82% 6%") {
                    stop(Tokens.ACCENT.withAlpha(0.10)); stop(Color.transparent, 60.pct)
                }
                val gridV = linearGradient(direction = 180.deg) {
                    stop(Color.white.withAlpha(0.022), 1.px); stop(Color.transparent, 1.px)
                }
                val gridH = linearGradient(direction = 90.deg) {
                    stop(Color.white.withAlpha(0.022), 1.px); stop(Color.transparent, 1.px)
                }
                backgroundImage = Image("$glow, $gridV, $gridH")
                backgroundSize = "100% 100%, 46px 46px, 46px 46px"
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
                overflow = Overflow.hidden
                padding = Padding(84.px, 0.px, 40.px, 0.px)
                transformOrigin("center bottom")
                backfaceVisibility(BackfaceVisibility.HIDDEN)
                transition("transform", 0.66.s, Timing("cubic-bezier(0.4, 0.0, 0.2, 1)"))
            }
            rule(".${WRAP}") {
                width = 100.pct
                maxWidth = 1080.px
                margin = Margin(0.px, LinearDimension.auto)
                padding = Padding(0.px, 24.px)
            }
            rule(".${MONO_LABEL}") {
                fontFamily = Tokens.MONO
                fontSize = 12.px
                letterSpacing = 0.16.em
                textTransform = TextTransform.uppercase
                color = Tokens.ACCENT_SOFT
                display = Display.block
                marginBottom = 32.px
            }
            rule(".${MONO_LABEL}::before") { content = QuotedString("// ") }
            rule(".${SECTION_LEAD}") {
                color = Tokens.MUTED
                fontSize = 18.px
                maxWidth = 640.px
                marginBottom = 40.px
                lineHeight = LineHeight("1.5")
            }

            // --- Header / nav --------------------------------------------------------------------
            rule(".${HEADER}") {
                position = Position.fixed
                top = 0.px; left = 0.px; right = 0.px
                height = 64.px
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.spaceBetween
                padding = Padding(0.px, 24.px)
                zIndex = 50
                backgroundColor = Tokens.BG.withAlpha(0.72)
                backdropFilter = blur(10.px).toString()
                borderBottom = Border(1.px, BorderStyle.solid, Tokens.LINE_SOFT)
            }
            rule(".${BRAND}") {
                fontFamily = Tokens.MONO
                fontWeight = FontWeight("700")
                color = Tokens.TEXT
                letterSpacing = 0.02.em
            }
            rule(".${NAV}") { display = Display.flex; gap = 28.px; alignItems = Align.center }
            rule(".${NAV_LINK}") {
                fontFamily = Tokens.MONO
                fontSize = 15.px
                color = Tokens.MUTED
                transition("color", 0.2.s)
            }
            rule(".${NAV_LINK}:hover") { color = Tokens.TEXT }
            rule(".${NAV_LINK}.${IS_ACTIVE}") { color = Tokens.ACCENT_SOFT }

            // --- Buttons -------------------------------------------------------------------------
            rule(".${CTA}") {
                fontFamily = Tokens.MONO
                fontSize = 14.px
                color = Tokens.TEXT
                backgroundColor = Tokens.ACCENT
                padding = Padding(11.px, 20.px)
                borderRadius = 5.px
                transition("transform", 0.15.s)
                transition("filter", 0.2.s)
            }
            rule(".${CTA}:hover") { filter(brightness(1.12)) }
            rule(".${CTA_GHOST}") {
                fontFamily = Tokens.MONO
                fontSize = 14.px
                color = Tokens.TEXT
                padding = Padding(11.px, 20.px)
                borderRadius = 5.px
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                transition("border-color", 0.2.s)
                transition("color", 0.2.s)
            }
            rule(".${CTA_GHOST}:hover") { borderColor = Tokens.ACCENT_SOFT; color = Tokens.ACCENT_SOFT }
            rule(".${BRAND}, .${NAV_LINK}, .${CTA}, .${CTA_GHOST}") {
                cursor = Cursor.pointer
            }

            // --- Hero ----------------------------------------------------------------------------
            rule(".${HERO}") {
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
            }
            rule(".${HERO_HEAD}") {
                fontSize = clamp(42.px, 7.vw, 88.px)
                fontWeight = FontWeight("800")
                lineHeight = LineHeight("1.03")
                letterSpacing = (-0.025).em
                margin = Margin(0.px)
            }
            rule(".${TERMINAL}") {
                fontFamily = Tokens.MONO
                fontSize = clamp(15.px, 2.2.vw, 20.px)
                color = Tokens.ACCENT_SOFT
                marginTop = 28.px
            }
            rule(".${CURSOR}") {
                display = Display.inlineBlock
                width = 9.px
                height = 1.05.em
                backgroundColor = Tokens.ACCENT
                marginLeft = 5.px
                verticalAlign = VerticalAlign("-0.15em")
                animation(blink, 1.05.s, Timing("steps(1)"), iterationCount = infinite)
            }
            rule(".${CHIP_ROW}") {
                display = Display.flex
                flexWrap = FlexWrap.wrap
                gap = 10.px
                marginTop = 36.px
            }
            rule(".${CHIP}") {
                fontFamily = Tokens.MONO
                fontSize = 12.px
                color = Tokens.MUTED
                padding = Padding(6.px, 13.px)
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                borderRadius = 999.px
            }
            rule(".${HERO_QUOTE}") {
                color = Tokens.MUTED
                fontStyle = FontStyle.italic
                maxWidth = 540.px
                marginTop = 40.px
            }
            rule(".${HERO_QUOTE_ROLE}") {
                fontFamily = Tokens.MONO
                fontSize = 12.px
                fontStyle = FontStyle.normal
                color = Tokens.ACCENT_SOFT
                marginTop = 16.px
                display = Display.inlineBlock
            }

            // Hero portrait: a static image inside a slowly-rotating conic ring, gently floating.
            rule(".${HERO_GRID}") {
                display = Display.grid
                gridTemplateColumns = GridTemplateColumns("1fr auto")
                gap = 56.px
                alignItems = Align.center
            }
            rule(".${HERO_MEDIA}") {
                position = Position.relative
                justifySelf = JustifySelf.center
                animation(floatKf, 6.s, Timing("ease-in-out"), iterationCount = infinite)
            }
            rule(".${HEADSHOT_FRAME}") {
                position = Position.relative
                width = clamp(220.px, 26.vw, 340.px)
                aspectRatio = AspectRatio("1")
                borderRadius = 50.pct
            }
            rule(".${HEADSHOT_FRAME}::before") {
                content = QuotedString("")
                position = Position.absolute
                top = (-4).px; left = (-4).px; right = (-4).px; bottom = (-4).px
                borderRadius = 50.pct
                backgroundImage = conicGradient(from = 0.deg) {
                    stop(Tokens.ACCENT)
                    stop(Tokens.ACCENT_SOFT)
                    stop(Color.transparent, 55.pct)
                    stop(Tokens.ACCENT)
                }
                animation(spin, 12.s, Timing.linear, iterationCount = infinite)
                boxShadow += BoxShadow(Tokens.ACCENT.withAlpha(0.55), 0.px, 0.px, 70.px, (-14).px)
                zIndex = 0
            }
            rule(".${HEADSHOT_IMG}") {
                position = Position.relative
                zIndex = 1
                width = 100.pct
                height = 100.pct
                borderRadius = 50.pct
                objectFit = ObjectFit.cover
                backgroundColor = Tokens.BG
            }

            // --- Proof: testimonial spotlight + metrics ------------------------------------------
            // One card visible at a time (JS toggles is-active); cards stack absolutely so switching
            // never reflows. Words fade + rise in a per-word stagger for a "types-in" reveal.
            rule(".${SPOTLIGHT}") {
                position = Position.relative
                width = 100.pct
                minHeight = clamp(240.px, 40.vh, 360.px)
                margin = Margin(10.px, 0.px, 18.px, 0.px)
            }
            rule(".${TESTIMONIAL_CARD}") {
                position = Position.absolute
                top = 0.px; left = 0.px; right = 0.px; bottom = 0.px
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
                gap = 22.px
                opacity = 0.0
                visibility = Visibility.hidden
                pointerEvents = PointerEvents.none
                transform { translateY(8.px) }
                transition("opacity", 0.4.s)
                transition("transform", 0.5.s)
            }
            rule(".${TESTIMONIAL_CARD}.${IS_ACTIVE}") {
                opacity = 1.0
                visibility = Visibility.visible
                pointerEvents = PointerEvents.auto
                transform { translateY(0.px) }
            }
            rule(".${TESTIMONIAL_MARK}") {
                fontFamily = "Georgia, 'Times New Roman', serif"
                fontSize = 68.px
                lineHeight = LineHeight("1")
                height = 34.px
                fontWeight = FontWeight("700")
                opacity = 0.85
            }
            rule(".${TESTIMONIAL_QUOTE}") {
                fontSize = clamp(19.px, 2.3.vw, 29.px)
                lineHeight = LineHeight("1.45")
                fontWeight = FontWeight("500")
                letterSpacing = (-0.01).em
                color = Tokens.TEXT
                maxWidth = 60.ch
            }
            rule(".${TESTIMONIAL_WORD}") {
                display = Display.inlineBlock
                opacity = 0.0
                transform { translateY(0.4.em) }
                transition("opacity", 0.5.s)
                transition("transform", 0.5.s)
            }
            rule(".${TESTIMONIAL_CARD}.${IS_ACTIVE} .${TESTIMONIAL_WORD}") {
                opacity = 1.0
                transform { translateY(0.px) }
            }
            rule(".${TESTIMONIAL_AUTHOR}") {
                display = Display.flex
                alignItems = Align.center
                gap = 14.px
            }
            rule(".${TESTIMONIAL_BADGE}") {
                width = 46.px; height = 46.px
                borderRadius = 50.pct
                border = Border(2.px, BorderStyle.solid, Tokens.LINE)
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.center
                fontFamily = Tokens.MONO
                fontWeight = FontWeight("700")
                fontSize = 18.px
                flexShrink = 0.0
            }
            rule(".${TESTIMONIAL_NAME}") {
                fontWeight = FontWeight("700"); fontSize = 16.px; color = Tokens.TEXT
            }
            rule(".${TESTIMONIAL_META}") {
                fontFamily = Tokens.MONO; fontSize = 12.5.px; color = Tokens.MUTED; marginTop = 3.px
            }
            rule(".${SPOTLIGHT_RAIL}") {
                display = Display.flex
                flexWrap = FlexWrap.wrap
                justifyContent = JustifyContent.center
                gap = 10.px
            }
            rule(".${SPOTLIGHT_CHIP}") {
                width = 40.px; height = 40.px
                borderRadius = 50.pct
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.center
                fontFamily = Tokens.MONO
                fontSize = 15.px
                color = Tokens.MUTED
                cursor = Cursor.pointer
                userSelect = UserSelect.none
                transition("transform", 0.2.s)
                transition("background-color", 0.2.s)
                transition("color", 0.2.s)
                transition("border-color", 0.2.s)
            }
            rule(".${SPOTLIGHT_CHIP}:hover") {
                borderColor = Tokens.ACCENT_SOFT; color = Tokens.TEXT
            }
            rule(".${SPOTLIGHT_CHIP}.${IS_ACTIVE}") {
                color = Tokens.BG
                transform { scale(1.12) }
            }
            rule(".${SPOTLIGHT_PROGRESS}") {
                width = 100.pct
                maxWidth = 220.px
                height = 2.px
                backgroundColor = Tokens.LINE
                borderRadius = 2.px
                margin = Margin(14.px, LinearDimension.auto, 0.px, LinearDimension.auto)
                overflow = Overflow.hidden
            }
            rule(".${SPOTLIGHT_PROGRESS_FILL}") {
                height = 100.pct
                width = 0.px
                backgroundColor = Tokens.ACCENT_SOFT
            }
            rule(".${METRIC_BAND}") {
                display = Display.grid
                gridTemplateColumns = GridTemplateColumns("repeat(4, 1fr)")
                gap = 24.px
                marginTop = 26.px
                paddingTop = 24.px
                borderTop = Border(1.px, BorderStyle.solid, Tokens.LINE)
            }
            rule(".${METRIC_VALUE}") {
                fontSize = clamp(34.px, 5.vw, 46.px)
                fontWeight = FontWeight("800")
                backgroundImage = linearGradient(direction = 135.deg) { stop(Tokens.TEXT); stop(Tokens.ACCENT_SOFT) }
                backgroundClipText()
                webkitTextFillColor(Color.transparent)
            }
            rule(".${METRIC_LABEL}") {
                fontFamily = Tokens.MONO; fontSize = 13.5.px; color = Tokens.MUTED; marginTop = 8.px; lineHeight = LineHeight("1.4")
            }

            // --- Projects: expanding-panel accordion ---------------------------------------------
            // Panels sit side by side and flex-grow when active (JS opens on hover); the detail block
            // fades open only on the active panel. Collapsed panels stay as slim spines.
            rule(".${PROJECTS}") {
                display = Display.flex
                gap = 12.px
                height = clamp(400.px, 54.vh, 540.px)
                marginTop = 22.px
            }
            rule(".${PROJECT_PANEL}") {
                position = Position.relative
                flexGrow = 1.0
                flexShrink = 1.0
                flexBasis = FlexBasis("0")
                minWidth = 0.px
                overflow = Overflow.hidden
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.flexStart
                gap = 12.px
                padding = Padding(24.px, 20.px)
                borderRadius = 14.px
                backgroundColor = Tokens.CARD
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                color = Tokens.TEXT
                textDecoration = TextDecoration.none
                cursor = Cursor.pointer
                transition("flex-grow", 0.55.s, Timing("cubic-bezier(0.4, 0.0, 0.2, 1)"))
                transition("background-color", 0.3.s)
                transition("border-color", 0.3.s)
            }
            rule(".${PROJECT_PANEL}.${IS_ACTIVE}") {
                flexGrow = 4.5
                backgroundColor = Tokens.BG_ELEV
            }
            rule(".${PROJECT_INDEX}") {
                fontFamily = Tokens.MONO
                fontSize = clamp(26.px, 2.6.vw, 40.px)
                fontWeight = FontWeight("800")
                lineHeight = LineHeight("1")
            }
            rule(".${PROJECT_NAME}") {
                fontSize = clamp(16.px, 1.5.vw, 21.px)
                fontWeight = FontWeight("700")
                letterSpacing = (-0.01).em
                color = Tokens.TEXT
            }
            // Collapsed spines are narrow: turn the name vertical (and larger, since there's height to spare).
            rule(".${PROJECT_PANEL}:not(.${IS_ACTIVE}) .${PROJECT_NAME}") {
                important("writing-mode", "vertical-rl")
                whiteSpace = WhiteSpace.nowrap
                alignSelf = Align.center
                marginTop = 8.px
                fontSize = clamp(26.px, 2.6.vw, 36.px)
                letterSpacing = 0.02.em
            }
            // Detail stays in flow (clipped by the panel while collapsed) so opening only animates the
            // panel width; the text is faded in after the width settles, hiding the reflow.
            rule(".${PROJECT_DETAIL}") {
                display = Display.flex
                flexDirection = FlexDirection.column
                gap = 12.px
                opacity = 0.0
                transition("opacity", 0.2.s, Timing.ease)
            }
            rule(".${PROJECT_PANEL}.${IS_ACTIVE} .${PROJECT_DETAIL}") {
                opacity = 1.0
                transition("opacity", 0.3.s, Timing.ease, 0.28.s)
            }
            rule(".${PROJECT_TAGLINE}") {
                fontFamily = Tokens.MONO; fontSize = 13.px
            }
            rule(".${PROJECT_BLURB}") {
                color = Tokens.MUTED; fontSize = 14.px; lineHeight = LineHeight("1.55"); maxWidth = 46.ch
            }
            rule(".${TAG_ROW}") { display = Display.flex; flexWrap = FlexWrap.wrap; gap = 8.px }
            rule(".${TAG}") {
                fontFamily = Tokens.MONO; fontSize = 11.px; color = Tokens.FAINT
                padding = Padding(3.px, 9.px); borderRadius = 4.px
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
            }
            rule(".${CARD_META}") { fontFamily = Tokens.MONO; fontSize = 11.px; color = Tokens.ACCENT }

            // --- Experience timeline -------------------------------------------------------------
            rule(".${TIMELINE}") {
                position = Position.relative
                display = Display.flex
                flexDirection = FlexDirection.column
                maxWidth = 780.px
                margin = Margin(0.px, LinearDimension.auto)
            }
            // A single warm blob (positioned by Effects.timeline) that slides smoothly behind the focused
            // role. The gradient fades fully to transparent well inside its own box, so it reads as a soft
            // amorphous cloud with no square edge, and bleeds left over the page bg.
            // Knockout glow: a big warm blob that extends past the active card on every side, with the card's
            // own rectangle punched out (mask-composite: exclude). The hole lets the aligned page grid show
            // straight through, while the warm cloud wraps the whole card and bleeds out over the bg.
            rule(".${TL_ROW}.${IS_ACTIVE} .${TL_MAIN}::after") {
                content = QuotedString("")
                position = Position.absolute
                top = (-180).px; left = (-180).px; right = (-180).px; bottom = (-180).px
                zIndex = -1
                pointerEvents = PointerEvents.none
                backgroundImage = radialGradient(shape = "closest-side", at = "50% 50%") {
                    stop(Color("#ff7a1a").withAlpha(0.34))
                    stop(Color("#f97316").withAlpha(0.17), 40.pct)
                    stop(Color("#f43f5e").withAlpha(0.06), 70.pct)
                    stop(Color.transparent)
                }
                // Two mask layers: a full cover minus a centred card-sized rectangle -> a card-shaped hole.
                val fill = "linear-gradient(#000, #000)"
                important("mask-image", "$fill, $fill")
                important("mask-size", "100% 100%, calc(100% - 360px) calc(100% - 360px)")
                important("mask-position", "center, center")
                important("mask-repeat", "no-repeat, no-repeat")
                important("mask-composite", "exclude")
                important("-webkit-mask-image", "$fill, $fill")
                important("-webkit-mask-size", "100% 100%, calc(100% - 360px) calc(100% - 360px)")
                important("-webkit-mask-position", "center, center")
                important("-webkit-mask-repeat", "no-repeat, no-repeat")
                important("-webkit-mask-composite", "xor")
                animation(breathe, 5.s, Timing("ease-in-out"), iterationCount = infinite)
            }
            // A bright spark of light running down the spine (sits on the spine, behind the node badges).
            rule(".${TL_SPARK}") {
                position = Position.absolute
                left = 24.px
                width = 8.px
                height = 8.px
                borderRadius = 50.pct
                zIndex = 0
                pointerEvents = PointerEvents.none
                backgroundColor = Color("#ffe0b0")
                boxShadow += BoxShadow(Color("#ff8a3a"), 0.px, 0.px, 16.px, 3.px)
                animation(sparkKf, 3.6.s, Timing.linear, iterationCount = infinite)
            }
            rule(".${TIMELINE}::before") {
                content = QuotedString("")
                position = Position.absolute
                top = 20.px; bottom = 20.px
                left = 27.px
                width = 2.px
                zIndex = 0
                // Glowing spine through a warm/hot spectrum, sitting behind the nodes.
                backgroundImage = linearGradient(direction = 180.deg) {
                    stop(Color.transparent)
                    stop(Color("#ff5e3a").withAlpha(0.9), 5.pct)
                    stop(Color("#ff7a1a").withAlpha(0.9), 30.pct)
                    stop(Color("#ffab2e").withAlpha(0.9), 50.pct)
                    stop(Color("#f43f5e").withAlpha(0.85), 72.pct)
                    stop(Tokens.SLATE.withAlpha(0.6), 90.pct)
                    stop(Color.transparent)
                }
                boxShadow += BoxShadow(Color("#ff7a1a").withAlpha(0.45), 0.px, 0.px, 9.px)
            }
            rule(".${TL_ROW}") {
                position = Position.relative
                display = Display.grid
                gridTemplateColumns = GridTemplateColumns("56px 1fr")
                gap = 18.px
                padding = Padding(8.px, 0.px)
                alignItems = Align.flexStart
                cursor = Cursor.pointer
            }
            rule(".${TL_NODE}") {
                justifySelf = JustifySelf.center
                position = Position.relative
                zIndex = 1
                width = 42.px
                height = 42.px
                borderRadius = 50.pct
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.center
                backgroundColor = Tokens.BG_ELEV
                border = Border(1.px, BorderStyle.solid, Tokens.ACCENT)
                boxShadow += BoxShadow(Tokens.ACCENT.withAlpha(0.35), 0.px, 0.px, 18.px, (-6).px)
                transition("transform", 0.3.s, Timing.ease)
            }
            rule(".${TL_MONO}") {
                fontFamily = Tokens.MONO
                fontSize = 15.px
                fontWeight = FontWeight("700")
                color = Tokens.ACCENT_SOFT
            }
            for (i in 1..8) {
                rule(".${TL_ROW}:nth-child($i)") {
                    transition("opacity", 0.6.s, Timing.ease, ((i - 1) * 0.08).s)
                    transition("transform", 0.6.s, Timing.ease, ((i - 1) * 0.08).s)
                }
            }
            // Every row carries the same horizontal inset, so the heading + years sit at a fixed x. The
            // active card's outline then extends out into that inset without nudging any text.
            rule(".${TL_MAIN}") {
                position = Position.relative
                display = Display.flex
                flexDirection = FlexDirection.column
                padding = Padding(11.px, 18.px)
                transformOrigin("left center")
                transition("transform", 0.3.s, Timing.ease)
                transition("opacity", 0.3.s, Timing.ease)
            }
            // The open row scales up + brightens; the rest sit back a touch.
            // The open role sits in a blue-outlined card (over the fixed inset) wired back to the spine.
            rule(".${TL_ROW}.${IS_ACTIVE} .${TL_MAIN}") {
                // Transparent interior: the outline frames the role and the page grid shows straight through.
                backgroundColor = Color.transparent
                border = Border(1.px, BorderStyle.solid, Tokens.ACCENT)
                borderRadius = 10.px
                boxShadow += BoxShadow(Tokens.ACCENT.withAlpha(0.28), 0.px, 0.px, 26.px, (-8).px)
            }
            // Connector: a short blue line joining the card's left edge back to the node on the spine.
            rule(".${TL_ROW}.${IS_ACTIVE} .${TL_MAIN}::before") {
                content = QuotedString("")
                position = Position.absolute
                top = 21.px
                left = (-20).px
                width = 20.px
                height = 2.px
                backgroundColor = Tokens.ACCENT
                boxShadow += BoxShadow(Tokens.ACCENT.withAlpha(0.5), 0.px, 0.px, 6.px)
            }
            rule(".${TL_ROW}:not(.${IS_ACTIVE}) .${TL_MAIN}") { opacity = 0.72 }
            rule(".${TL_ROW}.${IS_ACTIVE} .${TL_NODE}") { transform { scale(1.15) } }
            // Beacon: a warm ring that pulses out of the active node, marking the focused role.
            rule(".${TL_ROW}.${IS_ACTIVE} .${TL_NODE}::after") {
                content = QuotedString("")
                position = Position.absolute
                top = 0.px; left = 0.px; right = 0.px; bottom = 0.px
                borderRadius = 50.pct
                border = Border(2.px, BorderStyle.solid, Color("#ff8a3a"))
                pointerEvents = PointerEvents.none
                animation(beacon, 2.4.s, Timing("ease-out"), iterationCount = infinite)
            }
            rule(".${TL_HEAD}") {
                display = Display.flex
                alignItems = Align.baseline
                flexWrap = FlexWrap.wrap
                gap = 12.px
            }
            rule(".${TL_ROLE}") { fontWeight = FontWeight("600"); fontSize = 17.px; color = Tokens.TEXT }
            rule(".${TL_WHAT}") { fontFamily = Tokens.MONO; fontSize = 13.px }
            rule(".${TL_WHEN}") {
                fontFamily = Tokens.MONO; fontSize = 12.px; color = Tokens.MUTED; marginLeft = LinearDimension.auto
                transition("color", 0.3.s, Timing.ease)
            }
            // Focused role lights its date range bright blue, echoing the card outline.
            rule(".${TL_ROW}.${IS_ACTIVE} .${TL_WHEN}") {
                color = Color("#5aa2ff")
                textShadow += TextShadow(Color("#5aa2ff").withAlpha(0.55), 0.px, 0.px, 10.px)
            }
            // Body reveal: grid-rows 0fr→1fr animates the height smoothly with no known target height.
            rule(".${TL_DETAIL}") {
                display = Display.grid
                gridTemplateRows = GridTemplateRows("0fr")
                transition("grid-template-rows", 0.4.s, Timing("cubic-bezier(0.4, 0.0, 0.2, 1)"))
            }
            rule(".${TL_DETAIL} > div") {
                overflow = Overflow.hidden
                opacity = 0.0
                transition("opacity", 0.25.s, Timing.ease)
            }
            rule(".${TL_ROW}.${IS_ACTIVE} .${TL_DETAIL}") {
                gridTemplateRows = GridTemplateRows("1fr")
            }
            rule(".${TL_ROW}.${IS_ACTIVE} .${TL_DETAIL} > div") {
                opacity = 1.0
                transition("opacity", 0.3.s, Timing.ease, 0.18.s)
            }
            rule(".${TL_BODY}") {
                color = Tokens.MUTED; fontSize = 14.px; lineHeight = LineHeight("1.5"); marginTop = 8.px; maxWidth = 62.ch
            }
            // "Haze of time" for the unshown early-career years: just glowing motes drifting on the
            // page background (no mist/fade), revealed when the row is opened.
            rule(".${HAZE}") {
                position = Position.relative
                height = 64.px
                overflow = Overflow.hidden
            }
            rule(".${HAZE_MOTE}") {
                position = Position.absolute
                borderRadius = 50.pct
                backgroundImage = radialGradient(shape = "circle") {
                    stop(Tokens.SLATE.withAlpha(0.9)); stop(Color.transparent, 70.pct)
                }
                filter(blur(1.px))
            }
            for (i in 1..18) {
                val leftPct = (i * 53 % 96) + 2
                val topPx = (i * 29 % 44) + 8
                val size = 5 + (i % 4) * 2
                val dur = 3.4 + (i % 5) * 0.5
                val delay = (i % 7) * 0.4
                rule(".${HAZE_MOTE}:nth-child($i)") {
                    left = leftPct.pct
                    top = topPx.px
                    width = size.px
                    height = size.px
                    animation(mote, dur.s, Timing("ease-in-out"), delay = delay.s, iterationCount = infinite)
                }
            }

            // --- Contact -------------------------------------------------------------------------
            // --- Skills: three.js constellation --------------------------------------------------
            rule(".${SKILLS_STAGE}") {
                position = Position.relative
                width = 100.pct
                height = clamp(440.px, 66.vh, 680.px)
                overflow = Overflow.hidden
                cursor = Cursor.grab
                userSelect = UserSelect.none
            }
            rule(".${SKILLS_STAGE}:active") { cursor = Cursor.grabbing }
            rule(".${SKILLS_CANVAS}") {
                position = Position.absolute
                top = 0.px; left = 0.px
            }
            // The CSS2D label overlay sits on top; it must be click-through so drags reach the canvas.
            rule(".${SKILLS_LABELS}") {
                position = Position.absolute
                top = 0.px; left = 0.px
                pointerEvents = PointerEvents.none
            }
            rule(".${SKILLS_TOGGLE}") {
                fontFamily = Tokens.MONO; fontSize = 13.px; fontWeight = FontWeight("700"); letterSpacing = 0.04.em
                color = Tokens.TEXT
                padding = Padding(8.px, 16.px)
                borderRadius = 9.px
                backgroundColor = Tokens.ACCENT.withAlpha(0.16)
                border = Border(1.px, BorderStyle.solid, Tokens.ACCENT)
                cursor = Cursor.pointer
                transition("background-color", 0.2.s)
            }
            rule(".${SKILLS_TOGGLE}:hover") { backgroundColor = Tokens.ACCENT.withAlpha(0.3) }
            // Labels sit on a partly-opaque pill so they stay legible over nodes/stars behind them.
            rule(".${SK_HUB_LABEL}, .${SK_LEAF_LABEL}, .${SK_CORE_LABEL}") {
                display = Display.inlineBlock
                whiteSpace = WhiteSpace.nowrap
                pointerEvents = PointerEvents.none
                padding = Padding(2.px, 8.px)
                borderRadius = 999.px
                backgroundColor = Tokens.BG.withAlpha(0.62)
                border = Border(1.px, BorderStyle.solid, Tokens.LINE_SOFT)
            }
            rule(".${SK_HUB_LABEL}") {
                fontFamily = Tokens.SANS; fontSize = 14.px; fontWeight = FontWeight("600"); color = Tokens.TEXT
            }
            rule(".${SK_LEAF_LABEL}") {
                fontFamily = Tokens.MONO; fontSize = 12.px; color = Tokens.TEXT
            }
            rule(".${SK_CORE_LABEL}") {
                fontFamily = Tokens.MONO; fontSize = 14.px; fontWeight = FontWeight("700"); color = Tokens.TEXT
            }
            // Compound selectors so this always wins the cascade over .skills-stage/.skills-grid's own
            // `display`, regardless of declaration order (equal specificity would otherwise tie-break
            // on source order, and the two rules below are declared later in the sheet).
            rule(".${SKILLS_STAGE}.${SKILLS_HIDDEN}, .${SKILLS_GRID}.${SKILLS_HIDDEN}") { display = Display.none }
            // 2D fallback: a flat grid of domain cards, each a compact chip list. Matches the globe's
            // footprint so toggling doesn't reflow the rest of the deck panel.
            rule(".${SKILLS_GRID}") {
                display = Display.grid
                gridTemplateColumns = GridTemplateColumns("repeat(auto-fill, minmax(190px, 1fr))")
                gridAutoRows = GridAutoRows("min-content")
                alignContent = Align.start
                gap = 14.px
                width = 100.pct
                height = clamp(440.px, 66.vh, 680.px)
                overflowY = Overflow.auto
            }
            rule(".${SKILLS_CARD}") {
                display = Display.flex
                flexDirection = FlexDirection.column
                gap = 10.px
                padding = Padding(14.px, 16.px)
                borderRadius = 10.px
                backgroundColor = Tokens.BG_ELEV
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                transition("opacity", 0.2.s)
            }
            rule(".${SKILLS_CARD}.${SKILLS_CARD_DIM}") { opacity = 0.3 }
            rule(".${SKILLS_CARD_NAME}") {
                fontFamily = Tokens.MONO; fontSize = 13.px; fontWeight = FontWeight("700"); letterSpacing = 0.04.em
                textTransform = TextTransform.uppercase
            }
            rule(".${SKILLS_CHIPS}") {
                display = Display.flex
                flexWrap = FlexWrap.wrap
                gap = 7.px
            }
            rule(".${SKILLS_CHIP}") {
                fontFamily = Tokens.MONO; fontSize = 12.px; color = Tokens.MUTED
                padding = Padding(3.px, 10.px)
                borderRadius = 999.px
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                transition("color", 0.15.s); transition("border-color", 0.15.s)
            }
            rule(".${SKILLS_CHIP}.${SKILLS_CHIP_MATCH}") { color = Tokens.TEXT; borderColor = Tokens.ACCENT }
            // Hacker-style skill search under the globe, with the 2D/3D toggle alongside it.
            rule(".${SKILLS_CONTROLS}") {
                display = Display.flex
                alignItems = Align.center
                gap = 14.px
                maxWidth = 340.px
                margin = Margin(40.px, LinearDimension.auto, 0.px, LinearDimension.auto)
            }
            rule(".${SKILLS_SEARCH}") {
                display = Display.flex
                alignItems = Align.center
                gap = 9.px
                flexGrow = 1.0
                padding = Padding(0.px, 0.px, 6.px, 0.px)
                borderBottom = Border(1.px, BorderStyle.solid, Tokens.LINE)
                transition("border-color", 0.2.s)
            }
            rule(".${SKILLS_SEARCH}:focus-within") { borderBottomColor = Tokens.ACCENT }
            rule(".${SKILLS_SEARCH_PROMPT}") {
                fontFamily = Tokens.MONO; fontSize = 15.px; fontWeight = FontWeight("700"); color = Tokens.ACCENT
            }
            rule(".${SKILLS_SEARCH_INPUT}") {
                flexGrow = 1.0
                backgroundColor = Color.transparent
                border = Border(0.px, BorderStyle.none, Color.transparent)
                outline = Outline.none
                color = Tokens.TEXT
                fontFamily = Tokens.MONO
                fontSize = 14.px
                important("caret-color", Tokens.ACCENT.value)
            }
            rule(".${SKILLS_SEARCH_INPUT}::placeholder") { color = Tokens.FAINT }

            rule(".${CONTACT_GRID}") {
                display = Display.grid
                gridTemplateColumns = GridTemplateColumns("repeat(3, minmax(0, 1fr))")
                gap = 12.px
                maxWidth = 640.px
                margin = Margin(0.px, LinearDimension.auto)
            }
            rule(".${CONTACT_LINK}") {
                position = Position.relative
                display = Display.flex
                flexDirection = FlexDirection.column
                gap = 4.px
                padding = Padding(16.px, 18.px)
                borderRadius = 12.px
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                backgroundColor = Tokens.CARD
                color = Tokens.TEXT
                textDecoration = TextDecoration.none
                transition("border-color", 0.2.s)
                transition("background-color", 0.2.s)
                transition("transform", 0.2.s)
            }
            rule(".${CONTACT_LINK}:hover") {
                borderColor = Tokens.ACCENT
                backgroundColor = Tokens.BG_ELEV
                transform { translateY((-2).px) }
            }
            rule(".${CONTACT_ICON}") {
                width = 20.px; height = 20.px; display = Display.block; color = Tokens.MUTED; marginBottom = 6.px
                transition("color", 0.2.s)
            }
            rule(".${CONTACT_LINK}:hover .${CONTACT_ICON}") { color = Tokens.ACCENT_SOFT }
            rule(".${CONTACT_LINK_LABEL}") { fontWeight = FontWeight("600"); fontSize = 15.px }
            rule(".${CONTACT_LINK_SUB}") {
                fontFamily = Tokens.MONO; fontSize = 12.px; color = Tokens.MUTED
                overflow = Overflow.hidden; textOverflow = TextOverflow.ellipsis; whiteSpace = WhiteSpace.nowrap
            }
            rule(".${CONTACT_LINK_ARROW}") {
                position = Position.absolute
                top = 14.px; right = 16.px
                fontFamily = Tokens.MONO; fontSize = 14.px; color = Tokens.FAINT
                transition("transform", 0.2.s)
                transition("color", 0.2.s)
            }
            rule(".${CONTACT_LINK}:hover .${CONTACT_LINK_ARROW}") {
                color = Tokens.ACCENT_SOFT
                transform { translateX(2.px); translateY((-2).px) }
            }

            // --- Scroll reveal -------------------------------------------------------------------
            rule(".${REVEAL}") {
                opacity = 0
                transform { translateY(18.px) }
                transition("opacity", 0.6.s)
                transition("transform", 0.6.s)
            }
            rule(".${REVEAL}.${REVEALED}") { opacity = 1; transform { translateY(0.px) } }

            // Pressure meter + section dots on the right edge.
            rule(".${DECK_METER}") {
                position = Position.fixed
                right = 22.px
                top = 50.pct
                transform { translateY((-50).pct) }
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = Align.center
                gap = 14.px
                zIndex = 60
            }
            rule(".${DECK_METER_TRACK}") {
                position = Position.relative
                width = 4.px
                height = 90.px
                borderRadius = 2.px
                backgroundColor = Tokens.LINE
                overflow = Overflow.hidden
            }
            rule(".${DECK_METER_FILL}") {
                position = Position.absolute
                left = 0.px; right = 0.px; bottom = 0.px
                height = 0.px
                backgroundColor = Tokens.ACCENT
            }
            rule(".${DECK_DOTS}") {
                display = Display.flex
                flexDirection = FlexDirection.column
                gap = 9.px
            }
            rule(".${DECK_DOT}") {
                width = 7.px
                height = 7.px
                borderRadius = 50.pct
                border = Border(1.px, BorderStyle.solid, Tokens.FAINT)
                transition("background-color", 0.2.s)
                transition("border-color", 0.2.s)
            }
            rule(".${DECK_DOT}.${IS_ACTIVE}") {
                backgroundColor = Tokens.ACCENT
                borderColor = Tokens.ACCENT
            }
            // Per-card "next" chevron, bottom-center, gently nudging downward.
            rule(".${DECK_NEXT}") {
                position = Position.absolute
                bottom = 22.px
                left = 50.pct
                width = 40.px; height = 40.px
                display = Display.flex
                alignItems = Align.center
                justifyContent = JustifyContent.center
                borderRadius = 50.pct
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                backgroundColor = Tokens.BG_ELEV.withAlpha(0.6)
                color = Tokens.MUTED
                cursor = Cursor.pointer
                zIndex = 5
                animation(nudge, 1.8.s, Timing("ease-in-out"), iterationCount = infinite)
                transition("color", 0.2.s)
                transition("border-color", 0.2.s)
            }
            rule(".${DECK_NEXT}:hover") { color = Tokens.TEXT; borderColor = Tokens.ACCENT_SOFT }
            rule(".${DECK_NEXT} svg") { width = 20.px; height = 20.px; display = Display.block }

            // --- Responsive + reduced motion -----------------------------------------------------
            mediaMaxWidth(760.px) {
                rule(".${NAV}") { display = Display.none }
                rule(".${SECTION}") { padding = Padding(84.px, 0.px) }
                rule(".${HERO_GRID}") { gridTemplateColumns = GridTemplateColumns("1fr"); gap = 44.px }
                rule(".${HERO_MEDIA}") { justifySelf = JustifySelf.start; order = Order("-1") }
                rule(".${METRIC_BAND}") { gridTemplateColumns = GridTemplateColumns("repeat(2, 1fr)") }
                // Accordion doesn't fit horizontally on phones: stack the panels and open them all.
                rule(".${PROJECTS}") { flexDirection = FlexDirection.column; height = LinearDimension.auto }
                rule(".${PROJECT_PANEL}") { justifyContent = JustifyContent.flexStart }
                rule(".${PROJECT_DETAIL}") { opacity = 1.0; maxHeight = 600.px }
                // No hover on phones: keep every role's body open.
                rule(".${TL_DETAIL}") { gridTemplateRows = GridTemplateRows("1fr") }
                rule(".${TL_DETAIL} > div") { opacity = 1.0 }
                rule(".${CONTACT_GRID}") { gridTemplateColumns = GridTemplateColumns("repeat(2, 1fr)") }
            }
        }
    }
}
