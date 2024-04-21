package com.timcodes.blocksaber.command

import com.timcodes.blocksaber.BlockSaber
import com.timcodes.blocksaber.Game
import com.timcodes.blocksaber.beatmap.BeatMapLoader
import org.bukkit.entity.Player

class BlockSaberCommand: AbstractPlayerCommand("blocksaber") {

    override fun onCommand(player: Player, args: Array<out String>?): Boolean {
        val arg0 = args?.getOrNull(0)

        if (arg0 == null) {
            player.sendMessage("Usage: /blocksaber <start/stop>")
            return true
        }

        when (arg0) {
            "start" -> {
                if(BlockSaber.instance.game != null) {
                    player.sendMessage("BlockSaber is already running!")
                    return true
                }

                val beatMapName = args.getOrNull(1)

                if (beatMapName == null) {
                    player.sendMessage("Usage: /blocksaber start <beatmap>")
                    return true
                }

                try {
                    val beatMap = BeatMapLoader.loadBeatMap(beatMapName)

                    val game = Game(beatMap, player)
                    game.start()

                    BlockSaber.instance.game = game
                } catch (e: Exception) {
                    player.sendMessage("Failed to load beatmap: ${e.message}")
                    e.printStackTrace()
                    return true
                }
            }

            "stop" -> {
                val game = BlockSaber.instance.game
                game?.stop()
                BlockSaber.instance.game = null
            }

            else -> {
                player.sendMessage("Usage: /blocksaber <start/stop>")
            }
        }

        return true
    }
}