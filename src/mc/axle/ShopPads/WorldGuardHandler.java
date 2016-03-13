package mc.axle.ShopPads;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class WorldGuardHandler {

	@SuppressWarnings("deprecation")
	public
	static void editRegion(int block, ProtectedCuboidRegion region, Player player, int layer) {
		//WorldEditPlugin we = WorldEditHandler.getWorldEdit();
		Polygonal2DRegion weRegion = new Polygonal2DRegion(BukkitUtil.getLocalWorld(player.getWorld()),
				region.getPoints(), region.getMinimumPoint().getBlockY() + layer,
				region.getMinimumPoint().getBlockY() + layer);
		EditSession editSession = WorldEdit.getInstance().getEditSessionFactory()
				.getEditSession(BukkitUtil.getLocalWorld(player.getWorld()), -1);
		try {
			editSession.setBlocks(weRegion, new BaseBlock(block));
		} catch (MaxChangedBlocksException e) {
			// As of the blocks are unlimited this should not be called
		}

	}

	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null; // Maybe you want throw an exception instead
		}

		return (WorldGuardPlugin) plugin;
	}
}
