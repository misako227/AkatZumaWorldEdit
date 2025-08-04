package com.z227.AkatZumaWorldEdit.Commands.otherCommand;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

public class SetFlagCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("update")
                                .executes((context) -> {
                                    setFlag(context);
                                    return 1;
                                }))
        );
    }
    public static void setFlag(CommandContext<CommandSourceStack> context) {
        Player player = context.getSource().getPlayer();
        AttributeInstance attribute = player.getAttribute(AkatZumaWorldEdit.SET_FLAG_ATTRIBUTE.get());
        if(attribute != null) {
            double flag = attribute.getBaseValue();
            if(flag > 0) {
                attribute.setBaseValue(0);
                attribute.save();
                flag=0;

            } else {
                attribute.setBaseValue(1);
                attribute.save();
                flag=1;
            }

            Component component = Component.translatable("chat.akatzuma.set_flag_state_" + (int)flag);
            AkatZumaWorldEdit.sendAkatMessage(component, player);

        }

    }


}
