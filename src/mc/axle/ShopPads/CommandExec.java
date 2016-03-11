package mc.axle.ShopPads;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BlockID;

import nl.evolutioncoding.areashop.*;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class CommandExec implements CommandExecutor {
	ShopPads plugin;

	public AreaShop area;

	CommandExec(ShopPads plugin) {
		this.plugin = plugin;
	}

	public static BlockVector convertToSk89qBV(Location location) {
		return (new BlockVector(location.getX(), location.getY(), location.getZ()));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// AreaShop area = new AreaShop();
		if (cmd.getName().equalsIgnoreCase("sp"))
			if (sender instanceof Player) {
				Player player = (Player) sender;

				Location l = player.getLocation();
				Location l2 = player.getLocation();

				if (args.length > 0) {

					if (args[0].equalsIgnoreCase("delete")) {
						deleteAreaShop(args[1]);
					}

					else if (args[0].equalsIgnoreCase("create")) {
						ProtectedCuboidRegion region = null;
						if (args[1].isEmpty()) {
							sender.sendMessage("fail");
						}

						else {
							// Location loc4 =
							// player.getLocation().getDirection()
							Location loc1 = loca1(l, args[1]);
							Location loc2 = loca2(l2);

							region = new ProtectedCuboidRegion(args[1].toString().toLowerCase(),
									new BlockVector(convertToSk89qBV(loc2)), new BlockVector(convertToSk89qBV(loc1)));

						}

						// DefaultDomain owners = new DefaultDomain();
						// owners.addPlayer(getWorldGuard().wrapPlayer(player));
						// region.setOwners(owners);

						WorldGuardHandler.getWorldGuard().getRegionManager(player.getWorld()).addRegion(region);
						region.setPriority(1);

						WorldGuardHandler.editRegion(BlockID.BEDROCK, region, player, 0);
						WorldGuardHandler.editRegion(BlockID.AIR, region, player, 1);

						if (args.length == 2) {
							WorldGuardHandler.editRegion(BlockID.STONE_BRICK, region, player, 2);
						} else {
							int y = Integer.parseInt(args[2].toString());
							WorldGuardHandler.editRegion(y, region, player, 2);
						}

						placeSign(player, args[1],
								plugin.getConfig().getDouble("Shops." + args[1].toLowerCase() + ".cost"));

						return true;
					}
				}

			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Nope...");
				return true;
			}
		return false;
	}

	private double getPrice(String value) {
		double price = plugin.getConfig().getInt("Shops." + value.toLowerCase() + ".cost");
		return price;
	}

	private Location loca1(Location loc1, String value) {

		int size = plugin.getConfig().getInt("Shops." + value.toLowerCase() + ".size");
		int height = plugin.getConfig().getInt("Shops." + value.toLowerCase() + ".height");

		loc1.setX(loc1.getX() + size - 1);
		loc1.setY(loc1.getY() + height);
		loc1.setZ(loc1.getZ() + size - 1);

		return loc1;

	}

	private Location loca2(Location loc2) {

		loc2.setX(loc2.getX());
		loc2.setY(loc2.getY() - 3);
		loc2.setZ(loc2.getZ());

		return loc2;

	}

	private void placeSign(Player player, String rname, Double cost) {
		SignChangeEvent event;

		Location loc = player.getLocation();
		World world = player.getWorld();
		Block block1 = world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		block1.setType(Material.SIGN_POST);

		Sign sig = (Sign) block1.getState();
		// block1.setData((byte) 0x0);

		org.bukkit.material.Sign matSign = new org.bukkit.material.Sign(Material.SIGN_POST);
		BlockFace face = setSignDirection(player);
		matSign.setFacingDirection(face); // TODO edit to what you want
		// setSignDirection(player);

		sig.setLine(0, "[buy]");
		sig.setLine(1, rname);
		sig.setLine(2, "" + cost);

		sig.setData(matSign);

		sig.update();

		// Fire SignChangeEvent so AreaShop picks it up.
		event = new SignChangeEvent(block1, player, sig.getLines());
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	private BlockFace setSignDirection(Player player) {
		BlockFace face = null;
		org.bukkit.util.Vector facing = player.getLocation().getDirection();

		player.sendMessage("" + facing.getX());
		// player.sendMessage(""+facing.get);
		player.sendMessage("" + facing.getZ());
		if (facing.getX() > -0.5 && facing.getX() < 0.5) {
			/*
			 * 0,-1 North -1,-1 West -1,0 South 0,0 East
			 * 
			 * 
			 */
			if (facing.getZ() > -0.5 && facing.getZ() < 0.5) {
				face = BlockFace.WEST;
			} else if (facing.getZ() > 0.5 && facing.getZ() < 1.5) {
				face = BlockFace.NORTH;
			}
		} else if (facing.getX() > 0.5 && facing.getX() < 1.5) {
			if (facing.getZ() == 0) {
				face = BlockFace.NORTH;
			} else if (facing.getZ() == 1) {

			} else if (facing.getZ() > -0.5 && facing.getZ() < 0.5) {
				face = BlockFace.WEST;
			}
		}

		return face;
	}

	// Used to manually create AreaShop files.
	/*private void createAreaShop(String fname, Player player, CommandSender sender) {
		File file = new File(
				this.plugin.getDataFolder().getParentFile() + File.separator + "AreaShop" + File.separator + "regions",
				fname + ".yml");
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("general.name", fname);
		config.set("general.world", player.getWorld().getName());
		config.set("general.type", "buy");
		config.set("general.signs.'0'.location.world", player.getWorld().getName());
		config.set("general.signs.'0'.location.x", player.getLocation().getBlockX());
		config.set("general.signs.'0'.location.y", player.getLocation().getBlockY());
		config.set("general.signs.'0'.location.z", player.getLocation().getBlockY());
		config.set("general.signs.'0'.facing", "NORTH");
		config.set("general.signs.'0'.signType", "SIGN_POST");
		config.set("buy.price", getPrice(fname));
		try {
			config.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Bukkit.dispatchCommand(this.plugin.getServer().getConsoleSender(), "as reload");
	}
	*/
	// Used to delete AreaShop files
	private void deleteAreaShop(String fname) {
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
		}
		Bukkit.dispatchCommand(this.plugin.getServer().getConsoleSender(), "as reload");
	}
	/*
	 * private WorldEditPlugin getWorldEdit() { Plugin plugin =
	 * Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	 * 
	 * //WorldGuard may not be loaded if (plugin == null || !(plugin instanceof
	 * WorldEditPlugin)) { return null; // Maybe you want throw an exception
	 * instead }
	 * 
	 * return (WorldEditPlugin) plugin; }
	 */

	/*
	 * private WorldGuardPlugin getWorldGuard() { Plugin plugin =
	 * Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	 * 
	 * //WorldGuard may not be loaded if (plugin == null || !(plugin instanceof
	 * WorldGuardPlugin)) { return null; // Maybe you want throw an exception
	 * instead }
	 * 
	 * return (WorldGuardPlugin) plugin; }
	 */

	/*
	 * private void editRegion(int block, ProtectedCuboidRegion region, Player
	 * player, int layer){ WorldEditPlugin we = WorldEditHandler.getWorldEdit();
	 * 
	 * @SuppressWarnings("deprecation") Polygonal2DRegion weRegion = new
	 * Polygonal2DRegion(BukkitUtil.getLocalWorld(player.getWorld()),
	 * region.getPoints(), region.getMinimumPoint().getBlockY()+layer,
	 * region.getMinimumPoint().getBlockY()+layer); EditSession editSession =
	 * WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitUtil
	 * .getLocalWorld(player.getWorld()), -1); try {
	 * editSession.setBlocks(weRegion, new BaseBlock(block)); } catch
	 * (MaxChangedBlocksException e) { // As of the blocks are unlimited this
	 * should not be called }
	 * 
	 * }
	 */

}
