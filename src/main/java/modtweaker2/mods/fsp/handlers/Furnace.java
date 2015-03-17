package modtweaker2.mods.fsp.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.MutablePair;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import flaxbeard.steamcraft.api.SteamcraftRegistry;

@ZenClass("mods.fsp.Furnace")
public class Furnace {
    @ZenMethod
    public static void addSteamFood(IItemStack input, IItemStack output) {
        MineTweakerAPI.apply(new AddSteamFood(toStack(input), MutablePair.of(toStack(input).getItem(), toStack(input).getItemDamage()), MutablePair.of(toStack(output).getItem(), toStack(output).getItemDamage())));
    }

    private static class AddSteamFood extends BaseMapAddition {
        private final ItemStack stack;

        public AddSteamFood(ItemStack stack, MutablePair key, MutablePair recipe) {
            super("FSP Furnace - Steam Food", SteamcraftRegistry.steamedFoods, key, recipe);
            this.stack = stack;
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

    @ZenMethod
    public static void removeSteamFood(IItemStack input) {
        MineTweakerAPI.apply(new RemoveSteamFood(toStack(input), MutablePair.of(toStack(input).getItem(), toStack(input).getItemDamage())));
    }

    private static class RemoveSteamFood extends BaseMapRemoval {
        private final ItemStack stack;

        public RemoveSteamFood(ItemStack stack, MutablePair key) {
            super("FSP Furnace - Steam Food", SteamcraftRegistry.steamedFoods, key);
            this.stack = stack;
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
