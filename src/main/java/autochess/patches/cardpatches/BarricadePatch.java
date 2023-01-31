package autochess.patches.cardpatches;

import autochess.patches.CardLevelPatch;
import autochess.powers.EnergyRefund;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.red.Barricade;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;

public class BarricadePatch {
    @SpirePatch(clz = Barricade.class, method = "use", paramtypez = {AbstractPlayer.class, AbstractMonster.class})
    public static class UsePatch {
        @SpirePostfixPatch
        public static void PostFix(Barricade __instance, AbstractPlayer p, AbstractMonster m) {
            if(CardLevelPatch.getCardLevel(__instance) > 1) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p, new MetallicizePower(p, (int)Math.pow(2, CardLevelPatch.getCardLevel(__instance) - 2) * 10)));
            }
        }
    }
}
