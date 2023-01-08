package autochess.vfx;



         import autochess.patches.CardLevelPatch;
         import autochess.relics.ChessPiece;
         import com.badlogic.gdx.Gdx;
         import com.badlogic.gdx.graphics.g2d.SpriteBatch;
         import com.badlogic.gdx.math.MathUtils;
         import com.megacrit.cardcrawl.cards.AbstractCard;
         import com.megacrit.cardcrawl.cards.CardGroup;
         import com.megacrit.cardcrawl.core.Settings;
         import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
         import com.megacrit.cardcrawl.helpers.CardHelper;
         import com.megacrit.cardcrawl.helpers.CardLibrary;
         import com.megacrit.cardcrawl.relics.AbstractRelic;
         import com.megacrit.cardcrawl.relics.Omamori;
         import com.megacrit.cardcrawl.unlock.UnlockTracker;
         import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
         import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
         import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
         import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
         import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
         import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

         import java.util.ArrayList;

public class ShowTripleAndObtainEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 3.0F;
    private static final float FAST_DUR = 0.75F;
    private final ArrayList<AbstractCard> cards;

    private AbstractCard cardToObtain;
    private static final float PADDING = 30.0F * Settings.scale;

    private CardGroup.CardGroupType destination;

    private boolean middleOfAnimation;
    private float halfDuration;

    public ShowTripleAndObtainEffect(ArrayList<AbstractCard> cards, CardGroup.CardGroupType destination) {
        this.cards = cards;
        this.destination = destination;
        this.middleOfAnimation = false;
        if(cards.isEmpty()) {
            this.isDone = true;
            this.duration = 0.0F;
            this.halfDuration = 0.0f;
        } else {


            final int cardLevel = CardLevelPatch.getCardLevel(cards.get(0));

            this.cardToObtain = CardLibrary.getCard(cards.get(0).cardID).makeCopy();

            ChessPiece.modifyCard(this.cardToObtain, cardLevel + 1);

            if (Settings.FAST_MODE) {
                this.duration = FAST_DUR;
            } else {
                this.duration = EFFECT_DUR;
            }
            this.halfDuration = this.duration / 2.0f;
            identifySpawnLocation();

            for (AbstractCard card : cards) {
                AbstractDungeon.topLevelEffectsQueue.add(new CardPoofEffect(card.target_x, card.target_y));
                card.drawScale = 0.01F;
                card.targetDrawScale = 1.0F;
            }
        }




    }


    private void identifySpawnLocation() {

        int i = 1;
        int len = cards.size() + 1;
        for (AbstractCard card : cards) {
            card.target_x = Settings.WIDTH * ((float)i/len);
            card.target_y = Settings.HEIGHT * 0.5F;
            card.targetDrawScale = 1.0f;
            //card.target_x = Settings.WIDTH * 0.5F;
            //card.target_y = Settings.HEIGHT * 0.5F;
            i++;
        }

    }


    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        for (AbstractCard card: cards) {
            card.update();
        }

        if(this.duration < this.halfDuration && !middleOfAnimation) {
            middleOfAnimation = true;
            for (AbstractCard card : cards) {
                card.target_x = Settings.WIDTH * 0.5F;
                card.target_y = Settings.HEIGHT * 0.5F;
                card.targetDrawScale = 0.01f;
            }
        }

        if (this.duration < 0.0F) {

            this.isDone = true;
            switch (this.destination) {

                case DRAW_PILE:
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndAddToDrawPileEffect(this.cardToObtain,Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F, false));
                    break;
                case HAND:
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndAddToHandEffect(this.cardToObtain,Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F));
                    break;
                case DISCARD_PILE:
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndAddToDiscardEffect(this.cardToObtain,Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F));
                    break;
                case EXHAUST_PILE:
                    break;
                case MASTER_DECK:
                case CARD_POOL:
                case UNSPECIFIED:
                default:
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(this.cardToObtain,Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F, false));
            }

        }
    }


    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            for (AbstractCard card : cards) card.render(sb);
        }
    }

    public void dispose() {}
}


