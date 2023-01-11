package autochess.relics;

import autochess.AutoChessMod;
import autochess.patches.CardLevelPatch;
import autochess.savables.ChessSave;
import autochess.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MagicalGatling extends CustomRelic {

    public static final String ID = AutoChessMod.makeID("MagicalGatling");
    private static final Texture IMG = TextureLoader.getTexture(AutoChessMod.makeRelicPath("MagicalGatling.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(AutoChessMod.makeRelicOutlinePath("MagicalGatling.png"));

    private boolean cardSelected = true;

    public MagicalGatling() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MagicalGatling();
    }

    @Override
    public void onEquip() {
        this.cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        CardGroup cg = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        AbstractDungeon.player.masterDeck.group.forEach( c-> {
                    if(CardLevelPatch.getCardLevel(c) == 1 && c.type != AbstractCard.CardType.CURSE && c.type != AbstractCard.CardType.STATUS) {
                        cg.addToTop(c.makeStatEquivalentCopy());
                    }
                });

        AbstractDungeon.gridSelectScreen.open(cg, 1, this.DESCRIPTIONS[1], false, false, false, false);
    }

    @Override
    public void update() {
        super.update();
        if (!this.cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
            this.cardSelected = true;
            AbstractCard c = ((AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0)).makeStatEquivalentCopy();
            c.inBottleFlame = false;
            c.inBottleLightning = false;
            c.inBottleTornado = false;
            for (int i = 0; i < ChessSave.getNumCardsForTriple() -1; i++) {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    @Override
    public boolean canSpawn() {
        boolean canSpawn = false;
        for (AbstractCard card:AbstractDungeon.player.masterDeck.group) {
            if(card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS && CardLevelPatch.getCardLevel(card) == 1)
                canSpawn = true;
        }
        return canSpawn;
    }


}
