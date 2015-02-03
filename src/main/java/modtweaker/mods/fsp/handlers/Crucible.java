package modtweaker.mods.fsp.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.mods.fsp.FSPHelper.getLiquid;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseMapAddition;
import modtweaker.util.BaseMapRemoval;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.MutablePair;

import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.Tuple3;

@ZenClass("mods.fsp.Crucible")
public class Crucible {
    @ZenMethod
    public static void addLiquid(String name, IItemStack ingot, IItemStack plate, IItemStack nugget, int r, int g, int b) {
        MineTweakerAPI.apply(new AddLiquid(new CrucibleLiquid(name, toStack(ingot), toStack(plate), toStack(nugget), null, r, g, b)));
    }

    @ZenMethod
    public static void addLiquid(String name, IItemStack ingot, IItemStack plate, IItemStack nugget, int r, int g, int b, String l1, int n1, String l2, int n2, int n3) {
        MineTweakerAPI.apply(new AddLiquid(new CrucibleLiquid(name, toStack(ingot), toStack(plate), toStack(nugget), new CrucibleFormula(getLiquid(l1), n1, getLiquid(l2), n2, n3), r, g, b)));
    }

    private static class AddLiquid extends BaseListAddition {
        public AddLiquid(CrucibleLiquid recipe) {
            super("FSP Crucible Liquid", SteamcraftRegistry.liquids, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((CrucibleLiquid) recipe).name;
        }
    }

    @ZenMethod
    public static void addMelting(IItemStack input, String liquid, int volume) {
        ItemStack stack = toStack(input);
        CrucibleLiquid fluid = getLiquid(liquid);
        if (fluid != null) {
            MineTweakerAPI.apply(new AddMelting(stack, MutablePair.of(stack.getItem(), stack.getItemDamage()), MutablePair.of(fluid, volume)));
        }
    }

    private static class AddMelting extends BaseMapAddition {
        private final ItemStack stack;

        public AddMelting(ItemStack stack, Object key, Object recipe) {
            super("FSP Crucible Melting", SteamcraftRegistry.smeltThings, key, recipe);
            this.stack = stack;
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

    @ZenMethod
    public static void removeMelting(IItemStack input) {
        MineTweakerAPI.apply(new RemoveMelting(toStack(input), MutablePair.of(toStack(input).getItem(), toStack(input).getItemDamage())));
    }

    private static class RemoveMelting extends BaseMapRemoval {
        private final ItemStack stack;

        public RemoveMelting(ItemStack stack, Object key) {
            super("FSP Crucible Melting", SteamcraftRegistry.smeltThings, key);
            this.stack = stack;
        }
        
        public void apply(){
        	SteamcraftRegistry.smeltThings.remove(stack);
        	MutablePair<String, Integer> pair;
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

    @ZenMethod
    public static void addDunking(IItemStack input, String liquid, int volume, IItemStack output) {
        ItemStack stack = toStack(input);
        CrucibleLiquid fluid = getLiquid(liquid);
        if (fluid != null) {
            MineTweakerAPI.apply(new AddDunking(toStack(input), new Tuple3(stack.getItem(), stack.getItemDamage(), fluid), MutablePair.of(volume, toStack(output))));
        }
    }

    private static class AddDunking extends BaseMapAddition {
        private final ItemStack stack;

        public AddDunking(ItemStack stack, Object key, Object recipe) {
            super("FSP Crucible Dunking", SteamcraftRegistry.dunkThings, key, recipe);
            this.stack = stack;
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

    @ZenMethod
    public static void removeDunking(IItemStack input, String liquid) {
        MineTweakerAPI.apply(new RemoveDunking(toStack(input), new Tuple3(toStack(input).getItem(), toStack(input).getItemDamage(), getLiquid(liquid))));
    }

    private static class RemoveDunking extends BaseMapRemoval {
        private final ItemStack stack;

        public RemoveDunking(ItemStack stack, Object key) {
            super("FSP Crucible Dunking", SteamcraftRegistry.dunkThings, key);
            this.stack = stack;
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}
