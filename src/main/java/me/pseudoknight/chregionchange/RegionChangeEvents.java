package me.pseudoknight.chregionchange;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCPlayer;
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
import com.sk89q.worldguard.session.MoveType;

import java.util.HashMap;
import java.util.Map;

public class RegionChangeEvents {

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
					+ " {player | from: locationArray | to: locationArray | fromRegions:  An array that may contain"
					+ " regions the player is leaving | toRegions: An array that may contain regions the player is"
					+ " entering | type: The type of event that triggered this. (RESPAWN, EMBARK, MOVE, GLIDE,"
					+ " SWIM, TELEPORT, RIDE, OTHER_NON_CANCELLABLE, or OTHER_CANCELLABLE)}"
					+ " {}"
					+ " {}";
		}

		@Override
		public Driver driver() {
			return Driver.EXTENSION;
		}

		@Override
		public Map<String, Mixed> evaluate(BindableEvent event) throws EventException {
			if (event instanceof RegionChangeBindableEvent e) {
				Target t = Target.UNKNOWN;
				Map<String, Mixed> ret = new HashMap<>();
				ret.put("player", new CString(e.getPlayer().getName(), t));
				ret.put("from", ObjectGenerator.GetGenerator().location(e.getFrom()));
				ret.put("to", ObjectGenerator.GetGenerator().location(e.getTo()));
				ret.put("fromRegions", e.getFromRegions(t));
				ret.put("toRegions", e.getToRegions(t));
				ret.put("type", new CString(e.getMoveType().name(), t));
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
			return event instanceof RegionChangeBindableEvent;
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
				// sometimes the player is not online here (e.g. respawn events)
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
