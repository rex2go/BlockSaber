package com.timcodes.blocksaber.beatelement

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

class ExplosionBeatElement(startLocation: Location, targetLocation: Location, hitTime: Long) : AbstractBeatElement(
    startLocation,
    targetLocation,
    hitTime
) {
    override fun equipArmorStand() {
        val itemStack = ItemStack(Material.TNT)
        armorStand.equipment.helmet = itemStack
    }

    override fun hit() {

        armorStand.world.playSound(armorStand.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
        armorStand.world.spawnParticle(Particle.EXPLOSION_HUGE, armorStand.location, 20, 2.0, 2.0, 2.0, 0.0)

        super.hit()
    }
}