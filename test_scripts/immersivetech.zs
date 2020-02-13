//Solar Tower
mods.immersivetech.SolarTower.removeRecipe(<liquid:water>);
mods.immersivetech.SolarTower.removeRecipe(<liquid:dist_water>);

mods.immersivetech.SolarTower.addRecipe(<liquid:steam> * 1000, <liquid:water> * 1000, 10);
mods.immersivetech.SolarTower.addRecipe(<liquid:steam> * 2000, <liquid:dist_water> * 1000, 10);


//Distiller
mods.immersivetech.Distiller.removeRecipe(<liquid:water>);

mods.immersivetech.Distiller.addRecipe(<liquid:dist_water> * 750, <ore:dustSalt>, <liquid:water> * 1000, 500, 1, 0.001);


//Boiler
mods.immersivetech.Boiler.removeRecipe(<liquid:water>);
mods.immersivetech.Boiler.removeRecipe(<liquid:dist_water>);

mods.immersivetech.Boiler.addRecipe(<liquid:steam> * 1500, <liquid:water> * 1000, 10);
mods.immersivetech.Boiler.addRecipe(<liquid:steam> * 3000, <liquid:dist_water> * 1000, 10);


//Steam Turbine
mods.immersivetech.SteamTurbine.removeSteam(<liquid:steam>);

mods.immersivetech.SteamTurbine.registerSteam(<liquid:steam>, 50000);