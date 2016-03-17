package com.zeoldcraft.chworldguard;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import com.zeoldcraft.chworldguard.abstraction.events.bukkit.CHWorldGuardListener;

@MSExtension("CHWorldGuard")
public class CHWorldGuard extends AbstractExtension {

	private static CHWorldGuardListener dl;

	public Version getVersion() {
		return new SimpleVersion(1,1,0);
	}

	@Override
	public void onStartup() {
		CommandHelperPlugin chp = CommandHelperPlugin.self;
		dl = new CHWorldGuardListener(chp);
		System.out.println("CHWorldGuard " + getVersion() + " loaded.");
	}

	@Override
	public void onShutdown() {
		dl.unregister();
		System.out.println("CHWorldGuard " + getVersion() + " unloaded.");
	}
}
