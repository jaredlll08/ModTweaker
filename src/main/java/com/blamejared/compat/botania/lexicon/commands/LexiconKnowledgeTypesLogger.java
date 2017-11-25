package com.blamejared.compat.botania.lexicon.commands;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import vazkii.botania.api.BotaniaAPI;

public class LexiconKnowledgeTypesLogger extends CraftTweakerCommand {

	public LexiconKnowledgeTypesLogger() {
		super("botlextypes");
	}

	@Override
	public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {
		CraftTweakerAPI.logCommand("Botania Lexicon Knowledge Types");
		for (String key : BotaniaAPI.knowledgeTypes.keySet()) {
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
