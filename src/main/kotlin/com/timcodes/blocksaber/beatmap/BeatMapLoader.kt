package com.timcodes.blocksaber.beatmap

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.timcodes.blocksaber.BlockSaber
import com.timcodes.blocksaber.beatelement.BeatElementLocation
import com.timcodes.blocksaber.beatelement.BeatElementType
import java.io.File
import java.io.FileReader

class BeatMapLoader {

    companion object {
        fun loadBeatMap(name: String): BeatMap {
            val file = File("${BlockSaber.instance.dataFolder}${File.separator}beatmaps${File.separator}$name.json")

            if (!file.exists()) {
                throw Exception("Beatmap $name does not exist")
            }

            val gson = Gson()

            val json = gson.fromJson(FileReader(file), JsonObject::class.java)

            val title = json.get("title").asString
            val author = json.get("author").asString
            val difficulty = BeatMapDifficulty.valueOf(json.get("difficulty").asString)
            val sound = json.get("sound").asString
            val bpm = json.get("bpm").asInt
            val startMs = json.get("startMs").asLong
            val durationMs = json.get("durationMs").asLong
            val jsonBars = json.get("bars").asJsonObject

            val bars = mutableMapOf<Int, BeatBar>()

            for ((key, value) in jsonBars.entrySet()) {
                val bar = key.toInt()
                val actionsString = value.asString.replace("-", "--")

                // split actionsString every two characters
                val actions = actionsString.chunked(2)

                val subBeatActions = mutableMapOf<Int, BeatAction>()

                actions.forEachIndexed { index, action ->
                    run {
                        if (action == "--") {
                            return@run
                        }

                        val subBeat = index + 1

                        if(action == "SP") {
                            subBeatActions[subBeat] = BeatAction(BeatElementType.SPEED, BeatElementLocation.TOP)
                            return@run
                        }

                        if(action == "PA") {
                            subBeatActions[subBeat] = BeatAction(BeatElementType.PARTICLE, BeatElementLocation.TOP)
                            return@run
                        }

                        val parts = action.split("").filter { it.isNotEmpty() }

                        val beatElementType = BeatElementType.fromCode(parts[0])

                        val actionLocation: BeatElementLocation = if(!parts[1].contentEquals("?")) {
                            BeatElementLocation.fromId(parts[1].toInt())
                        } else {
                            BeatElementLocation.entries.toTypedArray().random()
                        }

                        subBeatActions[subBeat] = BeatAction(beatElementType, actionLocation)
                    }
                }

                if (subBeatActions.isNotEmpty()) {
                    bars[bar] = BeatBar(subBeatActions)
                }
            }


            return BeatMap(title, author, difficulty, sound, bpm, startMs, durationMs, bars)
        }
    }
}