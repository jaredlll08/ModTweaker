package chylex.hee.api.wrappers;
import java.util.Iterator;
import net.minecraft.item.Item;
import chylex.hee.world.loot.IItemPostProcessor;
import chylex.hee.world.loot.LootItemStack;
import chylex.hee.world.loot.WeightedLootList;

/**
 * A wrapper class for simplified access to weighted loot list.
 */
public class LootList{
	private final WeightedLootList wrappedCollection;
	
	public LootList(WeightedLootList loot){
		this.wrappedCollection = loot;
	}
	
	/**
	 * Adds new loot to the list.
	 * @param lootItemStack Instance of {@link chylex.hee.world.loot.LootItemStack LootItemStack} that holds basic generation properties.
	 */
	public void addLoot(LootItemStack lootItemStack){
		wrappedCollection.add(lootItemStack);
	}
	
	/**
	 * Adds a post processor to the list, using anonymous class is suggested.
	 * @param postProcessor Handler that will modify item right before it's placed into the container.
	 */
	public void addPostProcessor(IItemPostProcessor postProcessor){
		wrappedCollection.addItemPostProcessor(postProcessor);
	}
	
	/**
	 * Deletes a specific item from the list. Iteration is performed, and first instance of {@link chylex.hee.world.loot.LootItemStack LootItemStack} with specified item is removed.
	 * @param item Item to remove.
	 * @return True if any item was removed.
	 */
	public boolean removeItem(Item item){
		for(Iterator<LootItemStack> iter = wrappedCollection.iterator(); iter.hasNext();){
			if (iter.next().getItem() == item){
				iter.remove();
				return true;
			}
		}
		
		return false;
	}
}
