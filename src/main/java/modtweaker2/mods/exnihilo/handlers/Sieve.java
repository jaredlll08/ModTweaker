package modtweaker2.mods.exnihilo.handlers;

import static modtweaker2.helpers.InputHelper.isABlock;
import static modtweaker2.helpers.InputHelper.toStack;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.BaseUndoable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftingResult;

@ZenClass("mods.exnihilo.Sieve")
public class Sieve {
	// Adding a Ex Nihilo Sieve recipe
	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, int rarity) {
		if (isABlock(input)) {
			Block theBlock = Block.getBlockFromItem(toStack(input).getItem());
			int theMeta = toStack(input).getItemDamage();
			MineTweakerAPI.apply(new Add(theBlock, theMeta, toStack(output).getItem(), toStack(output).getItemDamage(), rarity));
		}
	}

	// Passes the list to the base list implementation, and adds the recipe
	private static class Add extends BaseUndoable {
		Block source;
		int sourceMeta;
		Item output;
		int outputMeta;
		int rarity;

		public Add(Block source, int sourceMeta, Item output, int outputMeta, int rarity) {
			super("ExNihilo Sieve", true);
			this.source = source;
			this.sourceMeta = sourceMeta;
			this.output = output;
			this.outputMeta = outputMeta;
			this.rarity = rarity;
		}

		public void apply() {
			SieveRegistry.register(source, sourceMeta, output, outputMeta, rarity);

		}

		public void undo() {
			SieveRegistry.unregisterReward(this.source, this.sourceMeta, this.output, this.outputMeta);
		}

		@Override
		public String getRecipeInfo() {
			return this.output.getUnlocalizedName() + " from " + this.source.getUnlocalizedName();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Removing a Ex Nihilo Sieve recipe
	@ZenMethod
	public static void removeRecipe(IItemStack input, IItemStack output) {
		if (isABlock(input)) {
			Block theBlock = Block.getBlockFromItem(toStack(input).getItem());
			int theMeta = toStack(input).getItemDamage();
			MineTweakerAPI.apply(new Remove(theBlock, theMeta, toStack(output).getItem(), toStack(output).getItemDamage()));
		}
	}


	@ZenMethod 
	public static void removeRewardFromAllBlocks(IItemStack output) 
	{
		Item theItem = toStack(output).getItem();
		int theMeta = toStack(output).getItemDamage();
		MineTweakerAPI.apply(new RemoveRewardFromAllBlocks(theItem, theMeta)); 		
	}

	@ZenMethod 
	public static void removeAllRewardsFromBlock(IItemStack output) 
	{
		if (isABlock(output)) {
			Block theBlock = Block.getBlockFromItem(toStack(output).getItem());
			int theMeta = toStack(output).getItemDamage();
			MineTweakerAPI.apply(new RemoveAllRewardsFromBlock(theBlock, theMeta)); 		
		}
	}



	// Removes a recipe, apply is never the same for anything, so will always
	// need to override it
	private static class Remove extends BaseUndoable {
		Block source;
		int sourceMeta;
		Item output;
		int outputMeta;
		int rarity;

		public Remove(Block source, int sourceMeta, Item output, int outputMeta) {
			super("ExNihilo Sieve", false);
			this.source = source;
			this.sourceMeta = sourceMeta;
			this.output = output;
			this.outputMeta = outputMeta;
		}

		public void apply() {
			ArrayList<SiftingResult> results = SieveRegistry.getSiftingOutput(this.source, this.sourceMeta);

			for (SiftingResult res : results) {
				if (res.item.equals(this.source) && res.meta == this.outputMeta)
					this.rarity = res.rarity;
			}

			SieveRegistry.unregisterReward(source, sourceMeta, output, outputMeta);
		}

		public void undo() {
			if (this.canUndo())
				SieveRegistry.register(this.source, this.sourceMeta, this.output, this.outputMeta, rarity);
		}

		@Override
		public String getRecipeInfo() {
			return this.output.getUnlocalizedName() + " from " + this.source.getUnlocalizedName();
		}
	}

	private static class RemoveAllRewardsFromBlock extends BaseUndoable{
		Block block;
		int blockMeta;

		public RemoveAllRewardsFromBlock(Block block, int blockMeta) {
			super("All ExNihilo Sieve", false);
			this.block = block;
			this.blockMeta = blockMeta;
		}

		public void apply() {
			SieveRegistry.unregisterAllRewardsFromBlock(block, blockMeta);
		}

		public boolean canUndo() {
			return false;
		}

		public void undo() {
		}

		@Override
		public String getRecipeInfo() {
			return "any recipe from "+ block.getUnlocalizedName();
		}
	}

	private static class RemoveRewardFromAllBlocks extends BaseUndoable {
		Item reward;
		int rewardMeta;

		public RemoveRewardFromAllBlocks(Item reward, int rewardMeta) {
			super("ExNihilo Sieve", false);
			this.reward = reward;
			this.rewardMeta = rewardMeta;
		}

		public void apply() {
			SieveRegistry.unregisterRewardFromAllBlocks(reward, rewardMeta);
		}

		public boolean canUndo() {
			return false;
		}

		public void undo() {
		}
		
		@Override
		public String getRecipeInfo() {
			return reward.getUnlocalizedName();
		}
	}
}
