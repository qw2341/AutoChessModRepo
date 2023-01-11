package autochess.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(clz = AbstractCard.class, method = "<class>")
public class CardUpgradabilityPatch {
    public static SpireField<Boolean> upgradable = new SpireField<>(() -> Boolean.valueOf(true));

    public static boolean getCardUpgradable(AbstractCard ac) {
        return CardUpgradabilityPatch.upgradable.get(ac);
    }

    public static void setCardUpgradable(AbstractCard ac, boolean upgradable) {
        CardUpgradabilityPatch.upgradable.set(ac,upgradable);
    }

    public static void autosetUpgradability(AbstractCard card) {
        setCardUpgradable(card,canLevelUp(card));
    }
    public static boolean canLevelUp(AbstractCard card) {
        return card.baseDamage > 0 || card.baseBlock > 0 || card.baseMagicNumber > 0;
    }
}
