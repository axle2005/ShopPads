package mc.axle.ShopPads;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class WorldEditHandler {

	static WorldEditPlugin getWorldEdit() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
			return null; // Maybe you want throw an exception instead
		}

		return (WorldEditPlugin) plugin;
	}

}
