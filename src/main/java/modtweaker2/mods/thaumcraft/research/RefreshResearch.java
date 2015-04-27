package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

public class RefreshResearch implements IUndoableAction {
    String research;
    String tab;

    public RefreshResearch(String target) {
        research = target;
        tab = ThaumcraftHelper.getResearchTab(research);
    }

    @Override
    public void apply() {
        if (tab != null) {
            ResearchItem target = ResearchCategories.researchCategories.get(tab).research.get(research);
            ResearchPage[] pages = target.getPages();
            for (int x = 0; x < pages.length; x++) {
                if (pages[x].recipe != null) {
                    if (pages[x].recipe instanceof IRecipe) {
                        IRecipe recipe = (IRecipe) pages[x].recipe;
                        for (Object craft : CraftingManager.getInstance().getRecipeList()) {
                            if (craft instanceof IRecipe) {
                                IRecipe theCraft = (IRecipe) craft;
                                if (theCraft.getRecipeOutput() != null && areEqual(theCraft.getRecipeOutput(), recipe.getRecipeOutput())) {
                                    pages[x] = new ResearchPage(theCraft);
                                    break;
                                }
                            }
                        }
                    } else if (pages[x].recipe instanceof IArcaneRecipe) {
                        IArcaneRecipe recipe = (IArcaneRecipe) pages[x].recipe;
                        for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                            if (craft instanceof IArcaneRecipe) {
                                IArcaneRecipe theCraft = (IArcaneRecipe) craft;
                                if (theCraft.getRecipeOutput() != null && areEqual(theCraft.getRecipeOutput(), recipe.getRecipeOutput())) {
                                    pages[x] = new ResearchPage(theCraft);
                                    break;
                                }
                            }
                        }
                    } else if (pages[x].recipe instanceof CrucibleRecipe) {
                        CrucibleRecipe recipe = (CrucibleRecipe) pages[x].recipe;
                        for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                            if (craft instanceof CrucibleRecipe) {
                                CrucibleRecipe theCraft = (CrucibleRecipe) craft;
                                if (theCraft.getRecipeOutput() != null && areEqual(theCraft.getRecipeOutput(), recipe.getRecipeOutput())) {
                                    pages[x] = new ResearchPage(theCraft);
                                    break;
                                }
                            }
                        }
                    } else if (pages[x].recipe instanceof InfusionRecipe) {
                        InfusionRecipe recipe = (InfusionRecipe) pages[x].recipe;
                        if (recipe.getRecipeOutput() instanceof ItemStack) {
                            for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                                if (craft instanceof InfusionRecipe) {
                                    InfusionRecipe theCraft = (InfusionRecipe) craft;
                                    if (theCraft.getRecipeOutput() != null && theCraft.getRecipeOutput() instanceof ItemStack && areEqual(((ItemStack) theCraft.getRecipeOutput()), (ItemStack) recipe.getRecipeOutput())) {
                                        pages[x] = new ResearchPage(theCraft);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String describe() {
        return "Refreshing Research: " + research;
    }

    @Override
    public boolean canUndo() {
        return tab != null;
    }

    @Override
    public void undo() {
        apply();
    }

    @Override
    public String describeUndo() {
        return "Refreshing Research Again?: " + research;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}