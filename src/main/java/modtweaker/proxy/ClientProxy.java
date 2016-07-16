package modtweaker.proxy;

import modtweaker.Commands;

public class ClientProxy extends CommonProxy{
	
	@Override
	public void registerCommands() {
		Commands.registerCommands();
	}

}
