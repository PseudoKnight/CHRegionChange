package com.zeoldcraft.chworldguard.abstraction.events.bukkit;

import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.bukkit.BukkitMCLocation;
import com.laytonsmith.abstraction.bukkit.entities.BukkitMCPlayer;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Target;
import com.zeoldcraft.chworldguard.abstraction.events.WGRegionChangeEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class BukkitWGRegionChangeEvent extends Event implements WGRegionChangeEvent, Cancellable {

	Player player;
	Location to;
	Location from;
	List<String> toRegions;
	List<String> fromRegions;
	boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();

	public BukkitWGRegionChangeEvent(Player pl, List<String> fromNames, List<String> toNames, Location f, Location t) {
		player = pl;
		toRegions = toNames;
		fromRegions = fromNames;
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
		for (String reg : fromRegions) {
			fromNames.push(new CString(reg, t), t);
		}
		return fromNames;
	}

	@Override
	public CArray getToRegions(Target t) {
		CArray toNames = new CArray(t);
		for (String reg : toRegions) {
			toNames.push(new CString(reg, t), t);
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
