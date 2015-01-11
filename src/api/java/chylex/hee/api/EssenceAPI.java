package chylex.hee.api;
import net.minecraft.item.ItemStack;
import chylex.hee.item.ItemList;
import chylex.hee.mechanics.essence.EssenceType;
import chylex.hee.mechanics.essence.handler.DragonEssenceHandler;
import chylex.hee.mechanics.essence.handler.dragon.AltarItemRecipe;

/**
 * API for Essences and Essence Altars.
 */
public final class EssenceAPI extends AbstractAPI{
	EssenceAPI(){}
	
	/**
	 * Adds an item exchange recipe to the Dragon Essence Altar.
	 * @param source ItemStack that will trigger the exchange. The stacks are compared using {@link ItemStack#isItemEqual(ItemStack) isItemEqual(ItemStack)}, if a different comparison method is needed, extend {@link chylex.hee.mechanics.essence.handler.dragon.AltarItemRecipe AltarItemRecipe}, override {@link chylex.hee.mechanics.essence.handler.dragon.AltarItemRecipe#isApplicable(ItemStack) isApplicable(ItemStack)} and register using {@link #addDragonItemRecipe(AltarItemRecipe)}. 
	 * @param result Final ItemStack that is created after the exchange.
	 * @param cost Amount of Dragon Essence used, the final number may be lower if sockets are used.
	 */
	public void addDragonItemRecipe(ItemStack source, ItemStack result, int cost){
		validate();
		DragonEssenceHandler.recipes.add(new AltarItemRecipe(source,result,cost));
	}
	
	/**
	 * Adds an item exchange recipe to the Dragon Essence Altar.
	 * @param recipe Custom instance of AltarItemRecipe to use.
	 */
	public void addDragonItemRecipe(AltarItemRecipe recipe){
		validate();
		DragonEssenceHandler.recipes.add(recipe);
	}
	
	/**
	 * Creates an ItemStack consisting of one essence of provided type.
	 * @param essenceType Type of essence.
	 * @return New ItemStack, or null if provided invalid value.
	 */
	public ItemStack createEssenceItemStack(EssenceType essenceType){
		validate();
		if (essenceType == null || essenceType == EssenceType.INVALID)return null;
		return new ItemStack(ItemList.essence,1,essenceType.getItemDamage());
	}
}
