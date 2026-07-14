---
name: CarolinianEvents
colors:
  surface: '#f8f9fa'
  surface-dim: '#d9dadb'
  surface-bright: '#f8f9fa'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f3f4f5'
  surface-container: '#edeeef'
  surface-container-high: '#e7e8e9'
  surface-container-highest: '#e1e3e4'
  on-surface: '#191c1d'
  on-surface-variant: '#434654'
  inverse-surface: '#2e3132'
  inverse-on-surface: '#f0f1f2'
  outline: '#737686'
  outline-variant: '#c3c5d7'
  surface-tint: '#1353d8'
  primary: '#003fb1'
  on-primary: '#ffffff'
  primary-container: '#1a56db'
  on-primary-container: '#d4dcff'
  inverse-primary: '#b5c4ff'
  secondary: '#795900'
  on-secondary: '#ffffff'
  secondary-container: '#ffc329'
  on-secondary-container: '#6f5100'
  tertiary: '#852b00'
  on-tertiary: '#ffffff'
  tertiary-container: '#ad3b00'
  on-tertiary-container: '#ffd4c5'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dbe1ff'
  primary-fixed-dim: '#b5c4ff'
  on-primary-fixed: '#00174d'
  on-primary-fixed-variant: '#003dab'
  secondary-fixed: '#ffdf9f'
  secondary-fixed-dim: '#f9bd22'
  on-secondary-fixed: '#261a00'
  on-secondary-fixed-variant: '#5c4300'
  tertiary-fixed: '#ffdbcf'
  tertiary-fixed-dim: '#ffb59a'
  on-tertiary-fixed: '#380d00'
  on-tertiary-fixed-variant: '#802a00'
  background: '#f8f9fa'
  on-background: '#191c1d'
  surface-variant: '#e1e3e4'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  title-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-lg:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.05em
  label-md:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  container-padding: 16px
  card-gap: 12px
---

## Brand & Style

The brand personality for this design system is organized, energetic, and community-focused. Designed for a campus environment, it prioritizes clarity and ease of navigation to help students discover and track events effortlessly. 

The aesthetic is rooted in **Modern Material (MD3)** principles, utilizing a structured but airy layout. It emphasizes functional minimalism through heavy use of white space, a disciplined color palette, and purposeful motion. The goal is to evoke a sense of reliability and excitement, ensuring the UI feels like a helpful companion rather than a complex tool.

## Colors

The color strategy uses a deep **Primary Blue (#1A56DB)** to represent the institutional trust and stability of a campus environment. This is paired with an **Accent Amber (#FBBF24)** used sparingly for high-impact call-to-actions, "Live Now" indicators, and featured event highlights.

The background is kept clean with a soft off-white neutral to reduce eye strain during long browsing sessions. Surface colors are strictly white to allow cards to pop against the subtle background.

## Typography

This design system utilizes **Plus Jakarta Sans** for headlines to provide a friendly, modern, and slightly rounded geometric feel that aligns with the "welcoming campus" vibe. **Inter** is used for body copy and labels to ensure maximum legibility at smaller sizes, especially for event details and timestamps.

On mobile devices, headlines scale down to prevent awkward line breaks. We prioritize a clear typographic hierarchy where event titles always lead the visual weight, followed by date/time metadata in a smaller, secondary label style.

## Layout & Spacing

The layout follows a **4-column fluid grid** for mobile devices. A standard **16px side margin** (container-padding) ensures content does not touch the edges of the screen. 

The vertical rhythm is based on an **8px grid system**. Card elements are separated by a **12px gap** to maintain a tight but breathable vertical flow. Large sections (e.g., "Categories" vs "Upcoming Events") are separated by **32px** of white space to clearly define content boundaries without the need for heavy dividers.

## Elevation & Depth

In accordance with MD3, depth is primarily communicated through **Tonal Layering** rather than heavy shadows. 

1.  **Level 0 (Background):** Neutral off-white (#F9FAFB).
2.  **Level 1 (Cards/Surfaces):** Pure white with a very subtle 1px border (#E5E7EB) and a soft, low-opacity ambient shadow (Blur 4px, Y 2px, 5% opacity).
3.  **Level 2 (Active/Floating):** Used for Floating Action Buttons (FABs) and active states. These use a more pronounced shadow (Blur 12px, Y 6px, 10% opacity) to signify interactability.

Backdrop blurs are reserved for the Navigation Bar and Top App Bar to provide a sense of context and "glass-like" modernism when scrolling through content.

## Shapes

The design system adopts a **Rounded** shape language to feel approachable and safe. 

- **Standard Elements:** 8px (0.5rem) corner radius for buttons and input fields.
- **Large Elements:** 16px (1rem) corner radius for event cards and bottom sheets.
- **Full Rounding:** Pill-shaped rounding is applied to chips and tags (e.g., "Academic", "Social") to distinguish them from actionable buttons.

## Components

### Buttons
- **Primary:** Solid Blue (#1A56DB) with white text, 8px rounded corners.
- **Secondary:** Outlined Blue with 1px stroke, used for less urgent actions like "View Map."
- **FAB:** Circular button in Primary Blue or Accent Gold, positioned at the bottom right for "Add Event."

### Cards
- **Event Card:** White background, 16px radius. Feature image at the top with a 16:9 aspect ratio. Text content has 16px internal padding.
- **State:** Use a subtle Blue overlay (5% opacity) for pressed states on cards.

### Chips
- Used for categories. Small, pill-shaped, with a light Blue background (#EFF6FF) and Primary Blue text. 

### Inputs
- **Search Bar:** Fully rounded (pill) with a soft grey background and a search icon prefix. 
- **Text Fields:** Outlined style with the label nesting into the border on focus, following the MD3 specification.

### Navigation
- **Bottom Navigation:** Use 4-5 icons with active states highlighted by a Primary Blue "pill" indicator behind the icon. Labels are always visible for accessibility.