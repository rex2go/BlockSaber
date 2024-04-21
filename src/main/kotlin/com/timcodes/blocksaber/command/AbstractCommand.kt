package com.timcodes.blocksaber.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor

public abstract class AbstractCommand(command: String) : CommandExecutor {

    init {
        Bukkit.getPluginCommand(command)?.setExecutor(this)
    }
}