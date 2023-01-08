package autochess.relics;

import autochess.AutoChessMod;
import autochess.savables.ChessSave;
import autochess.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ZephrysLamp extends CustomRelic {

    public static final String ID = AutoChessMod.makeID("ZephrysLamp");
    private static final Texture IMG = TextureLoader.getTexture(AutoChessMod.makeRelicPath("ZephrysLamp.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(AutoChessMod.makeRelicOutlinePath("ZephrysLamp.png"));


    public ZephrysLamp() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        int numToSet = ChessSave.getNumCardsForTriple() - 1;
        if (numToSet <2) numToSet = 2;
        ChessSave.setNumCardsForTriple(numToSet);
        if(AbstractDungeon.player.hasRelic(ChessPiece.ID)) AbstractDungeon.player.getRelic(ChessPiece.ID).onMasterDeckChange();
    }

    @Override
    public void onUnequip() {
        ChessSave.setNumCardsForTriple(ChessSave.getNumCardsForTriple() + 1);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(ChessPiece.ID) && ChessSave.getNumCardsForTriple() > 2;
    }
}
