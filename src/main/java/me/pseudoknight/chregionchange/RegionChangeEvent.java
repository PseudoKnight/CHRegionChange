package me.pseudoknight.chregionchange;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.bukkit.BukkitMCLocation;
import com.laytonsmith.abstraction.bukkit.entities.BukkitMCPlayer;
import com.laytonsmith.abstraction.events.MCPlayerRespawnEvent;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.events.AbstractEvent;
import com.laytonsmith.core.events.BindableEvent;
import com.laytonsmith.core.events.BoundEvent;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.exceptions.EventException;
import com.laytonsmith.core.exceptions.PrefilterNonMatchException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface RegionChangeEvent extends BindableEvent {
	MCPlayer getPlayer();
	MCLocation getFrom();
	MCLocation getTo();
	CArray getFromRegions(Target t);
	CArray getToRegions(Target t);
	boolean isCancelled();
	void setCancelled(boolean cancelled);

	public class RegionChangeEventImpl implements RegionChangeEvent, Cancellable {

		private Player player;
		private Location to;
		private Location from;
		private Set<ProtectedRegion> toRegions;
		private Set<ProtectedRegion> fromRegions;
		private boolean cancelled = false;

		public RegionChangeEventImpl(Player pl, Set<ProtectedRegion> fromSet, Set<ProtectedRegion> toSet, Location f, Location t) {
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
	
	@api
	public static class region_change extends AbstractEvent {

		@Override
		public BindableEvent convert(CArray arg, Target t) {
			throw new UnsupportedOperationException("This is not supported at this time.");
		}

		@Override
		public String docs() {
			return "{}"
					+ " Fires when a player moves to a block with a different region set than they are currently in."
					+ " {player | from: locationArray | to: locationArray | fromRegions: array of regions at the block"
					+ " they are coming from | toRegions: array of regions at the block they are moving to}"
					+ " {}"
					+ " {}";
		}

		@Override
		public Driver driver() {
			return Driver.EXTENSION;
		}

		@Override
		public Map<String, Mixed> evaluate(BindableEvent event) throws EventException {
			if (event instanceof RegionChangeEventImpl) {
				RegionChangeEventImpl e = (RegionChangeEventImpl) event;
				Target t = Target.UNKNOWN;
				Map<String, Mixed> ret = new HashMap<>();
				ret.put("player", new CString(e.getPlayer().getName(), t));
				ret.put("from", ObjectGenerator.GetGenerator().location(e.getFrom()));
				ret.put("to", ObjectGenerator.GetGenerator().location(e.getTo()));
				ret.put("fromRegions", e.getFromRegions(t));
				ret.put("toRegions", e.getToRegions(t));
				return ret;
			} else {
				throw new EventException("Not a proper region change event.");
			}
		}

		@Override
		public String getName() {
			return "region_change";
		}

		@Override
		public boolean matches(Map<String, Mixed> prefilter, BindableEvent event) throws PrefilterNonMatchException {
			return event instanceof RegionChangeEventImpl;
		}

		@Override
		public boolean modifyEvent(String key, Mixed value, BindableEvent event) {
			return false;
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}

		@Override
		public void preExecution(Environment env, BoundEvent.ActiveEvent activeEvent) {
			if(activeEvent.getUnderlyingEvent() instanceof RegionChangeEventImpl) {
				// sometimes the players is not online here, perhaps during certain events that trigger this
				MCPlayer player = ((RegionChangeEventImpl) activeEvent.getUnderlyingEvent()).getPlayer();
				Static.InjectPlayer(player);
			}
		}

		@Override
		public void postExecution(Environment env, BoundEvent.ActiveEvent activeEvent) {
			if(activeEvent.getUnderlyingEvent() instanceof RegionChangeEventImpl) {
				MCPlayer player = ((RegionChangeEventImpl) activeEvent.getUnderlyingEvent()).getPlayer();
				Static.UninjectPlayer(player);
			}
		}
	}
}
