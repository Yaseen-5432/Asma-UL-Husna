package com.example.asma_ul_husna

import com.example.asma_ul_husna.audio.RecitationState
import com.example.asma_ul_husna.audio.RecitationTiming
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RecitationTimingTest {

    @Test
    fun `verify exactly 50 timestamps are configured`() {
        assertEquals("Configured timestamps count must be exactly 50", 50, RecitationTiming.timestamps.size)
        assertEquals("End boundary must be 70120 ms (1m 10s 12h)", 70_120L, RecitationTiming.NAME_50_END_MS)
        assertEquals("Full recitation asset URI must match local asset", "asset:///videoplayback.mp3", RecitationTiming.FULL_RECITATION_ASSET_URI)
    }

    @Test
    fun `verify all 50 timestamps match exact user specifications`() {
        val expectedTimestamps = listOf(
            1 to 1_270L,   // Name 1  = 0.01.27
            2 to 3_060L,   // Name 2  = 0.03.06
            3 to 4_160L,   // Name 3  = 0.04.16
            4 to 5_250L,   // Name 4  = 0.05.25
            5 to 6_270L,   // Name 5  = 0.06.27
            6 to 7_110L,   // Name 6  = 0.07.11
            7 to 8_170L,   // Name 7  = 0.08.17
            8 to 9_280L,   // Name 8  = 0.09.28
            9 to 11_070L,  // Name 9  = 0.11.07
            10 to 12_200L, // Name 10 = 0.12.20
            11 to 14_250L, // Name 11 = 0.14.25
            12 to 16_190L, // Name 12 = 0.16.19
            13 to 18_000L, // Name 13 = 0.18.00
            14 to 19_150L, // Name 14 = 0.19.15
            15 to 20_280L, // Name 15 = 0.20.28
            16 to 22_100L, // Name 16 = 0.22.10
            17 to 24_020L, // Name 17 = 0.24.02
            18 to 25_200L, // Name 18 = 0.25.20
            19 to 27_000L, // Name 19 = 0.27.00
            20 to 28_120L, // Name 20 = 0.28.12
            21 to 30_000L, // Name 21 = 0.30.00
            22 to 32_000L, // Name 22 = 0.32.00
            23 to 33_000L, // Name 23 = 0.33.00
            24 to 35_030L, // Name 24 = 0.35.03
            25 to 36_000L, // Name 25 = 0.36.00
            26 to 37_070L, // Name 26 = 0.37.07
            27 to 38_200L, // Name 27 = 0.38.20
            28 to 39_500L, // Name 28 = 0.39.50
            29 to 41_070L, // Name 29 = 0.41.07
            30 to 42_000L, // Name 30 = 0.42.00
            31 to 43_000L, // Name 31 = 0.43.00
            32 to 44_160L, // Name 32 = 0.44.16
            33 to 45_500L, // Name 33 = 0.45.50
            34 to 47_050L, // Name 34 = 0.47.05
            35 to 48_110L, // Name 35 = 0.48.11
            36 to 50_020L, // Name 36 = 0.50.02
            37 to 51_000L, // Name 37 = 0.51.00
            38 to 52_120L, // Name 38 = 0.52.12
            39 to 53_000L, // Name 39 = 0.53.00
            40 to 55_000L, // Name 40 = 0.55.00
            41 to 56_020L, // Name 41 = 0.56.02
            42 to 57_130L, // Name 42 = 0.57.13
            43 to 58_170L, // Name 43 = 0.58.17
            44 to 60_000L, // Name 44 = 1.00.00
            45 to 61_130L, // Name 45 = 1.01.13
            46 to 63_070L, // Name 46 = 1.03.07
            47 to 64_120L, // Name 47 = 1.04.12
            48 to 65_190L, // Name 48 = 1.05.19
            49 to 67_020L, // Name 49 = 1.07.02
            50 to 69_000L  // Name 50 = 1.09.00
        )

        for (i in expectedTimestamps.indices) {
            val (expectedId, expectedMs) = expectedTimestamps[i]
            val actual = RecitationTiming.timestamps[i]
            assertEquals("Name ID at index $i", expectedId, actual.nameId)
            assertEquals("StartMs for Name $expectedId", expectedMs, actual.startMs)
        }
    }

    @Test
    fun `test getActiveNameId before Name 1 start returns null`() {
        assertNull(RecitationTiming.getActiveNameId(0L))
        assertNull(RecitationTiming.getActiveNameId(500L))
        assertNull(RecitationTiming.getActiveNameId(1_269L))
    }

    @Test
    fun `test getActiveNameId for boundary transitions`() {
        // Name 1: [1270, 3060)
        assertEquals(1, RecitationTiming.getActiveNameId(1_270L))
        assertEquals(1, RecitationTiming.getActiveNameId(2_000L))
        assertEquals(1, RecitationTiming.getActiveNameId(3_059L))

        // Name 2: [3060, 4160)
        assertEquals(2, RecitationTiming.getActiveNameId(3_060L))
        assertEquals(2, RecitationTiming.getActiveNameId(4_159L))

        // Middle samples:
        // Name 10: [12200, 14250)
        assertEquals(10, RecitationTiming.getActiveNameId(12_200L))
        assertEquals(10, RecitationTiming.getActiveNameId(13_000L))

        // Name 20: [28120, 30000)
        assertEquals(20, RecitationTiming.getActiveNameId(28_120L))
        assertEquals(20, RecitationTiming.getActiveNameId(29_999L))

        // Name 40: [55000, 56020)
        assertEquals(40, RecitationTiming.getActiveNameId(55_000L))
        assertEquals(40, RecitationTiming.getActiveNameId(56_019L))

        // Name 49: [67020, 69000)
        assertEquals(49, RecitationTiming.getActiveNameId(67_020L))
        assertEquals(49, RecitationTiming.getActiveNameId(68_999L))

        // Name 50: [69000, 70120)
        assertEquals(50, RecitationTiming.getActiveNameId(69_000L))
        assertEquals(50, RecitationTiming.getActiveNameId(70_119L))
    }

    @Test
    fun `test getActiveNameId at and after end boundary returns null`() {
        // 70120 is end boundary (1.10.12)
        assertNull(RecitationTiming.getActiveNameId(70_120L))
        assertNull(RecitationTiming.getActiveNameId(75_000L))
        assertNull(RecitationTiming.getActiveNameId(100_000L))
    }

    @Test
    fun `test RecitationState data class defaults and updates`() {
        val state = RecitationState()
        assertFalse(state.isLoading)
        assertFalse(state.isPlaying)
        assertFalse(state.isPaused)
        assertNull(state.currentNameId)
        assertEquals(0L, state.positionMs)
        assertEquals(0L, state.durationMs)
        assertNull(state.error)

        val activeState = state.copy(
            isPlaying = true,
            currentNameId = 25,
            positionMs = 36_000L,
            durationMs = 70_120L
        )
        assertTrue(activeState.isPlaying)
        assertFalse(activeState.isPaused)
        assertEquals(25, activeState.currentNameId)
        assertEquals(36_000L, activeState.positionMs)
    }
}
