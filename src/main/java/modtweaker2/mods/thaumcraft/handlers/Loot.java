package modtweaker2.mods.thaumcraft.handlers;

import java.util.List;

import net.minecraft.item.ItemStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.internal.WeightedRandomLoot;

@ZenClass("mods.thaumcraft.Loot")
public class Loot {

	@ZenMethod
	public static void addCommonLoot(IItemStack stack, int weight) {
		MineTweakerAPI.apply(new Add(WeightedRandomLoot.lootBagCommon, new WeightedRandomLoot(InputHelper.toStack(stack), weight)));
	}

	@ZenMethod
	public static void addUncommonLoot(IItemStack stack, int weight) {
		MineTweakerAPI.apply(new Add(WeightedRandomLoot.lootBagUncommon, new WeightedRandomLoot(InputHelper.toStack(stack), weight)));
	}

	@ZenMethod
	public static void addRareLoot(IItemStack stack, int weight) {
		MineTweakerAPI.apply(new Add(WeightedRandomLoot.lootBagRare, new WeightedRandomLoot(InputHelper.toStack(stack), weight)));
	}

	public static class Add extends BaseListAddition {
		public Add(List list, WeightedRandomLoot recipe) {
			super(list, recipe);
		}
	}

	@ZenMethod
	public static void removeCommonLoot(IItemStack stack) {
		MineTweakerAPI.apply(new Remove(WeightedRandomLoot.lootBagCommon, InputHelper.toStack(stack)));
	}

	@ZenMethod
	public static void removeUncommonLoot(IItemStack stack) {
		MineTweakerAPI.apply(new Remove(WeightedRandomLoot.lootBagUncommon, InputHelper.toStack(stack)));
	}

	@ZenMethod
	public static void removeRareLoot(IItemStack stack) {
		MineTweakerAPI.apply(new Remove(WeightedRandomLoot.lootBagRare, InputHelper.toStack(stack)));
	}

	public static class Remove extends BaseListRemoval {

		public Remove(List list, ItemStack stack) {
			super(list, stack);
		}

		@Override
		public void apply() {
			List<WeightedRandomLoot> loot = (List<WeightedRandomLoot>) list;
			WeightedRandomLoot remove = null;
			for (WeightedRandomLoot stack : loot) {
				if (stack.item.isItemEqual(this.stack)) {
					remove = stack;
					break;
				}
			}
			loot.remove(remove);
		}
		
		
		public static class RemoveAll extends BaseListRemoval {

		public Remove(List list) {
			super(list);
		}

		@Override
		public void apply() {
			List<WeightedRandomLoot> loot = (List<WeightedRandomLoot>) list;
			WeightedRandomLoot remove = null;
				}
			}
			loot.remove(remove);
		}

	}
}
