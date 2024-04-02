package lzy.lugin.firstplugin.myListener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeathListener implements Listener {

    private int dropItemCount; // 控制掉落的物品数量

    public DeathListener(int dropItemCount) {
        this.dropItemCount = dropItemCount;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // 检查服务器规则是否为死亡掉落物品
        if (!player.getWorld().getGameRuleValue("keepInventory").equals("false")) {
            return; // 若规则不为死亡掉落物品，则不执行下面的逻辑
        }

        // 获取玩家的物品列表
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                items.add(item);
            }
        }
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null && item.getType() != Material.AIR) {
                items.add(item);
            }
        }

        // 如果物品不足指定数量，掉落全部物品
        if (items.size() <= dropItemCount) {
            for (ItemStack item : items) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        } else {
            // 随机掉落指定数量的物品
            Random random = new Random();
            for (int i = 0; i < dropItemCount; i++) {
                ItemStack item = items.get(random.nextInt(items.size()));
                player.getWorld().dropItemNaturally(player.getLocation(), item);
                items.remove(item);
            }
        }
    }
}

