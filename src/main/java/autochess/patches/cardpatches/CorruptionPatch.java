package autochess.patches.cardpatches;

import autochess.patches.CardLevelPatch;
import autochess.powers.EnergyRefund;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.red.Corruption;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.Collections;

public class CorruptionPatch {
    @SpirePatch(clz = Corruption.class, method = "use", paramtypez = {AbstractPlayer.class, AbstractMonster.class})
    public static class UsePatch {
        @SpirePostfixPatch
        public static void PostFix(AbstractPlayer p, AbstractMonster m, Corruption __instance) {
            if(CardLevelPatch.getCardLevel(__instance) > 1) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new EnergyRefund(p, CardLevelPatch.getCardLevel(__instance) - 1)));
            }
        }
    }
}
