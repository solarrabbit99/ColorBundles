package com.solarrabbit.colorbundles.loader;

import com.solarrabbit.colorbundles.ColorBundles;
import com.solarrabbit.colorbundles.config.CustomBundleConfig;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;

public class ItemsAdderCustomRecipeLoader extends CustomRecipeLoader implements Listener {

    public ItemsAdderCustomRecipeLoader(ColorBundles plugin) {
        super(plugin);
    }

    @Override
    public void loadRecipes() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemsLoadEvent(ItemsAdderLoadDataEvent evt) {
        for (CustomBundleConfig conf : plugin.getUserConfig().getCustomBundleConfigs()) {
            NamespacedKey key = new NamespacedKey(plugin, conf.getKey() + "_bundle");
            ItemStack item = CustomStack.getInstance("colorbundles:" + conf.getKey() +
                    "_bundle").getItemStack();
            loadRecipe(conf.getDye(), item, key);
        }
    }

}
