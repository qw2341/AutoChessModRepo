package autochess;

import autochess.patches.CardLevelPatch;
import autochess.relics.ChessPiece;
import autochess.savables.ChessSave;
import basemod.*;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

@SpireInitializer
public class AutoChessMod implements EditStringsSubscriber, EditRelicsSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber, PreUpdateSubscriber, OnCreateDescriptionSubscriber {
    public static final Logger logger = LogManager.getLogger(AutoChessMod.class.getName());
    private static final String modID = "AutoChessMod";

    public static SpireConfig config = null;
    public static Properties theDefaultDefaultSettings = new Properties();

    private static final String MODNAME = "Auto Chess Mod";
    private static final String AUTHOR = "JasonW";
    private static final String DESCRIPTION = "An auto chess mod.";

    public static final String CHESS_SAVE_KEY = "chessSave";

    public static final String DEFAULT_MAYHEM_STACK_KEY = "dMayhemStacks";
    public static int defaultMayhemStacks = 5;
    public static final String DEFAULT_SCRY_STACK_KEY = "dScryStacks";
    public static int defaultScryStacks = 2;

    public static final String DEFAULT_UPGRADE_MAYHEM_COST_KEY = "dMayUpCost";
    public static int defaultMayhemUpgradeCost = 500;
    public static final String DEFAULT_UPGRADE_SCRY_COST_KEY = "dScryUpCost";
    public static int defaultScryUpgradeCost = 200;

    public static final String DEFAULT_UPGRADE_MAYHEM_PENALTY_KEY = "dMayUpPen";
    public static int defaultMayhemUpgradePenalty = 100;
    public static final String DEFAULT_UPGRADE_SCRY_PENALTY_KEY = "dScryUpPen";
    public static int defaultScryUpgradePenalty = 50;

    ModPanel settingsPanel;

    public AutoChessMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);

        BaseMod.addSaveField(CHESS_SAVE_KEY, new ChessSave());


        logger.info("Done subscribing");

        logger.info("Adding mod settings");
        theDefaultDefaultSettings.setProperty(DEFAULT_MAYHEM_STACK_KEY, String.valueOf(defaultMayhemStacks));
        theDefaultDefaultSettings.setProperty(DEFAULT_SCRY_STACK_KEY, String.valueOf(defaultScryStacks));
        theDefaultDefaultSettings.setProperty(DEFAULT_UPGRADE_MAYHEM_COST_KEY, String.valueOf(defaultMayhemUpgradeCost));
        theDefaultDefaultSettings.setProperty(DEFAULT_UPGRADE_SCRY_COST_KEY, String.valueOf(defaultScryUpgradeCost));
        theDefaultDefaultSettings.setProperty(DEFAULT_UPGRADE_MAYHEM_PENALTY_KEY, String.valueOf(defaultMayhemUpgradePenalty));
        theDefaultDefaultSettings.setProperty(DEFAULT_UPGRADE_SCRY_PENALTY_KEY, String.valueOf(defaultScryUpgradePenalty));


        try {
            config = new SpireConfig("autoChessMod", "autoChessConfig", theDefaultDefaultSettings);
            config.load();

            defaultMayhemStacks = config.getInt(DEFAULT_MAYHEM_STACK_KEY);
            defaultScryStacks = config.getInt(DEFAULT_SCRY_STACK_KEY);
            defaultMayhemUpgradeCost = config.getInt(DEFAULT_UPGRADE_MAYHEM_COST_KEY);
            defaultScryUpgradeCost = config.getInt(DEFAULT_UPGRADE_SCRY_COST_KEY);
            defaultMayhemUpgradePenalty = config.getInt(DEFAULT_UPGRADE_MAYHEM_PENALTY_KEY);
            defaultScryUpgradePenalty = config.getInt(DEFAULT_UPGRADE_SCRY_PENALTY_KEY);


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
        settingsPanel = new ModPanel();
        Texture badgeTexture = ImageMaster.INTENT_ATK_7;

        float startingXPos = 350.0f;
        float settingXPos = startingXPos;
        float xSpacing = 250.0f;
        float settingYPos = 750.0f;
        float lineSpacing = 50.0f;

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
        ModMinMaxSlider dScryCostSlider = new ModMinMaxSlider("",settingXPos,settingYPos,0,1000,defaultScryUpgradeCost,"x%.0f",settingsPanel,slider -> {
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

        ModLabel dMayhemCostPenSliderLabel = new ModLabel(SettingText[4],settingXPos,settingYPos,Settings.CREAM_COLOR, FontHelper.charDescFont, settingsPanel, label->{});
        settingYPos -= lineSpacing * 0.5F;
        settingsPanel.addUIElement(dMayhemCostPenSliderLabel);
        ModMinMaxSlider dMayhemCostPenSlider = new ModMinMaxSlider("",settingXPos,settingYPos,0,1000,defaultMayhemUpgradePenalty,"x%.0f",settingsPanel,slider -> {
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
        ModMinMaxSlider dScryCostPenSlider = new ModMinMaxSlider("",settingXPos,settingYPos,0,1000,defaultScryUpgradePenalty,"x%.0f",settingsPanel,slider -> {
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

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
    }


    @Override
    public void receivePostDungeonInitialize() {
        ChessSave.restoreDefault();

        if(RelicLibrary.isARelic(ChessPiece.ID)&&!AbstractDungeon.player.hasRelic(ChessPiece.ID)) RelicLibrary.getRelic(ChessPiece.ID).makeCopy().instantObtain();

        if(AbstractDungeon.player.hasRelic(ChessPiece.ID)) AbstractDungeon.player.getRelic(ChessPiece.ID).onMasterDeckChange();
        AbstractDungeon.player.energy.energyMaster = 0;
        AbstractDungeon.player.masterHandSize = 0;
    }

    @Override
    public void receiveEditStrings() {
        loadLocStrings("eng");
        if (!languageSupport().equals("eng"))
            loadLocStrings(languageSupport());
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/"+languageSupport()+"/Relic-Strings.json");
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
    }

    @Override
    public String receiveCreateCardDescription(String s, AbstractCard abstractCard) {
        int level = CardLevelPatch.getCardLevel(abstractCard);
        if(level > 5) return s + " NL ★" + level;
        else if(level > 1) return s + " NL " + StringUtils.repeat('★',level);
        else return s;
    }
}
