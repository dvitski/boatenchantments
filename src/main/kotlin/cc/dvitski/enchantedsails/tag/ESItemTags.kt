package cc.dvitski.enchantedsails.tag

import cc.dvitski.enchantedsails.EnchantedSails
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object ESItemTags {
    val ENCHANTABLE_BOATS = create("enchantable/boats")

    private fun create(id: String): TagKey<Item> {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EnchantedSails.MOD_ID, id))
    }
}
