package me.pseudoknight.chregionchange;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;

@MSExtension("CHRegionChange")
public class CHRegionChange extends AbstractExtension {

	public Version getVersion() {
		return new SimpleVersion(2,2,0);
	}

	@Override
	public void onStartup() {
		RegionChangeHandler.Register();
		Static.getLogger().info("CHRegionChange " + getVersion() + " loaded.");
	}

	@Override
	public void onShutdown() {
		Static.getLogger().info("CHRegionChange " + getVersion() + " unloaded.");
	}
}
