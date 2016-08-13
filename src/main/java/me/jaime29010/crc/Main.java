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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.bukkit.Material.*;

public final class Main extends JavaPlugin implements Listener {
    public static final List<String> REPAIRABLE_ITEMS = Collections.unmodifiableList(Arrays.asList(
            "WOOD_SWORD", "WOOD_SPADE", "WOOD_HOE", "WOOD_AXE", "WOOD_PICKAXE",
            "CHAINMAIL_HELMET", "CHAINMAIL_CHESTPLATE", "CHAINMAIL_LEGGINGS", "CHAINMAIL_BOOTS",
            "IRON_SWORD", "IRON_HELMET", "IRON_CHESTPLATE", "IRON_LEGGINGS", "IRON_BOOTS", "IRON_SPADE", "IRON_HOE", "IRON_AXE", "IRON_PICKAXE",
            "GOLD_SWORD", "GOLD_HELMET", "GOLD_CHESTPLATE", "GOLD_LEGGINGS", "GOLD_BOOTS", "GOLD_SPADE", "GOLD_HOE", "GOLD_AXE", "GOLD_PICKAXE",
            "DIAMOND_SWORD", "DIAMOND_HELMET", "DIAMOND_CHESTPLATE", "DIAMOND_LEGGINGS", "DIAMOND_BOOTS", "DIAMOND_SPADE", "DIAMOND_HOE", "DIAMOND_AXE", "DIAMOND_PICKAXE",
            "SHIELD", "CARROT_STICK", "FLINT_AND_STEEL", "FISHING_ROD", "SHEARS", "BOW"));
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
                if (REPAIRABLE_ITEMS.contains(item.getType().name())) {
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
