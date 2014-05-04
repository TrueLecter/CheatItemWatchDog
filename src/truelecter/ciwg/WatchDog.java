package truelecter.ciwg;

import java.util.Map;

import lib.Attributes;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class WatchDog extends JavaPlugin implements Listener {

	private ItemStack removeNotValidEnchantments(ItemStack item) {
		Map<Enchantment, Integer> Enchants = item.getEnchantments();
		Map<Enchantment, Integer> newEnchants = item.getEnchantments();
		ItemStack newItem = new ItemStack(item.getType());
		for (Enchantment e: Enchants.keySet()){
			 if (!e.canEnchantItem(new ItemStack(item.getType()))){
				 continue;
			 }
			 if (!(Enchants.get(e)>e.getMaxLevel())){
				 continue;
			 }
			 newEnchants.put(e, Enchants.get(e));
		}
		newItem.setItemMeta(item.getItemMeta());
		newItem.setDurability(item.getDurability());
		newItem.setAmount(item.getAmount());
		newItem.addEnchantments(newEnchants);
		return newItem;
	}
	
	@EventHandler
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
}
