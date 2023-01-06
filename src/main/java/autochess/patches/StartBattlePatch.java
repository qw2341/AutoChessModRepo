package autochess.patches;

import autochess.savables.ChessSave;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MayhemPower;
import com.megacrit.cardcrawl.powers.watcher.ForesightPower;

@SpirePatch(clz = AbstractPlayer.class, method = "applyPreCombatLogic")
public class StartBattlePatch {
    @SpirePrefixPatch
    public static void Prefix(AbstractPlayer __instance) {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(__instance,__instance, new ForesightPower(__instance, ChessSave.getScryStacks())));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(__instance,__instance, new MayhemPower(__instance, ChessSave.getMayhemStacks())));

    }
}
