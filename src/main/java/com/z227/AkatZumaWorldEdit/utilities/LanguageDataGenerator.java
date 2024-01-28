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
            this.add("item.akatzumaworldedit.wood_axe","§b选区斧头");
            this.add("block.akatzumaworldedit.building_consumable","§b建筑耗材");
            this.add("item.akatzumaworldedit.building_consumable.desc","用于copy等指令消耗的材料");
            this.add("item.test_item.desc1", "§a左键§f选取第一个点");
            this.add("item.test_item.desc2", "§6右键§f选取第二个点");
            this.add("item.akatzumaworldedit.query_blockstate_item", "§b查询方块状态");
            this.add("item.QueryBlockStateItem.desc1", "§a左键§f方块查询方块状态");
            this.add("item.QueryBlockStateItem.desc2", "§6右键§f放置一个查询的方块");
            this.add("item.QueryBlockStateItem.desc3", "点击消息可以复制状态到粘贴板");
            this.add("chat.item.query_block_state.null", "§c 没有找到要复制的状态，请先使用左键查看状态");
            this.add("chat.item.query_block_state.not_air", "§c 要放置的位置有方块，请先清除方块");

            this.add("chat.akatzuma.error.wait", "§c 请等待上次操作完成");
            this.add("chat.akatzuma.error.invalid_pos", "§c 无效的选区，请检查两个选区位置");
            this.add("chat.akatzuma.error.volume_too_long", "§c 选区过大，选区最大范围为");
            this.add("chat.akatzuma.error.chunk_not_loaded", "§c 区块没有加载");
            this.add("chat.akatzuma.error.black_list", "§c 这个方块在黑名单中，无法进行操作");
            this.add("chat.akatzuma.error.inventory_not_enough", "§c 背包中需要方块数量不够，无法进行操作，需要");
            this.add("chat.akatzuma.error.low_hight", "§c 已忽略低于高度限制的方块");
            this.add("chat.akatzuma.error.no_copy_map", "§c 没有找到复制的内容，请先使用§d /a copy §f指令创建一个");
            this.add("chat.akatzuma.error.not_permission_place", "§c 请检查当前位置是否有权限放置");
            this.add("chat.akatzuma.error.no_replace", "§c 没有找到要替换的方块");

            this.add("chat.akatzuma.error.not_undo", "§c 没有要撤销的操作");
            this.add("chat.akatzuma.error.not_redo", "§c 没有要redo的操作");
            this.add("chat.akatzuma.success_undo", "§a 撤销成功");
            this.add("chat.akatzuma.success_redo", "§a redo成功");
            this.add("chat.akatzuma.copyBlock.copy_success", "§a 已复制选区内方块数：");
            this.add("chat.akatzuma.success.flip", "§a 已翻转复制的选区内容，使用§d /a paste §f粘帖");
            this.add("chat.akatzuma.success.paste", "§a 粘帖成功，可以使用§d/a undo§f指令撤销操作");
            this.add("chat.akatzuma.success.stack", "§a 堆叠成功，可以使用§d/a undo§f指令撤销操作");

            this.add("chat.akatzuma.set.success", "已放置方块");
            this.add("chat.akatzuma.undo.tip", ",可以使用§d/a undo§f指令撤销操作");
            this.add("chat.action.copy", "点击复制: ");
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
        }
    }


}
