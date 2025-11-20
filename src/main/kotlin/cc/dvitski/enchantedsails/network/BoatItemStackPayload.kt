package cc.dvitski.enchantedsails.network

import cc.dvitski.enchantedsails.EnchantedSails
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

data class BoatItemStackPayload(val id: Int, val stack: ItemStack) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<BoatItemStackPayload> {
        return ESPacketTypes.BOAT_ITEM_STACK
    }

    companion object {
        val ID: Identifier = Identifier.fromNamespaceAndPath(EnchantedSails.MOD_ID, "boat_item_stack")

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BoatItemStackPayload> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, BoatItemStackPayload::id,
            ItemStack.OPTIONAL_STREAM_CODEC, BoatItemStackPayload::stack,
            ::BoatItemStackPayload,
        )
    }
}
