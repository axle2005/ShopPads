package mc.axle.ShopPads;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import mc.axle.ShopPads.Commands.createCommand;
import mc.axle.ShopPads.Commands.deleteCommand;
import org.bukkit.ChatColor;


public class CommandExec implements CommandExecutor {
	ShopPads plugin;

	//public AreaShop area;

	CommandExec(ShopPads plugin) {
		this.plugin = plugin;
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sp"))
			if (sender instanceof Player  && sender.hasPermission("ShopPads.access")) {
				Player player = (Player) sender;
				deleteCommand del = new deleteCommand(plugin);
				createCommand create = new createCommand(plugin);
				
				

				if (args.length > 0) {

					if (args[0].equalsIgnoreCase("delete")) {
						
						del.delete(args[1], player);
						
					}
					else if (args[0].equalsIgnoreCase("create")) {
						create.create(player, args);
								
						return true;
					}
				}

			} 
			//End's Player based commands. 
			else {
				sender.sendMessage(ChatColor.DARK_RED + "Nope...");
				return true;
			}
		return false;
	}


}
