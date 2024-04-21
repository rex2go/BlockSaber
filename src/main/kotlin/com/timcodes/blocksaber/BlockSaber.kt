package com.timcodes.blocksaber

import com.timcodes.blocksaber.command.BlockSaberCommand
import com.timcodes.blocksaber.listener.EntityInteractListener
import org.bukkit.plugin.java.JavaPlugin

class BlockSaber: JavaPlugin() {

    companion object {
        lateinit var instance: BlockSaber
            private set
    }

    var game: Game? = null;

    override fun onEnable() {
        instance = this

        registerCommands()
        registerListeners()

        saveResource("beatmaps/escape.json", false)
    }

    override fun onDisable() {
    }

    private fun registerCommands() {
        getCommand("blocksaber")?.setExecutor(BlockSaberCommand())
    }

    private fun registerListeners() {
        server.pluginManager.registerEvents(EntityInteractListener(), this)
    }
}