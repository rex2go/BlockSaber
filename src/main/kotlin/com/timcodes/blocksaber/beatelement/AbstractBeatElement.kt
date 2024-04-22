package com.timcodes.blocksaber.beatelement

import com.timcodes.blocksaber.BlockSaber
import com.timcodes.blocksaber.command.mm
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Slime
import org.bukkit.material.MaterialData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.abs
import kotlin.math.sin


abstract class AbstractBeatElement(private val startLocation: Location, private val targetLocation: Location, private val hitTime: Long) {

    private val game = BlockSaber.instance.game!!

    protected val armorStand: ArmorStand = startLocation.world.spawnEntity(startLocation.clone().add(0.0, 100.0, 0.0), EntityType.ARMOR_STAND) as ArmorStand

    private val hitbox: Slime

    private val spawnTime = System.currentTimeMillis()

    var isDone = false

    fun getHitBoxId(): Int {
        return hitbox.entityId
    }

    init {
        armorStand.setGravity(false)
        armorStand.isInvisible = true
        armorStand.isInvulnerable = true
        armorStand.isCollidable = false

        this.equipArmorStand()

        this.hitbox = startLocation.world.spawnEntity(startLocation.clone().add(0.0,100.0,0.0), EntityType.SLIME) as Slime

        hitbox.size = 2
        hitbox.isSilent = true
        hitbox.setGravity(false)
        hitbox.setAI(false)
        hitbox.isCollidable = false
        hitbox.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1, false, false, false))
    }

    abstract fun equipArmorStand()

    open fun hit() {
        val time = System.currentTimeMillis()
        val deviation = abs(time - hitTime)

        if(deviation < 50) {
            game.player.sendActionBar(mm("<green>Perfect!"))
        } else if(deviation < 100) {
            game.player.sendActionBar(mm("<green>Good!"))
        } else {
            game.player.sendActionBar(mm("<green>Nice!"))
        }

        game.player.playSound(game.player.location, Sound.BLOCK_LEVER_CLICK, 0.5f, 1.0f)

        hitbox.location.world.spawnParticle(Particle.BLOCK_CRACK, hitbox.location, 10, 0.2, 0.2, 0.2, 0.0, MaterialData(Material.WHITE_WOOL))

        game.combo++

        if(game.hp < 100) {
            game.hp += 1
        }

        remove()
    }

    fun tick() {
        val bpm = game.beatMap.bpm
        val percentage = (System.currentTimeMillis() - spawnTime) / (60000.0 / bpm * 8.0)

        if(percentage >= 1) {
            game.player.sendActionBar(mm("<red>Miss!"))
            game.hp -= 10
            game.combo = 0
            remove()
            return
        }


        val x = startLocation.x + (targetLocation.x - startLocation.x) * percentage
        var y = startLocation.y + (targetLocation.y - startLocation.y) * percentage
        val z = startLocation.z + (targetLocation.z - startLocation.z) * percentage
        val yaw = armorStand.yaw + 3
        val pitch = startLocation.pitch

        val sin = sin(percentage * Math.PI * 2)
        val sinY = sin * 0.5
        y += sinY

        armorStand.teleport(Location(startLocation.world, x, y, z, yaw, pitch))

        hitbox.teleport(Location(startLocation.world, x, y + 1, z, yaw, pitch))
    }

    fun remove() {
        armorStand.remove()
        hitbox.remove()

        isDone = true
    }
}