# CHRegionChange
Provides a `region_change` event for CommandHelper when using WorldGuard.

## Downloads and Compatibility
CHRegionChange 2.2.0 for CommandHelper 3.3.5  
CHRegionChange 2.1.1 for CommandHelper 3.3.4  
CHRegionChange 1.1.0 for CommandHelper 3.3.2

## Event Documentation

### region_change
Fires when a player moves to a block with a different region set than they are currently in.
#### Prefilters
#### Event Data
* `player`
* `from` locationArray
* `to` locationArray
* `fromRegions` An array that may contain regions the player is leaving.
* `toRegions` An array that may contain regions the player is entering.
* `type` The type of event that triggered this. (RESPAWN, EMBARK, MOVE, GLIDE, SWIM, TELEPORT, RIDE, OTHER_NON_CANCELLABLE, or OTHER_CANCELLABLE)
#### Mutable Fields
