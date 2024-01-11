package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContextKey;

public class InfoCommand implements Command<CommandSourceStack> {
    public static InfoCommand instance = new InfoCommand();

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Player player = context.getSource().getPlayer();
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        BlockPos bp1= PMD.getPos1(), bp2 = PMD.getPos2();
        Level world = player.getCommandSenderWorld();



        PlaceBlock.traverseCube(bp1,bp2,world,player, Blocks.STONE.defaultBlockState());
        player.sendSystemMessage(Component.literal("[").append(AkatZumaWorldEdit.preAkat).append(Component.literal("已替换方块")));
        return 0;
    }
}