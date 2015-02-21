package modtweaker2.mods.tconstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ToolMaterial;
import static modtweaker2.helpers.LogHelper.*;

public class MaterialLogger implements ICommandFunction {
	private static ArrayList<String> materials = new ArrayList();
	static {
		materials = new ArrayList();
		for (Map.Entry<String, ToolMaterial> entry : TConstructRegistry.toolMaterialStrings.entrySet()) {
			materials.add(entry.getKey());
		}

		Collections.sort(materials);
	}

	@Override
	public void execute(String[] arguments, IPlayer player) {
		System.out.println("Materials: " + materials.size());
		for (String s : materials) {
			System.out.println("Material " + s);
			MineTweakerAPI.logCommand("<" + s + "> -- ");
		}

		logPrinted(player);
	}
}
