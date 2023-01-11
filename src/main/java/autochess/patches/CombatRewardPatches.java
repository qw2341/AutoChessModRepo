package autochess.patches;

import autochess.AutoChessMod;
import autochess.rewards.MayhemReward;
import autochess.rewards.ScryReward;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

import java.util.ArrayList;

public class CombatRewardPatches {
    @SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class RewardPatch {
        @SpirePostfixPatch
        public static void Postfix(CombatRewardScreen __instance) {
            ArrayList<RewardItem> rewardItems = __instance.rewards;
            if(rewardItems != null) {
                AbstractRoom abstractRoom = AbstractDungeon.getCurrRoom();
                if(abstractRoom != null) {
                    if(abstractRoom instanceof MonsterRoomBoss) {
                        if(AutoChessMod.enableAutoBattle) {
                            rewardItems.add(new MayhemReward(1));
                            rewardItems.add(new ScryReward(2));
                        }
                    }
                    if(AutoChessMod.enableBonusCardDrop && abstractRoom instanceof MonsterRoom) {
                        rewardItems.add(new RewardItem());
                    }
                }


                __instance.positionRewards();
            }
        }
    }
}
