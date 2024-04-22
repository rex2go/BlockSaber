package com.timcodes.blocksaber.beatmap

class BeatMap(
    val title: String,
    val author: String,
    val difficulty: BeatMapDifficulty,
    val sound: String,
    val bpm: Int,
    val startMs: Long,
    val durationMs: Long,
    val bars: Map<Int, BeatBar>
) {
}