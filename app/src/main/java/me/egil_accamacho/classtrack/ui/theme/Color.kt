package me.egil_accamacho.classtrack.ui.theme

import androidx.compose.ui.graphics.Color

// ── Brand palette — ClassTrack Manual de Marca §8 ────────────────────────────

/** Primary: Purple 600 — primary buttons, FAB, key actions */
val CtPrimary            = Color(0xFF7C3AED)

/** Primary Dark: pressed / dark ripple variant */
val CtPrimaryDark        = Color(0xFF6D28D9)

/** Primary Light: secondary indicators */
val CtPrimaryLight       = Color(0xFFA78BFA)

/** Secondary accent elements */
val CtSecondary          = Color(0xFF8B5CF6)

// ── Semantic colors ───────────────────────────────────────────────────────────

/** Success: attendance confirmed, positive states */
val CtSuccess            = Color(0xFF10B981)

/** Warning: alerts, session expiry reminders */
val CtWarning            = Color(0xFFF59E0B)

/** Error: validation errors, destructive actions */
val CtError              = Color(0xFFEF4444)

// ── Light theme — Manual de Marca §9 ─────────────────────────────────────────

val CtBackgroundLight     = Color(0xFFF8FAFC)
val CtSurfaceLight        = Color(0xFFFFFFFF)
val CtSurfaceVariantLight = Color(0xFFF1F5F9)
val CtBorderLight         = Color(0xFFE2E8F0)
val CtPrimaryTextLight    = Color(0xFF0F172A)
val CtSecondaryTextLight  = Color(0xFF475569)

// ── Dark theme — Manual de Marca §10 ─────────────────────────────────────────

val CtBackgroundDark      = Color(0xFF0F172A)
val CtSurfaceDark         = Color(0xFF1E293B)
val CtSurfaceVariantDark  = Color(0xFF334155)
val CtPrimaryTextDark     = Color(0xFFF8FAFC)
val CtSecondaryTextDark   = Color(0xFFCBD5E1)

// ── On-color ──────────────────────────────────────────────────────────────────

/** Text/icon color on top of primary-colored surfaces */
val CtOnPrimary          = Color(0xFFFFFFFF)
