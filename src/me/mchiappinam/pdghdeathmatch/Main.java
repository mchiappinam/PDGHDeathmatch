/**
 * Copyright PDGH Minecraft Servers & HostLoad © 2013-XXXX
 * Todos os direitos reservados
 * Uso apenas para a PDGH.com.br e https://HostLoad.com.br
 * Caso você tenha acesso a esse sistema, você é privilegiado!
*/

/*
 * Copyright PDGH Minecraft Servers & HostLoad © 2013-XXXX
 * Todos os direitos reservados
 * Uso apenas para a PDGH.com.br e https://HostLoad.com.br
 * Caso você tenha acesso a esse sistema, você é privilegiado!
*/

package me.mchiappinam.pdghdeathmatch;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.p000ison.dev.simpleclans2.api.SCCore;
import com.p000ison.dev.simpleclans2.api.clanplayer.ClanPlayerManager;

import me.mchiappinam.pdghapiutility.Metodos;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
//import org.bukkit.scoreboard.Team;

public class Main extends JavaPlugin {
	protected HashMap<Player,Integer> respawndelay = new HashMap<Player,Integer>();
	protected SCCore core;
	protected SimpleClans core2;
	protected static Economy econ = null;
	public boolean vault = false;
	protected int version = 0;
	
	private int deathmatchEtapa = 0;
	protected boolean canStart = true;

	protected boolean jaTeleportado = false;
	public boolean noob=false;
	public boolean eventos=false;
	public boolean legendchat=false;
	public boolean noobEfetuado=false;
	//public boolean noobEfetuadoo=true;
	
	protected String key=null;
	
	protected HashMap<String,Integer> totalParticipantes = new HashMap<String,Integer>();
	protected List<String> participantes = new ArrayList<String>();
	protected List<String> vips = new ArrayList<String>();
	public boolean apiutility=false;
	private me.mchiappinam.pdghapiutility.Main api;
	protected String arenaEscolhida;
	
	FileConfiguration c_inventario;
	FileConfiguration c_armadura;
	FileConfiguration c_inventariovip;
	FileConfiguration c_armaduravip;
	FileConfiguration c_arenas;

	ItemStack[] inventario=null;
	ItemStack[] armadura=null;
	ItemStack[] inventariovip=null;
	ItemStack[] armaduravip=null;
	
	@Override
    public void onEnable() {
		getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2ativando... - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2verificando config... - Plugin by: mchiappinam");
		if (!(new File(getDataFolder(), "config.yml")).exists()) {
			try {
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2salvando config.yml pela primeira vez... - Plugin by: mchiappinam");
				saveResource("config_template.yml", false);
				File file2 = new File(getDataFolder(), "config_template.yml");
				file2.renameTo(new File(getDataFolder(), "config.yml"));
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2config.yml salva... - Plugin by: mchiappinam");
			} catch (Exception e) {}
		}
		if (!(new File(getDataFolder(), "inventario.yml")).exists()) {
			try {
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2salvando inventario.yml pela primeira vez... - Plugin by: mchiappinam");
				saveResource("inventario.yml", false);
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2inventario.yml inventario salva... - Plugin by: mchiappinam");
			} catch (Exception e) {}
		}
		if (!(new File(getDataFolder(), "armadura.yml")).exists()) {
			try {
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2salvando armadura.yml pela primeira vez... - Plugin by: mchiappinam");
				saveResource("armadura.yml", false);
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2armadura.yml salva... - Plugin by: mchiappinam");
			} catch (Exception e) {}
		}
		if (!(new File(getDataFolder(), "inventariovip.yml")).exists()) {
			try {
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2salvando inventariovip.yml pela primeira vez... - Plugin by: mchiappinam");
				saveResource("inventariovip.yml", false);
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2inventariovip.yml inventario salva... - Plugin by: mchiappinam");
			} catch (Exception e) {}
		}
		if (!(new File(getDataFolder(), "armaduravip.yml")).exists()) {
			try {
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2salvando armaduravip.yml pela primeira vez... - Plugin by: mchiappinam");
				saveResource("armaduravip.yml", false);
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2armaduravip.yml salva... - Plugin by: mchiappinam");
			} catch (Exception e) {}
		}
		if (!(new File(getDataFolder(), "arenas.yml")).exists()) {
			try {
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2salvando arenas.yml pela primeira vez... - Plugin by: mchiappinam");
				saveResource("arenas.yml", false);
				getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2arenas.yml salva... - Plugin by: mchiappinam");
			} catch (Exception e) {}
		}

		c_inventario = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "inventario.yml"));
		c_armadura = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "armadura.yml"));
		c_inventariovip = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "inventariovip.yml"));
		c_armaduravip = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "armaduravip.yml"));
		c_arenas = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "arenas.yml"));
		
		getServer().getPluginCommand("dm").setExecutor(new Comando(this));
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		if((getConfig().getBoolean("sistema.itens.darItens"))) {
			getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2darItens true, atualizando itens...");
			inventario=((List<ItemStack>) c_inventario.get("inventario")).toArray(new ItemStack[0]);
			armadura=((List<ItemStack>) c_armadura.get("armadura")).toArray(new ItemStack[0]);
			inventariovip=((List<ItemStack>) c_inventariovip.get("inventario")).toArray(new ItemStack[0]);
			armaduravip=((List<ItemStack>) c_armaduravip.get("armadura")).toArray(new ItemStack[0]);
			getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2Itens atualizados");
		}
		
		if (!setupEconomy()) {
			getLogger().warning("ERRO: Vault nao encontrado!");
			vault = false;
		}else{
			getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2Sucesso: Vault encontrado.");
			vault = true;
		}

		if (hookSimpleClans()) {
			getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2Sucesso: SimpleClans2 encontrado.");
			version = 2;
		} else if (getServer().getPluginManager().getPlugin("SimpleClans") != null) {
			getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2Sucesso: SimpleClans1 encontrado.");
			core2 = ((SimpleClans) getServer().getPluginManager().getPlugin("SimpleClans"));
			version = 1;
		} else {
			version = 0;
			getLogger().warning("ERRO: SimpleClans ou SimpleClans2 nao encontrado!");
		}
		
		if (getServer().getPluginManager().getPlugin("PDGHNoob") == null) {
			getLogger().warning("PDGHNoob API nao encontrado!");
			noob=false;
		}else{
			getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2Sucesso: PDGHNoob encontrado.");
			noob=true;
		}
		
		if (getServer().getPluginManager().getPlugin("PDGHEventos") == null) {
			getLogger().warning("PDGHEventos API nao encontrado!");
			eventos=false;
		}else{
			getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2Sucesso: PDGHEventos encontrado.");
			eventos=true;
		}
		
		if (getServer().getPluginManager().getPlugin("PDGHAPIUtility") == null) {
			getLogger().warning("PDGHAPIUtility nao encontrado!");
			apiutility=false;
		}else{
			getLogger().info("PDGHAPIUtility ativado!");
			api = (me.mchiappinam.pdghapiutility.Main)getServer().getPluginManager().getPlugin("PDGHAPIUtility");
			apiutility=true;
		}
		
		if (getServer().getPluginManager().getPlugin("Legendchat") == null) {
			getLogger().warning("Legendchat API nao encontrado!");
			legendchat=false;
		}else{
			getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2Sucesso: Legendchat encontrado.");
			getServer().getPluginManager().registerEvents(new ListenerLegendchat(this), this);
			legendchat=true;
		}
		timer();
		
		getServer().getScheduler().runTaskTimer(this, new Runnable() {
			public void run() {
				if(getConfig().getBoolean("autoStart.ativado"))
					for(String s : getConfig().getStringList("autoStart.dias")) {
						String data[] = s.split("-");
						int diaAutoStart = Utils.strToCalendar(data[0]);
						int horaAutoStart = Integer.parseInt(data[1].substring(0,2));
						int minAutoStart = Integer.parseInt(data[1].substring(2,4));
						if(!data[0].equalsIgnoreCase("todos")) {
							if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)==diaAutoStart)
								if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)==horaAutoStart)
									if(Calendar.getInstance().get(Calendar.MINUTE)==minAutoStart)
										prepareDeathmatch();
						}else{
							if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)==horaAutoStart)
								if(Calendar.getInstance().get(Calendar.MINUTE)==minAutoStart)
									prepareDeathmatch();
						}
					}
			}
		}, 0, 400);
		
		getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2ativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHDeathmatch] §2Acesse: http://pdgh.com.br/");
	}
	
	@Override
    public void onDisable() {
		if(getDeathmatchEtapa()!=0) {
			getServer().broadcastMessage("§8§l[Deathmatch] §eCancelando evento...");
			cancelDeathmatch();
			getServer().broadcastMessage("§8§l[Deathmatch] §eEvento cancelado!");
		}
		getServer().getConsoleSender().sendMessage("§3[Deathmatch] §2desativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[Deathmatch] §2Acesse: http://pdgh.com.br/");
	}

	public void sendActionText(Player p, String message){
		PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
	
	public void timer() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
	    		if(!respawndelay.isEmpty())
	    			for(Player p : respawndelay.keySet()) {
	    				if(respawndelay.get(p)<=0) {
	    					respawndelay.remove(p);
	    					//sendTitle(p,"Você renaceu! =D",0,3*20,2*20,ChatColor.BLUE);
	    					TitleAPI.sendTitle(p, 1,5*20,3*20, "§7§l[DM] §aRENASCEU! ;)", "§7§l[DM] §6VIPs renascem mais rápido");
	    					teleportarArena(p);
	    					p.setFlySpeed(0.1f);
	    					p.setGameMode(GameMode.SURVIVAL);
	    				}else{
	    					int tempo=respawndelay.get(p);
	    					//sendTitle(p,,0,3*20,0,ChatColor.RED);
	    					if(p.getGameMode()!=GameMode.SPECTATOR) {
		    					p.setGameMode(GameMode.SPECTATOR);
		    					p.setFlySpeed(0.0f);
	    					}
	    					sendActionText(p, "§c§lMORREU!§c Renascendo em §e"+((double)tempo/10)+"§cs");
	    					//TitleAPI.sendTitle(p, 0,60,0, "§7§l[DM] §cMORREU! =(", "§7§l[DM] §cRenascendo em §e"+((double)tempo/10)+"§cs");
	    					respawndelay.put(p, tempo-1);
	    				}
	    			}
			}
		}, 2, 2);
	}
	
	public void teleportarArena(Player p) {
		List<String> arenas = c_arenas.getStringList("arenas."+arenaEscolhida);
		final Random r=new Random();
	    int randomNum = r.nextInt(arenas.size());
		String ent[] = arenas.get(randomNum).split(";");
		Location loc = new Location(getServer().getWorld(ent[0]),Double.parseDouble(ent[1]),Double.parseDouble(ent[2]),Double.parseDouble(ent[3]),Float.parseFloat(ent[4]),Float.parseFloat(ent[5]));
		if(getDeathmatchEtapa()==0) {
			tpSpawn(p);
			return;
		}else if(getDeathmatchEtapa()==4) {
			tpSpawn(p);
			return;
		}
			
		if(getConfig().getBoolean("sistema.itens.clearInv"))
			clearInv(p);
		if(getConfig().getBoolean("sistema.itens.darItens")) {
			Kit(p);
			p.updateInventory();
		}
		if(getConfig().getBoolean("sistema.itens.clearPot"))
		    for(PotionEffect effect : p.getActivePotionEffects()) {
		    	p.removePotionEffect(effect.getType());
		    	p.sendMessage("§8§l[Deathmatch] §ePoção §6"+effect.getType().getName()+" §eremovida.");
		    }
		p.teleport(loc);
	}
	
	public void prepararArena() {
		List<String> arenas = new ArrayList<String>();
		for(String r : c_arenas.getConfigurationSection("arenas").getKeys(false))
			arenas.add(r);
		final Random r=new Random();
        int randomNum = r.nextInt(arenas.size());
        arenaEscolhida = arenas.get(randomNum);
		//getServer().broadcastMessage("§8§l[Deathmatch] §eArena escolhida: §e"+arenaEscolhida);
	}
	
    public Metodos getMetodos() {
    	return api.getMetodos();
    }
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

	private boolean hookSimpleClans() {
		try {
			for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
				if ((plugin instanceof SCCore)) {
					core = ((SCCore) plugin);
					return true;
				}
			}
		} catch (NoClassDefFoundError e) {
			return false;
		}
		return false;
	}

	public ClanPlayerManager getClanPlayerManager() {
		return core.getClanPlayerManager();
	}
	
	
	protected void prepareDeathmatch() {
		if(deathmatchEtapa!=0)
			return;
		prepararArena();
		//getServer().dispatchCommand(getServer().getConsoleSender(), "simpleclans globalff allow");
		deathmatchEtapa=1;
		tirarTagsAntigas();
		if((eventos)||(getConfig().getBoolean("APIPDGHEventos"))) {
			me.mchiappinam.pdgheventos.Comandos.cancelarTodosEventos();
			me.mchiappinam.pdgheventos.Comandos.setEvento("API");
		}
    	if(apiutility) {
    		getMetodos().sendTweet("EVENTO DEATHMATCH INICIANDO... Arena: "+arenaEscolhida);
    	}
		messagePrepare(getConfig().getInt("sistema.tempo.preparar.avisos"));
	}
	private void messagePrepare(final int vezes) {
		canStart=true;
		if(deathmatchEtapa!=1)
			return;
		canStart=false;
		if(vezes==0)
			preparedDeathmatch();
		else {
			getServer().broadcastMessage(" ");
			getServer().broadcastMessage("§8§l[Deathmatch] §eEvento deathmatch automático começando!");
			getServer().broadcastMessage("§8§l[Deathmatch] §ePara participar digite: §6§l/dm");
			getServer().broadcastMessage("§8§l[Deathmatch] §ePremio: §c$"+getConfig().getDouble("premio")+"§e e tag "+getConfig().getString("tags.vencedor").replace("&", "§"));
			getServer().broadcastMessage("§8§l[Deathmatch] §eTempo restante: §c"+vezes*getConfig().getInt("sistema.tempo.preparar.tempoEntre")+" segundos");
			getServer().broadcastMessage("§8§l[Deathmatch] §eJogadores: "+participantes.size());
			getServer().broadcastMessage("§8§l[Deathmatch] §eArena escolhida: §a"+arenaEscolhida);
			getServer().broadcastMessage("§8§l[Deathmatch] §eO primeiro que pegar §a"+getConfig().getInt("sistema.fragLimite")+" kills§e vence!");
			getServer().broadcastMessage(" ");
		}
		getServer().getScheduler().runTaskLater(this, new Runnable() {
			public void run() {
				canStart=true;
				if(deathmatchEtapa!=1)
					return;
				canStart=false;
				messagePrepare(vezes-1);
			}
		}, 20*getConfig().getInt("sistema.tempo.preparar.tempoEntre"));
	}
	
	
	
	
	
	protected void preparedDeathmatch() {
		if(participantes.size()<getConfig().getInt("sistema.jogadoresMinimos")) {
			cancelDeathmatch();
			getServer().broadcastMessage(" ");
			getServer().broadcastMessage("§8§l[Deathmatch] §eEvento deathmatch automático §cCANCELADO!");
			getServer().broadcastMessage("§8§l[Deathmatch] §eMotivo: Quantidade de jogadores menor que "+getConfig().getInt("sistema.jogadoresMinimos"));
			getServer().broadcastMessage(" ");
			return;
		}
		deathmatchEtapa=2;
		getServer().broadcastMessage(" ");
		getServer().broadcastMessage("§8§l[Deathmatch] §eEvento deathmatch sendo INICIADO!");
		getServer().broadcastMessage("§8§l[Deathmatch] §eTeleporte para o evento BLOQUEADO!");
		getServer().broadcastMessage(" ");
		canStart=false;
		messageiniciando(getConfig().getInt("sistema.tempo.iniciando.avisos"));
	}
	private void messageiniciando(final int vezes) {
		canStart=true;
		if(deathmatchEtapa!=2)
			return;
		canStart=false;
		if(vezes==0)
			startDeathmatch();
		else {
			sendMessageDeathmatch(" ");
			sendMessageDeathmatch("§8§l[Deathmatch] §eEvento deathmatch automático começando!");
			sendMessageDeathmatch("§8§l[Deathmatch] §eTempo inicial para os jogadores se prepararem!");
			sendMessageDeathmatch("§8§l[Deathmatch] §eTempo restante: §c"+vezes*getConfig().getInt("sistema.tempo.iniciando.tempoEntre")+" segundos");
			sendMessageDeathmatch(" ");
		}
		getServer().getScheduler().runTaskLater(this, new Runnable() {
			public void run() {
				canStart=true;
				if(deathmatchEtapa!=2)
					return;
				canStart=false;
				messageiniciando(vezes-1);
			}
		}, 20*getConfig().getInt("sistema.tempo.iniciando.tempoEntre"));
	}
	
	
	
	
	
	protected void startDeathmatch() {
		canStart=true;
		deathmatchEtapa=3;
		sendMessageDeathmatch(" ");
		sendMessageDeathmatch("§8§l[Deathmatch] §eVALENDO!");
		sendMessageDeathmatch("§8§l[Deathmatch] §eVALENDO!");
		sendMessageDeathmatch("§8§l[Deathmatch] §eVALENDO!");
		sendMessageDeathmatch(" ");
    	if(apiutility) {
    		getMetodos().sendTweet("EVENTO DEATHMATCH COMEÇOU! O PVP JÁ ESTÁ ON!");
    	}
	}
	
	
	
	
	
	protected void checkDeathmatchEnd() {
		if(totalParticipantes.containsValue(getConfig().getInt("sistema.fragLimite"))) {
			if(deathmatchEtapa==3) {
				deathmatchEtapa=4;
				String vencedor = null;
				for(String p : totalParticipantes.keySet())
					if(totalParticipantes.get(p)>=getConfig().getInt("sistema.fragLimite"))
						 vencedor = p;
				if(eventos)
					me.mchiappinam.pdgheventos.Comandos.setEvento("nenhum");
				noobEfetuado=true;
				jaTeleportado = false;
				econ.depositPlayer(vencedor, getConfig().getDouble("premio"));
				String v1 = null;
				int v1_v = -1;
				String v2 = null;
				int v2_v = -1;
				String v3 = null;
				int v3_v = -1;
				for(String n : totalParticipantes.keySet()){
					int matou = totalParticipantes.get(n);
					if(matou>v1_v) {
						v3_v=v2_v;
						v3=v2;
						v2_v=v1_v;
						v2=v1;
						v1=n;
						v1_v=matou;
					}else if(matou>v2_v) {
						v3=v2;
						v3_v=v2_v;
						v2=n;
						v2_v=matou;
					}else if(matou>v3_v) {
						v3=n;
						v3_v=matou;
					}
				}
				darTagsNovas(vencedor);

		    	if(apiutility) {
		    		getMetodos().sendTweet(vencedor+" VENCEU O EVENTO DEATHMATCH. TOP KILLS: 1º "+v1+" ("+v1_v+")"+(v2!=null?", 2º "+v2+" ("+v2_v+")" : "")+(v3!=null?", 3º "+v3+" ("+v3_v+")" : ""));
		    	}
				getServer().broadcastMessage(" ");
				getServer().broadcastMessage("§8§l[Deathmatch] §eEvento deathmatch FINALIZADO!");
				getServer().broadcastMessage("§8§l[Deathmatch] §eVencedor: §l"+vencedor);
				getServer().broadcastMessage("§8§l[Deathmatch] §ePremio: §c$"+getConfig().getDouble("premio")+"§e e tag "+getConfig().getString("tags.vencedor").replace("&", "§"));
				getServer().broadcastMessage("§8§l[Deathmatch] §eTOP 3 KILLS:");
				getServer().broadcastMessage("§8§l[Deathmatch] §e1º colocado: "+v1+" ("+v1_v+")");
				if(v2!=null)
					getServer().broadcastMessage("§8§l[Deathmatch] §e2º colocado: "+v2+" ("+v2_v+")");
				if(v3!=null)
					getServer().broadcastMessage("§8§l[Deathmatch] §e3º colocado: "+v3+" ("+v3_v+")");
				getServer().broadcastMessage(" ");
				sendMessageDeathmatch("§8§l[Deathmatch] §b§lVocê será teleportado em "+getConfig().getInt("sistema.tempo.finalizando")+" segundos!");
				//getServer().dispatchCommand(getServer().getConsoleSender(), "simpleclans globalff auto");
				getServer().getScheduler().runTaskLater(this, new Runnable() {
					public void run() {
						finalizarDeathmatch();
					}
				}, 20*getConfig().getInt("sistema.tempo.finalizando"));
				return;
			}
		}else if((participantes.size()==1) &&(deathmatchEtapa==3)) {
			//if(totalParticipantes.get(participantes.get(0))>=15)
			deathmatchEtapa=4;
			String vencedor = null;
			vencedor = participantes.get(0);
			if(eventos)
				me.mchiappinam.pdgheventos.Comandos.setEvento("nenhum");
			noobEfetuado=true;
			jaTeleportado = false;
			econ.depositPlayer(vencedor, getConfig().getDouble("premio"));
			String v1 = null;
			int v1_v = -1;
			String v2 = null;
			int v2_v = -1;
			String v3 = null;
			int v3_v = -1;
			for(String n : totalParticipantes.keySet()){
				int matou = totalParticipantes.get(n);
				if(matou>v1_v) {
					v3_v=v2_v;
					v3=v2;
					v2_v=v1_v;
					v2=v1;
					v1=n;
					v1_v=matou;
				}else if(matou>v2_v) {
					v3=v2;
					v3_v=v2_v;
					v2=n;
					v2_v=matou;
				}else if(matou>v3_v) {
					v3=n;
					v3_v=matou;
				}
			}
			darTagsNovas(vencedor);

	    	if(apiutility) {
	    		getMetodos().sendTweet(vencedor+" VENCEU O EVENTO DEATHMATCH. TOP KILLS: 1º "+v1+" ("+v1_v+")"+(v2!=null?", 2º "+v2+" ("+v2_v+")" : "")+(v3!=null?", 3º "+v3+" ("+v3_v+")" : ""));
	    	}
			getServer().broadcastMessage(" ");
			getServer().broadcastMessage("§8§l[Deathmatch] §eEvento deathmatch FINALIZADO!");
			getServer().broadcastMessage("§8§l[Deathmatch] §eVencedor: §l"+vencedor);
			getServer().broadcastMessage("§8§l[Deathmatch] §ePremio: §c$"+getConfig().getDouble("premio")+"§e e tag "+getConfig().getString("tags.vencedor").replace("&", "§"));
			getServer().broadcastMessage("§8§l[Deathmatch] §eTOP 3 KILLS:");
			getServer().broadcastMessage("§8§l[Deathmatch] §e1º colocado: "+v1+" ("+v1_v+")");
			if(v2!=null)
				getServer().broadcastMessage("§8§l[Deathmatch] §e2º colocado: "+v2+" ("+v2_v+")");
			if(v3!=null)
				getServer().broadcastMessage("§8§l[Deathmatch] §e3º colocado: "+v3+" ("+v3_v+")");
			getServer().broadcastMessage(" ");
			sendMessageDeathmatch("§8§l[Deathmatch] §b§lVocê será teleportado em "+getConfig().getInt("sistema.tempo.finalizando")+" segundos!");
			//getServer().dispatchCommand(getServer().getConsoleSender(), "simpleclans globalff auto");
			getServer().getScheduler().runTaskLater(this, new Runnable() {
				public void run() {
					finalizarDeathmatch();
				}
			}, 20*getConfig().getInt("sistema.tempo.finalizando"));
			return;
		}
	}
	
	
	
	
	protected void finalizarDeathmatch() {
		sendMessageDeathmatch(" ");
		sendMessageDeathmatch("§8§l[Deathmatch] §eFim do evento!");
		sendMessageDeathmatch(" ");
		cancelDeathmatch();
	}
	
	protected void darTagsNovas(String v2) {
		c_arenas.set("vencedor", v2);
		try {
			c_arenas.save(new File(getDataFolder(), "arenas.yml"));
		}catch (Exception e) {
			return;
		}
	}
	
	protected void tirarTagsAntigas() {
		c_arenas.set("vencedor", "");
		try {
			c_arenas.save(new File(getDataFolder(), "arenas.yml"));
		}catch (Exception e) {
			return;
		}
	}
	
	public void tpSpawn(Player p) {
		if(respawndelay.containsKey(p)) {
			respawndelay.remove(p);
    		p.setFlySpeed(0.1f);
			p.setGameMode(GameMode.SURVIVAL);
		}
		//sendTitle(p,"Você renaceu! =D",0,3*20,2*20,ChatColor.BLUE);
		if(getConfig().getBoolean("sistema.itens.clearInv"))
			clearInv(p);
		

		totalParticipantes.remove(p.getName());
		participantes.remove(p.getName());

		if(version!=0)
			if(version==1)
				if(core2.getClanManager().getClanPlayer(p) != null)
					core2.getClanManager().getClanPlayer(p).setFriendlyFire(false);
			else if(version==2)
				if(core.getClanPlayerManager().getClanPlayer(p) != null)
					core.getClanPlayerManager().getClanPlayer(p).setFriendlyFire(false);
		
		p.teleport(getServer().getWorld(getConfig().getString("mundoPrincipal")).getSpawnLocation());
		checkDeathmatchEnd();
	}
	
	protected void cancelDeathmatch() {
		if(deathmatchEtapa==0)
			return;
		//getServer().dispatchCommand(getServer().getConsoleSender(), "simpleclans globalff auto");
		deathmatchEtapa=0;
		for(String n : participantes) {
			//tpSpawn(getServer().getPlayer(n));
			Player p = getServer().getPlayer(n);
			if(p.isOnline()) {

				if(respawndelay.containsKey(p)) {
					respawndelay.remove(p);
		    		p.setFlySpeed(0.1f);
					p.setGameMode(GameMode.SURVIVAL);
				}
				//sendTitle(p,"Você renaceu! =D",0,3*20,2*20,ChatColor.BLUE);
				if(getConfig().getBoolean("sistema.itens.clearInv"))
					clearInv(p);
				
				if(version!=0)
					if(version==1)
						if(core2.getClanManager().getClanPlayer(p) != null)
							core2.getClanManager().getClanPlayer(p).setFriendlyFire(false);
					else if(version==2)
						if(core.getClanPlayerManager().getClanPlayer(p) != null)
							core.getClanPlayerManager().getClanPlayer(p).setFriendlyFire(false);
				
				p.teleport(getServer().getWorld(getConfig().getString("mundoPrincipal")).getSpawnLocation());
			//getServer().getPlayer(n).teleport(getServer().getWorld(getConfig().getString("mundoPrincipal")).getSpawnLocation());
		
			}
		}
		participantes.clear();
		totalParticipantes.clear();
		arenaEscolhida=null;
		if(eventos)
			me.mchiappinam.pdgheventos.Comandos.setEvento("nenhum");
		noobEfetuado=false;
		canStart=true;
	}
	
	protected int getDeathmatchEtapa() {
		return deathmatchEtapa;
	}
	
	protected boolean isInventoryEmpty(Player p) {
		for(ItemStack i : p.getInventory().getContents())
			if(i != null)
				if (i.getType() != Material.AIR)
					return false;
		for (ItemStack a : p.getInventory().getArmorContents())
			if (a.getType() != Material.AIR )
				return false;
		return true;
	}
	
	@SuppressWarnings("deprecation")
	protected void addPlayer(Player p) {
		//p.teleport(getServer().getWorld(getConfig().getString("mundoPrincipal")).getSpawnLocation());
		if(getConfig().getBoolean("sistema.itens.clearInv"))
			clearInv(p);
		if(getConfig().getBoolean("sistema.itens.semFome"))
			p.setFoodLevel(20);
		if(version!=0)
			if(version==1)
				if(core2.getClanManager().getClanPlayer(p) != null)
					core2.getClanManager().getClanPlayer(p).setFriendlyFire(true);
			else if(version==2)
				if(core.getClanPlayerManager().getClanPlayer(p) != null)
					core.getClanPlayerManager().getClanPlayer(p).setFriendlyFire(true);
		totalParticipantes.put(p.getName(), 0);
		participantes.add(p.getName());
		teleportarArena(p);
		p.sendMessage(" ");
		p.sendMessage("§8§l[Deathmatch] §eVocê entrou no evento deathmatch!");
		p.sendMessage("§8§l[Deathmatch] §cPara sair digite: §c§l/dm sair");
		p.sendMessage("§8§l[Deathmatch] §ePrepare-se enquanto o evento está iniciando!");
		p.sendMessage(" ");
		if(p.hasPermission("pdgh.admin"))
			return;
		if(p.hasPermission("pdgh.vip"))
			if(!vips.contains(p.getName().toLowerCase())) {
				getServer().broadcastMessage("§8§l[Deathmatch] §6§l"+p.getName()+" §eé VIP e entrou no evento deathmatch. VIPs renascem mais rápido e tem itens melhores.");
				vips.add(p.getName().toLowerCase());
			}
	}
	
	public void clearInv(Player p) {
		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.getInventory().clear();
	}
	
	public void Kit(Player p) {
		if(p.hasPermission("pdgh.vip")) {
			for(ItemStack a0 : inventariovip)
				if(a0!=null) {
		    	    ItemMeta b0 = a0.getItemMeta();
		    		List<String> l0 = new ArrayList<String>();
		    	    l0.add("§a§lItem do evento Deathmatch");
		    	    l0.add(" ");
		    	    l0.add("§cAdquirido por:");
		    	    l0.add("§c"+p.getName());
		    	    b0.setLore(l0);
		    	    a0.setDurability((short)0);
		    	    a0.setItemMeta(b0);
				}
			for(ItemStack a0 : armaduravip)
				if(a0!=null) {
		    	    ItemMeta b0 = a0.getItemMeta();
		    		List<String> l0 = new ArrayList<String>();
		    	    l0.add("§a§lItem do evento Deathmatch");
		    	    l0.add(" ");
		    	    l0.add("§cAdquirido por:");
		    	    l0.add("§c"+p.getName());
		    	    b0.setLore(l0);
		    	    a0.setDurability((short)0);
		    	    a0.setItemMeta(b0);
				}
			p.getInventory().setContents(inventariovip);
			p.getInventory().setArmorContents(armaduravip);
		}else{
			for(ItemStack a0 : inventario)
				if(a0!=null) {
		    	    ItemMeta b0 = a0.getItemMeta();
		    		List<String> l0 = new ArrayList<String>();
		    	    l0.add("§a§lItem do evento Deathmatch");
		    	    l0.add(" ");
		    	    l0.add("§cAdquirido por:");
		    	    l0.add("§c"+p.getName());
		    	    b0.setLore(l0);
		    	    a0.setDurability((short)0);
		    	    a0.setItemMeta(b0);
				}
			for(ItemStack a0 : armadura)
				if(a0!=null) {
		    	    ItemMeta b0 = a0.getItemMeta();
		    		List<String> l0 = new ArrayList<String>();
		    	    l0.add("§a§lItem do evento Deathmatch");
		    	    l0.add(" ");
		    	    l0.add("§cAdquirido por:");
		    	    l0.add("§c"+p.getName());
		    	    b0.setLore(l0);
		    	    a0.setDurability((short)0);
		    	    a0.setItemMeta(b0);
				}
			p.getInventory().setContents(inventario);
			p.getInventory().setArmorContents(armadura);
		}
	}
	
	protected void removePlayer(Player p,int motive) {//0=sair, 1=morrer, 2=quit, 3=kick
		if(!participantes.contains(p.getName()))
			return;
		if(deathmatchEtapa<2) {
			//totalParticipantes.remove(p.getName());
		}else if(deathmatchEtapa==3) {
			if(participantes.size()>1) {
				
				//getServer().broadcastMessage("§8§l[Deathmatch] §eRestam "+participantes.size()+" jogadores dentro do deathmatch!");
				checkDeathmatchEnd();
			}
		}
		if(deathmatchEtapa==1) {
			//totalParticipantes.remove(p.getName());
			if(motive!=3) {
				//participantes.remove(p.getName());
				tpSpawn(p);
				p.sendMessage("§8§l[Deathmatch] §eVocê saiu do evento deathmatch, para voltar: §c/dm");
			}else{
				//participantes.remove(p.getName());
				tpSpawn(p);
				p.sendMessage("§8§l[Deathmatch] §cVocê foi kickado do evento deathmatch");
			}
		}
		else {
			tpSpawn(p);
			if(motive==0) {
				//participantes.remove(p.getName());
				tpSpawn(p);
				p.sendMessage("§8§l[Deathmatch] §eVocê saiu do evento deathmatch");
			}else if(motive==1)
				p.sendMessage("§8§l[Deathmatch] §eVocê morreu no evento deathmatch");
			else if(motive==3) {
				//participantes.remove(p.getName());
				tpSpawn(p);
				p.sendMessage("§8§l[Deathmatch] §cVocê foi kickado do evento deathmatch");
			}
		}
	}
	
	protected void sendMessageDeathmatch(String msg) {
		for(String n : participantes)
			getServer().getPlayer(n).sendMessage(msg);
	}
}
