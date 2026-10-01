# Hide HUD Mod

Minecraft 1.20.1 client/server utility for hiding the complete in-game HUD from server commands.

## Commands

```
/hud hide @a
/hud show @a

/hud hide @a 5
/hud show @a 5

/hud hide @a 5 true
```

- The selector can target any players.
- The time is in seconds.
- Without a time, the state changes immediately.
- A timed hide/show uses a smooth screen fade transition.
- `true` on `hide` also hides the first-person hand.
- `show` restores the hand as well, regardless of how it was hidden.
- The normal permission level is 2.
- Builds are produced automatically by GitHub Actions for Fabric and Forge 1.20.1.

## HUD compatibility

The mod suppresses the vanilla HUD and loader/API HUD overlay paths rather than only hiding individual vanilla widgets. This is intended to also suppress HUD overlays such as minimaps when they render through the standard HUD/overlay pipeline, including Xaero's minimap on supported 1.20.1 installations.

No client command is required: the server command sends the HUD state to each targeted client.

## Build

Fabric uses Yarn mappings `1.20.1+build.10:v2` for the 1.20.1 source set.
