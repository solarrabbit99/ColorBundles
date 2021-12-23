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
