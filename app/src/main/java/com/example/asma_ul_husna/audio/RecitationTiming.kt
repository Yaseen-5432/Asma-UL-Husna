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
        RecitationTimestamp(1, 1_020L),    // 0.02.02
        RecitationTimestamp(2, 2_050L),    // 0.03.05
        RecitationTimestamp(3, 3_050L),    // 0.04.05
        RecitationTimestamp(4, 4_150L),    // 0.05.15
        RecitationTimestamp(5, 6_040L),    // 0.07.04
        RecitationTimestamp(6, 7_080L),    // 0.08.08
        RecitationTimestamp(7, 8_100L),    // 0.09.10
        RecitationTimestamp(8, 9_250L),   // 0.10.25
        RecitationTimestamp(9, 11_000L),   // 0.12.00
        RecitationTimestamp(10, 12_500L),  // 0.13.50
        RecitationTimestamp(11, 14_100L),  // 0.15.10
        RecitationTimestamp(12, 15_200L),  // 0.16.20
        RecitationTimestamp(13, 17_000L),  // 0.18.00
        RecitationTimestamp(14, 18_200L),  // 0.19.20
        RecitationTimestamp(15, 19_290L),  // 0.20.29
        RecitationTimestamp(16, 21_070L),  // 0.22.07
        RecitationTimestamp(17, 22_150L),  // 0.23.15
        RecitationTimestamp(18, 24_000L),  // 0.25.00
        RecitationTimestamp(19, 25_150L),  // 0.26.15
        RecitationTimestamp(20, 27_020L),  // 0.28.02
        RecitationTimestamp(21, 28_110L),  // 0.29.11
        RecitationTimestamp(22, 29_200L),  // 0.30.20
        RecitationTimestamp(23, 31_000L),  // 0.32.00
        RecitationTimestamp(24, 32_000L),  // 0.33.00
        RecitationTimestamp(25, 33_000L),  // 0.34.00
        RecitationTimestamp(26, 34_100L),  // 0.35.10
        RecitationTimestamp(27, 35_200L),  // 0.36.20
        RecitationTimestamp(28, 36_500L),  // 0.37.50
        RecitationTimestamp(29, 39_200L),  // 0.38.20
        RecitationTimestamp(30, 38_200L),  // 0.39.20
        RecitationTimestamp(31, 39_270L),  // 0.40.27
        RecitationTimestamp(32, 40_500L),  // 0.41.50
        RecitationTimestamp(33, 42_100L),  // 0.43.10
        RecitationTimestamp(34, 43_150L),  // 0.44.15
        RecitationTimestamp(35, 44_240L),  // 0.45.24
        RecitationTimestamp(36, 45_550L),  // 0.46.55
        RecitationTimestamp(37, 46_000L),  // 0.47.00
        RecitationTimestamp(38, 48_000L),  // 0.49.00
        RecitationTimestamp(39, 49_060L),  // 0.50.06
        RecitationTimestamp(40, 50_100L),  // 0.51.10
        RecitationTimestamp(41, 51_210L),  // 0.52.21
        RecitationTimestamp(42, 52_100L),  // 0.53.10
        RecitationTimestamp(43, 53_500L),  // 0.54.50
        RecitationTimestamp(44, 55_150L),  // 0.56.15
        RecitationTimestamp(45, 56_500L),  // 0.57.50
        RecitationTimestamp(46, 57_500L),  // 0.58.50
        RecitationTimestamp(47, 58_500L),  // 0.59.50
        RecitationTimestamp(48, 60_050L),  // 1.01.05
        RecitationTimestamp(49, 61_000L),  // 1.03.00
        RecitationTimestamp(50, 63_000L),  // 1.04.00
        RecitationTimestamp(51, 63_500L),  // 1.04.50
        RecitationTimestamp(52, 65_000L),  // 1.06.00
        RecitationTimestamp(53, 66_100L),  // 1.07.10
        RecitationTimestamp(54, 67_200L),  // 1.08.20
        RecitationTimestamp(55, 68_500L),  // 1.09.50
        RecitationTimestamp(56, 70_000L),  // 1.11.00
        RecitationTimestamp(57, 71_100L),  // 1.12.10
        RecitationTimestamp(58, 72_040L),  // 1.13.04
        RecitationTimestamp(59, 73_000L),  // 1.14.00
        RecitationTimestamp(60, 74_000L),  // 1.15.00
        RecitationTimestamp(61, 75_050L),  // 1.16.05
        RecitationTimestamp(62, 76_000L),  // 1.17.00
        RecitationTimestamp(63, 77_200L),  // 1.18.20
        RecitationTimestamp(64, 79_000L),  // 1.20.00
        RecitationTimestamp(65, 80_200L),  // 1.21.20
        RecitationTimestamp(66, 81_500L),  // 1.22.50
        RecitationTimestamp(67, 82_000L),  // 1.23.50
        RecitationTimestamp(68, 82_500L),  // 1.23.50
        RecitationTimestamp(69, 84_050L),  // 1.25.05
        RecitationTimestamp(70, 85_200L),  // 1.26.20
        RecitationTimestamp(71, 87_000L),  // 1.28.00
        RecitationTimestamp(72, 88_080L),  // 1.29.08
        RecitationTimestamp(73, 89_000L),  // 1.30.00
        RecitationTimestamp(74, 90_260L),  // 1.31.26
        RecitationTimestamp(75, 91_500L),  // 1.32.50
        RecitationTimestamp(76, 93_000L),  // 1.34.00
        RecitationTimestamp(77, 94_260L),  // 1.35.26
        RecitationTimestamp(78, 96_000L),  // 1.37.00
        RecitationTimestamp(79, 96_500L),  // 1.37.50
        RecitationTimestamp(80, 98_000L),  // 1.39.00
        RecitationTimestamp(81, 99_050L), // 1.40.05
        RecitationTimestamp(82, 100_120L), // 1.41.12
        RecitationTimestamp(83, 101_250L), // 1.42.25
        RecitationTimestamp(84, 104_000L), // 1.45.00
        RecitationTimestamp(85, 107_210L), // 1.48.21
        RecitationTimestamp(86, 109_000L), // 1.50.00
        RecitationTimestamp(87, 110_000L), // 1.51.00
        RecitationTimestamp(88, 111_000L), // 1.52.00
        RecitationTimestamp(89, 112_000L), // 1.53.00
        RecitationTimestamp(90, 113_140L), // 1.54.14
        RecitationTimestamp(91, 116_100L), // 1.57.10
        RecitationTimestamp(92, 117_150L), // 1.58.15
        RecitationTimestamp(93, 118_500L), // 1.59.50
        RecitationTimestamp(94, 120_000L), // 2.01.00
        RecitationTimestamp(95, 121_000L), // 2.02.00
        RecitationTimestamp(96, 122_230L), // 2.03.23
        RecitationTimestamp(97, 124_230L), // 2.05.23
        RecitationTimestamp(98, 126_000L), // 2.07.00
        RecitationTimestamp(99, 127_170L)  // 2.08.17

        //RecitationTimestamp(1, 2_020L),    // 0.02.02
//        RecitationTimestamp(2, 3_050L),    // 0.03.05
//        RecitationTimestamp(3, 4_050L),    // 0.04.05
//        RecitationTimestamp(4, 5_150L),    // 0.05.15
//        RecitationTimestamp(5, 7_040L),    // 0.07.04
//        RecitationTimestamp(6, 8_080L),    // 0.08.08
//        RecitationTimestamp(7, 9_100L),    // 0.09.10
//        RecitationTimestamp(8, 10_250L),   // 0.10.25
//        RecitationTimestamp(9, 12_000L),   // 0.12.00
//        RecitationTimestamp(10, 13_500L),  // 0.13.50
//        RecitationTimestamp(11, 15_100L),  // 0.15.10
//        RecitationTimestamp(12, 16_200L),  // 0.16.20
//        RecitationTimestamp(13, 18_000L),  // 0.18.00
//        RecitationTimestamp(14, 19_200L),  // 0.19.20
//        RecitationTimestamp(15, 20_290L),  // 0.20.29
//        RecitationTimestamp(16, 22_070L),  // 0.22.07
//        RecitationTimestamp(17, 23_150L),  // 0.23.15
//        RecitationTimestamp(18, 25_000L),  // 0.25.00
//        RecitationTimestamp(19, 26_150L),  // 0.26.15
//        RecitationTimestamp(20, 28_020L),  // 0.28.02
//        RecitationTimestamp(21, 29_110L),  // 0.29.11
//        RecitationTimestamp(22, 30_200L),  // 0.30.20
//        RecitationTimestamp(23, 32_000L),  // 0.32.00
//        RecitationTimestamp(24, 33_000L),  // 0.33.00
//        RecitationTimestamp(25, 34_000L),  // 0.34.00
//        RecitationTimestamp(26, 35_100L),  // 0.35.10
//        RecitationTimestamp(27, 36_200L),  // 0.36.20
//        RecitationTimestamp(28, 37_500L),  // 0.37.50
//        RecitationTimestamp(29, 38_200L),  // 0.38.20
//        RecitationTimestamp(30, 39_200L),  // 0.39.20
//        RecitationTimestamp(31, 40_270L),  // 0.40.27
//        RecitationTimestamp(32, 41_500L),  // 0.41.50
//        RecitationTimestamp(33, 43_100L),  // 0.43.10
//        RecitationTimestamp(34, 44_150L),  // 0.44.15
//        RecitationTimestamp(35, 45_240L),  // 0.45.24
//        RecitationTimestamp(36, 46_550L),  // 0.46.55
//        RecitationTimestamp(37, 47_000L),  // 0.47.00
//        RecitationTimestamp(38, 49_000L),  // 0.49.00
//        RecitationTimestamp(39, 50_060L),  // 0.50.06
//        RecitationTimestamp(40, 51_100L),  // 0.51.10
//        RecitationTimestamp(41, 52_210L),  // 0.52.21
//        RecitationTimestamp(42, 53_100L),  // 0.53.10
//        RecitationTimestamp(43, 54_500L),  // 0.54.50
//        RecitationTimestamp(44, 56_150L),  // 0.56.15
//        RecitationTimestamp(45, 57_500L),  // 0.57.50
//        RecitationTimestamp(46, 58_500L),  // 0.58.50
//        RecitationTimestamp(47, 59_500L),  // 0.59.50
//        RecitationTimestamp(48, 61_050L),  // 1.01.05
//        RecitationTimestamp(49, 63_000L),  // 1.03.00
//        RecitationTimestamp(50, 64_000L),  // 1.04.00
//        RecitationTimestamp(51, 64_500L),  // 1.04.50
//        RecitationTimestamp(52, 66_000L),  // 1.06.00
//        RecitationTimestamp(53, 67_100L),  // 1.07.10
//        RecitationTimestamp(54, 68_200L),  // 1.08.20
//        RecitationTimestamp(55, 69_500L),  // 1.09.50
//        RecitationTimestamp(56, 71_000L),  // 1.11.00
//        RecitationTimestamp(57, 72_100L),  // 1.12.10
//        RecitationTimestamp(58, 73_040L),  // 1.13.04
//        RecitationTimestamp(59, 74_000L),  // 1.14.00
//        RecitationTimestamp(60, 75_000L),  // 1.15.00
//        RecitationTimestamp(61, 76_050L),  // 1.16.05
//        RecitationTimestamp(62, 77_000L),  // 1.17.00
//        RecitationTimestamp(63, 78_200L),  // 1.18.20
//        RecitationTimestamp(64, 80_000L),  // 1.20.00
//        RecitationTimestamp(65, 81_200L),  // 1.21.20
//        RecitationTimestamp(66, 82_500L),  // 1.22.50
//        RecitationTimestamp(67, 83_500L),  // 1.23.50
//        RecitationTimestamp(68, 85_050L),  // 1.25.05
//        RecitationTimestamp(69, 86_200L),  // 1.26.20
//        RecitationTimestamp(70, 88_000L),  // 1.28.00
//        RecitationTimestamp(71, 89_080L),  // 1.29.08
//        RecitationTimestamp(72, 90_000L),  // 1.30.00
//        RecitationTimestamp(73, 91_260L),  // 1.31.26
//        RecitationTimestamp(74, 92_500L),  // 1.32.50
//        RecitationTimestamp(75, 94_000L),  // 1.34.00
//        RecitationTimestamp(76, 95_260L),  // 1.35.26
//        RecitationTimestamp(77, 97_000L),  // 1.37.00
//        RecitationTimestamp(78, 97_500L),  // 1.37.50
//        RecitationTimestamp(79, 99_000L),  // 1.39.00
//        RecitationTimestamp(80, 100_050L), // 1.40.05
//        RecitationTimestamp(81, 101_120L), // 1.41.12
//        RecitationTimestamp(82, 102_250L), // 1.42.25
//        RecitationTimestamp(83, 105_000L), // 1.45.00
//        RecitationTimestamp(84, 108_210L), // 1.48.21
//        RecitationTimestamp(85, 110_000L), // 1.50.00
//        RecitationTimestamp(86, 111_000L), // 1.51.00
//        RecitationTimestamp(87, 112_000L), // 1.52.00
//        RecitationTimestamp(88, 113_000L), // 1.53.00
//        RecitationTimestamp(89, 114_140L), // 1.54.14
//        RecitationTimestamp(90, 117_100L), // 1.57.10
//        RecitationTimestamp(91, 118_150L), // 1.58.15
//        RecitationTimestamp(92, 119_500L), // 1.59.50
//        RecitationTimestamp(93, 121_000L), // 2.01.00
//        RecitationTimestamp(94, 122_000L), // 2.02.00
//        RecitationTimestamp(95, 123_230L), // 2.03.23
//        RecitationTimestamp(96, 125_230L), // 2.05.23
//        RecitationTimestamp(97, 127_000L), // 2.07.00
//        RecitationTimestamp(98, 128_170L)  // 2.08.17

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