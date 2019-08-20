package me.pseudoknight.chregionchange;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import org.bukkit.Bukkit;

import java.util.logging.Level;

@MSExtension("CHRegionChange")
public class CHRegionChange extends AbstractExtension {
	
	private boolean registered = false;

	public Version getVersion() {
		return new SimpleVersion(2,0,0);
	}
	
	@Override
	public void onStartup() {
		if(!registered) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(CommandHelperPlugin.self, () -> {
				try {
					WorldGuardPlatform wg = WorldGuard.getInstance().getPlatform();
					registered = wg.getSessionManager().registerHandler(RegionChangeHandler.FACTORY, null);
				} catch (NullPointerException ex) {
					Static.getLogger().log(Level.SEVERE, "Failed to register WorldGuard handler. WG hasn't enabled yet?");
				}
			});
		}
		System.out.println("CHRegionChange " + getVersion() + " loaded.");
	}

	@Override
	public void onShutdown() {
		System.out.println("CHRegionChange " + getVersion() + " unloaded.");
	}
}
