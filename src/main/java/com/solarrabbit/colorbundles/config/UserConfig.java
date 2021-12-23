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

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;

public class UserConfig {
    private final ConfigurationSection config;

    public UserConfig(ConfigurationSection config) {
        this.config = config;
    }

    public Set<CustomBundleConfig> getCustomBundleConfigs() {
        Set<String> keys = getCustomBundleConfigSection().getKeys(false);
        return keys.stream()
                .map(str -> new CustomBundleConfig(getCustomBundleConfigSection().getConfigurationSection(str)))
                .collect(Collectors.toSet());
    }

    public boolean hasCustomLoader() {
        return getExternalPluginsConfigSection().getBoolean("enabled", false);
    }

    public String getCustomLoaderPluginName() {
        return getExternalPluginsConfigSection().getString("plugin", null);
    }

    private ConfigurationSection getCustomBundleConfigSection() {
        return config.getConfigurationSection("custom");
    }

    private ConfigurationSection getExternalPluginsConfigSection() {
        return config.getConfigurationSection("other-plugins");
    }
}
