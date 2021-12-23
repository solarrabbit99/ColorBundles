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

public class DefaultCustomRecipeLoader extends CustomRecipeLoader {

    public DefaultCustomRecipeLoader(ColorBundles plugin) {
        super(plugin);
    }

    @Override
    public void loadRecipes() {
        for (CustomBundleConfig conf : plugin.getUserConfig().getCustomBundleConfigs()) {
            NamespacedKey key = new NamespacedKey(plugin, conf.getKey() + "_bundle");
            loadRecipe(conf.getDye(), conf.getBundle(), key);
        }
    }

}
