package modtweaker2.mods.thaumcraft.research;

import static modtweaker2.helpers.StackHelper.areEqual;
import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.research.ResearchPage.PageType;
import static modtweaker2.mods.thaumcraft.ThaumcraftHelper.getResearchSafe;

public class AddPage implements IUndoableAction {
    String key;
    String tab;
    ResearchPage page;
    ResearchPage[] oldPages;
    PageType type;
    ItemStack target;
    Enchantment enchant;

    public AddPage(String res, PageType a, Object b) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        type = a;
        if (type == PageType.TEXT) page = new ResearchPage((String) b);
        else if (type == PageType.INFUSION_ENCHANTMENT) enchant = (Enchantment) b;
        if (b instanceof ItemStack) target = (ItemStack) b;
    }

    @Override
    public void apply() {
        if (type == PageType.NORMAL_CRAFTING) {
            for (Object craft : CraftingManager.getInstance().getRecipeList()) {
                if (craft instanceof IRecipe) {
                    IRecipe theCraft = (IRecipe) craft;
                    if (theCraft.getRecipeOutput() != null && areEqual(theCraft.getRecipeOutput(), target)) {
                        page = new ResearchPage(theCraft);
                        break;
                    }
                }
            }
        } else if (type == PageType.ARCANE_CRAFTING) {
            for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                if (craft instanceof IArcaneRecipe) {
                    IArcaneRecipe theCraft = (IArcaneRecipe) craft;
                    if (theCraft.getRecipeOutput() != null && areEqual(theCraft.getRecipeOutput(), target)) {
                        page = new ResearchPage(theCraft);
                        break;
                    }
                }
            }
        } else if (type == PageType.CRUCIBLE_CRAFTING) {
            for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                if (craft instanceof CrucibleRecipe) {
                    CrucibleRecipe theCraft = (CrucibleRecipe) craft;
                    if (theCraft.getRecipeOutput() != null && areEqual(theCraft.getRecipeOutput(), target)) {
                        page = new ResearchPage(theCraft);
                        break;
                    }
                }
            }
        } else if (type == PageType.INFUSION_CRAFTING) {
            for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                if (craft instanceof InfusionRecipe) {
                    InfusionRecipe theCraft = (InfusionRecipe) craft;
                    if (theCraft.getRecipeOutput() != null && theCraft.getRecipeOutput() instanceof ItemStack && areEqual(((ItemStack) (theCraft.getRecipeOutput())), target)) {
                        page = new ResearchPage(theCraft);
                        break;
                    }
                }
            }
        } else if (type == PageType.INFUSION_ENCHANTMENT) {
            for (Object craft : ThaumcraftApi.getCraftingRecipes()) {
                if (craft instanceof InfusionEnchantmentRecipe) {
                    InfusionEnchantmentRecipe theCraft = (InfusionEnchantmentRecipe) craft;
                    if (theCraft.getEnchantment() != null && theCraft.getEnchantment() == enchant) {
                        page = new ResearchPage(theCraft);
                        break;
                    }
                }
            }
        }
        if (page == null) return;
        oldPages = ResearchCategories.researchCategories.get(tab).research.get(key).getPages();
        if (oldPages == null) oldPages = new ResearchPage[0];
        ResearchPage[] newPages = new ResearchPage[oldPages.length + 1];
        for (int x = 0; x < oldPages.length; x++) {
            newPages[x] = oldPages[x];
        }
        newPages[oldPages.length] = page;
        ResearchCategories.researchCategories.get(tab).research.get(key).setPages(newPages);
    }

    @Override
    public String describe() {
        return "Adding Research Page to " + key;
    }

    @Override
    public boolean canUndo() {
        return oldPages != null;
    }

    @Override
    public void undo() {
        final ResearchItem research = getResearchSafe(tab, key);
        if(research == null) {
            return;
        }

        research.setPages(oldPages);
    }

    @Override
    public String describeUndo() {
        return "Removing Page from " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}