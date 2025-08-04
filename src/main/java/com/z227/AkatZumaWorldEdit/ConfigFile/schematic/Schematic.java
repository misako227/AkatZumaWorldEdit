//package com.z227.AkatZumaWorldEdit.ConfigFile.schematic;
//
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.NbtIo;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.player.Player;
//
//import java.io.DataInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.zip.GZIPInputStream;
//
//public class Schematic {
//    //Metadata
//    int WEOffsetX;
//    int WEOffsetY;
//    int WEOffsetZ;
//
//    //Palette
//    //{BlockState : 0}
//    //Object2IntMap<String> palette = new Object2IntLinkedOpenHashMap<>();
//    CompoundTag Palette;
//    List<CompoundTag> BlockEntities;
//
//    int DataVersion;
//
//    String Height;
//    String Length;
//    String Width;
//    int PaletteMax;
//    int Version;
//
//    //BlockData
//    byte[] BlockData;
//    //ByteArrayOutputStream BlockData = new ByteArrayOutputStream(width * height * length);
//    int[] Offset;
//
//    static String schematicDirectory = "config/AkatZumaWorldEdit/schematics";
//
//    public static void init(){
//        File dir = new File(schematicDirectory);
//        if (!dir.exists()) {
//            if (!dir.mkdirs()) {
//                System.err.println("Akatzuma Failed to create log directory: " + schematicDirectory);
//            }
//        }
//    }
//
//    public Schematic(String schematicName, Player  player) {
//        try {
//            File schematicFile = new File(schematicDirectory + "/" + schematicName + ".schem");
//            if (!schematicFile.exists()) {
//                AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.error.schematic_not_found"), player);
//                return;
//            }
//
//            // 读取NBT文件
//            DataInputStream data = new DataInputStream(new GZIPInputStream(new FileInputStream(schematicFile)));
//            CompoundTag schematicTag = NbtIo.read(data);
//            readSchematicData(schematicTag);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    public void readSchematicData(CompoundTag schematicTag) {
//        CompoundTag  Metadata = schematicTag.getCompound("Metadata");
//        WEOffsetX = Metadata.getInt("WEOffsetX");
//        WEOffsetY = Metadata.getInt("WEOffsetY");
//        WEOffsetZ = Metadata.getInt("WEOffsetZ");
//
//        Palette = schematicTag.getCompound("Palette");
//
//        this.DataVersion = schematicTag.getInt("DataVersion");
//        Height = schematicTag.getString("Height");
//        Width = schematicTag.getString("Width");
//        Length = schematicTag.getString("Length");
//        PaletteMax = schematicTag.getInt("PaletteMax");
//        Version = schematicTag.getInt("Version");
//        Offset = schematicTag.getIntArray("Offset");
//
//        BlockData = schematicTag.getByteArray("BlockData");
//        System.out.println(Arrays.toString(BlockData));
//
//
//
//    }
//}
