package com.z227.AkatZumaWorldEdit.utilities;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

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
}
