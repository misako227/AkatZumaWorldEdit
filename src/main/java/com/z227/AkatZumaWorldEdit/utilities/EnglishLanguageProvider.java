package com.z227.AkatZumaWorldEdit.utilities;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

// 英文语言文件
public class EnglishLanguageProvider extends LanguageProvider {
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
