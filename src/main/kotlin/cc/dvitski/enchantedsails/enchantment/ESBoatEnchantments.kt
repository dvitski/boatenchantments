package cc.dvitski.enchantedsails.enchantment

import cc.dvitski.enchantedsails.EnchantedSails
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment

object ESBoatEnchantments {
    val TAILWIND = register("tailwind")
    val CONTROL = register("control")

    private fun register(id: String): ResourceKey<Enchantment> {
        return ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(EnchantedSails.MOD_ID, id))
    }
}
