package autochess.rewards;

import autochess.AutoChessMod;
import autochess.patches.CustomRewardPatch;
import autochess.savables.ChessSave;
import basemod.abstracts.CustomReward;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ScryReward extends CustomReward {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(AutoChessMod.makeID("RewardButton"));

    public static final String[] TEXT = uiStrings.TEXT;

    public int amount;

    public ScryReward(int amount) {
        super(AbstractPower.atlas.findRegion("48/wireheading"), TEXT[0] + amount + TEXT[2], CustomRewardPatch.ACM_SCRY_REWARD);
        this.amount = amount;
    }

    public ScryReward() {
        this(1);
    }

    @Override
    public boolean claimReward() {
        ChessSave.setScryStacks(ChessSave.getScryStacks() + amount);
        return true;
    }
}
