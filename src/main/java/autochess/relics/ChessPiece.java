package autochess.relics;

import autochess.AutoChessMod;
import autochess.patches.CardLevelPatch;
import autochess.savables.ChessSave;
import autochess.util.TextureLoader;
import autochess.vfx.ShowTripleAndObtainEffect;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AutoplayCardAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.*;

public class ChessPiece extends CustomRelic implements CustomSavable<HashMap<Integer,Integer>> {

    public static final String ID = AutoChessMod.makeID("ChessPiece");
    private static final Texture IMG = TextureLoader.getTexture(AutoChessMod.makeRelicPath("ChessPiece.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(AutoChessMod.makeRelicOutlinePath("ChessPiece.png"));

    static Set<AbstractCard.CardType> exceptionTypeSet;
    static {
        exceptionTypeSet = new HashSet<>();
        exceptionTypeSet.add(AbstractCard.CardType.STATUS);
        exceptionTypeSet.add(AbstractCard.CardType.CURSE);
    }


    public ChessPiece() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + ChessSave.getNumCardsForTriple() + DESCRIPTIONS[1];
    }

    @Override
    public void onMasterDeckChange() {

        ArrayList<AbstractCard> dupeCards = getDuplicates(AbstractDungeon.player.masterDeck.group,ChessSave.getNumCardsForTriple());
        AutoChessMod.logger.info("Logging dupe Card: ");
        StringBuilder sb = new StringBuilder();
        for (AbstractCard card : dupeCards) {
            sb.append(card.cardID).append(" with level: ").append(CardLevelPatch.getCardLevel(card)).append('\n');
        }
        AutoChessMod.logger.info(sb.toString());

        if(dupeCards.isEmpty()) return;

        AbstractCard dupe = dupeCards.get(0);
        ArrayList<AbstractCard> cardsToRemove = new ArrayList<>();
        int i = 0;
        for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if(tierAndIdCheck(c,dupe) && i < ChessSave.getNumCardsForTriple()) {
                cardsToRemove.add(c);
                i++;
            }
            if(i>=ChessSave.getNumCardsForTriple()) break;
        }

        if(cardsToRemove.size() < ChessSave.getNumCardsForTriple()) return;

        for (AbstractCard cardToRemove: cardsToRemove) {
            AbstractDungeon.player.masterDeck.group.remove(cardToRemove);
        }

        AbstractDungeon.topLevelEffectsQueue.add(new ShowTripleAndObtainEffect(cardsToRemove, CardGroup.CardGroupType.MASTER_DECK));
    }

    public static ArrayList<AbstractCard> getDuplicates(ArrayList<AbstractCard> searchPile, int numCopy) {
        if(numCopy<=1) {
            return searchPile;
        }

        HashSet<String> lump = new HashSet<>();
        ArrayList<AbstractCard> dupes = new ArrayList<>();
        String idLevel;
        for (AbstractCard c : searchPile)
        {
            if(exceptionTypeSet.contains(c.type) || (!AutoChessMod.enableBasicCardTriple && c.rarity == AbstractCard.CardRarity.BASIC)) continue;
            idLevel = c.cardID + CardLevelPatch.getCardLevel(c);
            if (lump.contains(idLevel)) dupes.add(c);
            else lump.add(idLevel);
        }

        return getDuplicates(dupes,numCopy-1);
    }

    public static boolean tierAndIdCheck(AbstractCard c1, AbstractCard c2) {
        return c1.cardID.equals(c2.cardID) && (CardLevelPatch.getCardLevel(c1) == CardLevelPatch.getCardLevel(c2));
    }

//    @Override
//    public void onPreviewObtainCard(AbstractCard c) {
//        onObtainCard(c);
//    }
//
//    @Override
//    public void onObtainCard(AbstractCard c) {
//        if(CardLevelPatch.cardLevel.get(c) > 1) {
//            try {
//
//            } catch (Exception e) {
//                AutoChessMod.logger.info("Failed to modify: " + c.cardID + " when obtaining");
//            }
//        }
//    }


    @Override
    public void onRefreshHand() {
        if(AbstractDungeon.actionManager.actions.isEmpty() && !AbstractDungeon.isScreenUp) {
            if((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
                if(AbstractDungeon.player.hand.size() >= ChessSave.getNumCardsForTriple()) {
                    ArrayList<AbstractCard> dupeCards = getDuplicates(AbstractDungeon.player.hand.group,ChessSave.getNumCardsForTriple());
                    if(dupeCards.isEmpty()) return;

                    AbstractCard dupe = dupeCards.get(0);
                    ArrayList<AbstractCard> cardsToRemove = new ArrayList<>();
                    int i = 0;
                    for(AbstractCard c : AbstractDungeon.player.hand.group) {
                        if(tierAndIdCheck(c,dupe) && i < ChessSave.getNumCardsForTriple()) {
                            cardsToRemove.add(c);
                            i++;
                        }
                        if(i>=ChessSave.getNumCardsForTriple()) break;
                    }

                    for (AbstractCard cardToRemove: cardsToRemove) {
                        AbstractDungeon.player.hand.group.remove(cardToRemove);
                    }

                    AbstractDungeon.effectsQueue.add(new ShowTripleAndObtainEffect(cardsToRemove, CardGroup.CardGroupType.HAND));
                }
                if(!AbstractDungeon.player.hand.isEmpty() && !AbstractDungeon.actionManager.turnHasEnded) {
                    AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(AbstractDungeon.player.hand.getTopCard(),true, EnergyPanel.getCurrentEnergy(), true, true));
                }

            }
        }
    }

    public static void modifyCard(AbstractCard card, int desiredLevel) {
        int diff = desiredLevel - CardLevelPatch.getCardLevel(card);
        if(diff <= 0) return;
        levelUpCard(card);
        modifyCard(card,desiredLevel);
    }

    public static void levelUpCard(AbstractCard card) {
        CardLevelPatch.setCardLevel(card, CardLevelPatch.getCardLevel(card) + 1);
        card.cost *= 2;
        card.costForTurn = card.cost;
        card.baseDamage  *= 2;
        card.baseBlock  *= 2;
        card.baseMagicNumber  *= 2;
        card.magicNumber = card.baseMagicNumber;
        card.baseHeal  *= 2;
        card.baseDraw  *= 2;
        card.baseDiscard  *= 2;
        card.misc  *= 2;
        card.initializeDescription();

    }

    @Override
    public HashMap<Integer, Integer> onSave() {
        HashMap<Integer, Integer> sav = new HashMap<>();
        ArrayList<AbstractCard> playerDeck = AbstractDungeon.player.masterDeck.group;
        int size = playerDeck.size();
        for (int i = 0; i < size; i++) {
            AbstractCard card = playerDeck.get(i);
            if(CardLevelPatch.getCardLevel(card) > 1) {
                sav.put(i,CardLevelPatch.getCardLevel(card));
            }
        }
        return sav;
    }

    @Override
    public void onLoad(HashMap<Integer, Integer> sav) {
        if(sav == null) return;

        for (Integer i : sav.keySet()) {
            modifyCard(AbstractDungeon.player.masterDeck.group.get(i), sav.get(i));
        }
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards + AutoChessMod.bonusCardDropSelection;
    }


}
