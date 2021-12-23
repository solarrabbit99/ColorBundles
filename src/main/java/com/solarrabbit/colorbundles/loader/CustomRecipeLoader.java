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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public abstract class CustomRecipeLoader {
    protected final ColorBundles plugin;

    protected CustomRecipeLoader(ColorBundles plugin) {
        this.plugin = plugin;
    }

    public abstract void loadRecipes();

    protected void loadRecipe(Material dye, ItemStack result, NamespacedKey key) {
        if (Bukkit.getRecipe(key) != null)
            return;

        ShapelessRecipe recipe = new ShapelessRecipe(key, result);
        recipe.addIngredient(Material.BUNDLE);
        recipe.addIngredient(dye);

        Bukkit.addRecipe(recipe);
        plugin.addCustomRecipeKey(key);

        plugin.getServer().getConsoleSender()
                .sendMessage(ChatColor.GREEN + "[ColorBundles] Loaded recipe: " + key.getKey());
    }
}
