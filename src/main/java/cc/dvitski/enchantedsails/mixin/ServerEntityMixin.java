package cc.dvitski.enchantedsails.mixin;

import cc.dvitski.enchantedsails.entity.BoatAccessor;
import cc.dvitski.enchantedsails.network.BoatItemStackPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
abstract class ServerEntityMixin {
    @Shadow
    @Final
    private Entity entity;

    @Inject(method = "addPairing", at = @At("TAIL"))
    private void onAddPairing(ServerPlayer player, CallbackInfo ci) {
        if (this.entity instanceof BoatAccessor accessor) {
            ItemStack stack = accessor.getItemStack();
            ServerPlayNetworking.send(player, new BoatItemStackPayload(this.entity.getId(), stack));
        }
    }
}
