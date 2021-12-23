/*
 *  This file is part of ColorBundles. Copyright (c) 2021 SolarRabbit.
 *
 *  ColorBundles is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ColorBundles is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ColorBundles. If not, see <https://www.gnu.org/licenses/>.
 */

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
