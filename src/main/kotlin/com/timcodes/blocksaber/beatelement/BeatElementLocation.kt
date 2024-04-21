package com.timcodes.blocksaber.beatelement

enum class BeatElementLocation(val id: Int) {
    TOP(1), TOP_RIGHT(2), RIGHT(3), BOTTOM_RIGHT(4), BOTTOM(5), BOTTOM_LEFT(6), LEFT(7), TOP_LEFT(8);

    companion object {
        fun fromId(value: Int) = entries.first { it.id == value }
    }
}