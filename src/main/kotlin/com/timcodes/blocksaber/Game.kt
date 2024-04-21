package com.timcodes.blocksaber

import com.timcodes.blocksaber.beatelement.AbstractBeatElement
import com.timcodes.blocksaber.beatmap.BeatMap
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import kotlin.math.ceil
import kotlin.math.max

class Game(val beatMap: BeatMap, val player: Player) {

    var startTime: Long = 0

    var beatMatcher = BeatMatcher(beatMap.bpm, this)

    private var gameTask: BukkitTask? = null

    private var beat = 0

    private var subBeat = 0

    val beatElements = mutableListOf<AbstractBeatElement>()

    private val spread = 1.2

    private val locationOffsets = listOf(
        Vector(0.0, spread, 0.0),
        Vector(0.0, spread / 2, spread / 2),
        Vector(0.0, 0.0, spread),
        Vector(0.0, -spread / 2, spread / 2),
        Vector(0.0, -spread, 0.0),
        Vector(0.0, -spread / 2, -spread / 2),
        Vector(0.0, 0.0, -spread),
        Vector(0.0, spread / 2, -spread / 2)
    )

    fun start() {
        startTime = System.currentTimeMillis()

        beatMatcher.start()

        // TODO
        player.playSound(player.location, "blocksaber:blocksaber.escape", SoundCategory.MASTER, 1.0f, 1.0f, 0)

        player.teleport(Location(player.world, 0.0, 20000.0, 0.0, -90.0f, 0.0f))
        player.world.time = 16000
        player.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        player.world.weatherDuration = 99999999;
        player.world.setStorm(false)
        player.world.isThundering = false
        player.gameMode = GameMode.SURVIVAL
        player.allowFlight = true
        player.isFlying = true

        gameTask = object : BukkitRunnable() {
            override fun run() {
                val time = System.currentTimeMillis() - startTime

                if (!beatMatcher.isRunning() && time >= beatMap.startMs) {
                    beatMatcher.start()
                }

                if (time >= beatMap.durationMs) {
                    stop()
                }

                val removeList = mutableListOf<AbstractBeatElement>()

                beatElements.forEach {
                    if (it.isDone) {
                        removeList.add(it)
                        return@forEach
                    }

                    it.tick()
                }

                removeList.forEach {
                    beatElements.remove(it)
                }
            }
        }.runTaskTimer(BlockSaber.instance, 0, 1)
    }

    fun stop() {
        gameTask?.cancel()
        gameTask = null

        beatMatcher.stop()

        beatElements.forEach { it.remove() }
    }

    fun onBeat() {
        beat++

        val currentBeat = (beat - 1) % 4

        player.exp = currentBeat.toFloat() / 3.0f
        player.level = max(ceil(beat / 4.0).toInt(), 1)
    }

    fun onSubBeat() {
        subBeat++

        val currentBar = max(ceil(beat / 4.0).toInt() + 2, 1)
        var currentSubBeat = subBeat % 16

        val barLengthMs = 60000 / beatMap.bpm * 8

        if (currentSubBeat == 0) {
            currentSubBeat = 16
        }

        if (beatMap.bars.containsKey(currentBar)) {
            val bar = beatMap.bars[currentBar]!!

            if (bar.subBeatActions.containsKey(currentSubBeat)) {
                val subBeatAction = bar.subBeatActions[currentSubBeat]!!

                val startLocation = player.location.clone().add(locationOffsets[subBeatAction.location.id - 1])
                startLocation.x = player.location.x + 20
                startLocation.y -= 0.2

                val targetLocation = startLocation.clone()
                targetLocation.x = player.x
                targetLocation.y -= 0.2

                val hitTime = System.currentTimeMillis() + barLengthMs

                val beatElement = subBeatAction.beatElementType.kClass.constructors.first()
                    .call(startLocation, targetLocation, hitTime)
                beatElements.add(beatElement)

                //Bukkit.broadcastMessage("Bar $currentBar Subbeat $currentSubBeat: ${subBeatAction.beatElementType.code}")
            }
        }
    }
}