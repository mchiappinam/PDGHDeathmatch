/**
 * Copyright PDGH Minecraft Servers & HostLoad © 2013-XXXX
 * Todos os direitos reservados
 * Uso apenas para a PDGH.com.br e https://HostLoad.com.br
 * Caso vocêtenha acesso a esse sistema, você é privilegiado!
*/

package me.mchiappinam.pdghdeathmatch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Comando implements CommandExecutor {
	private Main plugin;
	public Comando(Main main) {
		plugin=main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("dm")) {
			if(args.length==0) {
				/**if(!sender.hasPermission("pdgh.dm")) {
					sender.sendMessage("§8§l[Deathmatch] §cEvento em beta, entrada não permitida!");
					return true;
				}*/
				if(sender==plugin.getServer().getConsoleSender()) {
					sender.sendMessage("§8§l[Deathmatch] §cConsole bloqueado de executar o comando!");
					return true;
				}
				if(plugin.getDeathmatchEtapa()==0) {
					sender.sendMessage("§8§l[Deathmatch] §cO evento deathmatch não está acontecendo!");
					return true;
				}
				if(plugin.getDeathmatchEtapa()>1) {
					sender.sendMessage("§8§l[Deathmatch] §cO evento deathmatch já começou!");
					return true;
				}
				if(plugin.participantes.contains(sender.getName())) {
					sender.sendMessage("§8§l[Deathmatch] §cVocê já entrou no evento deathmatch! Caso queira sair digite /dm sair");
					return true;
				}
                if(((Player)sender).isInsideVehicle()) {
				     sender.sendMessage("§8§l[Deathmatch] §cVocê está dentro de um veículo!");
				     return true;
				}
                if(((Player)sender).isDead()) {
				     sender.sendMessage("§8§l[Deathmatch] §cVocê está morto!");
				     return true;
				}
        		if((plugin.getConfig().getBoolean("sistema.itens.vazio"))&&(!plugin.isInventoryEmpty((Player)sender))) {
        			sender.sendMessage("§8§l[Deathmatch] §cSeu inventário deve estar vazio!");
        			return true;
        		}
				plugin.addPlayer((Player)sender);
				return true;
			}
			else {
				if(args[0].equalsIgnoreCase("sair")) {
					if(plugin.getDeathmatchEtapa()==0) {
						sender.sendMessage("§8§l[Deathmatch] §cO evento deathmatch não está aberto!");
						return true;
					}
					if(plugin.getDeathmatchEtapa()!=1) {
						sender.sendMessage("§8§l[Deathmatch] §cVocê não pode sair agora!");
						return true;
					}
					plugin.removePlayer((Player)sender,0);
					return true;
				}
				//outro cmds, admin!
				if(!sender.hasPermission("pdgh.admin")) {
					sender.sendMessage("§8§l[Deathmatch] §cVocê não tem permissão para executar esse comando!");
					return true;
				}
				if(args[0].equalsIgnoreCase("getinv")) {
					if((plugin.getConfig().getBoolean("sistema.itens.vazio"))&&(!plugin.isInventoryEmpty((Player)sender))) {
        				sender.sendMessage("§8§l[Deathmatch] §cSeu inventário deve estar vazio!");
        				return true;
        			}
					Player p = (Player)sender;

					p.getInventory().setContents(plugin.inventario);
					p.getInventory().setArmorContents(plugin.armadura);
					sender.sendMessage("§8§l[Deathmatch] §aItens se encontram no seu inventário!");
					return true;
				}
				if(args[0].equalsIgnoreCase("getvipinv")) {
					if((plugin.getConfig().getBoolean("sistema.itens.vazio"))&&(!plugin.isInventoryEmpty((Player)sender))) {
        				sender.sendMessage("§8§l[Deathmatch] §cSeu inventário deve estar vazio!");
        				return true;
        			}
					Player p = (Player)sender;

					p.getInventory().setContents(plugin.inventariovip);
					p.getInventory().setArmorContents(plugin.armaduravip);
					sender.sendMessage("§8§l[Deathmatch] §aItens VIP se encontram no seu inventário!");
					return true;
				}
				if(args[0].equalsIgnoreCase("setinv")) {
					if(plugin.getDeathmatchEtapa()!=0) {
						sender.sendMessage("§8§l[Deathmatch] §cJá existe um evento deathmatch sendo executado!");
						return true;
					}
					if(plugin.getDeathmatchEtapa()==0&&!plugin.canStart) {
						sender.sendMessage("§8§l[Deathmatch] §cUm evento deathmatch está sendo finalizado!");
						return true;
					}
					plugin.inventario=((Player)sender).getInventory().getContents();
					plugin.c_inventario.set("inventario", plugin.inventario);
					
					plugin.armadura=((Player)sender).getInventory().getArmorContents();
					plugin.c_armadura.set("armadura", plugin.armadura);
					try {
						plugin.c_inventario.save(new File(plugin.getDataFolder(), "inventario.yml"));
					}catch (Exception e) {
						sender.sendMessage("§cErro: "+e.getMessage());
						return true;
					}
					try {
						plugin.c_armadura.save(new File(plugin.getDataFolder(), "armadura.yml"));
					}catch (Exception e) {
						sender.sendMessage("§cErro: "+e.getMessage());
						return true;
					}
					sender.sendMessage("§8§l[Deathmatch] §aItens setados!");
					return true;
				}
				if(args[0].equalsIgnoreCase("setvipinv")) {
					if(plugin.getDeathmatchEtapa()!=0) {
						sender.sendMessage("§8§l[Deathmatch] §cJá existe um evento deathmatch sendo executado!");
						return true;
					}
					if(plugin.getDeathmatchEtapa()==0&&!plugin.canStart) {
						sender.sendMessage("§8§l[Deathmatch] §cUm evento deathmatch está sendo finalizado!");
						return true;
					}
					plugin.inventariovip=((Player)sender).getInventory().getContents();
					plugin.c_inventariovip.set("inventario", plugin.inventariovip);
					
					plugin.armaduravip=((Player)sender).getInventory().getArmorContents();
					plugin.c_armaduravip.set("armadura", plugin.armaduravip);
					try {
						plugin.c_inventariovip.save(new File(plugin.getDataFolder(), "inventariovip.yml"));
					}catch (Exception e) {
						sender.sendMessage("§cErro: "+e.getMessage());
						return true;
					}
					try {
						plugin.c_armaduravip.save(new File(plugin.getDataFolder(), "armaduravip.yml"));
					}catch (Exception e) {
						sender.sendMessage("§cErro: "+e.getMessage());
						return true;
					}
					sender.sendMessage("§8§l[Deathmatch] §aItens setados!");
					return true;
				}
				if(args[0].equalsIgnoreCase("forcestart")) {
					if(plugin.getDeathmatchEtapa()!=0) {
						sender.sendMessage("§8§l[Deathmatch] §cJá existe um evento deathmatch sendo executado!");
						return true;
					}
					if(plugin.getDeathmatchEtapa()==0&&!plugin.canStart) {
						sender.sendMessage("§8§l[Deathmatch] §cUm evento deathmatch está sendo finalizado!");
						return true;
					}
					sender.sendMessage("§8§l[Deathmatch] §eEvento deathmatch sendo iniciado!");
					plugin.prepareDeathmatch();
					return true;
				}
				if(args[0].equalsIgnoreCase("forcestop")) {
					if(plugin.getDeathmatchEtapa()==0) {
						sender.sendMessage("§8§l[Deathmatch] §cNão há nenhum evento deathmatch sendo executado!");
						return true;
					}
					plugin.getServer().broadcastMessage("§8§l[Deathmatch] §eCancelando evento...");
					plugin.cancelDeathmatch();
					plugin.getServer().broadcastMessage("§8§l[Deathmatch] §eEvento cancelado!");
					sender.sendMessage("§8§l[Deathmatch] §eEvento deathmatch parado!");
					return true;
				}
				if(args[0].equalsIgnoreCase("kick")) {
					if(args.length<2) {
						sender.sendMessage("§8§l[Deathmatch] §c/dm kick <nome>");
						return true;
					}
					String nome = args[1].toLowerCase();
					Player p = plugin.getServer().getPlayer(nome);
					if(p==null) {
						sender.sendMessage("§8§l[Deathmatch] §cJogador não encontrado!");
						return true;
					}
					plugin.removePlayer(p, 3);
					sender.sendMessage("§8§l[Deathmatch] §e"+nome+" foi kickado do evento deathmatch!");
					return true;
				}
				if(args[0].equalsIgnoreCase("info")) {
					if(plugin.getDeathmatchEtapa()!=3) {
						sender.sendMessage("§8§l[Deathmatch] §cO evento deathmatch não está acontecendo!");
						return true;
					}
					sender.sendMessage("§8§l[Deathmatch] §eRestam "+plugin.participantes.size()+" jogadores dentro do deathmatch!");
					return true;
				}
				if(args[0].equalsIgnoreCase("add")) {
					if((args.length<2) || (args.length>2)) {
						sender.sendMessage("§eUse /dm add <nome-da-arena>");
						return true;
					}
					Player p = (Player)sender;
					List<String> arenas = new ArrayList<String>();
					if(plugin.c_arenas.contains("arenas."+args[1]))
						for(String s : plugin.c_arenas.getStringList("arenas."+args[1]))
							arenas.add(s);
					arenas.add(p.getLocation().getWorld().getName()+";"+p.getLocation().getX()+";"+p.getLocation().getY()+";"+p.getLocation().getZ()+";"+p.getLocation().getYaw()+";"+p.getLocation().getPitch());
					plugin.c_arenas.set("arenas."+args[1], arenas);
					try {
						plugin.c_arenas.save(new File(plugin.getDataFolder(), "arenas.yml"));
					}catch (Exception e) {
						sender.sendMessage("§cErro: "+e.getMessage());
						return true;
					}
					sender.sendMessage("§a"+arenas.size()+"ª posição marcada na arena "+args[1]);
					return true;
				}
				if(args[0].equalsIgnoreCase("delete")) {
					if((args.length<2) || (args.length>2)) {
						sender.sendMessage("§eUse /dm delete <nome-da-arena>");
						return true;
					}
					Player p = (Player)sender;
					if(plugin.c_arenas.contains("arenas."+args[1]))
						plugin.c_arenas.set("arenas."+args[1], null);
					try {
						plugin.c_arenas.save(new File(plugin.getDataFolder(), "arenas.yml"));
					}catch (Exception e) {
						sender.sendMessage("§cErro: "+e.getMessage());
						return true;
					}
					sender.sendMessage("§aArena "+args[1]+" deletada");
					return true;
				}
				sendHelp((Player)sender);
			}
			return true;
		}
		return true;
	}
	
	private void sendHelp(Player p) {
		p.sendMessage("§d§lPDGHDeathmatch - Comandos do plugin:");
		p.sendMessage("§2/dm ? -§a- Lista de comandos");
		p.sendMessage("§c/dm add <nome-da-arena> -§a- Adiciona uma posição na arena");
		p.sendMessage("§c/dm forcestart -§a- Força o inicio do evento deathmatch");
		p.sendMessage("§c/dm forcestop -§a- Força a parada do evento deathmatch");
		p.sendMessage("§c/dm setinv -§a- Seta os itens do evento");
		p.sendMessage("§c/dm setvipinv -§a- Seta os itens do evento para vips");
		p.sendMessage("§c/dm getinv -§a- Pega os itens do evento");
		p.sendMessage("§c/dm getvipinv -§a- Pega os itens do evento para vips");
		p.sendMessage("§2/dm kick <nome> -§a- Kicka um jogador do evento deathmatch");
		p.sendMessage("§2/dm info -§a- Mostra quantos jogadores estão dentro do evento deathmatch");
	}

}
