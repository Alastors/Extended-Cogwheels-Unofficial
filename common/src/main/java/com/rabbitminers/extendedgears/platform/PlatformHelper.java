package com.rabbitminers.extendedgears.platform;

import java.util.ServiceLoader;

public class PlatformHelper {
    private static final ClientHooks CLIENT_HOOKS = load();

    private static ClientHooks load() {
        return ServiceLoader.load(ClientHooks.class).findFirst()
            .orElseThrow(() -> new RuntimeException("No ClientHooks implementation found!"));
    }

    public static void initClient() {
        CLIENT_HOOKS.init();
    }
}