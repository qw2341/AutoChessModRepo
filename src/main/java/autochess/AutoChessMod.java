package autochess;

import autochess.savables.ChessSave;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

@SpireInitializer
public class AutoChessMod implements EditStringsSubscriber, PostInitializeSubscriber, PostDungeonInitializeSubscriber {
    public static final Logger logger = LogManager.getLogger(AutoChessMod.class.getName());
    private static final String modID = "AutoChessMod";

    public static SpireConfig config = null;
    public static Properties theDefaultDefaultSettings = new Properties();

    private static final String MODNAME = "Auto Chess Mod";
    private static final String AUTHOR = "JasonW";
    private static final String DESCRIPTION = "An auto chess mod.";

    public static final String CHESS_SAVE_KEY = "chessSave";

    public static final String DEFAULT_MAYHEM_STACK_KEY = "dMayhemStacks";
    public static int defaultMayhemStacks = 3;
    public static final String DEFAULT_SCRY_STACK_KEY = "dScryStacks";
    public static int defaultScryStacks = 1;

    public static final String DEFAULT_UPGRADE_MAYHEM_COST_KEY = "dMayUpCost";
    public static int defaultMayhemUpgradeCost = 500;
    public static final String DEFAULT_UPGRADE_SCRY_COST_KEY = "dScryUpCost";
    public static int defaultScryUpgradeCost = 200;

    public static final String DEFAULT_UPGRADE_MAYHEM_PENALTY_KEY = "dMayUpPen";
    public static int defaultMayhemUpgradePenalty = 100;
    public static final String DEFAULT_UPGRADE_SCRY_PENALTY_KEY = "dScryUpPen";
    public static int defaultScryUpgradePenalty = 50;

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

    @Override
    public void receivePostInitialize() {
        ModPanel settingsPanel = new ModPanel();
        Texture badgeTexture = ImageMaster.INTENT_ATK_7;

        float startingXPos = 350.0f;
        float settingXPos = startingXPos;
        float xSpacing = 250.0f;
        float settingYPos = 750.0f;
        float lineSpacing = 50.0f;



        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
    }


    @Override
    public void receivePostDungeonInitialize() {
        ChessSave.restoreDefault();

        AbstractDungeon.player.energy.energyMaster = 0;
    }

    @Override
    public void receiveEditStrings() {
        loadLocStrings("eng");
        if (!languageSupport().equals("eng"))
            loadLocStrings(languageSupport());
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
}
