package com.blamejared.compat.forestry;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import forestry.api.arboriculture.ICharcoalManager;
import forestry.api.arboriculture.TreeManager;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.forestry.CharcoalWall")
@ModOnly("forestry")
@ZenRegister
public class CharcoalPile {

	public static final String NAME = "Forestry Charcoal Wall";

	/**
	 * add a charcoal wall for the given {@link IBlock}
	 * @param block the block for the wall
	 * @param amount the amount of charcoal the wall provides
	 */
	@ZenMethod
	public static void addWall(IBlock block, int amount) {
		ModTweaker.LATE_ADDITIONS.add(new Add(asBlock(block), amount));
	}

	/**
	 * add a charcoal wall for the given {@link crafttweaker.api.block.IBlockState}
	 * @param state the state for the wall (more specific than just a block)
	 * @param amount the amount of charcoal the wall provides
	 */
	@ZenMethod
	public static void addWallState(crafttweaker.api.block.IBlockState state, int amount) {
		ModTweaker.LATE_ADDITIONS.add(new Add(asBlockState(state), amount));
	}

	/**
	 * add a charcoal wall for the block corresponding {@link IItemStack}
	 * will fail if the stack can't be converted to a block
	 * @param stack the stack form of the wall
	 * @param amount the amount of charcoal the wall provides
	 */
	@ZenMethod
	public static void addWallStack(IItemStack stack, int amount) {
		ModTweaker.LATE_ADDITIONS.add(new Add(asBlock(stack), amount));
	}

	private static class Add extends BaseCharcoal {

		private final int amount;

		protected Add(Block block, int amount) {
			super(block);
			this.amount = amount;
		}

		protected Add(IBlockState state, int amount) {
			super(state);
			this.amount = amount;
		}

		@Override
		public void apply() {
			if (manager == null) {
				CraftTweakerAPI.logError("Charcoal manager is null, probably the charcoal module is not enabled");
				return;
			}
			if (block != null) {
				manager.registerWall(block, amount);
			} else if (state != null) {
				manager.registerWall(state, amount);
			} else {
				CraftTweakerAPI.logError("Both block and blockstate were null, no wall registered");
			}
		}
	}

	/**
	 * Removes the first wall that matches the given {@link IBlock}
	 * @param block the block to match
	 */
	@ZenMethod
	public static void removeWall(IBlock block) {
		ModTweaker.LATE_REMOVALS.add(new Remove(asBlock(block)));
	}

	/**
	 * Removes the first wall that matches the given {@link crafttweaker.api.block.IBlockState}
	 * @param state the state to match
	 */
	@ZenMethod
	public static void removeWallState(crafttweaker.api.block.IBlockState state) {
		ModTweaker.LATE_REMOVALS.add(new Remove(asBlockState(state)));
	}

	/**
	 * Removes the first wall that matches the block given by the {@link IItemStack}
	 * Will fail if the stack can't be converted to a block
	 * @param stack the stack to match
	 */
	@ZenMethod
	public static void removeWallStack(IItemStack stack) {
		ModTweaker.LATE_REMOVALS.add(new Remove(asBlock(stack)));
	}

	private static class Remove extends BaseCharcoal {

		protected Remove(Block block) {
			super(block);
		}

		protected Remove(IBlockState state) {
			super(state);
		}

		@Override
		public void apply() {
			if (manager == null) {
				CraftTweakerAPI.logError("Charcoal manager is null, probably the charcoal module is not enabled");
				return;
			}
			boolean success = false;
			if (block != null) {
				success = manager.removeWall(block);
			} else if (state != null) {
				success = manager.removeWall(state);
			} else {
				CraftTweakerAPI.logError("Both block and blockstate were null, no wall removed");
			}
			if (!success) {
				CraftTweakerAPI.logError("Removing wall was not successful");
			}
		}
	}

	private static class BaseCharcoal extends BaseAction {

		protected static final ICharcoalManager manager = TreeManager.charcoalManager;
		protected final Block block;
		protected final IBlockState state;

		protected BaseCharcoal(Block block) {
			super(CharcoalPile.NAME);
			this.block = block;
			this.state = null;
		}

		protected BaseCharcoal(IBlockState state) {
			super(CharcoalPile.NAME);
			this.state = state;
			this.block = null;
		}

		@Override
		public void apply() {
			CraftTweakerAPI.logError("BaseCharcoal.apply() is not implemented");
		}
	}


	// helpers, here for now
	@Nullable
	private static Block asBlock(IBlock block) {
		Object internal = block.getDefinition().getInternal();
		if (!(internal instanceof Block)) {
			CraftTweakerAPI.logError("Not a valid block: " + block);
			return null;
		}
		return (Block) internal;
	}

	@Nullable
	private static IBlockState asBlockState(crafttweaker.api.block.IBlockState blockstate) {
		Object internal = blockstate.getInternal();
		if (!(internal instanceof IBlockState)) {
			CraftTweakerAPI.logError("Not a valid blockstate: " + blockstate);
			return null;
		}
		return (IBlockState) internal;
	}

	@Nullable
	private static Block asBlock(IItemStack stack) {
		if(!InputHelper.isABlock(stack)) {
			CraftTweakerAPI.logError("Not a valid block: " + stack.getDisplayName());
			return null;
		}
		return asBlock(stack.asBlock());
	}
}
