package me.jaime29010.crc;

import me.jaimemartz.faucet.ConfigUtil;
import me.jaimemartz.faucet.Messager;
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
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;

public final class Main extends JavaPlugin implements Listener {
    private FileConfiguration config;

    @Override
    public void onEnable() {
        getConfig();

        if (config.getBoolean("auto-update")) {
            final SpigetUpdate updater = new SpigetUpdate(this, 18822);
            updater.checkForUpdate(new UpdateCallback() {
                @Override
                public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
                    if (hasDirectDownload) {
                        if (updater.downloadUpdate()) {
                            getLogger().info("The plugin has successfully updated to version " + newVersion);
                            getLogger().info("The next time you start your server the plugin will have the new version");
                        } else {
                            getLogger().warning("Update download failed, reason is " + updater.getFailReason());
                        }
                    }
                }

                @Override
                public void upToDate() {
                    getLogger().info("The plugin is in the latest version available");
                }
            });
        }


        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void on(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getInventory() instanceof AnvilInventory) {
                ItemStack item = event.getCurrentItem();
                if (item == null) return;
                if (config.getString("items").contains(item.getType().name())) {
                    Repairable repairable = (Repairable) item.getItemMeta();
                    int current = repairable.getRepairCost();
                    if (current != -1 && current != config.getInt("cost")) {
                        repairable.setRepairCost(config.getInt("cost"));
                        item.setItemMeta((ItemMeta) repairable);
                        event.setCurrentItem(item);
                        if (config.getBoolean("message.enabled")) {
                            player.sendMessage(Messager.colorize(config.getString("message.text").replace("%cost%", String.valueOf(config.getInt("cost")))));
                        }
                    }
                }
            }
        }
    }

    @Override
    public FileConfiguration getConfig() {
        config = ConfigUtil.loadConfig("config.yml", this);
        return config;
    }

    @Override
    public void reloadConfig() {
        getConfig();
    }

    @Override
    public void saveConfig() {
        ConfigUtil.saveConfig(config, "config.yml", this);
    }

    @Override
    public void saveDefaultConfig() {
        getConfig();
    }
}
