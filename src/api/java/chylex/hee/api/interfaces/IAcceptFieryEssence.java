package chylex.hee.api.interfaces;

/**
 * Use this interface for any tile entities that should use Fiery Essence for a speed boost (or some other effect).
 */
public interface IAcceptFieryEssence{
	/**
	 * Returns how many times the Altar can boost the acceptor. Higher levels of essence should have higher boost.
	 * @param essenceLevel current level of essence
	 * @return amount of boosts available
	 */
	int getBoostAmount(int essenceLevel);
	
	/**
	 * Performs a single boost. This method is called multiple times, based on number returned by {@link #getBoostAmount(int)}.
	 */
	void boost();
}
