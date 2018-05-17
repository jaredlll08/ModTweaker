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


<aspect:aer>.internal.chatColour ="r";
<minecraft:stone>.setAspects(<aspect:ignis>*5);

<thaumcraft:thaumonomicon>.removeAspects(<aspect:aqua>);

mods.thaumcraft.Infusion.registerRecipe("testName", "", <minecraft:diamond>, 20, [<aspect:aer>, <aspect:ignis>], <minecraft:grass>, [<minecraft:stick>, <minecraft:dirt>]);
mods.thaumcraft.Infusion.removeRecipe(<thaumcraft:mirror_essentia>);

mods.thaumcraft.ArcaneWorkbench.registerShapedRecipe("test", "", 20, [<aspect:aer>, <aspect:ignis>, <aspect:terra>], <minecraft:diamond>, [[<minecraft:dirt>], [<minecraft:stick>], [<minecraft:grass>]]);
mods.thaumcraft.ArcaneWorkbench.registerShapelessRecipe("tests", "", 20, [<aspect:aqua>, <aspect:ignis>, <aspect:terra>], <minecraft:diamond>, [<minecraft:sand>, <minecraft:stick>, <minecraft:grass>]);

mods.thaumcraft.ArcaneWorkbench.removeRecipe(<thaumcraft:goggles>);

mods.thaumcraft.Crucible.registerRecipe("crucibleTest", "", <minecraft:diamond>, <minecraft:stick>, [<aspect:aer>]);
mods.thaumcraft.Crucible.removeRecipe(<minecraft:leather>);

<entity:minecraft:sheep>.setAspects(<aspect:aqua>);
