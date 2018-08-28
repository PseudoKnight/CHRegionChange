package com.zeoldcraft.chworldguard.abstraction.events.bukkit;

import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class CHWorldGuardListener implements Listener {

	public CHWorldGuardListener(CommandHelperPlugin chp) {
		chp.registerEvents(this);
	}

	static RegionContainer GetRegionContainer() {
		return WorldGuard.getInstance().getPlatform().getRegionContainer();
	}
	
	public void unregister() {
		PlayerMoveEvent.getHandlerList().unregister(this);
		VehicleMoveEvent.getHandlerList().unregister(this);
		PlayerTeleportEvent.getHandlerList().unregister(this);
		VehicleEnterEvent.getHandlerList().unregister(this);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (testRegionChange(event.getPlayer(), event.getFrom(), event.getTo())) {
			// Use WG's method of cancelling this event
			Location newLoc = event.getFrom();
			newLoc.setX(newLoc.getBlockX() + 0.5);
			newLoc.setY(newLoc.getBlockY());
			newLoc.setZ(newLoc.getBlockZ() + 0.5);
			newLoc.setPitch(event.getTo().getPitch());
			newLoc.setYaw(event.getTo().getYaw());
			event.setTo(newLoc);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onVehicleMove(VehicleMoveEvent event) {
		Vehicle vehicle = event.getVehicle();
		List<Entity> passengers = vehicle.getPassengers();
		if(!passengers.isEmpty() && passengers.get(0) instanceof Player) {
			if(testRegionChange((Player) passengers.get(0), event.getFrom(), event.getTo())) {
				vehicle.setVelocity(new Vector(0, 0, 0));
				vehicle.teleport(event.getFrom());
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(testRegionChange(event.getPlayer(), event.getFrom(), event.getTo())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerEnterVehicle(VehicleEnterEvent event) {
		Entity entity = event.getEntered();
		if(entity instanceof Player) {
			if(testRegionChange((Player) entity, entity.getLocation(), event.getVehicle().getLocation())) {
				event.setCancelled(true);
			}
		}
	}

	private static boolean testRegionChange(Player player, Location from, Location to){
		if(from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()){
			RegionQuery query = GetRegionContainer().createQuery();
			ApplicableRegionSet fromSet = query.getApplicableRegions(BukkitAdapter.adapt(from));
			ApplicableRegionSet toSet = query.getApplicableRegions(BukkitAdapter.adapt(to));
			if(regionsChanged(fromSet, toSet)){
				BukkitWGRegionChangeEvent rgchange = new BukkitWGRegionChangeEvent(player, fromSet, toSet, from, to);
				EventUtils.TriggerListener(Driver.EXTENSION, "region_change", rgchange);
				if(rgchange.isCancelled()) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean regionsChanged(ApplicableRegionSet from, ApplicableRegionSet to){
		if(from.size() != to.size()){
			return(true);
		}
		Set<ProtectedRegion> toSet = to.getRegions();
		for(ProtectedRegion region : from){
			if(!toSet.contains(region)){
				return(true);
			}
		}
		return(false);
	}

	private static boolean regionsChanged(List<String> from, List<String> to){
		if(from.size() != to.size()){
			return(true);
		}
		for(String id : from){
			if(!to.contains(id)){
				return(true);
			}
		}
		return(false);
	}
}
