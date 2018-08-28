package com.zeoldcraft.chworldguard.abstraction.events.bukkit;

import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.bukkit.BukkitMCLocation;
import com.laytonsmith.abstraction.bukkit.entities.BukkitMCPlayer;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Target;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.zeoldcraft.chworldguard.abstraction.events.WGRegionChangeEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitWGRegionChangeEvent extends Event implements WGRegionChangeEvent, Cancellable {

	private Player player;
	private Location to;
	private Location from;
	private ApplicableRegionSet toRegions;
	private ApplicableRegionSet fromRegions;
	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();

	public BukkitWGRegionChangeEvent(Player pl, ApplicableRegionSet fromSet, ApplicableRegionSet toSet, Location f, Location t) {
		player = pl;
		toRegions = toSet;
		fromRegions = fromSet;
		from = f;
		to = t;
	}

	@Override
	public Object _GetObject() {
		return this;
	}

	@Override
	public MCLocation getFrom() {
		return new BukkitMCLocation(from);
	}

	@Override
	public MCLocation getTo() {
		return new BukkitMCLocation(to);
	}

	@Override
	public CArray getFromRegions(Target t) {
		CArray fromNames = new CArray(t);
		for (ProtectedRegion reg : fromRegions) {
			fromNames.push(new CString(reg.getId(), t), t);
		}
		return fromNames;
	}

	@Override
	public CArray getToRegions(Target t) {
		CArray toNames = new CArray(t);
		for (ProtectedRegion reg : toRegions) {
			toNames.push(new CString(reg.getId(), t), t);
		}
		return toNames;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	@Override
	public MCPlayer getPlayer() {
		return new BukkitMCPlayer(player);
	}
}
