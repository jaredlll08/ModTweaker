package com.blamejared.compat.forestry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import forestry.api.arboriculture.ICharcoalManager;
import forestry.api.arboriculture.TreeManager;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.forestry.Carpenter")
@ModOnly("forestry")
@ZenRegister
public class CharcoalPile {

	public static final String NAME = "Forestry Charcoal Piles";

	@ZenMethod
	public static void addPile(IBlock block, int amount) {
		ModTweaker.LATE_ADDITIONS.add(new Add(asBlock(block), amount));
	}

	@ZenMethod
	public static void addPileState(crafttweaker.api.block.IBlockState state, int amount) {
		ModTweaker.LATE_ADDITIONS.add(new Add(asBlockState(state), amount));
	}

	@ZenMethod
	public static void addPileStack(IItemStack stack, int amount) {
		ModTweaker.LATE_ADDITIONS.add(new Add(asBlock(stack.asBlock()), amount));
	}

	private static class Add extends BaseCharcoal {

		private int amount;

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

	@ZenMethod
	public static void removePile(IBlock block) {
		ModTweaker.LATE_REMOVALS.add(new Remove(asBlock(block)));
	}

	@ZenMethod
	public static void removePileState(crafttweaker.api.block.IBlockState state) {
		ModTweaker.LATE_REMOVALS.add(new Remove(asBlockState(state)));
	}

	@ZenMethod
	public static void removePileStack(IItemStack stack) {
		ModTweaker.LATE_REMOVALS.add(new Remove(asBlock(stack.asBlock())));
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

		protected static ICharcoalManager manager = TreeManager.charcoalManager;
		protected Block block;
		protected IBlockState state;

		protected BaseCharcoal(Block block) {
			super(CharcoalPile.NAME);
			this.block = block;
		}

		protected BaseCharcoal(IBlockState state) {
			super(CharcoalPile.NAME);
			this.state = state;
		}

		@Override
		public void apply() {

		}
	}


	// helpers, here for now
	private static Block asBlock(IBlock block) {
		Object internal = block.getDefinition().getInternal();
		if (!(internal instanceof Block)) {
			CraftTweakerAPI.logError("Not a valid block: " + block);
		}
		return (Block) internal;
	}

	private static IBlockState asBlockState(crafttweaker.api.block.IBlockState blockstate) {
		Object internal = blockstate.getInternal();
		if (!(internal instanceof IBlockState)) {
			CraftTweakerAPI.logError("Not a valid blockstate: " + blockstate);
		}
		return (IBlockState) internal;
	}
}
