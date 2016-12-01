package modtweaker.mods.refinedstorage.handlers;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.raoulvdberge.refinedstorage.api.solderer.ISoldererRecipe;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@ZenClass("mods.refinedstorage.Solderer")
public class Solderer {

    @ZenMethod
    public static void add(IItemStack output, int time, IItemStack row2) {
        add(output, time, null, row2, null);
    }

    @ZenMethod
    public static void add(IItemStack output, int time, IItemStack row1, IItemStack row2, @Optional IItemStack row3) {
        MineTweakerAPI.apply(new Add(createISoldererRecipe(InputHelper.toStack(output), time, InputHelper.toStack(row1), InputHelper.toStack(row2), InputHelper.toStack(row3))));
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        MineTweakerAPI.apply(new Remove(new SoldererRemovalRecipe(InputHelper.toStack(output))));
    }

    private static ISoldererRecipe createISoldererRecipe(ItemStack output, int time, ItemStack... rows) {
        return API.instance().getSoldererRegistry().createSimpleRecipe(output, time, rows);
    }

    private static class Add extends BaseListAddition<ISoldererRecipe> {

        protected Add(ISoldererRecipe recipe) {
            super("Solderer", API.instance().getSoldererRegistry().getRecipes());
            this.recipes.add(recipe);
        }

        @Override
        protected String getRecipeInfo(ISoldererRecipe recipe) {
            StringBuilder build = new StringBuilder();
            build.append("ISoldererRecipe");
            build.append(LogHelper.getStackDescription(recipe.getResult())).append("; ");
            build.append("duration:").append(recipe.getDuration()).append("; ");
            for (int i = 0; i < 3; i++) {
                build.append(LogHelper.getStackDescription(recipe.getRow(i))).append(", ");
            }
            build.setLength(build.length() - 2);
            return build.toString();
        }

        @Override
        public void undo() {
            for (ISoldererRecipe recipe : this.successful) {
                if (recipe != null) {
                    if (API.instance().getSoldererRegistry().removeRecipe(recipe.getResult(), recipe.getRow(0), recipe.getRow(1), recipe.getRow(2)).size() > 0) {
                    } else {
                        LogHelper.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                    }
                } else {
                    LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
                }
            }
        }
    }

    private static class Remove extends BaseListRemoval<ISoldererRecipe> {

        protected Remove(ISoldererRecipe recipe) {
            super("Solderer", API.instance().getSoldererRegistry().getRecipes());
            this.recipes.add(recipe);
        }

        @Override
        public void apply() {
            if (recipes.isEmpty()) {
                return;
            }

            for (ISoldererRecipe recipe : this.recipes) {
                if (recipe != null) {
                    List<ISoldererRecipe> removed = API.instance().getSoldererRegistry().removeRecipe(recipe.getResult());
                    if (removed.size() > 0) {
                        successful.addAll(removed);
                    } else {
                        LogHelper.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                    }
                } else {
                    LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
                }
            }
        }

        @Override
        protected String getRecipeInfo(ISoldererRecipe recipe) {
            return "SoldererRemovalRecipe:" + LogHelper.getStackDescription(recipe.getResult());
        }
    }

    private static class SoldererRemovalRecipe implements ISoldererRecipe {
        private ItemStack output;

        protected SoldererRemovalRecipe(ItemStack output) {
            this.output = output;
        }

        @Nullable
        @Override
        public ItemStack getRow(int i) {
            return null;
        }

        @Nonnull
        @Override
        public ItemStack getResult() {
            return this.output;
        }

        @Override
        public int getDuration() {
            return 0;
        }
    }
}
