package autochess.patches.cardpatches;

import autochess.AutoChessMod;
import autochess.powers.RealityMastery;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


public class MasterRealityPatch2 {
    @SpirePatch(clz = MakeTempCardInDiscardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, boolean.class})
    public static class DiscardPatch {
        @SpirePostfixPatch
        public static void Postfix(MakeTempCardInDiscardAction __instance, AbstractCard card, boolean sameUUID, AbstractCard ___c) {
            if(AbstractDungeon.player.hasPower(RealityMastery.POWER_ID)) {
                int amt = AbstractDungeon.player.getPower(RealityMastery.POWER_ID).amount;
                ___c.cost = Math.max(___c.cost - amt, 0);
                ___c.costForTurn = Math.max(___c.costForTurn - amt, 0);
            }

        }
    }
    @SpirePatch(clz = MakeTempCardInHandAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class,int.class})
    public static class HandPatch {
        @SpirePostfixPatch
        public static void Postfix(MakeTempCardInHandAction __instance, AbstractCard card, int amount, AbstractCard ___c) {
            if(AbstractDungeon.player.hasPower(RealityMastery.POWER_ID)) {
                int amt = AbstractDungeon.player.getPower(RealityMastery.POWER_ID).amount;
                ___c.cost = Math.max(___c.cost - amt, 0);
                ___c.costForTurn = Math.max(___c.costForTurn - amt, 0);
            }

        }
    }
    @SpirePatch(clz = MakeTempCardInHandAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class,boolean.class})
    public static class Hand2Patch {
        @SpirePostfixPatch
        public static void Postfix(MakeTempCardInHandAction __instance, AbstractCard card, boolean isOtherCardInCenter, AbstractCard ___c) {
            if(AbstractDungeon.player.hasPower(RealityMastery.POWER_ID)) {
                int amt = AbstractDungeon.player.getPower(RealityMastery.POWER_ID).amount;
                ___c.cost = Math.max(___c.cost - amt, 0);
                ___c.costForTurn = Math.max(___c.costForTurn - amt, 0);
            }

        }
    }

}
