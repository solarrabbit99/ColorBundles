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
