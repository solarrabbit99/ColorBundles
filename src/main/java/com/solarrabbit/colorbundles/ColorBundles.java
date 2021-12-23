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

package com.solarrabbit.colorbundles;

import java.util.HashSet;
import java.util.Set;

import com.solarrabbit.colorbundles.config.UserConfig;
import com.solarrabbit.colorbundles.listener.CraftingListener;
import com.solarrabbit.colorbundles.loader.CustomRecipeLoader;
import com.solarrabbit.colorbundles.loader.DefaultCustomRecipeLoader;
import com.solarrabbit.colorbundles.loader.ItemsAdderCustomRecipeLoader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ColorBundles extends JavaPlugin {
    private UserConfig userConfig;
    private Set<NamespacedKey> customRecipeKeys;
    private CommandSender console;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        userConfig = new UserConfig(getConfig());
        customRecipeKeys = new HashSet<>();
        console = getServer().getConsoleSender();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new CraftingListener(this), this);

        loadDefaultBundleRecipe();
        CustomRecipeLoader loader = new DefaultCustomRecipeLoader(this);
        if (userConfig.hasCustomLoader()) {
            String pluginName = userConfig.getCustomLoaderPluginName();
            if (pluginManager.getPlugin(pluginName) == null)
                console.sendMessage(
                        ChatColor.GOLD + "[ColorBundles] " + pluginName
                                + " cannot be found! Using default loader instead...");
            else
                switch (pluginName) {
                    case "ItemsAdder":
                        console.sendMessage(
                                ChatColor.AQUA + "[ColorBundles] " + pluginName
                                        + " detected! Waiting for ItemsAdder to load items...");
                        loader = new ItemsAdderCustomRecipeLoader(this);
                        break;
                    default:
                        console.sendMessage(
                                ChatColor.AQUA + "[ColorBundles] " + pluginName
                                        + " is not supported! Using default loader instead...");
                        break;
                }
        }

        loader.loadRecipes();
    }

    public void addCustomRecipeKey(NamespacedKey key) {
        customRecipeKeys.add(key);
    }

    public boolean isCustomRecipe(Recipe recipe) {
        if (!(recipe instanceof ShapelessRecipe))
            return false;
        return customRecipeKeys.contains(((ShapelessRecipe) recipe).getKey());
    }

    public UserConfig getUserConfig() {
        return userConfig;
    }

    private void loadDefaultBundleRecipe() {
        NamespacedKey key = new NamespacedKey(this, "bundle");
        if (Bukkit.getRecipe(key) != null)
            return;

        ShapedRecipe recipe = new ShapedRecipe(key, new ItemStack(Material.BUNDLE));
        recipe.shape("SRS", "R R", "RRR");
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('R', Material.RABBIT_HIDE);

        Bukkit.addRecipe(recipe);
        console.sendMessage(ChatColor.GREEN + "[ColorBundles] Loaded recipe: " + key.getKey());
    }
}
