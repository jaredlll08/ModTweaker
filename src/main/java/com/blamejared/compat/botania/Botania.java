package com.blamejared.compat.botania;

import com.blamejared.compat.botania.commands.BotaniaBrewLogger;
import com.blamejared.compat.botania.commands.BotaniaLogger;
import com.blamejared.compat.botania.commands.BotaniaOrechidLogger;
import com.blamejared.compat.botania.lexicon.commands.LexiconCategoryLogger;
import com.blamejared.compat.botania.lexicon.commands.LexiconEntryLogger;
import com.blamejared.compat.botania.lexicon.commands.LexiconKnowledgeTypesLogger;
import com.blamejared.compat.botania.lexicon.commands.LexiconPageLogger;

import crafttweaker.mc1120.commands.CTChatCommand;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class Botania {
    
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
