package cc.dvitski.enchantedsails.mixin;

import cc.dvitski.enchantedsails.BoatHandler;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BoatItem.class)
abstract class BoatItemMixin {
    @WrapMethod(method = "getBoat")
    private AbstractBoat onPlaceBoat(Level level, HitResult hit, ItemStack stack, Player player, Operation<AbstractBoat> original) {
        AbstractBoat boat = original.call(level, hit, stack, player);
        BoatHandler.INSTANCE.modifyPlacedBoat(boat, level, hit, stack, player);
        return boat;
    }
}
