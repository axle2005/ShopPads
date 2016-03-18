package mc.axle.ShopPads.Commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

import mc.axle.ShopPads.ShopPads;
import mc.axle.ShopPads.WorldEditHandler;
import mc.axle.ShopPads.WorldGuardHandler;

public class createCommand {
	ShopPads plugin;
	
	public createCommand(ShopPads plugin)
	{
		this.plugin = plugin;
	}
	
	public void create(Player player, String [] args)
	{
		ProtectedCuboidRegion region = null;
		Location l = player.getLocation();
		Location l2 = player.getLocation();
		String fname = null;
		
		
		if (args[1].isEmpty()) {
			player.sendMessage("fail");
		}
		
		

		else {

			Location loc1 = loca1(l, args[1], player);
			Location loc2 = loca2(l2, player);
			fname = checkAreaFiles(args[1]);
			region = new ProtectedCuboidRegion(fname.toLowerCase(),
					new BlockVector(WorldEditHandler.convertToSk89qBV(loc2)), new BlockVector(WorldEditHandler.convertToSk89qBV(loc1)));

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

		placeSign(player, fname, getPrice(args[1]));
		//region = new ProtectedCuboidRegion(args[1].toString().toLowerCase(),
		//		new BlockVector(convertToSk89qBV(loc2)), new BlockVector(convertToSk89qBV(loc1)));
	
	
		
		
	
	
	}
	private String checkAreaFiles(String fname)
	{
		String newname = null;
		File file = new File(
				this.plugin.getDataFolder().getParentFile() + File.separator + "AreaShop" + File.separator + "regions",
				fname + "1.yml");
		if (file.exists()) {
			int i =1;
			while(file.exists())
			{
				newname= fname+""+i;
				file = new File(this.plugin.getDataFolder().getParentFile() + File.separator + "AreaShop" + File.separator + "regions",
						newname + ".yml");
				
			}
				
		} else {
		newname = fname+"1";
		
		}
		return newname;
	}

	
	private double getPrice(String value) {
		double price = plugin.getConfig().getInt("Shops." + value.toLowerCase() + ".cost");
		return price;
	}
	private String direction(Player player)
	{
		String direct = null;
		org.bukkit.util.Vector facing = player.getLocation().getDirection();
		
		
		if (facing.getX() > -0.5 && facing.getX() < 0.5) {
			if (facing.getZ() < -0.5 && facing.getZ() > -1.5) {
				direct = "North";
				
			} else if (facing.getZ() > 0.5 && facing.getZ() < 1.5) {
				//Works
				direct = "South";
				
			}
		} else if (facing.getX() > 0.5 && facing.getX() < 1.5) {
			if (facing.getZ() > -0.5 && facing.getZ() < 0.5) {
				//Works
				direct = "East";
				
			
			}
		} else if (facing.getX() < -0.5 && facing.getBlockX() > -1.5){
			if (facing.getZ() > -0.5 && facing.getZ() < 0.5) {
				//Works
				direct = "West";
				
			}
		}
		return direct;
	}
	private Location loca1(Location loc1, String value, Player player) {

		int size = plugin.getConfig().getInt("Shops." + value.toLowerCase() + ".size");
		int height = plugin.getConfig().getInt("Shops." + value.toLowerCase() + ".height");
		String direction = direction(player);
		if(direction == "North")
		{
			loc1.setX(loc1.getBlockX() - size);
			loc1.setZ(loc1.getBlockZ() - size -1);
		}
		else if(direction =="East")
		{
			loc1.setZ(loc1.getBlockZ() - size);
			loc1.setX(loc1.getBlockX() + size +1);
		}
		else if(direction =="South")
		{
			loc1.setX(loc1.getBlockX() + size);
			loc1.setZ(loc1.getBlockZ() + size +1);
		}
		else 
		{
			loc1.setZ(loc1.getBlockZ() + size);
			loc1.setX(loc1.getBlockX() - size -1);

		}
		
		
		
		loc1.setY(loc1.getBlockY() + height);
		

		return loc1;

	}

	private Location loca2(Location loc2, Player player) {
		String direction = direction(player);
		if(direction == "North")
		{
			
			loc2.setZ(loc2.getBlockZ() -1);
			loc2.setX(loc2.getBlockX());
		}
		else if(direction =="East")
		{
		
			loc2.setX(loc2.getBlockX() +1);
			loc2.setZ(loc2.getBlockZ());
		}
		else if(direction =="South")
		{
		
			loc2.setZ(loc2.getBlockZ() +1);
			loc2.setX(loc2.getBlockX());
		}
		else 
		{
			loc2.setZ(loc2.getBlockZ());
			loc2.setX(loc2.getBlockX() -1);

		}

		//-3 Places the bottom block 3 below player to allow the Bedrock and Air space. 
		loc2.setY(loc2.getBlockY() - 3);


		return loc2;

	}
	
	private BlockFace setSignDirection(String facing) {
		BlockFace face = null;
		//org.bukkit.util.Vector facing = player.getLocation().getDirection();
		if(facing == "North")
		{
			face = BlockFace.SOUTH;
		}
		else if(facing =="East")
		{
			face = BlockFace.WEST;
		}
		else if(facing =="South")
		{
			face = BlockFace.NORTH;
		}
		else 
		{
			face = BlockFace.EAST;
		}
		
		return face;
	}
	
	private void placeSign(Player player, String rname, Double cost) {
		SignChangeEvent event;

		Location loc = player.getLocation();
		World world = player.getWorld();
		
		String direction = direction(player);
		Block block1 = null;
		
		if(direction == "North")
		{
			block1 = world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()-1);
		}
		else if(direction =="East")
		{
			block1 = world.getBlockAt(loc.getBlockX()+1, loc.getBlockY(), loc.getBlockZ());
		}
		else if(direction =="South")
		{
			block1 = world.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()+1);
		}
		else 
		{
			block1 = world.getBlockAt(loc.getBlockX()-1, loc.getBlockY(), loc.getBlockZ());
		}
		player.sendMessage(direction);
		block1.setType(Material.SIGN_POST);

		Sign sig = (Sign) block1.getState();
		// block1.setData((byte) 0x0);

		org.bukkit.material.Sign matSign = new org.bukkit.material.Sign(Material.SIGN_POST);
		BlockFace face = setSignDirection(direction);
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
}
