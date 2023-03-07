package com.rabbitminers.extendedgears.mixin;

import com.jozufozu.flywheel.api.MaterialManager;
import com.rabbitminers.extendedgears.cogwheels.CustomCogwheelTileEntity;
import com.rabbitminers.extendedgears.cogwheels.CustomCogwheelTileInstance;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticTileInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BracketedKineticTileInstance.class)
public class MixinBracketedKineticTileInstance extends SingleRotatingInstance {
    public MixinBracketedKineticTileInstance(MaterialManager modelManager, KineticTileEntity tile) {
        super(modelManager, tile);
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/Direction;fromAxisAndDirection(Lnet/minecraft/core/Direction$Axis;Lnet/minecraft/core/Direction$AxisDirection;)Lnet/minecraft/core/Direction;"
            ),
            remap = false,
            cancellable = true
    )
    public void disableAdditionalShaft(CallbackInfo ci) {
        if (super.blockEntity instanceof CustomCogwheelTileEntity)
            ci.cancel();
    }
}