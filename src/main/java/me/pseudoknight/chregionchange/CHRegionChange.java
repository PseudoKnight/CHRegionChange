package me.pseudoknight.chregionchange;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;

import java.util.logging.Level;

@MSExtension("CHRegionChange")
public class CHRegionChange extends AbstractExtension {

	public Version getVersion() {
		return new SimpleVersion(2,1,0);
	}

	@Override
	public void onStartup() {
		RegionChangeHandler.Register();
		Static.getLogger().log(Level.INFO, "CHRegionChange " + getVersion() + " loaded.");
	}

	@Override
	public void onShutdown() {
		Static.getLogger().log(Level.INFO, "CHRegionChange " + getVersion() + " unloaded.");
	}
}
