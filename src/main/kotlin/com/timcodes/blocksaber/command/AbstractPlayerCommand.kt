package com.timcodes.blocksaber.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

public abstract class AbstractPlayerCommand(command: String) : AbstractCommand(command) {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            return onCommand(sender, args)
        }

        sender.sendMessage("This command can only be executed by a player!")

        return true
    }

    protected abstract fun onCommand(player: Player, args: Array<out String>?): Boolean

}