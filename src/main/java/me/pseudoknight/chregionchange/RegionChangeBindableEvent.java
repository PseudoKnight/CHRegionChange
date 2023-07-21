package me.pseudoknight.chregionchange;

import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.bukkit.BukkitMCLocation;
import com.laytonsmith.abstraction.bukkit.entities.BukkitMCPlayer;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.BindableEvent;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.Set;

public class RegionChangeBindableEvent implements BindableEvent, Cancellable {

	private final MoveType moveType;
	private final Player player;
	private final Location to;
	private final Location from;
	private final Set<ProtectedRegion> toRegions;
	private final Set<ProtectedRegion> fromRegions;
	private boolean cancelled = false;

	public RegionChangeBindableEvent(Player pl, Set<ProtectedRegion> fromSet, Set<ProtectedRegion> toSet, Location f, Location t, MoveType mt) {
		moveType = mt;
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

	public MCLocation getFrom() {
		return new BukkitMCLocation(from);
	}

	public MCLocation getTo() {
		return new BukkitMCLocation(to);
	}

	public CArray getFromRegions(Target t) {
		CArray fromNames = new CArray(t);
		for (ProtectedRegion reg : fromRegions) {
			fromNames.push(new CString(reg.getId(), t), t);
		}
		return fromNames;
	}

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

	public MCPlayer getPlayer() {
		return new BukkitMCPlayer(player);
	}

	public MoveType getMoveType() {
		return moveType;
	}
}
