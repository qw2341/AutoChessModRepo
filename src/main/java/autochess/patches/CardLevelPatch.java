package autochess.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(clz = AbstractCard.class, method = "<class>")
public class CardLevelPatch {

    public static SpireField<Integer> cardLevel = new SpireField<>(() -> Integer.valueOf(1));

    public static int getCardLevel(AbstractCard ac) {
        return CardLevelPatch.cardLevel.get(ac);
    }

    public static void setCardLevel(AbstractCard ac, int level) {
        CardLevelPatch.cardLevel.set(ac,level);
    }

}
