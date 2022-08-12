CHRegionChange
=====
Provides a `region_change` event for CommandHelper when using WorldGuard.

#### region_change
Fires when a player moves to a block with a different region set than they are currently in.
* `player`
* `from` locationArray
* `to` locationArray
* `fromRegions` An array that may contain regions the player is leaving.
* `toRegions` An array that may contain regions the player is entering.
* `type` The type of event that triggered this. (RESPAWN, EMBARK, MOVE, GLIDE, SWIM, TELEPORT, RIDE, OTHER_NON_CANCELLABLE, or OTHER_CANCELLABLE)