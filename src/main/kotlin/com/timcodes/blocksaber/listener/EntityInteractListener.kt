package com.timcodes.blocksaber.listener

import com.timcodes.blocksaber.BlockSaber
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EntityInteractListener: Listener {

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val damager = event.damager

        // check if entity is slime
        if(entity.type.name != "SLIME") {
            return
        }

        // check if damager is player
        if(damager.type.name != "PLAYER") {
            return
        }

        val game = BlockSaber.instance.game ?: return

        game.beatElements.forEach { element ->
            if(element.getHitBoxId() == entity.entityId) {
                element.hit()
            }
        }
    }
}