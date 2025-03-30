// forge/src/main/java/com/rabbitminers/extendedgears/forge/ForgeClientHooks.java
package com.rabbitminers.extendedgears.forge;

import com.rabbitminers.extendedgears.platform.ClientHooks;
import com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry;
import com.rabbitminers.extendedgears.client.instance.DynamicCogwheelInstance;
import com.rabbitminers.extendedgears.registry.ExtendedCogwheelsTileEntities;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeClientHooks implements ClientHooks {
    @Override
    public void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent event) -> {
            event.enqueueWork(() -> {
                InstancedRenderRegistry.configure(
                    ExtendedCogwheelsTileEntities.CUSTOM_COGWHEEL_TILE_ENTITY.get()
                ).factory(DynamicCogwheelInstance::new).apply();
            });
        });
    }
}
