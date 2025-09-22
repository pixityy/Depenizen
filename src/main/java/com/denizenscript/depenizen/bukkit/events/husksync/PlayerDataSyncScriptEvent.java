package com.denizenscript.depenizen.bukkit.events.husksync;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.william278.husksync.event.DataSaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDataSyncScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player data syncs
    // husksync player data syncs
    // husksync player data saves
    //
    // @Location true
    //
    // @Cancellable true
    //
    // @Triggers when a player's data is synchronized/saved with HuskSync.
    //
    // @Context
    // <context.save_cause> returns the cause of the data save. 
    // Will be one of: 'DISCONNECT', 'WORLD_SAVE', 'SERVER_SHUTDOWN', 'API', 'LEGACY_MIGRATION', 'MPDB_MIGRATION'.
    // <context.user_uuid> returns the UUID of the user whose data is being synced.
    // <context.user_username> returns the username of the user whose data is being synced.
    //
    // @Plugin Depenizen, HuskSync
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public PlayerDataSyncScriptEvent() {
        registerCouldMatcher("player data syncs");
        registerCouldMatcher("husksync player data syncs");
        registerCouldMatcher("husksync player data saves");
    }

    public DataSaveEvent event;
    public PlayerTag player;
    public String saveCause;
    public String userUUID;
    public String userName;

    @Override
    public boolean matches(ScriptPath path) {
        if (player == null || !runInCheck(path, player.getLocation())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        return switch (name) {
            case "save_cause" -> new ElementTag(saveCause);
            case "user_uuid" -> new ElementTag(userUUID);
            case "user_username" -> new ElementTag(userName);
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onPlayerDataSync(DataSaveEvent event) {
        // Check if there's an associated player
        if (event.getUser().getPlayer().isEmpty()) {
            return;
        }
        
        org.bukkit.entity.Player bukkitPlayer = event.getUser().getPlayer().get();
        if (EntityTag.isNPC(bukkitPlayer)) {
            return;
        }
        
        player = PlayerTag.mirrorBukkitPlayer(bukkitPlayer);
        saveCause = event.getSaveCause().name();
        userUUID = event.getUser().getUuid().toString();
        userName = event.getUser().getUsername();
        this.event = event;
        fire(event);
    }
}