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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;

public final class ColorBundles extends JavaPlugin implements Listener {
    private List<String> recipeKeys;
    private boolean hasItemsAdder;

    private static enum Dyes {
        BLACK("black"), BLUE("blue"), BROWN("brown"), CYAN("cyan"), GRAY("gray"), GREEN("green"),
        LIGHT_BLUE("light_blue"), LIGHT_GRAY("light_gray"), LIME("lime"), MAGENTA("magenta"), ORANGE("orange"),
        PINK("pink"), PURPLE("purple"), RED("red"), WHITE("white"), YELLOW("yellow");

        private Material dye;
        private String name;

        Dyes(String name) {
            this.dye = Material.matchMaterial(name.toUpperCase() + "_DYE");
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        private Material getDye() {
            return this.dye;
        }
    }

    @Override
    public void onEnable() {
        this.hasItemsAdder = this.getServer().getPluginManager().getPlugin("ItemsAdder") != null;
        if (this.hasItemsAdder) {
            getServer().getConsoleSender().sendMessage(
                    ChatColor.AQUA + "[ColorBundles] ItemsAdder detected! Waiting for ItemsAdder to load items...");
        } else {
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD
                    + "[ColorBundles] Ignore the error message regarding plugin failing to register ItemsAdder's events if you do not have ItemsAdder installed...");
        }

        if (!this.getDataFolder().exists()) {
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new CraftingListener(this), this);
        getServer().getPluginManager().registerEvents(this, this);

        loadDefaultBundleRecipe();

        recipeKeys = new ArrayList<>();
        if (!this.hasItemsAdder) {
            for (Dyes dye : Dyes.values()) {
                ItemStack item = new ItemStack(Material.BUNDLE);
                ItemMeta meta = item.getItemMeta();
                int modelData = getConfig().getInt(dye.toString());
                meta.setCustomModelData(modelData);
                meta.setDisplayName(
                        ChatColor.WHITE + WordUtils.capitalize(dye.toString().replaceAll("_", " ")) + " Bundle");
                item.setItemMeta(meta);

                loadRecipe(dye, item);
                recipeKeys.add(dye + "_bundle");
            }
        }
    }

    @EventHandler
    public void onItemsLoadEvent(ItemsAdderLoadDataEvent evt) {
        for (Dyes dye : Dyes.values()) {
            ItemStack item = CustomStack.getInstance("colorbundles:" + dye + "_bundle").getItemStack();
            loadRecipe(dye, item);
            recipeKeys.add(dye + "_bundle");
        }
    }

    private void loadDefaultBundleRecipe() {
        NamespacedKey key = new NamespacedKey(this, "bundle");
        if (Bukkit.getRecipe(key) == null && Bukkit.getVersion().contains("1.17")) { // not yet loaded
            ShapedRecipe recipe = new ShapedRecipe(key, new ItemStack(Material.BUNDLE));
            recipe.shape("SRS", "R R", "RRR");
            recipe.setIngredient('S', Material.STRING);
            recipe.setIngredient('R', Material.RABBIT_HIDE);

            Bukkit.addRecipe(recipe);
        }
    }

    private void loadRecipe(Dyes dye, ItemStack result) {
        NamespacedKey key = new NamespacedKey(this, dye + "_bundle");
        if (Bukkit.getRecipe(key) == null) { // not yet loaded
            ShapelessRecipe recipe = new ShapelessRecipe(key, result);
            recipe.addIngredient(Material.BUNDLE);
            recipe.addIngredient(dye.getDye());

            Bukkit.addRecipe(recipe);
            getServer().getConsoleSender()
                    .sendMessage(ChatColor.GREEN + "[ColorBundles] Loaded recipes: " + dye + "_bundle");
        }
    }

    public List<String> getRecipeKeys() {
        return this.recipeKeys;
    }

    public boolean hasItemsAdder() {
        return this.hasItemsAdder;
    }
}
