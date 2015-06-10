package modtweaker2.mods.exnihilo.handlers;

import static modtweaker2.helpers.InputHelper.isABlock;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import exnihilo.utils.ItemInfo;

@ZenClass("mods.exnihilo.Hammer")
public class Hammer {
	// Adding a Ex Nihilo Hammer recipe
	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, double chance, double luck) {
		if (isABlock(input)) {
			Block theBlock = Block.getBlockFromItem(toStack(input).getItem());
			int theMeta = toStack(input).getItemDamage();
			MineTweakerAPI.apply(new Add(theBlock, theMeta, toStack(output).getItem(), toStack(output).getItemDamage(), (float) chance, (float) luck));
		}
	}

	// Passes the list to the base list implementation, and adds the recipe
	private static class Add implements IUndoableAction {

		Block block;
		int blockMeta;
		Item output;
		int outputMeta;
		float chance;
		float luck;

		public Add(Block block, int blockMeta, Item output, int outputMeta, float chance, float luck) {
			this.block = block;
			this.blockMeta = blockMeta;
			this.output = output;
			this.outputMeta = outputMeta;
			this.chance = chance;
			this.luck = luck;
		}

		public void apply() {
			HammerRegistry.register(block, blockMeta, output, outputMeta, chance, luck);
		}

		public boolean canUndo() {
			return false;
		}

		public String describe() {
			return "Adding ExNihilo Hammer recipe by mining " + block.getLocalizedName() + " to get the result of " + output.getUnlocalizedName();
		}

		public String describeUndo() {
			return "";
		}

		public Object getOverrideKey() {
			return null;
		}

		public void undo() {
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Removing a Ex Nihilo Hammer recipe
	@ZenMethod
	public static void removeRecipe(IItemStack inputBlock, int inputBlockMeta, IItemStack outputItem, int outputItemMeta) {
		MineTweakerAPI.apply(new Remove(Block.getBlockFromItem(toStack(inputBlock).getItem()), inputBlockMeta, toStack(outputItem).getItem(), outputItemMeta));
	}

	// Removes a recipe, apply is never the same for anything, so will always
	// need to override it
	private static class Remove implements IUndoableAction {
		Block block;
		int inputBlockMeta;
		Item output;
		int outputItemMeta;
			

		public Remove(Block block, int inputBlockMeta, Item output, int outputItemMeta) {
			this.block = block;
			this.inputBlockMeta = inputBlockMeta;
			this.output = output;
			this.outputItemMeta = outputItemMeta;
		}

		// Loops through the registry, to find the item that matches, saves that
		// recipe then removes it
		public void apply() {
			HammerRegistry.remove(block, inputBlockMeta, output, outputItemMeta);
		}

		public boolean canUndo() {
			return false;
		}

		public String describe() {
			return "Removing ExNihilo Hammer recipe";
		}

		public String describeUndo() {
			return null;
		}

		public Object getOverrideKey() {
			return null;
		}

		public void undo() {
		}
	}
}
