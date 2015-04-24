package modtweaker2.mods.thaumcraft.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.player.IPlayer;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.mods.thaumcraft.research.AddPage;
import modtweaker2.mods.thaumcraft.research.AddPrereq;
import modtweaker2.mods.thaumcraft.research.AddResearch;
import modtweaker2.mods.thaumcraft.research.AddSibling;
import modtweaker2.mods.thaumcraft.research.AddTab;
import modtweaker2.mods.thaumcraft.research.ClearPages;
import modtweaker2.mods.thaumcraft.research.ClearPrereqs;
import modtweaker2.mods.thaumcraft.research.ClearSiblings;
import modtweaker2.mods.thaumcraft.research.Difficulty;
import modtweaker2.mods.thaumcraft.research.MoveResearch;
import modtweaker2.mods.thaumcraft.research.OrphanResearch;
import modtweaker2.mods.thaumcraft.research.RefreshResearch;
import modtweaker2.mods.thaumcraft.research.RemoveResearch;
import modtweaker2.mods.thaumcraft.research.RemoveTab;
import modtweaker2.mods.thaumcraft.research.SetAspects;
import modtweaker2.mods.thaumcraft.research.SetResearch;
import modtweaker2.utils.TweakerPlugin;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage.PageType;

@ZenClass("mods.thaumcraft.Research")
public class Research {
	private static final ResourceLocation defaultBackground = new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png");

	@ZenMethod
	public static void addTab(String key, String iconDomain, String iconPath) {
		addTab(key, iconDomain, iconPath, null, null);
	}

	@ZenMethod
	public static void addTab(String key, String iconDomain, String iconPath, String backDomain, String backPath) {
		ResourceLocation icon = new ResourceLocation(iconDomain, iconPath);
		ResourceLocation background;
		if (backPath == null)
			background = defaultBackground;
		else
			background = new ResourceLocation(backDomain, backPath);
		addTab(key, icon, background);
	}

	private static void addTab(String key, ResourceLocation icon, ResourceLocation background) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddTab(key, icon, background));
	}

	@ZenMethod
	public static void removeTab(String tab) {

		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new RemoveTab(tab));
	}

	@ZenMethod
	public static void removeResearch(String research) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new RemoveResearch(research));
	}

	@ZenMethod
	public static void orphanResearch(String research) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new OrphanResearch(research));
	}

	@ZenMethod
	public static void addResearch(String key, String tab, @Optional String aspects, int x, int y, int difficulty, String domain, String path) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddResearch(new ResearchItem(key, tab, ThaumcraftHelper.parseAspects(aspects), x, y, difficulty, new ResourceLocation(domain, path))));
	}

	@ZenMethod
	public static void addResearch(String key, String tab, @Optional String aspects, int x, int y, int difficulty, IItemStack item) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddResearch(new ResearchItem(key, tab, ThaumcraftHelper.parseAspects(aspects), x, y, difficulty, toStack(item))));
	}

	@ZenMethod
	public static void addPage(String key, String unlocalized) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddPage(key, PageType.TEXT, unlocalized));
	}

	@ZenMethod
	public static void addCraftingPage(String key, IItemStack item) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddPage(key, PageType.NORMAL_CRAFTING, toStack(item)));
	}

	@ZenMethod
	public static void addCruciblePage(String key, IItemStack item) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddPage(key, PageType.CRUCIBLE_CRAFTING, toStack(item)));
	}

	@ZenMethod
	public static void addArcanePage(String key, IItemStack item) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddPage(key, PageType.ARCANE_CRAFTING, toStack(item)));
	}

	@ZenMethod
	public static void addInfusionPage(String key, IItemStack item) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddPage(key, PageType.INFUSION_CRAFTING, toStack(item)));
	}

	@ZenMethod
	public static void addEnchantmentPage(String key, int i) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddPage(key, PageType.INFUSION_ENCHANTMENT, Enchantment.enchantmentsList[i]));
	}

	@ZenMethod
	public static void clearPages(String key) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new ClearPages(key));
	}

	@ZenMethod
	public static void addPrereq(String key, String req, @Optional boolean hidden) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddPrereq(key, req, hidden));
	}

	@ZenMethod
	public static void clearPrereqs(String key) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new ClearPrereqs(key));
	}

	@ZenMethod
	public static void addSibling(String key, String sibling) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new AddSibling(key, sibling));
	}

	@ZenMethod
	public static void clearSiblings(String key) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new ClearSiblings(key));
	}

	@ZenMethod
	public static void setRound(String key, boolean flag) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new SetResearch(key, flag, SetType.ROUND));
	}

	@ZenMethod
	public static void setSpikey(String key, boolean flag) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new SetResearch(key, flag, SetType.SPIKE));
	}

	@ZenMethod
	public static void setStub(String key, boolean flag) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new SetResearch(key, flag, SetType.STUB));
	}

	@ZenMethod
	public static void setSecondary(String key, boolean flag) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new SetResearch(key, flag, SetType.SECONDARY));
	}

	@ZenMethod
	public static void setVirtual(String key, boolean flag) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new SetResearch(key, flag, SetType.VIRTUAL));
	}

	@ZenMethod
	public static void setAutoUnlock(String key, boolean flag) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new SetResearch(key, flag, SetType.AUTO));
	}

	@ZenMethod
	public static void setConcealed(String key, boolean flag) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new SetResearch(key, flag, SetType.CONCEAL));
	}

	@ZenMethod
	public static void setAspects(String key, String aspects) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new SetAspects(key, ThaumcraftHelper.parseAspects(aspects)));
	}

	@ZenMethod
	public static void setComplexity(String key, int complexity) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new Difficulty(key, complexity));
	}

	@ZenMethod
	public static void refreshResearchRecipe(String key) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new RefreshResearch(key));
	}

	@ZenMethod
	public static void moveResearch(String key, String destination, int x, int y) {
		if (!TweakerPlugin.hasInit())
			MineTweakerAPI.apply(new MoveResearch(key, destination, x, y));
	}

	@ZenMethod
	public static boolean hasResearched(IPlayer player, String key) {

		return ThaumcraftApiHelper.isResearchComplete(player.getName(), key);
	}

	public static enum SetType {
		AUTO, ROUND, SPIKE, SECONDARY, STUB, VIRTUAL, CONCEAL
	}
}
