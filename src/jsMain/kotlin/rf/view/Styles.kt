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
            val infinite = IterationCount("infinite")

            // --- Sections & layout ---------------------------------------------------------------
            rule(".${CssClasses.DECK}") {
                position = Position.fixed
                top = 0.px; left = 0.px; right = 0.px; bottom = 0.px
                important("perspective", "1600px")
                overflow = Overflow.hidden
            }
            // Each section is a full-screen, opaque rolodex panel hinged at the bottom of the screen;
            // [Deck] rotates them (old cards fall back, new cards flip in from the front).
            rule(".${CssClasses.SECTION}") {
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
            rule(".${CssClasses.WRAP}") {
                width = 100.pct
                maxWidth = 1080.px
                margin = Margin(0.px, LinearDimension.auto)
                padding = Padding(0.px, 24.px)
            }
            rule(".${CssClasses.MONO_LABEL}") {
                fontFamily = Tokens.MONO
                fontSize = 12.px
                letterSpacing = 0.16.em
                textTransform = TextTransform.uppercase
                color = Tokens.ACCENT_SOFT
                display = Display.block
                marginBottom = 32.px
            }
            rule(".${CssClasses.MONO_LABEL}::before") { content = QuotedString("// ") }
            rule(".${CssClasses.SECTION_LEAD}") {
                color = Tokens.MUTED
                fontSize = 18.px
                maxWidth = 640.px
                marginBottom = 40.px
                lineHeight = LineHeight("1.5")
            }

            // --- Header / nav --------------------------------------------------------------------
            rule(".${CssClasses.HEADER}") {
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
            rule(".${CssClasses.BRAND}") {
                fontFamily = Tokens.MONO
                fontWeight = FontWeight("700")
                color = Tokens.TEXT
                letterSpacing = 0.02.em
            }
            rule(".${CssClasses.NAV}") { display = Display.flex; gap = 28.px; alignItems = Align.center }
            rule(".${CssClasses.NAV_LINK}") {
                fontFamily = Tokens.MONO
                fontSize = 13.px
                color = Tokens.MUTED
                transition("color", 0.2.s)
            }
            rule(".${CssClasses.NAV_LINK}:hover") { color = Tokens.TEXT }
            rule(".${CssClasses.NAV_LINK}.${CssClasses.IS_ACTIVE}") { color = Tokens.ACCENT_SOFT }

            // --- Buttons -------------------------------------------------------------------------
            rule(".${CssClasses.CTA}") {
                fontFamily = Tokens.MONO
                fontSize = 14.px
                color = Tokens.TEXT
                backgroundColor = Tokens.ACCENT
                padding = Padding(11.px, 20.px)
                borderRadius = 5.px
                transition("transform", 0.15.s)
                transition("filter", 0.2.s)
            }
            rule(".${CssClasses.CTA}:hover") { filter(brightness(1.12)) }
            rule(".${CssClasses.CTA_GHOST}") {
                fontFamily = Tokens.MONO
                fontSize = 14.px
                color = Tokens.TEXT
                padding = Padding(11.px, 20.px)
                borderRadius = 5.px
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                transition("border-color", 0.2.s)
                transition("color", 0.2.s)
            }
            rule(".${CssClasses.CTA_GHOST}:hover") { borderColor = Tokens.ACCENT_SOFT; color = Tokens.ACCENT_SOFT }
            rule(".${CssClasses.BRAND}, .${CssClasses.NAV_LINK}, .${CssClasses.CTA}, .${CssClasses.CTA_GHOST}") {
                cursor = Cursor.pointer
            }

            // --- Hero ----------------------------------------------------------------------------
            rule(".${CssClasses.HERO}") {
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
            }
            rule(".${CssClasses.HERO_HEAD}") {
                fontSize = clamp(42.px, 7.vw, 88.px)
                fontWeight = FontWeight("800")
                lineHeight = LineHeight("1.03")
                letterSpacing = (-0.025).em
                margin = Margin(0.px)
            }
            rule(".${CssClasses.TERMINAL}") {
                fontFamily = Tokens.MONO
                fontSize = clamp(15.px, 2.2.vw, 20.px)
                color = Tokens.ACCENT_SOFT
                marginTop = 28.px
            }
            rule(".${CssClasses.CURSOR}") {
                display = Display.inlineBlock
                width = 9.px
                height = 1.05.em
                backgroundColor = Tokens.ACCENT
                marginLeft = 5.px
                verticalAlign = VerticalAlign("-0.15em")
                animation(blink, 1.05.s, Timing("steps(1)"), iterationCount = infinite)
            }
            rule(".${CssClasses.CHIP_ROW}") {
                display = Display.flex
                flexWrap = FlexWrap.wrap
                gap = 10.px
                marginTop = 36.px
            }
            rule(".${CssClasses.CHIP}") {
                fontFamily = Tokens.MONO
                fontSize = 12.px
                color = Tokens.MUTED
                padding = Padding(6.px, 13.px)
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                borderRadius = 999.px
            }
            rule(".${CssClasses.HERO_QUOTE}") {
                color = Tokens.MUTED
                fontStyle = FontStyle.italic
                maxWidth = 540.px
                marginTop = 40.px
            }
            rule(".${CssClasses.HERO_QUOTE_ROLE}") {
                fontFamily = Tokens.MONO
                fontSize = 12.px
                fontStyle = FontStyle.normal
                color = Tokens.ACCENT_SOFT
                marginTop = 16.px
                display = Display.inlineBlock
            }

            // Hero portrait: a static image inside a slowly-rotating conic ring, gently floating.
            rule(".${CssClasses.HERO_GRID}") {
                display = Display.grid
                gridTemplateColumns = GridTemplateColumns("1fr auto")
                gap = 56.px
                alignItems = Align.center
            }
            rule(".${CssClasses.HERO_MEDIA}") {
                position = Position.relative
                justifySelf = JustifySelf.center
                animation(floatKf, 6.s, Timing("ease-in-out"), iterationCount = infinite)
            }
            rule(".${CssClasses.HEADSHOT_FRAME}") {
                position = Position.relative
                width = clamp(220.px, 26.vw, 340.px)
                aspectRatio = AspectRatio("1")
                borderRadius = 50.pct
            }
            rule(".${CssClasses.HEADSHOT_FRAME}::before") {
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
            rule(".${CssClasses.HEADSHOT_IMG}") {
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
            rule(".${CssClasses.SPOTLIGHT}") {
                position = Position.relative
                width = 100.pct
                minHeight = clamp(240.px, 40.vh, 360.px)
                margin = Margin(10.px, 0.px, 18.px, 0.px)
            }
            rule(".${CssClasses.TESTIMONIAL_CARD}") {
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
            rule(".${CssClasses.TESTIMONIAL_CARD}.${CssClasses.IS_ACTIVE}") {
                opacity = 1.0
                visibility = Visibility.visible
                pointerEvents = PointerEvents.auto
                transform { translateY(0.px) }
            }
            rule(".${CssClasses.TESTIMONIAL_MARK}") {
                fontFamily = "Georgia, 'Times New Roman', serif"
                fontSize = 68.px
                lineHeight = LineHeight("1")
                height = 34.px
                fontWeight = FontWeight("700")
                opacity = 0.85
            }
            rule(".${CssClasses.TESTIMONIAL_QUOTE}") {
                fontSize = clamp(19.px, 2.3.vw, 29.px)
                lineHeight = LineHeight("1.45")
                fontWeight = FontWeight("500")
                letterSpacing = (-0.01).em
                color = Tokens.TEXT
                maxWidth = 60.ch
            }
            rule(".${CssClasses.TESTIMONIAL_WORD}") {
                display = Display.inlineBlock
                opacity = 0.0
                transform { translateY(0.4.em) }
                transition("opacity", 0.5.s)
                transition("transform", 0.5.s)
            }
            rule(".${CssClasses.TESTIMONIAL_CARD}.${CssClasses.IS_ACTIVE} .${CssClasses.TESTIMONIAL_WORD}") {
                opacity = 1.0
                transform { translateY(0.px) }
            }
            rule(".${CssClasses.TESTIMONIAL_AUTHOR}") {
                display = Display.flex
                alignItems = Align.center
                gap = 14.px
            }
            rule(".${CssClasses.TESTIMONIAL_BADGE}") {
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
            rule(".${CssClasses.TESTIMONIAL_NAME}") {
                fontWeight = FontWeight("700"); fontSize = 16.px; color = Tokens.TEXT
            }
            rule(".${CssClasses.TESTIMONIAL_META}") {
                fontFamily = Tokens.MONO; fontSize = 12.5.px; color = Tokens.MUTED; marginTop = 3.px
            }
            rule(".${CssClasses.SPOTLIGHT_RAIL}") {
                display = Display.flex
                flexWrap = FlexWrap.wrap
                justifyContent = JustifyContent.center
                gap = 10.px
            }
            rule(".${CssClasses.SPOTLIGHT_CHIP}") {
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
            rule(".${CssClasses.SPOTLIGHT_CHIP}:hover") {
                borderColor = Tokens.ACCENT_SOFT; color = Tokens.TEXT
            }
            rule(".${CssClasses.SPOTLIGHT_CHIP}.${CssClasses.IS_ACTIVE}") {
                color = Tokens.BG
                transform { scale(1.12) }
            }
            rule(".${CssClasses.SPOTLIGHT_PROGRESS}") {
                width = 100.pct
                maxWidth = 220.px
                height = 2.px
                backgroundColor = Tokens.LINE
                borderRadius = 2.px
                margin = Margin(14.px, LinearDimension.auto, 0.px, LinearDimension.auto)
                overflow = Overflow.hidden
            }
            rule(".${CssClasses.SPOTLIGHT_PROGRESS_FILL}") {
                height = 100.pct
                width = 0.px
                backgroundColor = Tokens.ACCENT_SOFT
            }
            rule(".${CssClasses.METRIC_BAND}") {
                display = Display.grid
                gridTemplateColumns = GridTemplateColumns("repeat(4, 1fr)")
                gap = 24.px
                marginTop = 26.px
                paddingTop = 24.px
                borderTop = Border(1.px, BorderStyle.solid, Tokens.LINE)
            }
            rule(".${CssClasses.METRIC_VALUE}") {
                fontSize = clamp(34.px, 5.vw, 46.px)
                fontWeight = FontWeight("800")
                backgroundImage = linearGradient(direction = 135.deg) { stop(Tokens.TEXT); stop(Tokens.ACCENT_SOFT) }
                backgroundClipText()
                webkitTextFillColor(Color.transparent)
            }
            rule(".${CssClasses.METRIC_LABEL}") {
                fontFamily = Tokens.MONO; fontSize = 12.px; color = Tokens.MUTED; marginTop = 8.px; lineHeight = LineHeight("1.4")
            }

            // --- Projects: expanding-panel accordion ---------------------------------------------
            // Panels sit side by side and flex-grow when active (JS opens on hover); the detail block
            // fades open only on the active panel. Collapsed panels stay as slim spines.
            rule(".${CssClasses.PROJECTS}") {
                display = Display.flex
                gap = 12.px
                height = clamp(400.px, 54.vh, 540.px)
                marginTop = 22.px
            }
            rule(".${CssClasses.PROJECT_PANEL}") {
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
            rule(".${CssClasses.PROJECT_PANEL}.${CssClasses.IS_ACTIVE}") {
                flexGrow = 4.5
                backgroundColor = Tokens.BG_ELEV
            }
            rule(".${CssClasses.PROJECT_INDEX}") {
                fontFamily = Tokens.MONO
                fontSize = clamp(26.px, 2.6.vw, 40.px)
                fontWeight = FontWeight("800")
                lineHeight = LineHeight("1")
            }
            rule(".${CssClasses.PROJECT_NAME}") {
                fontSize = clamp(16.px, 1.5.vw, 21.px)
                fontWeight = FontWeight("700")
                letterSpacing = (-0.01).em
                color = Tokens.TEXT
            }
            // Detail stays in flow (clipped by the panel while collapsed) so opening only animates the
            // panel width; the text is faded in after the width settles, hiding the reflow.
            rule(".${CssClasses.PROJECT_DETAIL}") {
                display = Display.flex
                flexDirection = FlexDirection.column
                gap = 12.px
                opacity = 0.0
                transition("opacity", 0.2.s, Timing.ease)
            }
            rule(".${CssClasses.PROJECT_PANEL}.${CssClasses.IS_ACTIVE} .${CssClasses.PROJECT_DETAIL}") {
                opacity = 1.0
                transition("opacity", 0.3.s, Timing.ease, 0.28.s)
            }
            rule(".${CssClasses.PROJECT_TAGLINE}") {
                fontFamily = Tokens.MONO; fontSize = 13.px
            }
            rule(".${CssClasses.PROJECT_BLURB}") {
                color = Tokens.MUTED; fontSize = 14.px; lineHeight = LineHeight("1.55"); maxWidth = 46.ch
            }
            rule(".${CssClasses.TAG_ROW}") { display = Display.flex; flexWrap = FlexWrap.wrap; gap = 8.px }
            rule(".${CssClasses.TAG}") {
                fontFamily = Tokens.MONO; fontSize = 11.px; color = Tokens.FAINT
                padding = Padding(3.px, 9.px); borderRadius = 4.px
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
            }
            rule(".${CssClasses.CARD_META}") { fontFamily = Tokens.MONO; fontSize = 11.px; color = Tokens.ACCENT }

            // --- Experience timeline -------------------------------------------------------------
            rule(".${CssClasses.TIMELINE}") { position = Position.relative; display = Display.flex; flexDirection = FlexDirection.column }
            rule(".${CssClasses.TIMELINE}::before") {
                content = QuotedString("")
                position = Position.absolute
                top = 20.px; bottom = 20.px
                left = 27.px
                width = 2.px
                backgroundImage = linearGradient(direction = 180.deg) {
                    stop(Color.transparent); stop(Tokens.LINE, 8.pct); stop(Tokens.LINE, 92.pct); stop(Color.transparent)
                }
            }
            rule(".${CssClasses.TL_ROW}") {
                position = Position.relative
                display = Display.grid
                gridTemplateColumns = GridTemplateColumns("56px 1fr")
                gap = 18.px
                padding = Padding(8.px, 0.px)
                alignItems = Align.flexStart
                cursor = Cursor.pointer
            }
            rule(".${CssClasses.TL_NODE}") {
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
            rule(".${CssClasses.TL_MONO}") {
                fontFamily = Tokens.MONO
                fontSize = 15.px
                fontWeight = FontWeight("700")
                color = Tokens.ACCENT_SOFT
            }
            for (i in 1..8) {
                rule(".${CssClasses.TL_ROW}:nth-child($i)") {
                    transition("opacity", 0.6.s, Timing.ease, ((i - 1) * 0.08).s)
                    transition("transform", 0.6.s, Timing.ease, ((i - 1) * 0.08).s)
                }
            }
            rule(".${CssClasses.TL_MAIN}") {
                display = Display.flex
                flexDirection = FlexDirection.column
                paddingTop = 9.px
                transformOrigin("left center")
                transition("transform", 0.3.s, Timing.ease)
                transition("opacity", 0.3.s, Timing.ease)
            }
            // The open row scales up + brightens; the rest sit back a touch.
            rule(".${CssClasses.TL_ROW}.${CssClasses.IS_ACTIVE} .${CssClasses.TL_MAIN}") { transform { scale(1.04) } }
            rule(".${CssClasses.TL_ROW}:not(.${CssClasses.IS_ACTIVE}) .${CssClasses.TL_MAIN}") { opacity = 0.5 }
            rule(".${CssClasses.TL_ROW}.${CssClasses.IS_ACTIVE} .${CssClasses.TL_NODE}") { transform { scale(1.15) } }
            rule(".${CssClasses.TL_HEAD}") {
                display = Display.flex
                alignItems = Align.baseline
                flexWrap = FlexWrap.wrap
                gap = 12.px
            }
            rule(".${CssClasses.TL_ROLE}") { fontWeight = FontWeight("600"); fontSize = 17.px; color = Tokens.TEXT }
            rule(".${CssClasses.TL_WHAT}") { fontFamily = Tokens.MONO; fontSize = 13.px }
            rule(".${CssClasses.TL_WHEN}") {
                fontFamily = Tokens.MONO; fontSize = 12.px; color = Tokens.MUTED; marginLeft = LinearDimension.auto
            }
            // Body reveal: grid-rows 0fr→1fr animates the height smoothly with no known target height.
            rule(".${CssClasses.TL_DETAIL}") {
                display = Display.grid
                gridTemplateRows = GridTemplateRows("0fr")
                transition("grid-template-rows", 0.4.s, Timing("cubic-bezier(0.4, 0.0, 0.2, 1)"))
            }
            rule(".${CssClasses.TL_DETAIL} > div") {
                overflow = Overflow.hidden
                opacity = 0.0
                transition("opacity", 0.25.s, Timing.ease)
            }
            rule(".${CssClasses.TL_ROW}.${CssClasses.IS_ACTIVE} .${CssClasses.TL_DETAIL}") {
                gridTemplateRows = GridTemplateRows("1fr")
            }
            rule(".${CssClasses.TL_ROW}.${CssClasses.IS_ACTIVE} .${CssClasses.TL_DETAIL} > div") {
                opacity = 1.0
                transition("opacity", 0.3.s, Timing.ease, 0.18.s)
            }
            rule(".${CssClasses.TL_BODY}") {
                color = Tokens.MUTED; fontSize = 14.px; lineHeight = LineHeight("1.5"); marginTop = 8.px; maxWidth = 62.ch
            }
            // "Haze of time" for the unshown early-career years: just glowing motes drifting on the
            // page background (no mist/fade) — revealed when the row is opened.
            rule(".${CssClasses.HAZE}") {
                position = Position.relative
                height = 64.px
                overflow = Overflow.hidden
            }
            rule(".${CssClasses.HAZE_MOTE}") {
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
                rule(".${CssClasses.HAZE_MOTE}:nth-child($i)") {
                    left = leftPct.pct
                    top = topPx.px
                    width = size.px
                    height = size.px
                    animation(mote, dur.s, Timing("ease-in-out"), delay = delay.s, iterationCount = infinite)
                }
            }

            // --- Contact -------------------------------------------------------------------------
            // --- Skills: three.js constellation --------------------------------------------------
            rule(".${CssClasses.SKILLS_STAGE}") {
                position = Position.relative
                width = 100.pct
                height = clamp(440.px, 66.vh, 680.px)
                cursor = Cursor.grab
                userSelect = UserSelect.none
            }
            rule(".${CssClasses.SKILLS_STAGE}:active") { cursor = Cursor.grabbing }
            rule(".${CssClasses.SKILLS_CANVAS}") {
                position = Position.absolute
                top = 0.px; left = 0.px
            }
            // The CSS2D label overlay sits on top; it must be click-through so drags reach the canvas.
            rule(".${CssClasses.SKILLS_LABELS}") {
                position = Position.absolute
                top = 0.px; left = 0.px
                pointerEvents = PointerEvents.none
            }
            rule(".${CssClasses.SKILLS_TOGGLE}") {
                position = Position.absolute
                top = 16.px; right = 16.px
                zIndex = 2
                fontFamily = Tokens.MONO; fontSize = 15.px; fontWeight = FontWeight("700"); letterSpacing = 0.04.em
                color = Tokens.TEXT
                padding = Padding(10.px, 20.px)
                borderRadius = 9.px
                backgroundColor = Tokens.ACCENT.withAlpha(0.16)
                border = Border(1.px, BorderStyle.solid, Tokens.ACCENT)
                cursor = Cursor.pointer
                transition("background-color", 0.2.s)
            }
            rule(".${CssClasses.SKILLS_TOGGLE}:hover") { backgroundColor = Tokens.ACCENT.withAlpha(0.3) }
            // Labels sit on a partly-opaque pill so they stay legible over nodes/stars behind them.
            rule(".${CssClasses.SK_HUB_LABEL}, .${CssClasses.SK_LEAF_LABEL}, .${CssClasses.SK_CORE_LABEL}") {
                display = Display.inlineBlock
                whiteSpace = WhiteSpace.nowrap
                pointerEvents = PointerEvents.none
                padding = Padding(2.px, 8.px)
                borderRadius = 999.px
                backgroundColor = Tokens.BG.withAlpha(0.62)
                border = Border(1.px, BorderStyle.solid, Tokens.LINE_SOFT)
            }
            rule(".${CssClasses.SK_HUB_LABEL}") {
                fontFamily = Tokens.SANS; fontSize = 14.px; fontWeight = FontWeight("600"); color = Tokens.TEXT
            }
            rule(".${CssClasses.SK_LEAF_LABEL}") {
                fontFamily = Tokens.MONO; fontSize = 12.px; color = Tokens.TEXT
            }
            rule(".${CssClasses.SK_CORE_LABEL}") {
                fontFamily = Tokens.MONO; fontSize = 14.px; fontWeight = FontWeight("700"); color = Tokens.TEXT
            }
            // Hacker-style skill search under the globe.
            rule(".${CssClasses.SKILLS_SEARCH}") {
                display = Display.flex
                alignItems = Align.center
                gap = 9.px
                maxWidth = 340.px
                margin = Margin(16.px, LinearDimension.auto, 0.px, LinearDimension.auto)
                padding = Padding(9.px, 14.px)
                borderRadius = 8.px
                backgroundColor = Tokens.BG_ELEV
                border = Border(1.px, BorderStyle.solid, Tokens.LINE)
                transition("border-color", 0.2.s)
            }
            rule(".${CssClasses.SKILLS_SEARCH}:focus-within") { borderColor = Tokens.ACCENT }
            rule(".${CssClasses.SKILLS_SEARCH_PROMPT}") {
                fontFamily = Tokens.MONO; fontSize = 15.px; fontWeight = FontWeight("700"); color = Tokens.ACCENT
            }
            rule(".${CssClasses.SKILLS_SEARCH_INPUT}") {
                flexGrow = 1.0
                backgroundColor = Color.transparent
                border = Border(0.px, BorderStyle.none, Color.transparent)
                outline = Outline.none
                color = Tokens.TEXT
                fontFamily = Tokens.MONO
                fontSize = 14.px
                important("caret-color", Tokens.ACCENT.value)
            }
            rule(".${CssClasses.SKILLS_SEARCH_INPUT}::placeholder") { color = Tokens.FAINT }

            rule(".${CssClasses.CONTACT_GRID}") {
                display = Display.grid
                gridTemplateColumns = GridTemplateColumns("repeat(3, minmax(0, 1fr))")
                gap = 12.px
                maxWidth = 640.px
            }
            rule(".${CssClasses.CONTACT_LINK}") {
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
            rule(".${CssClasses.CONTACT_LINK}:hover") {
                borderColor = Tokens.ACCENT
                backgroundColor = Tokens.BG_ELEV
                transform { translateY((-2).px) }
            }
            rule(".${CssClasses.CONTACT_ICON}") {
                width = 20.px; height = 20.px; display = Display.block; color = Tokens.MUTED; marginBottom = 6.px
                transition("color", 0.2.s)
            }
            rule(".${CssClasses.CONTACT_LINK}:hover .${CssClasses.CONTACT_ICON}") { color = Tokens.ACCENT_SOFT }
            rule(".${CssClasses.CONTACT_LINK_LABEL}") { fontWeight = FontWeight("600"); fontSize = 15.px }
            rule(".${CssClasses.CONTACT_LINK_SUB}") {
                fontFamily = Tokens.MONO; fontSize = 12.px; color = Tokens.MUTED
                overflow = Overflow.hidden; textOverflow = TextOverflow.ellipsis; whiteSpace = WhiteSpace.nowrap
            }
            rule(".${CssClasses.CONTACT_LINK_ARROW}") {
                position = Position.absolute
                top = 14.px; right = 16.px
                fontFamily = Tokens.MONO; fontSize = 14.px; color = Tokens.FAINT
                transition("transform", 0.2.s)
                transition("color", 0.2.s)
            }
            rule(".${CssClasses.CONTACT_LINK}:hover .${CssClasses.CONTACT_LINK_ARROW}") {
                color = Tokens.ACCENT_SOFT
                transform { translateX(2.px); translateY((-2).px) }
            }

            // --- Scroll reveal -------------------------------------------------------------------
            rule(".${CssClasses.REVEAL}") {
                opacity = 0
                transform { translateY(18.px) }
                transition("opacity", 0.6.s)
                transition("transform", 0.6.s)
            }
            rule(".${CssClasses.REVEAL}.${CssClasses.REVEALED}") { opacity = 1; transform { translateY(0.px) } }

            // Pressure meter + section dots on the right edge.
            rule(".${CssClasses.DECK_METER}") {
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
            rule(".${CssClasses.DECK_METER_TRACK}") {
                position = Position.relative
                width = 4.px
                height = 90.px
                borderRadius = 2.px
                backgroundColor = Tokens.LINE
                overflow = Overflow.hidden
            }
            rule(".${CssClasses.DECK_METER_FILL}") {
                position = Position.absolute
                left = 0.px; right = 0.px; bottom = 0.px
                height = 0.px
                backgroundColor = Tokens.ACCENT
            }
            rule(".${CssClasses.DECK_DOTS}") {
                display = Display.flex
                flexDirection = FlexDirection.column
                gap = 9.px
            }
            rule(".${CssClasses.DECK_DOT}") {
                width = 7.px
                height = 7.px
                borderRadius = 50.pct
                border = Border(1.px, BorderStyle.solid, Tokens.FAINT)
                transition("background-color", 0.2.s)
                transition("border-color", 0.2.s)
            }
            rule(".${CssClasses.DECK_DOT}.${CssClasses.IS_ACTIVE}") {
                backgroundColor = Tokens.ACCENT
                borderColor = Tokens.ACCENT
            }
            // Per-card "next" chevron, bottom-center, gently nudging downward.
            rule(".${CssClasses.DECK_NEXT}") {
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
            rule(".${CssClasses.DECK_NEXT}:hover") { color = Tokens.TEXT; borderColor = Tokens.ACCENT_SOFT }
            rule(".${CssClasses.DECK_NEXT} svg") { width = 20.px; height = 20.px; display = Display.block }

            // --- Responsive + reduced motion -----------------------------------------------------
            mediaMaxWidth(760.px) {
                rule(".${CssClasses.NAV}") { display = Display.none }
                rule(".${CssClasses.SECTION}") { padding = Padding(84.px, 0.px) }
                rule(".${CssClasses.HERO_GRID}") { gridTemplateColumns = GridTemplateColumns("1fr"); gap = 44.px }
                rule(".${CssClasses.HERO_MEDIA}") { justifySelf = JustifySelf.start; order = Order("-1") }
                rule(".${CssClasses.METRIC_BAND}") { gridTemplateColumns = GridTemplateColumns("repeat(2, 1fr)") }
                // Accordion doesn't fit horizontally on phones: stack the panels and open them all.
                rule(".${CssClasses.PROJECTS}") { flexDirection = FlexDirection.column; height = LinearDimension.auto }
                rule(".${CssClasses.PROJECT_PANEL}") { justifyContent = JustifyContent.flexStart }
                rule(".${CssClasses.PROJECT_DETAIL}") { opacity = 1.0; maxHeight = 600.px }
                // No hover on phones: keep every role's body open.
                rule(".${CssClasses.TL_DETAIL}") { gridTemplateRows = GridTemplateRows("1fr") }
                rule(".${CssClasses.TL_DETAIL} > div") { opacity = 1.0 }
                rule(".${CssClasses.CONTACT_GRID}") { gridTemplateColumns = GridTemplateColumns("repeat(2, 1fr)") }
            }
        }
    }
}
