package cc.dvitski.enchantedsails.data.server

import cc.dvitski.enchantedsails.enchantment.ESBoatEnchantments
import cc.dvitski.enchantedsails.tag.ESItemTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.util.Util
import net.minecraft.world.item.enchantment.Enchantment
import java.util.Optional
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

class EnchantmentProvider(output: FabricDataOutput, future: CompletableFuture<HolderLookup.Provider>) : FabricCodecDataProvider<Enchantment>(output, future, Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC) {
    override fun configure(consumer: BiConsumer<Identifier, Enchantment>, provider: HolderLookup.Provider) {
        fun register(key: ResourceKey<Enchantment>,
                     enchantment: Enchantment.EnchantmentDefinition,
                     exclusiveSet: HolderSet<Enchantment> = HolderSet.direct(),
                     effects: DataComponentMap = DataComponentMap.EMPTY
        ) {
            val location = key.identifier()
            val description = Component.translatable(Util.makeDescriptionId("enchantment", location))
            consumer.accept(location, Enchantment(description, enchantment, exclusiveSet, effects))
        }

        val itemTags = provider.lookupOrThrow(Registries.ITEM)
        itemTags.getOrThrow(ESItemTags.ENCHANTABLE_BOATS).let { tag ->
            register(
                ESBoatEnchantments.TAILWIND, Enchantment.EnchantmentDefinition(
                    tag,
                    Optional.empty(),
                    8,
                    2,
                    Enchantment.dynamicCost(5, 25),
                    Enchantment.dynamicCost(25, 15),
                    8,
                    emptyList(),
                )
            )

            register(
                ESBoatEnchantments.CONTROL, Enchantment.EnchantmentDefinition(
                    tag,
                    Optional.empty(),
                    1,
                    1,
                    Enchantment.dynamicCost(25, 25),
                    Enchantment.dynamicCost(75, 25),
                    10,
                    emptyList(),
                )
            )
        }
    }

    override fun getName(): String {
        return "Enchantments"
    }
}
