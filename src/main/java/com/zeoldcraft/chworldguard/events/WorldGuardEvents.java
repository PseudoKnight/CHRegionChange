package com.zeoldcraft.chworldguard.events;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.AbstractEvent;
import com.laytonsmith.core.events.BindableEvent;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.exceptions.EventException;
import com.laytonsmith.core.exceptions.PrefilterNonMatchException;
import com.laytonsmith.core.natives.interfaces.Mixed;
import com.zeoldcraft.chworldguard.abstraction.events.WGRegionChangeEvent;

import java.util.HashMap;
import java.util.Map;

public class WorldGuardEvents {
	
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
			if (event instanceof WGRegionChangeEvent) {
				WGRegionChangeEvent e = (WGRegionChangeEvent) event;
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
			return event instanceof WGRegionChangeEvent;
		}

		@Override
		public boolean modifyEvent(String key, Mixed value, BindableEvent event) {
			return false;
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}
	}
}
