package com.zeoldcraft.chworldguard.abstraction.events;

import com.laytonsmith.abstraction.MCLocation;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.BindableEvent;

public interface WGRegionChangeEvent extends BindableEvent {
	public MCPlayer getPlayer();
	public MCLocation getFrom();
	public MCLocation getTo();
	public CArray getFromRegions(Target t);
	public CArray getToRegions(Target t);
}
