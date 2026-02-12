package cc.dvitski.enchantedsails.mixin;

import cc.dvitski.enchantedsails.BoatHandler;
import cc.dvitski.enchantedsails.EnchantedSails;
import cc.dvitski.enchantedsails.entity.BoatAccessor;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoat.class)
abstract class AbstractBoatMixin extends VehicleEntity implements BoatAccessor {
    @Shadow
    private boolean inputUp;
    @Shadow
    private boolean inputDown;
    @Shadow
    private float deltaRotation;
    @Shadow
    private boolean inputLeft;
    @Shadow
    private boolean inputRight;
    @Unique
    private static final String enchantedsails$ITEM_STACK_KEY = Identifier.fromNamespaceAndPath(EnchantedSails.MOD_ID, "stack").toString();

    @NotNull
    @Unique
    private ItemStack itemStack = ItemStack.EMPTY;

    private AbstractBoatMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @WrapMethod(method = "getPickResult")
    private ItemStack onPickItem(Operation<ItemStack> original) {
        return BoatHandler.INSTANCE.modifyDroppedBoat(this.getItemStack(), original.call());
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void onSave(ValueOutput values, CallbackInfo ci) {
        ItemStack stack = this.getItemStack();
        values.store(enchantedsails$ITEM_STACK_KEY, ItemStack.OPTIONAL_CODEC, stack);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void onRead(ValueInput values, CallbackInfo ci) {
        values.read(enchantedsails$ITEM_STACK_KEY, ItemStack.CODEC).ifPresent(this::setItemStack);
    }

    @ModifyVariable(method = "controlBoat", at = @At(value = "STORE", ordinal = 0))
    private float onControlBoat(float original) {
        return original + BoatHandler.INSTANCE.addBoatSpeed((AbstractBoat) (Object) this, this.itemStack, this.inputUp, this.inputDown);
    }

    @Inject(method = "floatBoat", at = @At("TAIL"))
    private void endFloatBoat(CallbackInfo ci) {
        this.deltaRotation = BoatHandler.INSTANCE.modifyBoatRotation((AbstractBoat) (Object) this, this.itemStack, this.deltaRotation, this.inputLeft, this.inputRight);
    }

    @Unique
    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Unique
    @Override
    public void setItemStack(@NotNull ItemStack stack) {
        this.itemStack = stack.copy();
        BoatHandler.INSTANCE.sendBoatStackPayload(this, stack);
    }
}
