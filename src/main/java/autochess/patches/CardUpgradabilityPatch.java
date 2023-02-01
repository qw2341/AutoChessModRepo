package autochess.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Catalyst;
import com.megacrit.cardcrawl.cards.purple.MasterReality;
import com.megacrit.cardcrawl.cards.red.Barricade;
import com.megacrit.cardcrawl.cards.red.BodySlam;
import com.megacrit.cardcrawl.cards.red.Corruption;

import java.util.HashSet;

@SpirePatch(clz = AbstractCard.class, method = "<class>")
public class CardUpgradabilityPatch {
    public static HashSet<String> exceptions = new HashSet<>();
    static {
        exceptions.add(BodySlam.ID);
        exceptions.add(Corruption.ID);
        exceptions.add(Catalyst.ID);
        exceptions.add(Barricade.ID);
        exceptions.add(MasterReality.ID);
    }
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
