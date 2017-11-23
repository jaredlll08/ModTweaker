package com.blamejared.compat.botania.commands;

import java.util.Set;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import vazkii.botania.api.BotaniaAPI;

public class BotaniaBrewLogger extends CraftTweakerCommand{

	public BotaniaBrewLogger() {
		super("botbrews");
	}
	
    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {
    	Set<String> brew_keys=BotaniaAPI.brewMap.keySet();
        System.out.println("Brews: " + brew_keys.size());
        for (String key : brew_keys) {
            System.out.println("Brew " + key);
            CraftTweakerAPI.logCommand(key);
        }

        if (sender != null) {
            sender.sendMessage(new TextComponentString("List generated; see crafttweaker.log in your minecraft dir"));
        }
    }
    
	@Override
	protected void init() {
	}
}
