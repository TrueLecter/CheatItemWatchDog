package truelecter.ciwg;

import lib.Attributes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class WatchDog extends JavaPlugin implements Listener {

	@EventHandler
	public void onInventory(InventoryEvent event) {
		ItemStack[] items = event.getInventory().getContents();
		for (ItemStack item : items) {
			if (item != null) {
				Attributes attr = new Attributes(item);
				attr.clear();
				item = attr.getStack();
			}
		}
	}

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}
}
