package autochess.rewards;

import autochess.AutoChessMod;
import autochess.patches.CustomRewardPatch;
import autochess.savables.ChessSave;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MayhemReward extends CustomReward {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(AutoChessMod.makeID("RewardButton"));

    public static final String[] TEXT = uiStrings.TEXT;

    public int amount;

    public MayhemReward(int amount) {
        super(AbstractPower.atlas.findRegion("48/mayhem"), TEXT[0] + amount + TEXT[1], CustomRewardPatch.ACM_MAYHEM_REWARD);
        this.amount = amount;
    }

    public MayhemReward() {
        this(1);
    }

    @Override
    public boolean claimReward() {
        ChessSave.setMayhemStacks(ChessSave.getMayhemStacks() + amount);
        return true;
    }
}
