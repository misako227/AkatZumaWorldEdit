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
            this.add("chat.item.wood_axe.right", "§6右键§f选择了位置:");
            this.add("chat.item.query_block_state.right", "方块状态:");
            this.add("item.akatzumaworldedit.wood_axe","§b选区斧头");
            this.add("item.test_item.desc1", "§a左键§f选取第一个点");
            this.add("item.test_item.desc2", "§6右键§f选取第二个点");
            this.add("item.akatzumaworldedit.query_blockstate_item", "§b查询方块状态");
            this.add("item.QueryBlockStateItem.desc1", "右键§a方块§f查询方块状态");
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
