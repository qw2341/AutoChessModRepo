package autochess;

import autochess.patches.CardLevelPatch;
import autochess.patches.CardUpgradabilityPatch;
import autochess.patches.CustomRewardPatch;
import autochess.potions.MayhemPotion;
import autochess.relics.*;
import autochess.rewards.MayhemReward;
import autochess.rewards.ScryReward;
import autochess.savables.ChessSave;
import autochess.util.TextureLoader;
import basemod.*;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Catalyst;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.MantraPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

@SpireInitializer
public class AutoChessMod implements EditStringsSubscriber, EditRelicsSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, PreUpdateSubscriber, OnCreateDescriptionSubscriber, OnStartBattleSubscriber, RelicGetSubscriber, OnPlayerTurnStartSubscriber, PostPowerApplySubscriber, OnCardUseSubscriber, PreStartGameSubscriber {
    public static final Logger logger = LogManager.getLogger(AutoChessMod.class.getName());
    private static final String modID = "AutoChessMod";
    private static final String BADGE_IMAGE = "AutoChessModResources/images/Badge.png";;

    public static SpireConfig config = null;
    public static Properties theDefaultDefaultSettings = new Properties();

    private static final String MODNAME = "Auto Chess Mod";
    private static final String AUTHOR = "JasonW";
    private static final String DESCRIPTION = "An auto chess mod.";

    public static final String CHESS_SAVE_KEY = "chessSave";

    public static final String DEFAULT_MAYHEM_STACK_KEY = "dMayhemStacks";
    public static int defaultMayhemStacks = 3;
    public static final String DEFAULT_SCRY_STACK_KEY = "dScryStacks";
    public static int defaultScryStacks = 5;

    public static final String DEFAULT_UPGRADE_MAYHEM_COST_KEY = "dMayUpCost";
    public static int defaultMayhemUpgradeCost = 300;
    public static final String DEFAULT_UPGRADE_SCRY_COST_KEY = "dScryUpCost";
    public static int defaultScryUpgradeCost = 100;

    public static final String DEFAULT_UPGRADE_MAYHEM_PENALTY_KEY = "dMayUpPen";
    public static int defaultMayhemUpgradePenalty = 100;
    public static final String DEFAULT_UPGRADE_SCRY_PENALTY_KEY = "dScryUpPen";
    public static int defaultScryUpgradePenalty = 50;

    public static final String BONUS_CARD_DROP_SELECTION_KEY = "bCardDrops";
    public static int bonusCardDropSelection = 2;

    public static final String ENABLE_BONUS_CARD_DROP_KEY = "eBonusCards";
    public static boolean enableBonusCardDrop = true;

    public static final String ENABLE_BASIC_CARDS_TRIPLES_KEY = "eBasicCardsTriples";
    public static boolean enableBasicCardTriple = false;

    public static final String LIMITED_AUTOPLAY_CRADS_DRAWN= "limitAPCD";
    public static boolean limitedAutoDrawnCards = true;

    public static final String ENABLE_AUTO_BATTLE_KEY = "enableAutoBat";
    public static boolean enableAutoBattle = true;

    public static final String ENABLE_TRIPLES = "enableTriples";
    public static boolean enableTriples = true;

    public static char levelSymbol;

    ModPanel settingsPanel;

    public AutoChessMod() {
        logger.info("Subscribe to BaseMod hooks");
        BaseMod.subscribe(this);
        logger.info("Done subscribing");
        logger.info("Adding save fields");
        BaseMod.addSaveField(CHESS_SAVE_KEY, new ChessSave());

        ChessSave.restoreDefault();
        logger.info("Done adding save fields");


        logger.info("Adding mod settings");
        theDefaultDefaultSettings.setProperty(DEFAULT_MAYHEM_STACK_KEY, "3");
        theDefaultDefaultSettings.setProperty(DEFAULT_SCRY_STACK_KEY, "5");
        theDefaultDefaultSettings.setProperty(DEFAULT_UPGRADE_MAYHEM_COST_KEY, "300");
        theDefaultDefaultSettings.setProperty(DEFAULT_UPGRADE_SCRY_COST_KEY, "100");
        theDefaultDefaultSettings.setProperty(DEFAULT_UPGRADE_MAYHEM_PENALTY_KEY, "100");
        theDefaultDefaultSettings.setProperty(DEFAULT_UPGRADE_SCRY_PENALTY_KEY, "50");
        theDefaultDefaultSettings.setProperty(BONUS_CARD_DROP_SELECTION_KEY, "2");
        theDefaultDefaultSettings.setProperty(ENABLE_BONUS_CARD_DROP_KEY, "TRUE");
        theDefaultDefaultSettings.setProperty(ENABLE_BASIC_CARDS_TRIPLES_KEY, "FALSE");
        theDefaultDefaultSettings.setProperty(LIMITED_AUTOPLAY_CRADS_DRAWN, "TRUE");
        theDefaultDefaultSettings.setProperty(ENABLE_AUTO_BATTLE_KEY,"TRUE");
        theDefaultDefaultSettings.setProperty(ENABLE_TRIPLES, "TRUE");
        try {
            config = new SpireConfig("autoChessMod", "autoChessConfig", theDefaultDefaultSettings);
            config.load();





            defaultMayhemStacks = config.getInt(DEFAULT_MAYHEM_STACK_KEY);
            defaultScryStacks = config.getInt(DEFAULT_SCRY_STACK_KEY);
            defaultMayhemUpgradeCost = config.getInt(DEFAULT_UPGRADE_MAYHEM_COST_KEY);
            defaultScryUpgradeCost = config.getInt(DEFAULT_UPGRADE_SCRY_COST_KEY);
            defaultMayhemUpgradePenalty = config.getInt(DEFAULT_UPGRADE_MAYHEM_PENALTY_KEY);
            defaultScryUpgradePenalty = config.getInt(DEFAULT_UPGRADE_SCRY_PENALTY_KEY);
            bonusCardDropSelection = config.getInt(BONUS_CARD_DROP_SELECTION_KEY);
            enableBonusCardDrop = config.getBool(ENABLE_BONUS_CARD_DROP_KEY);
            enableBasicCardTriple = config.getBool(ENABLE_BASIC_CARDS_TRIPLES_KEY);
            limitedAutoDrawnCards = config.getBool(LIMITED_AUTOPLAY_CRADS_DRAWN);
            enableAutoBattle = config.getBool(ENABLE_AUTO_BATTLE_KEY);
            enableTriples = config.getBool(ENABLE_TRIPLES);

            //TODO delete this
            if(defaultMayhemStacks == 5) {
                defaultMayhemStacks = 3;
                config.setInt(DEFAULT_MAYHEM_STACK_KEY, 3);
            }
            if(defaultMayhemUpgradeCost == 500) {
                defaultMayhemUpgradeCost = 300;
                config.setInt(DEFAULT_UPGRADE_MAYHEM_COST_KEY, 300);
            }
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void initialize() {
        logger.info("========================= Initializing Auto Chess Mod.  =========================");
        AutoChessMod autoChessMod = new AutoChessMod();
        logger.info("========================= /Auto Chess Mod Initialized./ =========================");
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    private static String getModID() {
        return modID;
    }

    public static String makeCampfireUIPath(String resourcePath) {
        return getModID() + "Resources/images/ui/campfire/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    @Override
    public void receivePostInitialize() {
        logger.info("========================= Adding Auto Chess Mod Badge  =========================");

        settingsPanel = new ModPanel();
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        float startingXPos = 350.0f * Settings.scale;
        float settingXPos = startingXPos;
        float xSpacing = 300.0f * Settings.scale;
        float startingYPos = 750.0f * Settings.yScale;
        float settingYPos = startingYPos;
        float lineSpacing = 50.0f * Settings.yScale;

        UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(makeID("OptionsMenu"));
        String[] SettingText = UIStrings.TEXT;


        ModLabel dMayhemSliderLabel = new ModLabel(SettingText[0],settingXPos,settingYPos,Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, label->{});
        settingYPos -= lineSpacing * 0.5F;
        settingsPanel.addUIElement(dMayhemSliderLabel);
        ModMinMaxSlider dMayhemSlider = new ModMinMaxSlider("",settingXPos,settingYPos,0,10,defaultMayhemStacks,"x%.0f",settingsPanel,slider -> {
            float fVal = slider.getValue();
            int iVal = Math.round(fVal);
            defaultMayhemStacks = iVal;
            try {
                config.setInt(DEFAULT_MAYHEM_STACK_KEY, iVal);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(dMayhemSlider);

        ModLabel dScrySliderLabel = new ModLabel(SettingText[1],settingXPos,settingYPos,Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, label->{});
        settingYPos -= lineSpacing * 0.5F;
        settingsPanel.addUIElement(dScrySliderLabel);
        ModMinMaxSlider dScrySlider = new ModMinMaxSlider("",settingXPos,settingYPos,0,10,defaultScryStacks,"x%.0f",settingsPanel,slider -> {
            float fVal = slider.getValue();
            int iVal = Math.round(fVal);
            defaultScryStacks = iVal;
            try {
                config.setInt(DEFAULT_SCRY_STACK_KEY, iVal);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(dScrySlider);

        settingXPos += xSpacing;
        settingYPos = startingYPos;
        ModLabel dMayhemCostSliderLabel = new ModLabel(SettingText[2],settingXPos,settingYPos,Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, label->{});
        settingYPos -= lineSpacing * 0.5F;
        settingsPanel.addUIElement(dMayhemCostSliderLabel);
        ModMinMaxSlider dMayhemCostSlider = new ModMinMaxSlider("",settingXPos,settingYPos,0,1000,defaultMayhemUpgradeCost,"x%.0f",settingsPanel,slider -> {
            float fVal = slider.getValue();
            int iVal = Math.round(fVal);
            defaultMayhemUpgradeCost = iVal;
            try {
                config.setInt(DEFAULT_UPGRADE_MAYHEM_COST_KEY, iVal);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(dMayhemCostSlider);

        ModLabel dScryCostSliderLabel = new ModLabel(SettingText[3],settingXPos,settingYPos,Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, label->{});
        settingYPos -= lineSpacing * 0.5F;
        settingsPanel.addUIElement(dScryCostSliderLabel);
        ModMinMaxSlider dScryCostSlider = new ModMinMaxSlider("",settingXPos,settingYPos,0,500,defaultScryUpgradeCost,"x%.0f",settingsPanel,slider -> {
            float fVal = slider.getValue();
            int iVal = Math.round(fVal);
            defaultScryUpgradeCost = iVal;
            try {
                config.setInt(DEFAULT_UPGRADE_SCRY_COST_KEY, iVal);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(dScryCostSlider);

        settingXPos += xSpacing;
        settingYPos = startingYPos;
        ModLabel dMayhemCostPenSliderLabel = new ModLabel(SettingText[4],settingXPos,settingYPos,Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, label->{});
        settingYPos -= lineSpacing * 0.5F;
        settingsPanel.addUIElement(dMayhemCostPenSliderLabel);
        ModMinMaxSlider dMayhemCostPenSlider = new ModMinMaxSlider("",settingXPos,settingYPos,0,500,defaultMayhemUpgradePenalty,"x%.0f",settingsPanel,slider -> {
            float fVal = slider.getValue();
            int iVal = Math.round(fVal);
            defaultMayhemUpgradePenalty = iVal;
            try {
                config.setInt(DEFAULT_UPGRADE_MAYHEM_PENALTY_KEY, iVal);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(dMayhemCostPenSlider);

        ModLabel dScryCostPenSliderLabel = new ModLabel(SettingText[5],settingXPos,settingYPos,Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, label->{});
        settingYPos -= lineSpacing * 0.5F;
        settingsPanel.addUIElement(dScryCostPenSliderLabel);
        ModMinMaxSlider dScryCostPenSlider = new ModMinMaxSlider("",settingXPos,settingYPos,0,500,defaultScryUpgradePenalty,"x%.0f",settingsPanel,slider -> {
            float fVal = slider.getValue();
            int iVal = Math.round(fVal);
            defaultScryUpgradePenalty = iVal;
            try {
                config.setInt(DEFAULT_UPGRADE_SCRY_PENALTY_KEY, iVal);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(dScryCostPenSlider);

        settingXPos = startingXPos;
        ModLabel bCardDropSelSliderLabel = new ModLabel(SettingText[6],settingXPos,settingYPos,Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, label->{});
        settingYPos -= lineSpacing * 0.5F;
        settingsPanel.addUIElement(bCardDropSelSliderLabel);
        ModMinMaxSlider bCardDropSelSlider = new ModMinMaxSlider("",settingXPos,settingYPos,0,5,bonusCardDropSelection,"x%.0f",settingsPanel,slider -> {
            float fVal = slider.getValue();
            int iVal = Math.round(fVal);
            bonusCardDropSelection = iVal;
            try {
                config.setInt(BONUS_CARD_DROP_SELECTION_KEY, iVal);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(bCardDropSelSlider);

        ModLabeledToggleButton bCardDropButton = new ModLabeledToggleButton(SettingText[7],settingXPos,settingYPos,Settings.CREAM_COLOR,FontHelper.charDescFont, enableBonusCardDrop,settingsPanel, label -> {},button -> {

            enableBonusCardDrop = button.enabled;
            try {
                config.setBool(ENABLE_BONUS_CARD_DROP_KEY, enableBonusCardDrop);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(bCardDropButton);

        ModLabeledToggleButton bCardTripleButton = new ModLabeledToggleButton(SettingText[8],settingXPos,settingYPos,Settings.CREAM_COLOR,FontHelper.charDescFont, enableBasicCardTriple,settingsPanel, label -> {},button -> {

            enableBasicCardTriple = button.enabled;
            try {
                config.setBool(ENABLE_BASIC_CARDS_TRIPLES_KEY, enableBasicCardTriple);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(bCardTripleButton);

        ModLabeledToggleButton lAutoCardsButton = new ModLabeledToggleButton(SettingText[9], SettingText[10],settingXPos,settingYPos,Settings.CREAM_COLOR,FontHelper.charDescFont, limitedAutoDrawnCards,settingsPanel, label -> {},button -> {

            limitedAutoDrawnCards = button.enabled;
            try {
                config.setBool(LIMITED_AUTOPLAY_CRADS_DRAWN, limitedAutoDrawnCards);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(lAutoCardsButton);

        ModLabeledToggleButton eAutoButton = new ModLabeledToggleButton(SettingText[11],settingXPos,settingYPos,Settings.CREAM_COLOR,FontHelper.charDescFont, enableAutoBattle,settingsPanel, label -> {},button -> {

            enableAutoBattle = button.enabled;
            try {
                config.setBool(ENABLE_AUTO_BATTLE_KEY, enableAutoBattle);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(eAutoButton);

        ModLabeledToggleButton eTripleButton = new ModLabeledToggleButton(SettingText[12],settingXPos,settingYPos,Settings.CREAM_COLOR,FontHelper.charDescFont, enableTriples,settingsPanel, label -> {},button -> {

            enableTriples = button.enabled;
            try {
                config.setBool(ENABLE_TRIPLES, enableTriples);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        settingYPos -= lineSpacing;
        settingsPanel.addUIElement(eTripleButton);

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
        logger.info("========================= Done Adding Auto Chess Mod Badge  =========================");

        logger.info("Adding custom rewards");
        BaseMod.registerCustomReward(CustomRewardPatch.ACM_MAYHEM_REWARD,rewardSave -> new MayhemReward(rewardSave.amount), customReward -> new RewardSave(customReward.type.toString(),null, ((MayhemReward) customReward).amount, 0));
        BaseMod.registerCustomReward(CustomRewardPatch.ACM_SCRY_REWARD,rewardSave -> new ScryReward(rewardSave.amount), customReward -> new RewardSave(customReward.type.toString(),null, ((ScryReward) customReward).amount, 0));
        logger.info("Done adding custom rewards");

        BaseMod.addPotion(MayhemPotion.class, Color.GOLD, Color.SCARLET, Color.FIREBRICK, MayhemPotion.POTION_ID);
    }


    @Override
    public void receivePostDungeonInitialize() {
        if(!Settings.isEndless || AbstractDungeon.floorNum == 0) ChessSave.restoreDefault();

        if(RelicLibrary.isARelic(ChessPiece.ID)&&!AbstractDungeon.player.hasRelic(ChessPiece.ID)) RelicLibrary.getRelic(ChessPiece.ID).makeCopy().instantObtain();

        if(AbstractDungeon.player.hasRelic(ChessPiece.ID)) AbstractDungeon.player.getRelic(ChessPiece.ID).onMasterDeckChange();

        if(enableAutoBattle) {
            AbstractDungeon.player.energy.energyMaster = 0;
            ChessSave.setAutoCardsLimit(ChessSave.getAutoCardsLimit() + AbstractDungeon.player.masterHandSize);
            AutoChessMod.logger.info("Post Init Auto limit: " + ChessSave.getAutoCardsLimit());
            AbstractDungeon.player.masterHandSize = 0;
        }

        if(Loader.isModLoaded("loadout")) ChessSave.setAutoCardsLimit(5);

    }

    @Override
    public void receiveEditStrings() {
        loadLocStrings("eng");
        if (!languageSupport().equals("eng")) {
            loadLocStrings(languageSupport());
        }

         levelSymbol = languageSupport().equals("eng") ? '+' : '★';



    }

    public static String languageSupport() {
        switch (Settings.language) {
            case ZHS:
                return "zhs";
//            case ZHT:
//                return "zht";
//            case KOR:
//                return "kor";
//            case JPN:
//                return "jpn";
//            case FRA:
//                return "fra";
//            case RUS:
//                return "rus";
        }
        return "eng";
    }

    private void loadLocStrings(String language) {
        BaseMod.loadCustomStringsFile(UIStrings.class, getModID() + "Resources/localization/" + language + "/UI-Strings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/"+ language +"/Relic-Strings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/"+ language +"/Potion-Strings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/"+ language +"/Power-Strings.json");
    }

    @Override
    public void receivePreUpdate() {
//        if(AbstractDungeon.isPlayerInDungeon() && ) {
//
//        }
    }


    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new ChessPiece(), RelicType.SHARED);
        BaseMod.addRelic(new ZephrysLamp(), RelicType.SHARED);
        BaseMod.addRelic(new MagicalGatling(), RelicType.SHARED);
        BaseMod.addRelic(new CapturedFlag(), RelicType.SHARED);
        BaseMod.addRelic(new BeerMug(), RelicType.SHARED);
    }

    @Override
    public String receiveCreateCardDescription(String s, AbstractCard abstractCard) {
        int level = CardLevelPatch.getCardLevel(abstractCard);
        if(level > 5) return s + " NL " + levelSymbol + level;
        else if(level > 1) return s + " NL " + StringUtils.repeat(levelSymbol,level);
        else return s;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {

    }

    @Override
    public void receiveRelicGet(AbstractRelic abstractRelic) {
        if(enableAutoBattle){
            AutoChessMod.logger.info("Relic get Auto limit: " + ChessSave.getAutoCardsLimit());
            AbstractDungeon.player.energy.energyMaster = 0;
            ChessSave.setAutoCardsLimit(ChessSave.getAutoCardsLimit() + AbstractDungeon.player.masterHandSize);
            AbstractDungeon.player.masterHandSize = 0;
        }
    }


    @Override
    public void receiveOnPlayerTurnStart() {

    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if(target instanceof AbstractPlayer) {
            if(power instanceof MantraPower && power.amount >= 10) {
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)target, (AbstractCreature)source, (AbstractPower)new MantraPower((AbstractCreature)target, 0), 0));
            }
        }
    }

    @Override
    public void receiveCardUsed(AbstractCard card) {
        //logger.info(card.cardID + " used! Card level = " + CardLevelPatch.getCardLevel(card) + " Can be leveled up? " + CardUpgradabilityPatch.canLevelUp(card));
        if(CardLevelPatch.getCardLevel(card) > 1) {
            if(CardUpgradabilityPatch.exceptions.contains(card.cardID)) {
                switch (card.cardID) {
                    case com.megacrit.cardcrawl.cards.red.BodySlam.ID:
                        AbstractCard copy = CardLibrary.getCard(card.cardID).makeCopy();
                        ChessPiece.modifyCard(copy,CardLevelPatch.getCardLevel(card) - 1);
                        ChessPiece.addToAutoPlayTop(copy);
                        break;
                    case Catalyst.ID:
                        AbstractCard catalyst = new Catalyst();
                        CardLevelPatch.setCardLevel(catalyst,CardLevelPatch.getCardLevel(card) - 1);
                        if(card.upgraded) catalyst.upgrade();
                        break;
                    case com.megacrit.cardcrawl.cards.red.Corruption.ID:
                    case com.megacrit.cardcrawl.cards.red.Barricade.ID:
                    default:
                        break;
                }
            } else if(!CardUpgradabilityPatch.canLevelUp(card)) {
                AbstractCard copy = CardLibrary.getCard(card.cardID).makeCopy();
                ChessPiece.modifyCard(copy,CardLevelPatch.getCardLevel(card) - 1);
                ChessPiece.addToAutoPlayTop(copy);
            }
        }
    }

    @Override
    public void receivePreStartGame() {

    }
}
