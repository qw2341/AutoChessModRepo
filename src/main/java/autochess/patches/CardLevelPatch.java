package autochess.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static autochess.AutoChessMod.makeID;

@SpirePatch(clz = AbstractCard.class, method = "<class>")
public class CardLevelPatch {
    public static String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID("CardLevelText")).TEXT;
    public static SpireField<Integer> cardLevel = new SpireField<>(() -> Integer.valueOf(1));

    public static int getCardLevel(AbstractCard ac) {
        return CardLevelPatch.cardLevel.get(ac);
    }

    public static void setCardLevel(AbstractCard ac, int level) {
        CardLevelPatch.cardLevel.set(ac,level);
    }
    public static int getLeveledPowerAmount(AbstractCard ac) {
        return (int) Math.pow(2, CardLevelPatch.getCardLevel(ac) - 2);
    }

}
