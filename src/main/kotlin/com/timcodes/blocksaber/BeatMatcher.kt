package com.timcodes.blocksaber

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class BeatMatcher(private val bpm: Int, private val game: Game) {

    var beat: Int = 0

    var subBeat: Int = 0

    private var runnable: BukkitTask? = null

    fun isRunning(): Boolean {
        return runnable != null
    }

    fun start() {
        val startTime = System.currentTimeMillis()
        val beatTime = 60000 / bpm // in ms
        val subBeatTime = beatTime / 4 // in ms

        beat = 1
        subBeat = 1

        game.onBeat()
        game.onSubBeat()

        runnable = object : BukkitRunnable() {
            override fun run() {
                val time = System.currentTimeMillis() - startTime
                val lastBeat = beat
                val lastSubBeat = subBeat

                beat = (time / beatTime).toInt() % 4 + 1
                subBeat = ((time % beatTime) / subBeatTime).toInt() % 4 + 1

                if (lastBeat != beat) {
                    game.onBeat()
                }

                if (lastSubBeat != subBeat) {
                    game.onSubBeat()
                }
            }
        }.runTaskTimer(BlockSaber.instance, 0, 1)
    }

    fun stop() {
        runnable?.cancel()
        runnable = null

        beat = 0
        subBeat = 0
    }
}