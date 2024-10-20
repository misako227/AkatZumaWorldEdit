//package com.z227.AkatZumaWorldEdit.utilities;
//
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import net.minecraft.data.PackOutput;
//import net.minecraft.data.recipes.FinishedRecipe;
//import net.minecraft.data.recipes.RecipeCategory;
//import net.minecraft.data.recipes.RecipeProvider;
//import net.minecraft.data.recipes.ShapedRecipeBuilder;
//import net.minecraft.world.item.Items;
//
//import java.util.function.Consumer;
//
//public class AkatRecipe extends RecipeProvider {
//
//    public AkatRecipe(PackOutput pOutput) {
//        super(pOutput);
//    }
//
//    @Override
//    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
//        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AkatZumaWorldEdit.Query_Item.get())
//                .define('#', Items.GLASS)
//                .define('X', Items.STICK)
//                .pattern("#")
//                .pattern("X")
//                .unlockedBy("has_stick", has(Items.STICK))
//                .showNotification(true)
//                .save(pWriter);
//        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AkatZumaWorldEdit.WOOD_AXE.get())
//                .define('#', Items.WOODEN_AXE)
//                .define('X', Items.STICK)
//                .pattern("#")
//                .pattern("X")
//                .unlockedBy("has_stick", has(Items.STICK))
//                .showNotification(true)
//                .save(pWriter);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AkatZumaWorldEdit.Building_Consumable_Item.get(),2)
//                .define('X', Items.OAK_LOG)
//                .pattern("X")
//                .pattern("X")
//                .unlockedBy("has_stick", has(Items.STICK))
//                .showNotification(false)
//                .save(pWriter);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AkatZumaWorldEdit.Projector_Item.get())
//                .define('X', Items.IRON_INGOT)
//                .define('Y', Items.LAPIS_LAZULI)
//                .define('Z', Items.REDSTONE)
//                .pattern("XZX")
//                .pattern("XYX")
//                .pattern("XZX")
//                .unlockedBy("has_stick", has(Items.STICK))
//                .showNotification(true)
//                .save(pWriter);
//        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AkatZumaWorldEdit.Line_Item.get())
//                .define('X', Items.STICK)
//                .define('Y', Items.FEATHER)
//                .pattern("Y")
//                .pattern("X")
//                .unlockedBy("has_stick", has(Items.STICK))
//                .showNotification(false)
//                .save(pWriter);
//    }
//}
//
//
