package cc.dvitski.enchantedsails.client

import cc.dvitski.enchantedsails.EnchantedSails
import cc.dvitski.enchantedsails.entity.BoatAccessor
import com.mojang.blaze3d.vertex.PoseStack
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.state.BoatRenderState
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.vehicle.boat.AbstractBoat
import net.minecraft.world.item.ItemStack

object ClientBoatHandler {
    val GLINT_RENDER_STATE: RenderStateDataKey<Boolean> = RenderStateDataKey.create { "Glint ((${EnchantedSails.MOD_NAME}))" }

    fun renderGlint(model: EntityModel<BoatRenderState>, state: BoatRenderState, stack: PoseStack, collector: SubmitNodeCollector, light: Int) {
        if (state.getDataOrDefault(GLINT_RENDER_STATE, false)) {
            collector.submitModel(model, state, stack, RenderTypes.entityGlint(), light, OverlayTexture.NO_OVERLAY, state.outlineColor, null)
        }
    }

    fun extractRenderState(entity: AbstractBoat, state: BoatRenderState) {
        if (entity !is BoatAccessor) {
            return
        }

        state.setData(GLINT_RENDER_STATE, entity.itemStack.hasFoil())
    }

    fun receiveItemStackPayload(entity: Entity, stack: ItemStack) {
        if (entity !is BoatAccessor) {
            return
        }

        entity.itemStack = stack
    }
}
