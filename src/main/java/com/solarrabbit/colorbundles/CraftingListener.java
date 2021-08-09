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
 *
 */

package com.solarrabbit.colorbundles;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.BundleMeta;
import dev.lone.itemsadder.api.CustomStack;

public class CraftingListener implements Listener {
    private ColorBundles plugin;

    public CraftingListener(ColorBundles plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent evt) {
        Recipe recipe = evt.getRecipe();
        if (recipe instanceof ShapelessRecipe) {
            String currentKey = ((ShapelessRecipe) recipe).getKey().getKey();
            List<String> recipeKeys = this.plugin.getRecipeKeys();
            for (int i = 0; i < recipeKeys.size(); i++) {
                if (currentKey.equals(recipeKeys.get(i))) {
                    prepareResult(evt);
                    break;
                }
            }
        }
    }

    private void prepareResult(PrepareItemCraftEvent evt) {
        Recipe recipe = evt.getRecipe();
        CraftingInventory inventory = evt.getInventory();

        HashMap<Integer, ? extends ItemStack> mapping = inventory.all(Material.BUNDLE);
        Integer[] array = mapping.keySet().toArray(new Integer[2]);
        // original bundle
        if (array[1] == null) {
            return;
        }
        ItemStack ingredientBundle = inventory.getItem(array[1]).clone();
        // #getResult returns a clone
        ItemStack originalResult = recipe.getResult();
        BundleMeta resultMeta = (BundleMeta) originalResult.getItemMeta();

        BundleMeta meta = (BundleMeta) ingredientBundle.getItemMeta();
        resultMeta.setItems(meta.getItems());
        if (meta.hasDisplayName() && !meta.getDisplayName().equals(this.getOriginalName(ingredientBundle))) {
            resultMeta.setDisplayName(meta.getDisplayName());
        }
        if (meta.hasLore()) {
            resultMeta.setLore(meta.getLore());
        }
        originalResult.setItemMeta(resultMeta);
        meta.getEnchants().forEach(originalResult::addUnsafeEnchantment);

        Bukkit.getScheduler().runTask(this.plugin, () -> {
            boolean anyPermitted = evt.getViewers().stream()
                    .anyMatch(human -> human.hasPermission("colorbundles.craft"));
            inventory.setResult(anyPermitted ? originalResult : null);
        });
    }

    /**
     * Returns the original name of the bundle item.
     * 
     * @param item ingredient bundle item
     * @return {@code null} if {@link ItemMeta#hasDisplayName()} returns
     *         {@code false}.
     */
    private String getOriginalName(ItemStack item) {
        if (this.plugin.hasItemsAdder() && CustomStack.byItemStack(item) != null) {
            return CustomStack.getInstance(CustomStack.byItemStack(item).getNamespacedID()).getDisplayName();
        } else if (!item.getItemMeta().hasCustomModelData()) {
            return null;
        } else {
            return this.plugin.getConfig().getKeys(false).stream()
                    .filter(str -> this.plugin.getConfig().getInt(str) == item.getItemMeta().getCustomModelData())
                    .findFirst()
                    .map(str -> ChatColor.WHITE + WordUtils.capitalize(str.replaceAll("_", " ")) + " Bundle")
                    .orElse(null);
        }
    }
}
