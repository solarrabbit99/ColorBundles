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

package com.solarrabbit.colorbundles.listener;

import java.util.Optional;
import java.util.stream.Stream;

import com.solarrabbit.colorbundles.ColorBundles;
import com.solarrabbit.colorbundles.config.CustomBundleConfig;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import dev.lone.itemsadder.api.CustomStack;

public class CraftingListener implements Listener {
    private ColorBundles plugin;

    public CraftingListener(ColorBundles plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent evt) {
        Recipe recipe = evt.getRecipe();
        if (!plugin.isCustomRecipe(recipe))
            return;
        boolean hasPerm = evt.getViewers().stream()
                .anyMatch(human -> human.hasPermission("colorbundles.craft"));
        CraftingInventory inventory = evt.getInventory();
        Bukkit.getScheduler().runTask(this.plugin,
                () -> inventory.setResult(hasPerm ? getResult(recipe, inventory) : null));
    }

    private ItemStack getResult(Recipe recipe, CraftingInventory inventory) {
        ItemStack ingredientBundle = Stream.of(inventory.getMatrix())
                .filter(item -> item != null && item.getType() == Material.BUNDLE).findFirst().orElse(null);
        ItemStack result = recipe.getResult(); // Recipe#getResult() returns a clone
        BundleMeta resultMeta = (BundleMeta) result.getItemMeta();

        BundleMeta ingredientMeta = (BundleMeta) ingredientBundle.getItemMeta();
        resultMeta.setItems(ingredientMeta.getItems());
        if (!hasOriginalName(ingredientMeta))
            resultMeta.setDisplayName(ingredientMeta.getDisplayName());
        if (ingredientMeta.hasLore())
            resultMeta.setLore(ingredientMeta.getLore());
        result.setItemMeta(resultMeta);
        ingredientMeta.getEnchants().forEach(result::addUnsafeEnchantment);

        return result;
    }

    private boolean hasOriginalName(ItemMeta meta) {
        if (!meta.hasDisplayName())
            return true;
        return meta.getDisplayName() == getOriginalName(meta);
    }

    private String getOriginalName(ItemMeta meta) {
        if (plugin.getUserConfig().hasCustomLoader()) {
            ItemStack item = new ItemStack(Material.BUNDLE);
            item.setItemMeta(meta);
            return Optional.ofNullable(CustomStack.byItemStack(item)).map(CustomStack::getDisplayName).orElse(null);
        }
        PersistentDataContainer prc = meta.getPersistentDataContainer();
        NamespacedKey nsk = new NamespacedKey(JavaPlugin.getPlugin(ColorBundles.class),
                CustomBundleConfig.DEFAULT_NAME_KEY);
        return prc.get(nsk, PersistentDataType.STRING);
    }
}
