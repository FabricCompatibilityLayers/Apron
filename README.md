# Apron
Compatibility layer for RML, Froge/Refroged, ShockAhPI and friends. Built for Babric.

Only compatible with mods that use RML, Forge/Reforged, ShockAhPI, AudioMod, GUIAPI, ModOptionsAPI, ItemSpriteAPI and PlayerAPI. Base class edits are not supported unless they are converted to Mixin manually.

## Build
NOTE: You need to target Java 8 for all modules EXCEPT `apron-stapi-fixes`. On IntelliJ IDEA, press F4, then Project Settings > SDK and set it to JDK 8.

Run gradle build normally.
```
gradlew build
```
Compiled jar is stored in `build/libs`.
