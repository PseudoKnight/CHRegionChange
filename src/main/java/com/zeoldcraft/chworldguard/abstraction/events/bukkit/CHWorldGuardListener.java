package com.zeoldcraft.chworldguard.abstraction.events.bukkit;

import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
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

import java.util.Set;

public class CHWorldGuardListener implements Listener {

	public CHWorldGuardListener(CommandHelperPlugin chp) {
		chp.registerEvents(this);
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
		Entity passenger = vehicle.getPassenger();
		if(passenger != null && passenger instanceof Player) {
			if(testRegionChange((Player) passenger, event.getFrom(), event.getTo())) {
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

	private boolean testRegionChange(Player player, Location from, Location to) {
		// As WG does, check if they are even in a different block
		if(from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()){
			World fromworld = from.getWorld();
			World toWorld = to.getWorld();
			Set<ProtectedRegion> fromSet = WGBukkit.getRegionManager(fromworld).getApplicableRegions(from).getRegions();
			Set<ProtectedRegion> toSet = WGBukkit.getRegionManager(toWorld).getApplicableRegions(to).getRegions();
			if(fromSet.size() != toSet.size() && fromSet.hashCode() != toSet.hashCode()){
				BukkitWGRegionChangeEvent rgchange = new BukkitWGRegionChangeEvent(player, fromSet, toSet, from, to);
				EventUtils.TriggerListener(Driver.EXTENSION, "region_change", rgchange);
				if(rgchange.isCancelled()) {
					return true;
				}
			}
		}
		return false;
	}
}
