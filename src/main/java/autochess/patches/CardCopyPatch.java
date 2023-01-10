package autochess.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
public class CardCopyPatch {

    @SpireInsertPatch(rloc = 10, localvars = {"card"})
    public static void Insert(AbstractCard __instance, AbstractCard card) {
        CardLevelPatch.setCardLevel(card, CardLevelPatch.getCardLevel(__instance));
    }
}
