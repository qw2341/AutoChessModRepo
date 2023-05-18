package autochess.patches;

import autochess.relics.ChessPiece;
import autochess.savables.ChessSave;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.ShopScreen;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Collections;

public class ShopPatches {
    @SpirePatch(clz = ShopScreen.class, method = "purchaseCard", paramtypez = {AbstractCard.class})
    public static class ShopCardsGlowPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
        public static void Insert(ShopScreen __instance, AbstractCard hoveredCard, AbstractCard c) {
            ArrayList<AbstractCard> tempDeck = new ArrayList<>(AbstractDungeon.player.masterDeck.group);

            if(!AbstractDungeon.player.masterDeck.contains(hoveredCard)) tempDeck.add(hoveredCard);

            if(ChessPiece.tierAndIdCheck(hoveredCard, c)) {
                //if its the same card, remove the dupe first because of merging
                int numCardsToRemove = ChessSave.getNumCardsForTriple();
                ArrayList<AbstractCard> cardsToRemove = new ArrayList<>();
                for(AbstractCard ac: tempDeck) {
                    if(ChessPiece.tierAndIdCheck(ac, hoveredCard)) {
                        numCardsToRemove --;
                        cardsToRemove.add(ac);
                    }
                    if(numCardsToRemove <= 0) {

                        break;
                    }
                }

                if(numCardsToRemove <= 0) tempDeck.removeAll(cardsToRemove);
            }

            ChessPiece.makeTriplesGlow(c, tempDeck);

            //other cards in the shop
            for(AbstractCard cc : __instance.coloredCards) {
                ChessPiece.makeTriplesGlow(cc, tempDeck);
            }

            for(AbstractCard clc : __instance.colorlessCards) {
                ChessPiece.makeTriplesGlow(clc, tempDeck);
            }
        }


    }
    private static class Locator extends SpireInsertLocator {

        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(ShopScreen.class, "setPrice");
            return LineFinder.findAllInOrder(ctBehavior, (Matcher)methodCallMatcher);
        }
    }
}
