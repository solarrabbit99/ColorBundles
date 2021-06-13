/*
 *  This file is part of ColorBundles. Copyright (c) 2021 Paratopia.
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

package com.paratopiamc.colorbundles;

import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftingListener implements Listener {
    private ColorBundles plugin;

    public CraftingListener(ColorBundles plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent evt) {
        Recipe recipe = evt.getRecipe();
        if (recipe instanceof ShapelessRecipe) {
            ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
            String currentKey = shapeless.getKey().getKey();
            List<String> recipeKeys = this.plugin.getRecipeKeys();
            for (int i = 0; i < recipeKeys.size(); i++) {
                if (currentKey.equals(recipeKeys.get(i))) {
                    ItemStack originalResult = shapeless.getResult();
                    int customModelData = originalResult.getItemMeta().getCustomModelData();

                    CraftingInventory inventory = evt.getInventory();
                    HashMap<Integer, ? extends ItemStack> mapping = inventory.all(Material.BUNDLE);
                    Integer[] array = mapping.keySet().toArray(new Integer[2]);
                    ItemStack finalResult = inventory.getItem(array[1]).clone();

                    ItemMeta meta = finalResult.getItemMeta();
                    meta.setCustomModelData(customModelData);
                    finalResult.setItemMeta(meta);

                    inventory.setResult(finalResult);
                    break;
                }
            }
        }
    }
}
