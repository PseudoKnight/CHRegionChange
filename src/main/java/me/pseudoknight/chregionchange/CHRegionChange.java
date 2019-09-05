package me.pseudoknight.chregionchange;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;

@MSExtension("CHRegionChange")
public class CHRegionChange extends AbstractExtension {

	public Version getVersion() {
		return new SimpleVersion(2,0,1);
	}

	@Override
	public void onStartup() {
		RegionChangeHandler.Register();
		System.out.println("CHRegionChange " + getVersion() + " loaded.");
	}

	@Override
	public void onShutdown() {
		System.out.println("CHRegionChange " + getVersion() + " unloaded.");
	}
}
