package com.example.asma_ul_husna.audio

/**
 * Represents a timestamp entry for rhythmic full recitation of a specific Name of Allah.
 *
 * @property nameId The 1-based unique identifier of the Name (1 to 99).
 * @property startMs The exact playback time in milliseconds when recitation of this Name begins.
 */
data class RecitationTimestamp(
    val nameId: Int,
    val startMs: Long
)

/**
 * Source of truth for full rhythmic recitation timing and timestamp synchronization.
 *
 * Configured with exact manual timestamps for Names 1–50.
 * Names 51–99 are reserved as placeholders and will be configured in a future update.
 */
object RecitationTiming {

    /**
     * Asset URI pointing to the bundled full 99 Names recitation MP3.
     */
    const val FULL_RECITATION_ASSET_URI = "asset:///videoplayback.mp3"

    /**
     * End boundary for the currently supplied synchronization data (Name 50).
     * Corresponds to 1.10.12 (70,120 ms).
     */
    const val NAME_50_END_MS = 70_120L

    /**
     * Explicit timestamps table for full rhythmic recitation.
     * Format from source: minutes.seconds.hundredths converted to milliseconds.
     */
    val timestamps: List<RecitationTimestamp> = listOf(
        RecitationTimestamp(1, 1_270L),    // 0.01.27
        RecitationTimestamp(2, 3_060L),    // 0.03.06
        RecitationTimestamp(3, 4_160L),    // 0.04.16
        RecitationTimestamp(4, 5_250L),    // 0.05.25
        RecitationTimestamp(5, 6_270L),    // 0.06.27
        RecitationTimestamp(6, 7_110L),    // 0.07.11
        RecitationTimestamp(7, 8_170L),    // 0.08.17
        RecitationTimestamp(8, 9_280L),    // 0.09.28
        RecitationTimestamp(9, 11_070L),   // 0.11.07
        RecitationTimestamp(10, 12_200L),  // 0.12.20
        RecitationTimestamp(11, 14_250L),  // 0.14.25
        RecitationTimestamp(12, 16_190L),  // 0.16.19
        RecitationTimestamp(13, 18_000L),  // 0.18.00
        RecitationTimestamp(14, 19_150L),  // 0.19.15
        RecitationTimestamp(15, 20_280L),  // 0.20.28
        RecitationTimestamp(16, 22_100L),  // 0.22.10
        RecitationTimestamp(17, 24_020L),  // 0.24.02
        RecitationTimestamp(18, 25_200L),  // 0.25.20
        RecitationTimestamp(19, 27_000L),  // 0.27.00
        RecitationTimestamp(20, 28_120L),  // 0.28.12
        RecitationTimestamp(21, 30_000L),  // 0.30.00
        RecitationTimestamp(22, 32_000L),  // 0.32.00
        RecitationTimestamp(23, 33_000L),  // 0.33.00
        RecitationTimestamp(24, 35_030L),  // 0.35.03
        RecitationTimestamp(25, 36_000L),  // 0.36.00
        RecitationTimestamp(26, 37_070L),  // 0.37.07
        RecitationTimestamp(27, 38_200L),  // 0.38.20
        RecitationTimestamp(28, 39_500L),  // 0.39.50
        RecitationTimestamp(29, 41_070L),  // 0.41.07
        RecitationTimestamp(30, 42_000L),  // 0.42.00
        RecitationTimestamp(31, 43_000L),  // 0.43.00
        RecitationTimestamp(32, 44_160L),  // 0.44.16
        RecitationTimestamp(33, 45_500L),  // 0.45.50
        RecitationTimestamp(34, 47_050L),  // 0.47.05
        RecitationTimestamp(35, 48_110L),  // 0.48.11
        RecitationTimestamp(36, 50_020L),  // 0.50.02
        RecitationTimestamp(37, 51_000L),  // 0.51.00
        RecitationTimestamp(38, 52_120L),  // 0.52.12
        RecitationTimestamp(39, 53_000L),  // 0.53.00
        RecitationTimestamp(40, 55_000L),  // 0.55.00
        RecitationTimestamp(41, 56_020L),  // 0.56.02
        RecitationTimestamp(42, 57_130L),  // 0.57.13
        RecitationTimestamp(43, 58_170L),  // 0.58.17
        RecitationTimestamp(44, 60_000L),  // 1.00.00
        RecitationTimestamp(45, 61_130L),  // 1.01.13
        RecitationTimestamp(46, 63_070L),  // 1.03.07
        RecitationTimestamp(47, 64_120L),  // 1.04.12
        RecitationTimestamp(48, 65_190L),  // 1.05.19
        RecitationTimestamp(49, 67_020L),  // 1.07.02
        RecitationTimestamp(50, 69_000L)   // 1.09.00

        // =========================================================================
        // PLACEHOLDER: Names 51–99 timestamps will be added in a future update.
        // DO NOT invent or extrapolate fake timestamps.
        // Example format when adding future names:
        // RecitationTimestamp(51, ...L),
        // ...
        // RecitationTimestamp(99, ...L),
        // =========================================================================
    )

    /**
     * Determines which Name ID is active for a given playback position in milliseconds.
     *
     * @param positionMs Current playback position in milliseconds.
     * @return The active Name ID (1..50), or null if before Name 1 or after the configured boundary.
     */
    fun getActiveNameId(positionMs: Long): Int? {
        if (timestamps.isEmpty() || positionMs < timestamps.first().startMs) {
            return null
        }

        for (i in timestamps.indices) {
            val current = timestamps[i]
            val nextStartMs = if (i + 1 < timestamps.size) {
                timestamps[i + 1].startMs
            } else {
                NAME_50_END_MS
            }

            if (positionMs in current.startMs until nextStartMs) {
                return current.nameId
            }
        }

        return null
    }
}
