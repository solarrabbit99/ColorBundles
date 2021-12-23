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

package com.solarrabbit.colorbundles.config;

import com.solarrabbit.colorbundles.ColorBundles;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomBundleConfig {
    /**
     * The default {@link NamespacedKey}'s key used to store item's original name in
     * its {@link PersistentDataContainer}.
     */
    public static final String DEFAULT_NAME_KEY = "default-name";
    private final ConfigurationSection config;

    public CustomBundleConfig(ConfigurationSection config) {
        this.config = config;
    }

    public ItemStack getBundle() {
        ItemStack item = new ItemStack(Material.BUNDLE);
        ItemMeta meta = item.getItemMeta();

        int modelData = this.getCustomModelData();
        if (modelData != 0)
            meta.setCustomModelData(modelData);

        String name = this.getName();
        if (name != null)
            meta.setDisplayName(
                    ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', name));

        PersistentDataContainer prc = meta.getPersistentDataContainer();
        NamespacedKey nsk = new NamespacedKey(JavaPlugin.getPlugin(ColorBundles.class), DEFAULT_NAME_KEY);
        prc.set(nsk, PersistentDataType.STRING, name);

        item.setItemMeta(meta);
        return item;
    }

    public Material getDye() {
        String materialName = this.config.getString("dye", null);
        return materialName == null ? null : Material.matchMaterial(materialName);
    }

    public String getKey() {
        return config.getName();
    }

    private String getName() {
        return this.config.getString("name", null);
    }

    /**
     * Returns the model data set for this model in the configurations, or
     * {@code 0} if it is not set.
     *
     * @return model data if found, {@code 0} otherwise
     */
    private int getCustomModelData() {
        return this.config.getInt("model-data");
    }
}
