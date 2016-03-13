package mc.axle.ShopPads.Commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mc.axle.ShopPads.ShopPads;
import mc.axle.ShopPads.WorldGuardHandler;

public class deleteCommand {
	ShopPads plugin;
	
	public deleteCommand(ShopPads plugin)
	{
		this.plugin = plugin;
	}
	public void delete (String fname, Player player){
		this.plugin = plugin;
		// Deletes Files
				boolean deleted = true;
				File file = new File(
						this.plugin.getDataFolder().getParentFile() + File.separator + "AreaShop" + File.separator + "regions",
						fname + ".yml");
				if (file.exists()) {
					try {
						deleted = file.delete();
					} catch (Exception e) {
						deleted = false;
					}
					if (!deleted) {

					}
					WorldGuardHandler.getWorldGuard().getRegionManager(player.getWorld()).removeRegion(fname);
				}
				Bukkit.dispatchCommand(this.plugin.getServer().getConsoleSender(), "as reload");
	}
}
