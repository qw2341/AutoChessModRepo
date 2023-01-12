package autochess.relics;

import autochess.AutoChessMod;
import autochess.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CapturedFlag extends CustomRelic {

    public static final String ID = AutoChessMod.makeID("CapturedFlag");
    private static final Texture IMG = TextureLoader.getTexture(AutoChessMod.makeRelicPath("CapturedFlag.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(AutoChessMod.makeRelicOutlinePath("CapturedFlag.png"));


    public CapturedFlag() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CapturedFlag();
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.masterDeck.group.forEach(this::cardStatUp);
        this.flash();
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.masterDeck.group.forEach(this::cardStatDown);
    }

    @Override
    public void onPreviewObtainCard(AbstractCard c) {
        //onObtainCard(c);
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        this.flash();
        cardStatUp(c);
    }

    private void cardStatUp(AbstractCard c){
        if(c.baseDamage >= 0) c.baseDamage++;
        if(c.baseBlock >= 0) c.baseBlock++;
        if(c.baseMagicNumber >= 0) {
            c.baseMagicNumber ++;
            c.magicNumber = c.baseMagicNumber;
        }
        if(c.baseDraw >= 0) c.baseDraw++;
        if(c.baseDiscard >= 0) c.baseDiscard++;
        if(c.baseHeal >= 0) c.baseHeal++;
        if(c.misc >= 0) c.misc ++;
        c.initializeDescription();

    }
    private void cardStatDown(AbstractCard c){
        if(c.baseDamage > 0) c.baseDamage--;
        if(c.baseBlock > 0) c.baseBlock--;
        if(c.baseMagicNumber > 0) {
            c.baseMagicNumber --;
            c.magicNumber = c.baseMagicNumber;
        }
        if(c.baseDraw >= 0) c.baseDraw--;
        if(c.baseDiscard >= 0) c.baseDiscard--;
        if(c.baseHeal >= 0) c.baseHeal--;
        if(c.misc > 0) c.misc --;
        c.initializeDescription();

    }

}
