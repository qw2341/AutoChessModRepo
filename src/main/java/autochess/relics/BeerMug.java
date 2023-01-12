package autochess.relics;

import autochess.AutoChessMod;
import autochess.savables.ChessSave;
import autochess.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BeerMug extends CustomRelic {

    public static final String ID = AutoChessMod.makeID("BeerMug");
    private static final Texture IMG = TextureLoader.getTexture(AutoChessMod.makeRelicPath("BeerMug.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(AutoChessMod.makeRelicOutlinePath("BeerMug.png"));

    private static final int AMOUNT_DISCOUNT = 20;

    public BeerMug() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + AMOUNT_DISCOUNT + DESCRIPTIONS[1] + AMOUNT_DISCOUNT + DESCRIPTIONS[2];
    }


    @Override
    public AbstractRelic makeCopy() {
        return new BeerMug();
    }

    @Override
    public void onEquip() {
        ChessSave.setMayhemCosts((ChessSave.getMayhemCosts() * (100 - AMOUNT_DISCOUNT))/100);
        ChessSave.setScryCosts((ChessSave.getScryCosts() * (100 + AMOUNT_DISCOUNT))/100);
    }

    @Override
    public void onUnequip() {
        ChessSave.setMayhemCosts((ChessSave.getMayhemCosts() * 100)/(100 - AMOUNT_DISCOUNT));
        ChessSave.setScryCosts((ChessSave.getScryCosts() * 100)/(100 + AMOUNT_DISCOUNT));
    }


}
