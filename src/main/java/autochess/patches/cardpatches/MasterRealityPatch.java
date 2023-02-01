package autochess.patches.cardpatches;

import autochess.powers.RealityMastery;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.MakeTempCardAtBottomOfDeckAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;


@SpirePatches2({@SpirePatch2(clz = MakeTempCardAtBottomOfDeckAction.class, method = "update"),

        @SpirePatch2(clz = MakeTempCardInDrawPileAction.class, method = "update")})
public class MasterRealityPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
    public static void Insert(AbstractCard c) {
        if(AbstractDungeon.player.hasPower(RealityMastery.POWER_ID)) {
            int amt = AbstractDungeon.player.getPower(RealityMastery.POWER_ID).amount;
            c.cost = Math.max(c.cost - amt, 0);
            c.costForTurn = Math.max(c.costForTurn - amt, 0);
        }

    }
    private static class Locator
            extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "upgrade");
            return LineFinder.findInOrder(ctMethodToPatch, (Matcher)methodCallMatcher);
        }
    }
}
