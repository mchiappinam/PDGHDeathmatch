/**
 * Copyright PDGH Minecraft Servers & HostLoad © 2013-XXXX
 * Todos os direitos reservados
 * Uso apenas para a PDGH.com.br e https://HostLoad.com.br
 * Caso vocêtenha acesso a esse sistema, você é privilegiado!
*/

package me.mchiappinam.pdghdeathmatch;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
//import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Listeners implements Listener {
	private Main plugin;

	public Listeners(Main main) {
		plugin = main;
	}

	/**
	 * if(getConfig().getBoolean("sistema.itens.clearInv")) clearInv(p);
	 * if(getConfig().getBoolean("sistema.itens.darItens")) { Kit(p);
	 * p.updateInventory(); }
	 */

	@EventHandler
	private void onRespawn(PlayerRespawnEvent e) {
		if (!e.getPlayer().isOnline())
			return;
		if (!plugin.participantes.contains(e.getPlayer().getName()))
			return;
		//if (plugin.getDeathmatchEtapa() == 3) {
		e.setRespawnLocation(e.getPlayer().getLocation().add(0.0, 1.5, 0.0));
		if (e.getPlayer().hasPermission("pdgh.vip"))
			plugin.respawndelay.put(e.getPlayer(), 5*10);
		else
			plugin.respawndelay.put(e.getPlayer(), 15*10);
		//}
	}

	@EventHandler
	private void onDeath(PlayerDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			Player killer = e.getEntity().getKiller();
			if(plugin.participantes.contains(killer.getName())&&plugin.participantes.contains(e.getEntity().getName())) {
				if(killer==e.getEntity()) {
					if (plugin.getConfig().getBoolean("sistema.itens.denyDrop"))
						e.getDrops().clear();
					if (plugin.getConfig().getBoolean("sistema.itens.denyDropXP")) {
						e.setKeepLevel(true);
						e.setDroppedExp(0);
					}
					return;
				}
				if((plugin.noob)&&(!plugin.noobEfetuado)) {
					plugin.noobEfetuado=true;
					me.mchiappinam.pdghnoob.Main.setNoob(e.getEntity().getName(), (me.mchiappinam.pdghnoob.Main) Bukkit.getPluginManager().getPlugin("PDGHNoob"));
					plugin.getServer().broadcastMessage("§8§l[Deathmatch] §d§l"+e.getEntity().getName()+" §cfoi o primeiro a morrer e virou o novo §dⓃⓄⓄⒷ§c!");
				}
				int k = plugin.totalParticipantes.get(killer.getName());
				plugin.totalParticipantes.put(killer.getName(), k + 1);
				plugin.getServer().broadcastMessage("§8§l[Deathmatch] §e["+plugin.totalParticipantes.get(killer.getName())+"]"+killer.getName()+" matou ["+plugin.totalParticipantes.get(e.getEntity().getName())+"]"+e.getEntity().getName());
				if (plugin.getConfig().getBoolean("sistema.itens.darItens")) {
					plugin.Kit(killer);
					killer.updateInventory();
					if (plugin.getConfig().getBoolean("sistema.itens.denyDrop"))
						e.getDrops().clear();
					if (plugin.getConfig().getBoolean("sistema.itens.denyDropXP")) {
						e.setKeepLevel(true);
						e.setDroppedExp(0);
					}
				}
			}
		} else {
			if ((plugin.getDeathmatchEtapa() == 3)) {
				if (plugin.participantes.contains(e.getEntity().getName())) {
					if (plugin.getConfig().getBoolean("sistema.itens.denyDrop"))
						e.getDrops().clear();
					if (plugin.getConfig().getBoolean("sistema.itens.denyDropXP")) {
						e.setKeepLevel(true);
						e.setDroppedExp(0);
					}
				}
			}
		}
		// plugin.removePlayer(e.getEntity(),1);
		plugin.checkDeathmatchEnd();
	}

	@EventHandler
	private void onQuit(PlayerQuitEvent e) {

		//plugin.totalParticipantes.remove(e.getPlayer().getName());
		//plugin.participantes.remove(e.getPlayer().getName());
		plugin.removePlayer(e.getPlayer(), 2);
		//plugin.checkDeathmatchEnd();
	}

	@EventHandler
	private void onKick(PlayerKickEvent e) {

		//plugin.totalParticipantes.remove(e.getPlayer().getName());
		//plugin.participantes.remove(e.getPlayer().getName());
		plugin.removePlayer(e.getPlayer(), 2);
		//plugin.checkDeathmatchEnd();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	private void onDamage(EntityDamageByEntityEvent e) {
		if (plugin.getDeathmatchEtapa() != 0)
			if (e.getEntity() instanceof Player)
				if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) {
					Player ent = (Player) e.getEntity();
					Player dam = null;
					if (e.getDamager() instanceof Player)
						dam = (Player) e.getDamager();
					else {
						Projectile a = (Projectile) e.getDamager();
						if (a.getShooter() instanceof Player)
							dam = (Player) a.getShooter();
					}
					if (plugin.participantes.contains(ent.getName()))
						if ((plugin.getDeathmatchEtapa() != 3)) {
							e.setCancelled(true);
							if (dam != null)
								dam.sendMessage("§8§l[Deathmatch] §4PvP desativado no momento!");
							return;
						}
				}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	private void onDamageP(PotionSplashEvent e) {
		for (Entity ent2 : e.getAffectedEntities())
			if (ent2 instanceof Player)
				if (plugin.getDeathmatchEtapa() != 0) {
					Player ent = (Player) ent2;
					Player dam = null;
					if (e.getPotion().getShooter() instanceof Player)
						dam = (Player) e.getEntity().getShooter();
					if (plugin.participantes.contains(ent.getName()))
						if ((plugin.getDeathmatchEtapa() != 3)) {
							e.setCancelled(true);
							if (dam != null)
								dam.sendMessage("§8§l[Deathmatch] §4PvP desativado no momento!");
							return;
						}
				}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onPCmd(PlayerCommandPreprocessEvent e) {
		if(plugin.getDeathmatchEtapa()!=0) {
			if(e.getPlayer().hasPermission("pdgh.op"))
				return;
			if(plugin.participantes.contains(e.getPlayer().getName())) {
				boolean digitou=false;
				for(String cmd : plugin.getConfig().getStringList("comandos.allowEvento"))
					if(e.getMessage().toLowerCase().startsWith(cmd)) {
						digitou=true;
				}
				if(!digitou) {
					e.getPlayer().sendMessage("§8§l[Deathmatch] §cComando bloqueado com o evento deathmatch em andamento!");
					e.setCancelled(true);
					return;
				}
				/**List<String> l = plugin.getConfig().getStringList("comandos.allowEvento");
				String msg = e.getMessage();
				for (String s : l) {
					e.getPlayer().sendMessage(s);
					if(!msg.split("\\/")[1].startsWith(s)) {
						e.setCancelled(true);
						e.getPlayer().sendMessage("§8§l[Deathmatch] §cComando bloqueado dentro do evento deathmatch!");
						return;
					}
				}*/
				/**List<String> allowedcmds = plugin.getConfig().getStringList("comandos.allowEvento");
				String cmd = allowedcmds.toString();
				String[] command = e.getMessage().split(" ");
				String use = command[0].replace("/", "");
				if(cmd.contains(use)){
				    e.setCancelled(true);
					e.getPlayer().sendMessage("§8§l[Deathmatch] §cComando bloqueado dentro do evento deathmatch!");
				}*/
				/**if(!plugin.getConfig().getStringList("comandos.allowEvento").contains(e.getMessage().toLowerCase().split("\\/")[0])) {
					e.setCancelled(true);
					return;
				}
				*/
			}
			
			for(String cmd : plugin.getConfig().getStringList("comandos.denyEventoOn"))
				if(e.getMessage().toLowerCase().startsWith(cmd)) {
				e.getPlayer().sendMessage("§8§l[Deathmatch] §cComando bloqueado com o evento deathmatch em andamento!");
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent e) {
		if (plugin.getConfig().getBoolean("sistema.itens.semFome"))
			if (plugin.participantes.contains(e.getEntity().getName()))
				e.setCancelled(true);
	}
}
