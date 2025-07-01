package com.z227.ImGuiRender;


import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.InputStream;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

@OnlyIn(Dist.CLIENT)
public class ImGuiInit {
    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private static long windowHandle;

    public static void onGlfwInit(long handle) {
        initializeImGui(handle);
        imGuiGlfw.init(handle,true);
        imGuiGl3.init();
        windowHandle = handle;
    }

    public static void onFrameRender() {
        //todo, 此处退出会崩溃,siginfo: EXCEPTION_ACCESS_VIOLATION (0xc0000005), reading address 0x000002ad14d96000
        if(!EditModeData.getEditMode()) return;
        imGuiGlfw.newFrame();
        imGuiGl3.newFrame();
        ImGui.newFrame();


        EditModeUI.render();

        ImGui.render();
        endFrame(windowHandle);
    }

    public static void onFirstFrameRender() {
        //todo, 此处退出会崩溃,siginfo: EXCEPTION_ACCESS_VIOLATION (0xc0000005), reading address 0x000002ad14d96000

        imGuiGlfw.newFrame();
        imGuiGl3.newFrame();
        ImGui.newFrame();

        ImGui.render();
        endFrame(windowHandle);
    }

    public static void initializeImGui(long glHandle) {
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename(null);                               // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);     // Enable Docking
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);   // Enable Multi-Viewport / Platform Windows
        io.setConfigViewportsNoTaskBarIcon(false);

        initFonts(io);

        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final ImGuiStyle style = ImGui.getStyle();

            style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1));
            style.setGrabRounding(5);
            style.setFrameRounding(5);
            style.setPopupRounding(5);
            style.setChildRounding(5);
            style.setWindowRounding(5);
            style.setTabRounding(5);
        }
    }

    private static void endFrame(long windowPtr) {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }

//        glfwSwapBuffers(windowPtr);
//        glfwPollEvents();
    }


    public static void initFonts(final ImGuiIO io) {
        ImFontAtlas fontAtlas = io.getFonts();
        ImFontConfig fontConfig = new ImFontConfig();
        fontAtlas.setFreeTypeRenderer(true);
        fontConfig.setPixelSnapH(true);

        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
//        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesChineseFull());

        fontAtlas.addFontFromMemoryTTF(loadFromResources("NotoSansCJK-Regular-1.otf"),18, fontConfig);
        fontAtlas.build();

        fontConfig.destroy();
    }

    private static byte[] loadFromResources(String name) {
        try {
            InputStream is = ImGui.class.getClassLoader().getResourceAsStream("assets/akatzumaworldedit/libs/fonts/" + name);
            return is.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

