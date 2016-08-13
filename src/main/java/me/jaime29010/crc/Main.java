package me.jaime29010.crc;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
    private FileConfiguration config;
    private int cost = 10;

    @Override
    public void onEnable() {
        config = ConfigurationManager.loadConfig("config.yml", this);
        cost = config.getInt("cost");

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getInventory() instanceof AnvilInventory) {
                ItemStack item = event.getCurrentItem();
                if (item == null) return;
                if (item.getItemMeta() instanceof Repairable) {
                    Repairable repairable = (Repairable) item.getItemMeta();
                    int current = repairable.getRepairCost();
                    if (current != -1 && current != cost) {
                        repairable.setRepairCost(cost);
                        item.setItemMeta((ItemMeta) repairable);
                        event.setCurrentItem(item);
                        if (config.getBoolean("message.enabled")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("message.text")
                                    .replace("%cost%", String.valueOf(cost))
                            ));
                        }
                    }
                }
            }
        }
    }
}
