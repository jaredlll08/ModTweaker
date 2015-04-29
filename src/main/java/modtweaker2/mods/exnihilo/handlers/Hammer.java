package modtweaker2.mods.exnihilo.handlers;

import static modtweaker2.helpers.InputHelper.isABlock;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.BaseUndoable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;

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
	private static class Add extends BaseUndoable {
		
		Block block;
		int blockMeta;
		Item output;
		int outputMeta;
		float chance;
		float luck;
		
		public Add(Block block, int blockMeta, Item output, int outputMeta, float chance, float luck) {
			super("ExNihilo Hammer", true);
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

		public void undo() {
			List<Smashable> smashables = HammerRegistry.rewards;
			for(int i = 0; i < smashables.size(); i++){
				Smashable smash = smashables.get(i);
				if (smash != null && smash.source != null)
					if (smash.source == block){
						HammerRegistry.rewards.remove(smash);
						return;
					}
			}
		}

		@Override
		public String getRecipeInfo() {
			return output.getUnlocalizedName() + " from " + block.getUnlocalizedName();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Removing a Ex Nihilo Hammer recipe
	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(toStack(output)));
	}

	// Removes a recipe, apply is never the same for anything, so will always
	// need to override it
	private static class Remove extends BaseUndoable {
		ItemStack stack;
		
		public Remove(ItemStack stack) {
			super("ExNihilo Hammer", false);
			this.stack = stack;
		}

		// Loops through the registry, to find the item that matches, saves that
		// recipe then removes it
		public void apply(){
			List<Smashable> smashables = HammerRegistry.rewards;
			for(int i = 0; i < smashables.size(); i++){
				Smashable smash = smashables.get(i);
				if (smash != null && smash.source != null && Block.getBlockFromItem(stack.getItem()) != null)
					if (smash.source == Block.getBlockFromItem(stack.getItem())){
						HammerRegistry.rewards.remove(smash);
						return;
					}
			}
		}

		public boolean canUndo() {
			return false;
		}

		public void undo() {
		}

		@Override
		public String getRecipeInfo() {
			return stack.getUnlocalizedName();
		}
	}
}
