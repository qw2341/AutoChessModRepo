package autochess.ui.campfire;

import autochess.AutoChessMod;
import autochess.savables.ChessSave;
import autochess.util.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

public class UpgradeScryOption extends AbstractCampfireOption {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(AutoChessMod.makeID("UpgradeScryButton"));

    public static final String[] TEXT = uiStrings.TEXT;

    //public boolean usable = true;

    public int upgradeCost;

    public UpgradeScryOption() {
        super();
        this.upgradeCost = ChessSave.getScryCosts();
        this.usable = AbstractDungeon.player.gold >= this.upgradeCost;
        this.img = getImg();
        updateLabel();
    }

    @Override
    public void useOption() {
        ChessSave.setScryStacks(ChessSave.getScryStacks() + 1);
        this.upgradeCost += AutoChessMod.defaultScryUpgradePenalty;
        ChessSave.setScryCosts(this.upgradeCost);

        CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY");
        //CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, true);
        CardCrawlGame.metricData.addCampfireChoiceData("LIFT", Integer.toString(ChessSave.getMayhemStacks()));
        AbstractDungeon.topLevelEffects.add(new BorderFlashEffect(new Color(0.8F, 0.6F, 0.1F, 0.0F)));
    }

    private Texture getImg() {
        return TextureLoader.getTexture(AutoChessMod.makeCampfireUIPath("scry.png"));
    }

    private void updateLabel() {
        this.label = this.usable ? TEXT[0] : TEXT[1] + this.upgradeCost + TEXT[2];

        this.description = TEXT[3] + this.upgradeCost + TEXT[2];
    }

    public void update() {
        super.update();

        if (AbstractDungeon.player.gold < this.upgradeCost && this.usable) {
            this.usable = false;
            updateLabel();
        }
        if (AbstractDungeon.player.gold >= this.upgradeCost && !this.usable) {
            this.usable = true;
            updateLabel();
        }

    }
}

