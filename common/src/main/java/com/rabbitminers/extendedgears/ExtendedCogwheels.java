package com.rabbitminers.extendedgears;

import com.rabbitminers.extendedgears.base.lang.ExtendedCogwheelsLanguageProvider;
import com.rabbitminers.extendedgears.datagen.ExtendedCogwheelsCuttingRecipeGen;
import com.rabbitminers.extendedgears.datagen.ExtendedCogwheelsDeployingRecipeGen;
import com.rabbitminers.extendedgears.datagen.ExtendedCogwheelsStandardRecipeGen;
import com.rabbitminers.extendedgears.registry.ExtendedCogwheelsBlocks;
import com.rabbitminers.extendedgears.registry.ExtendedCogwheelsCogwheelMaterials;
import com.rabbitminers.extendedgears.registry.ExtendedCogwheelsCreativeModeTabs;
import com.rabbitminers.extendedgears.registry.ExtendedCogwheelsItems;
import com.rabbitminers.extendedgears.registry.ExtendedCogwheelsPackets;
import com.rabbitminers.extendedgears.registry.ExtendedCogwheelsTileEntities;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class ExtendedCogwheels {
    // Keep extended gears as to not break past worlds
    public static final String MOD_ID = "extendedgears";
    public static final String NAME = "Extended Cogwheels Unofficial";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final int DATA_FIXER_VERSION = 1;
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ExtendedCogwheels.MOD_ID);

    public static void init() {
        ExtendedCogwheelsCreativeModeTabs.init();
        ExtendedCogwheelsItems.init();
        ExtendedCogwheelsBlocks.init();
        ExtendedCogwheelsTileEntities.init();
        ExtendedCogwheelsCogwheelMaterials.init();
        ExtendedCogwheelsPackets.PACKETS.registerC2SListener();
    }

    public static void gatherData(DataGenerator.PackGenerator gen) {
        gen.addProvider(ExtendedCogwheelsStandardRecipeGen::new);
        gen.addProvider(ExtendedCogwheelsCuttingRecipeGen::new);
        gen.addProvider(ExtendedCogwheelsDeployingRecipeGen::new);
        gen.addProvider((PackOutput output) -> ExtendedCogwheelsLanguageProvider
                .createMerger(output, MOD_ID, "Extended Cogwheels Unofficial", ExtendedCogwheelsLanguageProvider.values()));
    }

    @ExpectPlatform
    public static Path configDir() {
        throw new AssertionError();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
