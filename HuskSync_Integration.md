# HuskSync Bridge Implementation

This implementation adds comprehensive HuskSync support to Depenizen, providing integration with HuskSync's data synchronization API.

## Features Implemented

### Bridge Registration
- HuskSyncBridge registered in Depenizen.java
- HuskSync dependency added to pom.xml
- Proper error handling and API initialization

### Player Properties
- `<PlayerTag.husksync_is_syncing>` - Returns whether player is currently syncing
- `<PlayerTag.husksync_user_exists>` - Returns whether player has a HuskSync profile

### Global Tags
- `<husksync.is_enabled>` - Returns whether HuskSync is enabled
- `<husksync.version>` - Returns HuskSync plugin version

### Events
- `player data syncs` / `husksync player data syncs` - Triggers on data save
- `player pre syncs data` / `husksync player pre syncs data` - Triggers before sync

### Commands
- `husksync save` - Forces a save of player data

## Usage Examples

### Listen for data synchronization:
```yaml
sync_listener:
  type: world
  events:
    on husksync player data syncs:
    - narrate "Your data was synchronized! Cause: <context.save_cause>"
```

### Check if player is syncing:
```yaml
check_sync_status:
  type: task
  script:
  - if <player.husksync_is_syncing>:
    - narrate "Please wait, your data is being synchronized..."
```

### Force save player data:
```yaml
force_save:
  type: task
  script:
  - husksync save
  - narrate "Your data has been saved!"
```

## Technical Details

The implementation follows Depenizen's established patterns:
- Bridge extends Bridge class and implements init() method
- Properties implement Property interface with proper tag handlers
- Events extend BukkitScriptEvent with EventHandler methods
- Commands extend AbstractCommand with autoExecute pattern

All code includes comprehensive error handling and follows the documentation standards used by other Depenizen bridges.