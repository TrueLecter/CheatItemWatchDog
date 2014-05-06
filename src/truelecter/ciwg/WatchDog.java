package truelecter.ciwg;

import java.util.Map;

import lib.Attributes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class WatchDog extends JavaPlugin implements Listener {
	private boolean naturalLevels = true;
	private int maxLevel = 10;
	private boolean allowEnchants = true;
	private String prefix = "[&3CIWD&r] ";
	private String notEnoughArguments = "&4Not enough arguments!&r";
	private String accessDenied = "&4Not enough permissions!&r";
	private String reloadComplete = "&2Succesfully reloaded&r";
	private String consoleReloadNootify = "&2Reloaded config by &6$PLAYERNAME$&r";

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ciwd")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor
						.translateAlternateColorCodes(
								'&',
								prefix
										+ "&rBy &6_TrueLecter_&r for &2Ran&1dom&6Cra&4ft&r :)"));
				return true;
			}
			if (!sender.hasPermission("watchdog.admin")) {
				if (accessDenied != "")
					sender.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', accessDenied));
				return true;
			}
			if (args.length < 2) {
				if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
					this.reloadConfig();
					this.loadSettings();
					sender.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', reloadComplete));
					if (sender instanceof Player)
						this.getServer()
								.getConsoleSender()
								.sendMessage(
										ChatColor.translateAlternateColorCodes(
												'&',
												consoleReloadNootify
														.replaceAll(
																"$PLAYERNAME$",
																sender.getName())));
					return true;
				}
				if (notEnoughArguments != "")
					sender.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', notEnoughArguments));
				return true;
			}

			return true;
		}
		return false;
	}

	private ItemStack removeNotValidEnchantments(ItemStack item) {
		Map<Enchantment, Integer> Enchants = item.getEnchantments();
		Map<Enchantment, Integer> newEnchants = item.getEnchantments();
		ItemStack newItem = new ItemStack(item.getType());
		for (Enchantment e : Enchants.keySet()) {
			if (!allowEnchants) {
				continue;
			}
			if (!e.canEnchantItem(new ItemStack(item.getType()))) {
				if (naturalLevels)
					continue;
			}
			if (e.getMaxLevel() > maxLevel)
				item.getEnchantments().put(e, maxLevel);
			newEnchants.put(e, Enchants.get(e));
		}
		newItem.setItemMeta(item.getItemMeta());
		newItem.setDurability(item.getDurability());
		newItem.setAmount(item.getAmount());
		newItem.addEnchantments(newEnchants);
		return newItem;
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventory(InventoryEvent event) {
		ItemStack[] items = event.getInventory().getContents();
		for (ItemStack item : items) {
			if (item != null) {
				Attributes attr = new Attributes(item);
				attr.clear();
				item = removeNotValidEnchantments(attr.getStack());
			}
		}
	}

	private void loadSettings() {
		this.saveDefaultConfig();
		naturalLevels = this.getConfig().getBoolean("enchants.naturalLevels",
				true);
		maxLevel = this.getConfig().getInt("enchants.maxlevel", 10);
		allowEnchants = this.getConfig().getBoolean("enchants.naturalLevels",
				true);
		prefix = this.getConfig().getString("locale.prefix", prefix);
		notEnoughArguments = prefix.concat(this.getConfig().getString(
				"locale.notEnoughArguments", notEnoughArguments));
		accessDenied = prefix.concat(this.getConfig().getString(
				"locale.accessDenied", accessDenied));
		reloadComplete = prefix.concat(this.getConfig().getString(
				"locale.reloadComplete", reloadComplete));
		consoleReloadNootify = prefix.concat(this.getConfig().getString(
				"locale.consoleReloadNootify", consoleReloadNootify));
	}

	public void onEnable() {
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);
		loadSettings();
	}
}
