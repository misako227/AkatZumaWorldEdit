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
            this.add("chat.item.wood_axe.left", "§a左键§f选择了位置:");
            this.add("chat.item.wood_axe.left_error", "§c最低选区高度:§4");
            this.add("chat.item.wood_axe.right", "§6右键§f选择了位置:");
            this.add("chat.item.query_block_state.right", "方块状态:");
            this.add("item.akatzumaworldedit.wood_axe","§b选区斧头");
            this.add("item.test_item.desc1", "§a左键§f选取第一个点");
            this.add("item.test_item.desc2", "§6右键§f选取第二个点");
            this.add("item.akatzumaworldedit.query_blockstate_item", "§b查询方块状态");
            this.add("item.QueryBlockStateItem.desc1", "右键§a方块§f查询方块状态");
            this.add("item.QueryBlockStateItem.desc2", "点击消息复制状态");

            this.add("chat.akatzuma.error.wait", "§c 请等待上次操作完成");
            this.add("chat.akatzuma.error.invalid_pos", "§c 无效的选区，请检查两个选区位置");
            this.add("chat.akatzuma.error.volume_too_long", "§c 选区过大，选区最大范围为§5");
            this.add("chat.akatzuma.error.chunk_not_loaded", "§c 区块没有加载");
            this.add("chat.akatzuma.error.black_list", "§c 此方块在黑名单中，无法进行操作");

            this.add("chat.akatzuma.set.success", "已设置方块");
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
