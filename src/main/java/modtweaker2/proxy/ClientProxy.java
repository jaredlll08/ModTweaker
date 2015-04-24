package modtweaker2.proxy;

import modtweaker2.Commands;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void registerCommands() {
		Commands.registerCommands();
	}

}
