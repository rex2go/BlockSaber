package com.timcodes.blocksaber.beatelement

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class RedBeatElement(startLocation: Location, targetLocation: Location, hitTime: Long) : AbstractBeatElement(
    startLocation,
    targetLocation,
    hitTime
) {
    override fun equipArmorStand() {
        val itemStack = ItemStack(Material.RED_WOOL)
        armorStand.equipment.helmet = itemStack
    }
}