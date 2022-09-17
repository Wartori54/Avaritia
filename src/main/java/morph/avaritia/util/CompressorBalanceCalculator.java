package morph.avaritia.util;

import morph.avaritia.handler.ConfigHandler;
import net.minecraftforge.fml.ModList;

public class CompressorBalanceCalculator {

    public static int modifier = 0;
    public static int multiplier = 1;

    public static void gatherBalanceModifier() {//TODO, Update these for the correct ModID's
        if (ModList.get().isLoaded("thaumcraft")) {
            modifier += 100;
        }
        if (ModList.get().isLoaded("tconstruct")) {
            modifier += 100;
            multiplier += 1;
        }
        if (ModList.get().isLoaded("thermalexpansion") || ModList.get().isLoaded("ic2") || ModList.get().isLoaded("thaumictinkerer")) {
            modifier += 300;
        }
        if (ModList.get().isLoaded("technom")) {
            modifier += 600;
        }
        if (ModList.get().isLoaded("mysticalagriculture")) {
            multiplier += 1;
        }
        if (ModList.get().isLoaded("agricraft")) {
            multiplier += 1;
        }
        if (ModList.get().isLoaded("minefactoryreloaded")) {
            multiplier += 3;
        }
        if (ModList.get().isLoaded("bigreactors")) {
            modifier += 100;
        }//The mod name is ExtremeReactors but in game still uses bigreactors for items
        if (ModList.get().isLoaded("ee3")) {
            multiplier += 1;
        } else if (ModList.get().isLoaded("projecte")) {
            multiplier += 3;
        }
        if (ModList.get().isLoaded("botania")) {
            modifier += 50;
        }
        if (ModList.get().isLoaded("extrautils2")) {
            modifier += 500;
        }
        if (ModList.get().isLoaded("appliedenergistics2")) {
            modifier += 200;
        }
        if (ModList.get().isLoaded("immersiveengineering")) {
            modifier += 300;
        }
        if (ModList.get().isLoaded("mekanism")) {
            modifier += 500;
            multiplier += 1;

        }
        if (ModList.get().isLoaded("torcherino")) {
            multiplier += 2;
        }
        if (ModList.get().isLoaded("draconicevolution")) {
            modifier += 300;
            multiplier += 1;
        }
        if (ModList.get().isLoaded("rftools") || ModList.get().isLoaded("rftoolsdim")) {
            modifier += 300;
            multiplier += 1;
        }

        modifier = Math.max(modifier + ConfigHandler.modifier, 0);
        multiplier = Math.max(multiplier + ConfigHandler.multiplier, 0);
    }

    public static int balanceCost(int cost) {
        return (cost + modifier) * multiplier;
    }

}
