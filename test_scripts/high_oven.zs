import crafttweaker.oredict.IOreDictEntry;

import mods.tcomplement.highoven.HighOven;
import mods.tcomplement.highoven.MixRecipeBuilder;
import mods.tcomplement.highoven.MixRecipeManager;

HighOven.removeFuel(<minecraft:coal:1>);
HighOven.addFuel(<minecraft:diamond>, 1000, 25);

HighOven.addMeltingOverride(<liquid:steel> * 144, <ore:ingotIron>, 2567);

HighOven.removeHeatRecipe(<liquid:steam>);
HighOven.addHeatRecipe(<liquid:lava> * 100, <liquid:water> * 1000, 450);

HighOven.removeMixRecipe(<liquid:steel>);
HighOven.newMixRecipe(<liquid:cobalt> * 144, <liquid:obsidian> * 244, 3000)
	.addOxidizer(<minecraft:redstone>, 99)
	.addReducer(<minecraft:glowstone>, 98)
	.addPurifier(<minecraft:dirt>, 3)
	.register();

HighOven.newMixRecipe(<liquid:cobalt> * 144, <liquid:iron> * 144, 1000)
	.addOxidizer(<minecraft:stone>, 25)
	.addReducer(<minecraft:cobblestone>, 35)
	.addPurifier(<minecraft:redstone>, 45)
	.addPurifier(<ore:stickWood>, 100)
	.register();


var manager = HighOven.manageMixRecipe(<liquid:pigiron>, null) as MixRecipeManager;
manager.removeReducer(<minecraft:dye:15>);
manager.addPurifier(<minecraft:dirt>, 97);

HighOven.manageMixRecipe(<liquid:cobalt>, null)
	.addOxidizer(<minecraft:bedrock>, 0);

HighOven.manageMixRecipe(<liquid:cobalt>, <liquid:iron>)
	.removePurifier(<minecraft:redstone>)
	.addPurifier(<minecraft:dye:8>, 42);

