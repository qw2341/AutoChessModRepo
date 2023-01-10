package autochess.patches;

import autochess.AutoChessMod;
import autochess.ui.campfire.UpgradeMayhemOption;
import autochess.ui.campfire.UpgradeScryOption;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.util.ArrayList;

@SpirePatch(cls = "com.megacrit.cardcrawl.rooms.CampfireUI", method = "initializeButtons")
public class CampfirePatch
{
    public static void Postfix(Object meObj) {
        CampfireUI me = (CampfireUI)meObj;
        ArrayList<AbstractCampfireOption> campfireButtons = (ArrayList<AbstractCampfireOption>) ReflectionHacks.getPrivate(me, CampfireUI.class, "buttons");
        if (campfireButtons != null) {
            if(AutoChessMod.enableAutoBattle) {
                campfireButtons.add(new UpgradeMayhemOption());
                campfireButtons.add(new UpgradeScryOption());
            }
            ReflectionHacks.setPrivate(me, CampfireUI.class, "buttons", campfireButtons);
        }


    }
}