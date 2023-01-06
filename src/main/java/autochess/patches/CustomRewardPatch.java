package autochess.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.rewards.RewardItem;

public class CustomRewardPatch {
    @SpireEnum
    public static RewardItem.RewardType ACM_MAYHEM_REWARD;

    @SpireEnum
    public static RewardItem.RewardType ACM_SCRY_REWARD;
}
