package com.paratopiamc.colorbundles;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import net.md_5.bungee.api.ChatColor;

public final class ColorBundles extends JavaPlugin {
    private List<String> recipeKeys;

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
        if (!this.getDataFolder().exists()) {
            try {
                this.getDataFolder().mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new CraftingListener(this), this);

        recipeKeys = new ArrayList<>();
        for (Dyes dye : Dyes.values()) {
            ItemStack item = new ItemStack(Material.BUNDLE);
            ItemMeta meta = item.getItemMeta();

            int modelData = getConfig().getInt(dye.toString());
            meta.setCustomModelData(modelData);

            item.setItemMeta(meta);
            NamespacedKey key = new NamespacedKey(this, dye + "_bundle");
            ShapelessRecipe recipe = new ShapelessRecipe(key, item);

            recipe.addIngredient(Material.BUNDLE);
            recipe.addIngredient(dye.getDye());

            Bukkit.addRecipe(recipe);
            recipeKeys.add(dye + "_bundle");

            getServer().getConsoleSender()
                    .sendMessage(ChatColor.GREEN + "[ColorBundles] Loaded recipes: " + dye + "_bundle");
        }
    }

    public List<String> getRecipeKeys() {
        return this.recipeKeys;
    }
}
