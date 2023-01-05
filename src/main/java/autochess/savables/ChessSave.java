package autochess.savables;

import autochess.AutoChessMod;
import basemod.abstracts.CustomSavable;

import java.util.HashMap;

public class ChessSave implements CustomSavable<HashMap<String,Integer>> {

    public static HashMap<String,Integer> save = new HashMap<>(2);
    public static final String MAYHEM_STACK_KEY = "mayhemStacks";
    public static final String SCRY_STACK_KEY = "scryStacks";

    @Override
    public HashMap<String,Integer> onSave() {
        return save;
    }

    @Override
    public void onLoad(HashMap<String,Integer> map) {
        if (map != null) save = map;
        else restoreDefault();
    }

    public static void restoreDefault() {
        save.put(MAYHEM_STACK_KEY, AutoChessMod.defaultMayhemStacks);
        save.put(SCRY_STACK_KEY, AutoChessMod.defaultScryStacks);
    }

    public static int getMayhemStacks() {
        return save.get(MAYHEM_STACK_KEY);
    }

    public static void setMayhemStacks(int stacks) {
        save.put(MAYHEM_STACK_KEY, stacks);
    }

    public static int getScryStacks() {
        return save.get(SCRY_STACK_KEY);
    }

    public static void setScryStacks(int stacks) {
        save.put(SCRY_STACK_KEY, stacks);
    }
}
