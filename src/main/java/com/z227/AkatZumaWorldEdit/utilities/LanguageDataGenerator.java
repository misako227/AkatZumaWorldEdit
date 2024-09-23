package com.z227.AkatZumaWorldEdit.utilities;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;


public class LanguageDataGenerator {



    // 中文语言文件
    public static class ChineseLanguageProvider extends LanguageProvider {
        public ChineseLanguageProvider(PackOutput packOutput) {
            super(packOutput, AkatZumaWorldEdit.MODID, "zh_cn");
        }

        @Override
        protected void addTranslations() {
            this.add("item_group.akatzumaworldedit.item", "AkatZuma的工具");
            this.add("chat.item.wood_axe.left", "§a左键§f选择了位置:");
            this.add("chat.item.wood_axe.left_error", "§c最低选区高度:§4");
            this.add("chat.item.wood_axe.right", "§6右键§f选择了位置:");
            this.add("chat.item.query_block_state.right", "方块状态:");
            this.add("item.akatzumaworldedit.wood_axe", "§b选区斧头");
            this.add("block.akatzumaworldedit.building_consumable", "§b建筑耗材");
            this.add("item.akatzumaworldedit.building_consumable.desc", "用于copy等指令消耗的材料\n(可穿戴到头部)");
            this.add("item.wood_axe.desc1", "§a左键§9方块§f/§9空气§f选取第一个点");
            this.add("item.wood_axe.desc2", "§6右键§9方块§f/§9空气§f选取第二个点");
            this.add("item.wood_axe.desc3", "§eCtrl+滚轮§f扩大/缩小选区位置1");
            this.add("item.wood_axe.desc4", "§eAlt +滚轮§f扩大/缩小选区位置2");
            this.add("item.akatzumaworldedit.projector", "§b投影");
            this.add("item.projector_item.desc1", "§a左键§f复制选区的建筑");
            this.add("item.projector_item.desc2", "§6右键§f粘帖建筑");
            this.add("item.projector_item.desc3", "§6ctrl+右键§f忽略空气粘帖(/a paste -a)");
            this.add("item.projector_item.desc4", "手持此物品可查看复制的投影");
            this.add("item.akatzumaworldedit.query_blockstate_item", "§b查询方块状态");
            this.add("item.query_block_state.desc1", "§a左键§f方块查询方块状态");
            this.add("item.query_block_state.desc2", "§aCtrl+左键§f设置替换方块状态");
            this.add("item.query_block_state.desc3", "§6右键§f放置/替换成查询的方块");
            this.add("item.query_block_state.desc4", "§6Ctrl+右键§f把选区设置/替换为查询的方块");
            this.add("item.query_block_state.desc5", "§eCtrl+滚轮§f切换模式");
            this.add("item.query_block_state.desc6", "§eCtrl+Alt+滚轮§f切换两个方块位置");




            this.add("item.query_block_state.desc_block", "查询的方块：");
            this.add("chat.item.query_block_state.null", "§c 没有找到要复制的状态，请先使用左键查看状态");
            this.add("chat.item.query_block_state.not_air", "§c 要放置的位置有方块，请先清除方块");
            this.add("item.akatzumaworldedit.bind_inventory", "§b绑定箱子工具");

            this.add("key.akatzuma", "AkatZumaWorldEdit");
            this.add("key.category.undo", "撤销");

            this.add("chat.akatzuma.error.wait", "§c 请等待上次操作完成");
            this.add("chat.akatzuma.error.invalid_pos", "§c 无效的选区，请检查两个选区位置");
            this.add("chat.akatzuma.error.volume_too_long", "§c 选区过大，选区最大范围为");
            this.add("chat.akatzuma.error.chunk_not_loaded", "§c 区块没有加载");
            this.add("chat.akatzuma.error.black_list", "§c 这个方块在黑名单中，无法进行操作");
            this.add("chat.akatzuma.error.inventory_not_enough", "§c 背包中方块数量不够，需要");
            this.add("chat.akatzuma.error.current_num", "§c 当前数量");
            this.add("chat.akatzuma.error.ignore_low_hight", "§c 已忽略低于高度限制的方块");
            this.add("chat.akatzuma.error.low_hight", "§c 低于高度限制");
            this.add("chat.akatzuma.error.no_copy_map", "§c 没有找到复制的内容，请先使用§d /a copy §f指令创建一个");
            this.add("chat.akatzuma.error.not_permission_place", "§c 请检查是否有权限放置");
            this.add("chat.akatzuma.error.no_replace", "§c 没有找到要替换的方块");
            this.add("chat.akatzuma.error.black_world", "§c 当前世界在黑名单中，无法进行操作");
            this.add("chat.akatzuma.error.too_fast", "§c 速度太快，请稍后再发送");
            this.add("chat.akatzuma.error.line_too_short", "§c 选区太小，请扩大选区");

            this.add("chat.akatzuma.error.not_undo", "§c 没有要撤销的操作");
            this.add("chat.akatzuma.error.not_redo", "§c 没有要redo的操作");
            this.add("chat.akatzuma.success_undo", "§a 撤销成功");
            this.add("chat.akatzuma.success_redo", "§a redo成功");
            this.add("chat.akatzuma.copyBlock.copy_success", "§a 已复制选区内方块数：");
            this.add("chat.akatzuma.success.flip", "§a 已翻转复制的选区内容，使用§d /a paste §f粘帖");
            this.add("chat.akatzuma.success.rotate", "§a 已旋转复制的选区内容，使用§d /a paste §f粘帖");

            this.add("chat.akatzuma.success.paste", "§a 粘帖成功，可以使用§d/a undo§f指令撤销操作");
            this.add("chat.akatzuma.success.stack", "§a 堆叠成功，可以使用§d/a undo§f指令撤销操作");

            this.add("chat.akatzuma.success.add_viplayer", "添加成功 ");
            this.add("chat.akatzuma.success.del_viplayer", "删除成功 ");
            this.add("chat.akatzuma.error.not_viplayer", "没有找到:");

            this.add("chat.akatzuma.set.success", "已放置方块");
            this.add("chat.akatzuma.undo.tip", ",可以使用§d/a undo§f指令撤销操作");
            this.add("chat.action.copy", "点击复制: ");

            this.add("hud.akatzuma.ctrl_scroll", "§actrl+滚轮§f切换");
            this.add("hud.akatzuma.right", "§6右键§f绑定箱子");
            this.add("hud.akatzuma.ctrl_right", "§6ctrl+右键§f传送到绑定的坐标上");

            this.add("hud.akatzuma.query_mode", "模式：");
            this.add("hud.akatzuma.query_mode_1", "放置");
            this.add("hud.akatzuma.query_mode_2", "替换");
            this.add("hud.akatzuma.query_block1", "方块：");
            this.add("hud.akatzuma.query_block2", "替换：");

            this.add("chat.akatzuma.mask_null", "§c 当前物品没有绑定笔刷");

            this.add("hud.akatzuma.mask_mode_white", "白名单");
            this.add("hud.akatzuma.mask_mode_black", "§c黑名单");
            this.add("chat.akatzuma.error.brush_air", "§c 绑定失败，请使用其他物品");
            this.add("chat.akatzuma.error.unbind", "§c 当前物品没有绑定笔刷");


            this.add("chat.akatzuma.success.bind_pos", "§a 绑定成功");
            this.add("chat.akatzuma.success.tp", "§a 传送成功");
            this.add("chat.akatzuma.error.bind_pos", "§c 绑定失败，请检查是否有权限或是否支持这个容器");
            this.add("chat.akatzuma.error.void_pos", "§c 无效坐标");
            this.add("chat.akatzuma.error.open_gui_fail", "§c 打开GUI失败");


            this.add("chat.akatzuma.error.line_pos_inadequate", "§c 点位不够，请创建两个以上的点位");
        }
    }

    // 英文语言文件
    public static class EnglishLanguageProvider extends LanguageProvider {
        public EnglishLanguageProvider(PackOutput packOutput) {
            super(packOutput,  AkatZumaWorldEdit.MODID,"en_us");
        }

        @Override
        protected void addTranslations() {
            this.add("chat.item.wood_axe.left", "left:");
            this.add("chat.item.wood_axe.right", "right:");
            this.add("item.akatzumaworldedit.wood_axe","§bwooden axe");
            this.add("item.akatzumaworldedit.query_blockstate_item", "§bquery block state");

            this.add("chat.akatzuma.success.add_viplayer", "添加成功:");
            this.add("chat.akatzuma.success.del_viplayer", "删除成功:");
            this.add("chat.akatzuma.error.not_viplayer",   "没有找到:");
        }
    }


}
