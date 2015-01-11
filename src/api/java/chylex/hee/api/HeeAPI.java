package chylex.hee.api;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;

/**
 * Main API class that contains and provides specific API instances.
 */
public final class HeeAPI{
	private static final EssenceAPI essenceAPI = new EssenceAPI();
	private static final WorldAPI worldAPI = new WorldAPI();
	private static final DecompositionAPI decompositionAPI = new DecompositionAPI();
	
	/**
	 * @return API instance for Essences and Essence Altars.
	 */
	public static EssenceAPI essence(){
		validate();
		return essenceAPI;
	}
	
	/**
	 * @return API instance for loot and new biomes in the End.
	 */
	public static WorldAPI world(){
		validate();
		return worldAPI;
	}
	
	/**
	 * @return  API instance for Stardust and Decomposition Table.
	 */
	public static DecompositionAPI decomposition(){
		validate();
		return decompositionAPI;
	}
	
	private static void validate(){
		if (!Loader.instance().isInState(LoaderState.POSTINITIALIZATION))throw new IllegalStateException("Do not use HEE API outside post initialization!");
	}
	
	private HeeAPI(){}
}
