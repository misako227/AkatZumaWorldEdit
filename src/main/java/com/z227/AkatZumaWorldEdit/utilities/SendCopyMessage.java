package com.z227.AkatZumaWorldEdit.utilities;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class SendCopyMessage {
    public static Component send(String message) {
        Component msg = Component.translatable("chat.action.copy").append(Component.literal(message).withStyle(ChatFormatting.GREEN));
        Component component = Component.literal(message)
                .withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, msg))
                .withColor(ChatFormatting.GREEN)
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, message))
                );
        return component;

    }


    public static void sendSuccessMsg(BlockState blockState, Player player) {
        Component blockName = blockState.getBlock().getName().withStyle(ChatFormatting.GREEN);
        Component setSuccess = Component.translatable("chat.akatzuma.set.success").append(blockName).append(Component.translatable("chat.akatzuma.undo.tip"));
        AkatZumaWorldEdit.sendClientMessage(setSuccess, player);
    }

    public static void sendCommand(String command){
        LocalPlayer Lplayer = Minecraft.getInstance().player;
        if (Lplayer != null) {
            Lplayer.connection.sendCommand(command);
        }
    }
}
