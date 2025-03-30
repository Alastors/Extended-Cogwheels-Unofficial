package com.rabbitminers.extendedgears.forge;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry;
import com.rabbitminers.extendedgears.client.instance.DynamicCogwheelInstance;
import com.rabbitminers.extendedgears.cogwheels.DynamicCogwheelRenderer;
import com.rabbitminers.extendedgears.config.ExtendedCogwheelsConfig;
import com.rabbitminers.extendedgears.registry.ExtendedCogwheelsTileEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ExtendedCogwheelsClientImpl {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent event) -> {
            event.enqueueWork(() -> {
                // ✅ Only register the Flywheel instance if casing rendering is ON
                if (ExtendedCogwheelsConfig.common().renderCasings.get()) {
                    InstancedRenderRegistry.configure(
                                    ExtendedCogwheelsTileEntities.CUSTOM_COGWHEEL_TILE_ENTITY.get()
                            )
                            .factory(DynamicCogwheelInstance::new)
                            .apply();
                }

                // ✅ BlockEntityRenderer always gets registered
                BlockEntityRenderers.register(
                        ExtendedCogwheelsTileEntities.CUSTOM_COGWHEEL_TILE_ENTITY.get(),
                        ctx -> new DynamicCogwheelRenderer()
                );
            });
        });
    }
}