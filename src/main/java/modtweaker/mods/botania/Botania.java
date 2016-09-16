package modtweaker.mods.botania;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.botania.handlers.*;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class Botania {
    public Botania() {
        MineTweakerAPI.registerClass(Apothecary.class);
        MineTweakerAPI.registerClass(Brew.class);
        MineTweakerAPI.registerClass(ElvenTrade.class);
        MineTweakerAPI.registerClass(ManaInfusion.class);
        MineTweakerAPI.registerClass(Orechid.class);
        MineTweakerAPI.registerClass(PureDaisy.class);
        MineTweakerAPI.registerClass(RuneAltar.class);
        MineTweakerAPI.registerClass(Lexicon.class);
    }

    public static boolean isSubtile(ItemStack stack) {
        return stack.getItem() instanceof ItemBlockSpecialFlower;
    }

    public static boolean subtileMatches(ItemStack stack, ItemStack stack2) {
        return (ItemBlockSpecialFlower.getType(stack2).equals(ItemBlockSpecialFlower.getType(stack)));
    }
}
