package truelecter.ciwg;

import java.util.Map;
import java.util.logging.Logger;

import lib.Attributes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class WatchDog extends JavaPlugin implements Listener {
	public Logger logger = Logger.getLogger("Minecraft");
	public String prefix = ChatColor.GRAY + "[" + ChatColor.GREEN + "CIWD" + ChatColor.GRAY + "] "
			+ ChatColor.RESET;

	public String getDeveloper() {
		String DEV = "";
		DEV = ChatColor.RED + "" + ChatColor.MAGIC + "!" + ChatColor.WHITE + "_" + ChatColor.WHITE + "T"
				+ ChatColor.AQUA + "r" + ChatColor.AQUA + "u" + ChatColor.BLUE + "e" + ChatColor.BLUE + "L"
				+ ChatColor.BLUE + "e" + ChatColor.BLUE + "c" + ChatColor.AQUA + "t" + ChatColor.AQUA + "e"
				+ ChatColor.WHITE + "r" + ChatColor.WHITE + "_" + ChatColor.RED + "" + ChatColor.MAGIC + "!";
		return DEV;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		PluginDescriptionFile pdfFile = getDescription();
		if (cmd.getName().equalsIgnoreCase("ciwd")) {
			sender.sendMessage(ChatColor.DARK_GREEN + this.prefix + "v" + pdfFile.getVersion() + ChatColor.GREEN
					+ ChatColor.BOLD + " �� " + getDeveloper());
		}
		return true;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		ItemStack item = p.getItemInHand();
		if (checkAttributes(item)) {
			item = removeAttributes(item);
			event.setCancelled(true);
			toConsoleAttr(p.getName());
		}
		p.getEquipment().setHelmet(removeAttributes(p.getEquipment().getHelmet()));
		if (!item.getEnchantments().isEmpty()) {
			boolean isHave = false;
			for (Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet()) {
				if (((Integer) ench.getValue()).intValue() > 5) {
					p.getItemInHand().removeEnchantment((Enchantment) ench.getKey());
					isHave = true;
				}
			}
			if (isHave) {
				event.setCancelled(true);
				toConsoleEnch(p.getName());
			}
		}
	}

	public ItemStack removeAttributes(ItemStack item) {
		if (item == null) {
			return item;
		}
		if (item.getType() == Material.AIR) {
			return item;
		}
		Attributes attr = new Attributes(item);
		attr.clear();
		return attr.getStack();
	}

	public boolean checkAttributes(ItemStack item) {
		if (item == null) {
			return false;
		}
		if (item.getType().equals(Material.AIR)) {
			return false;
		}
		Attributes attr = new Attributes(item);
		return attr.values().iterator().hasNext();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		ItemStack item = event.getCursor();
		if (checkAttributes(item)) {
			item = removeAttributes(item);
			event.setCancelled(true);
			toConsoleAttr(event.getWhoClicked().getName());
		}
		event.getWhoClicked().getEquipment()
				.setHelmet(removeAttributes(event.getWhoClicked().getEquipment().getHelmet()));
		if (event.getCurrentItem() == null) {
			return;
		}
		if (!item.getEnchantments().isEmpty()) {
			boolean isHave = false;
			for (Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet()) {
				if (((Integer) ench.getValue()).intValue() > 5) {
					item.removeEnchantment((Enchantment) ench.getKey());
					isHave = true;
				}
			}
			if (isHave) {
				event.setCancelled(true);
				event.getInventory().setItem(event.getSlot(), item);
				toConsoleEnch(event.getWhoClicked().getName());
			}
		}
	}
	
	private void toConsoleEnch(String name) {
		this.logger.warning(ChatColor.RED + "Wrong enchantments cleared! [" + name + "]");
	}

	private void toConsoleAttr(String name) {
		this.logger.warning(ChatColor.RED + "Wrong attributes cleared! [" + name + "]");
	}

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
}
