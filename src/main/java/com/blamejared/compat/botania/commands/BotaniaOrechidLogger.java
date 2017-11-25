package com.blamejared.compat.botania.commands;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import vazkii.botania.api.BotaniaAPI;

public class BotaniaOrechidLogger extends CraftTweakerCommand {

	public BotaniaOrechidLogger() {
		super("botorechid");
	}

	@Override
	public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {

		for (String str : BotaniaAPI.oreWeights.keySet()) {
			CraftTweakerAPI.logCommand(str + ": " + BotaniaAPI.oreWeights.get(str));
		}
		if (sender != null) {
			sender.sendMessage(new TextComponentString("List generated; see crafttweaker.log in your minecraft dir"));
		}
	}

	@Override
	protected void init() {
	}
}
