package com.z227.ImGuiRender.mode;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public abstract class ModeBase {
    public String hoverText = "";
    public VertexBuffer vertexBuffer;
    public boolean refreshVertexBuffer = true;

    public void updateVertexBuffer() {

        if(vertexBuffer != null){
            this.vertexBuffer.close();
            this.vertexBuffer = null;
        }
        this.refreshVertexBuffer = true;

    }


    // ImGui渲染
    public void render(){}

    // 鼠标更新
    public void mouseUpdate(){}

    // Level渲染
    public void levelRender(PoseStack stack, Matrix4f mat4f, Vec3 view){}

}
