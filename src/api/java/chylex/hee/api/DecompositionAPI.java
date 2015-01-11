package chylex.hee.api;
import net.minecraft.item.Item;
import chylex.hee.mechanics.misc.StardustDecomposition;

/**
 * API for Stardust and Decomposition Table (uncrafting).
 */
public final class DecompositionAPI extends AbstractAPI{
	DecompositionAPI(){}
	
	/**
	 * Blacklists an item and all of its damage values in the Decomposition Table.
	 * @param item Item to be banned. To ban a block, use {@link net.minecraft.item.Item#getItemFromBlock(net.minecraft.block.Block) getItemFromBlock(Block)} to convert it.
	 */
	public static void blacklistItem(Item item){
		blacklistItem(item,-1);
	}
	
	/**
	 * Blacklists an item and selected damage values of it in the Decomposition Table.
	 * @param item Item to be banned. To ban a block, use {@link net.minecraft.item.Item#getItemFromBlock(net.minecraft.block.Block) getItemFromBlock(Block)} to convert it.
	 * @param blacklistedDamageValues Array of damage values to blacklist.
	 */
	public static void blacklistItem(Item item, int...blacklistedDamageValues){
		short[] damages = new short[blacklistedDamageValues.length];
		for(int a = 0; a < blacklistedDamageValues.length; a++)damages[a] = (short)blacklistedDamageValues[a];
		StardustDecomposition.addToBlacklist(item,damages);
	}
	
	/**
	 * Blacklists items by parsing a string, which follows the same syntax rules as decompositionBlackList in configuration - {@code http://hardcore-ender-expansion.wikia.com/wiki/Configuration}.
	 * @param data String to parse.
	 */
	public static void blacklistItemFromString(String data){
		StardustDecomposition.addFromString(data);
	}
}
