package cc.dvitski.enchantedsails

import cc.dvitski.enchantedsails.enchantment.ESBoatEnchantments
import cc.dvitski.enchantedsails.entity.BoatAccessor
import cc.dvitski.enchantedsails.network.BoatItemStackPayload
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.boat.AbstractBoat
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult

object BoatHandler {
    fun modifyPlacedBoat(entity: AbstractBoat, level: Level, hit: HitResult, stack: ItemStack, player: Player) {
        val accessor = entity as BoatAccessor
        accessor.itemStack = stack
    }

    fun modifyDroppedBoat(stack: ItemStack?, originalStack: ItemStack): ItemStack {
        return stack?.also { stack ->
            stack.applyComponents(originalStack.componentsPatch)
        } ?: originalStack;
    }

    fun sendBoatStackPayload(entity: Entity, stack: ItemStack) {
        val level = entity.level()
        if (level is ServerLevel) {
            val chunkMap = level.chunkSource.chunkMap
            val payload = BoatItemStackPayload(entity.id, stack)
            chunkMap.sendToTrackingPlayers(entity, ServerPlayNetworking.createS2CPacket(payload))
        }
    }

    fun addBoatSpeed(entity: AbstractBoat, stack: ItemStack, inputUp: Boolean, inputDown: Boolean): Float {
        if (entity.status == AbstractBoat.Status.ON_LAND) {
            return 0.0f
        }

        val registryAccess = entity.registryAccess()
        val enchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)

        val tailwindLevel = stack.enchantments.getLevel(enchantments.getOrThrow(ESBoatEnchantments.TAILWIND))
        return if (inputUp) {
            tailwindLevel * 0.04f / 2
        } else {
            if (inputDown) {
                tailwindLevel * -0.02f / 2
            } else {
                0.0f
            }
        }
    }

    fun modifyBoatRotation(entity: AbstractBoat, stack: ItemStack, original: Float, inputLeft: Boolean, inputRight: Boolean): Float {
        val registryAccess = entity.registryAccess()
        val enchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT)

        val controlLevel = stack.enchantments.getLevel(enchantments.getOrThrow(ESBoatEnchantments.CONTROL))

        if (controlLevel > 0) {
            return if (inputLeft) {
                -6.0f
            } else if (inputRight) {
                6.0f
            } else {
                0.0f
            }
        }

        return original
    }
}
