package me.pseudoknight.chregionchange;

import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;

import java.util.Set;

public class RegionChangeHandler extends Handler {
	public static final RegionChangeHandler.Factory FACTORY = new RegionChangeHandler.Factory();
	public static class Factory extends Handler.Factory<RegionChangeHandler> {
		@Override
		public RegionChangeHandler create(Session session) {
			return new RegionChangeHandler(session);
		}
	}
	
	public RegionChangeHandler(Session session) {
		super(session);
	}

	@Override
	public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
		if(entered.size() > 0 || exited.size() > 0) {
			RegionChangeEvent event = new RegionChangeEvent.RegionChangeEventImpl(
					BukkitAdapter.adapt(player), exited, entered, BukkitAdapter.adapt(from), BukkitAdapter.adapt(to));
			EventUtils.TriggerListener(Driver.EXTENSION, "region_change", event);
			return !moveType.isCancellable() || !event.isCancelled();
		}
		return true;
	}
}