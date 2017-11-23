package com.blamejared.compat.botania;

import com.blamejared.compat.botania.commands.BotaniaBrewLogger;
import com.blamejared.compat.botania.commands.BotaniaLogger;
import com.blamejared.compat.botania.commands.BotaniaOrechidLogger;
import com.blamejared.compat.botania.handlers.Apothecary;
import com.blamejared.compat.botania.handlers.Brew;
import com.blamejared.compat.botania.handlers.ElvenTrade;
import com.blamejared.compat.botania.handlers.Lexicon;
import com.blamejared.compat.botania.handlers.ManaInfusion;
import com.blamejared.compat.botania.handlers.Orechid;
import com.blamejared.compat.botania.handlers.PureDaisy;
import com.blamejared.compat.botania.handlers.RuneAltar;
import com.blamejared.compat.botania.lexicon.commands.LexiconCategoryLogger;
import com.blamejared.compat.botania.lexicon.commands.LexiconEntryLogger;
import com.blamejared.compat.botania.lexicon.commands.LexiconKnowledgeTypesLogger;
import com.blamejared.compat.botania.lexicon.commands.LexiconPageLogger;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CTChatCommand;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class Botania {
    public Botania() {
        CraftTweakerAPI.registerClass(Apothecary.class);
        CraftTweakerAPI.registerClass(Brew.class);
        CraftTweakerAPI.registerClass(ElvenTrade.class);
        CraftTweakerAPI.registerClass(ManaInfusion.class);
        CraftTweakerAPI.registerClass(Orechid.class);
        CraftTweakerAPI.registerClass(PureDaisy.class);
        CraftTweakerAPI.registerClass(RuneAltar.class);
        CraftTweakerAPI.registerClass(Lexicon.class);
    }

    public static boolean isSubtile(ItemStack stack) {
        return stack.getItem() instanceof ItemBlockSpecialFlower;
    }

    public static boolean subtileMatches(ItemStack stack, ItemStack stack2) {
        return (ItemBlockSpecialFlower.getType(stack2).equals(ItemBlockSpecialFlower.getType(stack)));
    }
    
    public static void registerCommands() {
    	CTChatCommand.registerCommand(new BotaniaBrewLogger());
    	CTChatCommand.registerCommand(new BotaniaLogger());
    	CTChatCommand.registerCommand(new BotaniaOrechidLogger());
    	CTChatCommand.registerCommand(new LexiconCategoryLogger());
    	CTChatCommand.registerCommand(new LexiconEntryLogger());
    	CTChatCommand.registerCommand(new LexiconKnowledgeTypesLogger());
    	CTChatCommand.registerCommand(new LexiconPageLogger());
    }
}
