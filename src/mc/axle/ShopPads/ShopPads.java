package mc.axle.ShopPads;

import java.io.File;
import java.util.logging.Logger;


import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


public class ShopPads extends JavaPlugin {

	Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("[" + pdfFile.getName() + "] Version: " + pdfFile.getVersion() + " is now enabled!");
		try {
			firstRun();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getCommand("sp").setExecutor(new CommandExec(this));

	}

	private void firstRun() throws Exception {
		File file = new File(this.getDataFolder(), "config.yml");
		if (file.exists()) {

		} else {
			this.getConfig().set("Name", "ShopPads");
			this.getConfig().set("Shops.large.size", 15);
			this.getConfig().set("Shops.large.height", 15);
			this.getConfig().set("Shops.large.cost", 60000);

			this.getConfig().set("Shops.medium.size", 11);
			this.getConfig().set("Shops.medium.height", 11);
			this.getConfig().set("Shops.medium.cost", 40000);

			this.getConfig().set("Shops.small.size", 6);
			this.getConfig().set("Shops.small.height", 6);
			this.getConfig().set("Shops.small.cost", 20000);

		}
		this.saveConfig();
	}

}
