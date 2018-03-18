package com.blamejared.compat.inspirations;

import static crafttweaker.mc1120.commands.SpecialMessagesChat.getClickableCommandText;
import static crafttweaker.mc1120.commands.SpecialMessagesChat.getLinkToCraftTweakerLog;
import static crafttweaker.mc1120.commands.SpecialMessagesChat.getNormalMessage;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CTChatCommand;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.PotionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

public class Inspirations {

    public static void registerCommands() {
        CTChatCommand.registerCommand(new CraftTweakerCommand("inspirations") {

            @Override
            protected void init() {
                setDescription(getClickableCommandText("\u00A72/ct inspirations {dyes|potions}", "/ct inspirations {dyes|potions}", false),
                		getNormalMessage(" \u00A73Dumps all the dye names or potion type resource locations to the log"));
            }

            @Override
            public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {
                List<String> args = Arrays.asList(arguments);
                if(args.contains("dyes")) {
                    CraftTweakerAPI.logCommand("Inspirations Dye Colors:");
                    for(EnumDyeColor dye : EnumDyeColor.values()) {
                        CraftTweakerAPI.logCommand(dye.getName());
                    }
                } else if(args.contains("potions")) {
                    CraftTweakerAPI.logCommand("Inspirations Potion Types:");
                    // ensure the potion names are sorted
                    for(ResourceLocation potion : new TreeSet<>(PotionType.REGISTRY.getKeys())) {
                        CraftTweakerAPI.logCommand(potion.toString());
                    }
                } else {
                    if(sender != null) {
                        sender.sendMessage(new TextComponentString("Invalid arguments for command. Valid arguments: dyes, potions"));
                    }
                }

                if(sender != null) {
                    sender.sendMessage(getLinkToCraftTweakerLog("List generated;", sender));
                }
            }
        });
    }
}
