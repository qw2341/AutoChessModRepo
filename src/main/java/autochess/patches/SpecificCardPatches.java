package autochess.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.purple.Wish;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Collections;

public class SpecificCardPatches {
    @SpirePatch(clz = Wish.class, method = "use", paramtypez = {AbstractPlayer.class, AbstractMonster.class})
    public static class WishPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"stanceChoices"})
        public static void Insert(Wish __instance, AbstractPlayer p, AbstractMonster m, ArrayList<AbstractCard> stanceChoices) {
            for (AbstractCard card : stanceChoices) {
                switch (card.cardID) {
                    case "BecomeAlmighty":
                        card.baseDamage = __instance.baseDamage;
                        break;
                    case "FameAndFortune":
                        card.baseMagicNumber = __instance.baseMagicNumber;
                        card.magicNumber = __instance.magicNumber;
                        break;
                    case "LiveForever":
                        card.baseMagicNumber = __instance.baseBlock;
                        card.magicNumber = card.baseMagicNumber;
                }
            }
        }

        private static class Locator
                extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher.NewExprMatcher newExprMatcher = new Matcher.NewExprMatcher(ChooseOneAction.class);
                return LineFinder.findInOrder(ctMethodToPatch, (Matcher)newExprMatcher);
            }
        }
    }
}
