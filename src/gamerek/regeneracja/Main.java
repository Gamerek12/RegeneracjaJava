package gamerek.regeneracja;

import gamerek.regeneracja.helpers.Regenerate;
import gamerek.regeneracja.helpers.Terrain;
import gamerek.regeneracja.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    private List<Terrain> toRegenerate = new ArrayList<Terrain>();

    @Override
    public void onEnable() {
        getCommand("regeneruj").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        Inventory inventory = Bukkit.createInventory(null, 5*9, "Regeneracja");

        int i = 0;
        for (Iterator<Terrain> it = toRegenerate.iterator(); it.hasNext();) {
            Terrain terrain = it.next();
            Location location = (Location) terrain.getLocation();

            ItemStack itemStack = new ItemStack(Material.GRASS, 1);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(Arrays.asList(
                    ChatUtil.fix("&cData utworzenia: &7" + new SimpleDateFormat("YYYY-mm-dd HH:mm:ss").format(new Date(terrain.getTime()))),
                    ChatUtil.fix("&cLokalizacja wybuchu: &7x=" + location.getBlockX() + " y=" + location.getBlockY() + " z=" + location.getBlockZ())
            ));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(i, itemStack);
            i++;
        }
        player.openInventory(inventory);

        return true;
    }

    @EventHandler
    public void onExplode(final EntityExplodeEvent event) {
        List<BlockState> states = new ArrayList<BlockState>();
        for (Block block : event.blockList())
            states.add(block.getState());

        Terrain terrain = new Terrain(states, event.getLocation(), System.currentTimeMillis());
        toRegenerate.add(terrain);

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory() == null || event.getCurrentItem() == null || event.getSlotType().equals(InventoryType.SlotType.OUTSIDE))
            return;

        if (!event.getView().getTitle().equalsIgnoreCase("Regeneracja"))
            return;

        Inventory inventory = event.getInventory();
        event.setCancelled(true);

        final Player player = (Player) event.getWhoClicked();

        int index = event.getSlot();
        if (index+1 > toRegenerate.size())
            return;

        inventory.removeItem(inventory.getItem(index));

        player.closeInventory();
        player.openInventory(inventory);

        Terrain terrain = toRegenerate.get(index);
        toRegenerate.remove(index);

        new Regenerate(player, terrain).runTaskTimer(this, 8L, 5L);
    }
}
