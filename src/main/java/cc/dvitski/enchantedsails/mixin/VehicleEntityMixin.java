package cc.dvitski.enchantedsails.mixin;

import cc.dvitski.enchantedsails.BoatHandler;
import cc.dvitski.enchantedsails.entity.BoatAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VehicleEntity.class)
abstract class VehicleEntityMixin {
    @WrapOperation(
            method = "destroy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/Item;)V",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private ItemStack onBoatItem(ItemLike itemLike, Operation<ItemStack> original) {
        ItemStack originalStack = original.call(itemLike);

        if (this instanceof BoatAccessor accessor) {
            return BoatHandler.INSTANCE.modifyDroppedBoat(accessor.getItemStack(), originalStack);
        }

        return originalStack;
    }
}
