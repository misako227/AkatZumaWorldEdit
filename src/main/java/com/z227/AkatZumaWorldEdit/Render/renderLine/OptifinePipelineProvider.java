package com.z227.AkatZumaWorldEdit.Render.renderLine;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;


/*  by https://www.mcmod.cn/class/13662.html, https://github.com/zbx1425/WorldEditCUI-Arch
 *  author:zbx1425
 */

/**
 * Optifine shaders uses a lot more frame buffers to render the world -- we have to make sure we're on the right one.
 *
 * @see <a href="https://github.com/sp614x/optifine/blob/master/OptiFineDoc/doc/shaders.txt">the shaders documentation</a>
 */
public final class OptifinePipelineProvider  {
//    private static final Logger LOGGER = LogUtils.getLogger();
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    // For inspection purposes, get the active program
//    private static final MethodHandle SHADERS_ACTIVE_PROGRAM;
//    private static final MethodHandle PROGRAM_GET_NAME;

    // Our part of the rendering pipeline -- we want to disable textures (maybe more?)
    private static final MethodHandle CONFIG_IS_SHADERS;
    private static final MethodHandle SHADERS_BEGIN_LEASH;
    private static final MethodHandle SHADERS_END_LEASH;
//    private static final MethodHandle SHADERS_IS_SHADOW_PASS;
    private static boolean optifineDisabled = false;

    static {
        MethodHandle configIsShaders = null;
        MethodHandle shadersEndLeash = null;
        MethodHandle shadersBeginLeash = null;
//        MethodHandle programGetName = null;
//        MethodHandle shadersActiveProgram = null;
//        MethodHandle shadersIsShadowPass = null;
        try {
            final Class<?> config = Class.forName("net.optifine.Config");
            final Class<?> shaders = Class.forName("net.optifine.shaders.Shaders");
//            final Class<?> program = Class.forName("net.optifine.shaders.Program");
            configIsShaders = LOOKUP.findStatic(config, "isShaders", MethodType.methodType(boolean.class));
            shadersEndLeash = LOOKUP.findStatic(shaders, "endLeash", MethodType.methodType(void.class));
            shadersBeginLeash = LOOKUP.findStatic(shaders, "beginLeash", MethodType.methodType(void.class));
//            programGetName = LOOKUP.findVirtual(program, "getName", MethodType.methodType(String.class));
//            shadersActiveProgram = LOOKUP.findStaticGetter(shaders, "activeProgram", program);
//            shadersIsShadowPass = LOOKUP.findStaticGetter(shaders, "isShadowPass", boolean.class);
//            LOGGER.debug("Optifine integration successfully initialized");
        } catch (final IllegalAccessException | ClassNotFoundException | NoSuchMethodException ignore) {
            // Optifine not available
            optifineDisabled = true;
        }

        CONFIG_IS_SHADERS = configIsShaders;
        SHADERS_END_LEASH = shadersEndLeash;
        SHADERS_BEGIN_LEASH = shadersBeginLeash;
//        PROGRAM_GET_NAME = programGetName;
//        SHADERS_ACTIVE_PROGRAM = shadersActiveProgram;
//        SHADERS_IS_SHADOW_PASS = shadersIsShadowPass;
    }


    public static boolean available() {
        return SHADERS_END_LEASH != null && SHADERS_BEGIN_LEASH != null && CONFIG_IS_SHADERS != null && !optifineDisabled;
    }


    public static Runnable beginLeash() {
        return () -> {
            if (!available()) {
                return;
            }

            try {
                final boolean shadersEnabled = (boolean) CONFIG_IS_SHADERS.invoke();
                if (shadersEnabled) {
                    SHADERS_BEGIN_LEASH.invoke();
                }
            } catch (final Throwable thr) {
                optifineDisabled = true;
//                LOGGER.error("Failed to render WECUI using OptiFine hooks", thr);
            }
        };
    }

    public static Runnable endLeash() {
        return () -> {
            if (!available()) {
                return;
            }

            try {
                final boolean shadersEnabled = (boolean) CONFIG_IS_SHADERS.invoke();
                if (shadersEnabled) {
                    SHADERS_END_LEASH.invoke();
                }
            } catch (final Throwable thr) {
                optifineDisabled = true;
//                LOGGER.error("Failed to render WECUI using OptiFine hooks", thr);
            }
        };
    }
}
