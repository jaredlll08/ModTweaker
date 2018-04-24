//gold coins have a value of 2000, diamonds are 50
//array is:
//common: 0
//uncommon: 1
//rare: 2
mods.thaumcraft.LootBag.addLoot(<minecraft:dirt>%20000, [0,1,2]);
mods.thaumcraft.LootBag.removeLoot(<minecraft:gold_nugget>, [1,2]);
//*technically* negative warp can probably be a thing
//not sure how this works with a warp of "0", if it still *applies* warp
mods.thaumcraft.Warp.setWarp(<minecraft:dirt>, 500);


mods.thaumcraft.SmeltingBonus.addSmeltingBonus(<ore:oreIron>, <minecraft:diamond>% 50);
mods.thaumcraft.SmeltingBonus.removeSmeltingBonus(<ore:oreIron>, <thaumcraft:nugget:10>);