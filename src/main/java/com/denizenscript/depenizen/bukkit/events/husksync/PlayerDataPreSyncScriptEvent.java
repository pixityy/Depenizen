package com.denizenscript.depenizen.bukkit.events.husksync;

import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import net.william278.husksync.event.PreSyncEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDataPreSyncScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player pre syncs data
    // husksync player pre syncs data
    // husksync player data pre loads
    //
    // @Location true
    //
    // @Cancellable true
    //
    // @Triggers when a player's data is about to be synchronized/loaded with HuskSync.
    //
    // @Context
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

    public PlayerDataPreSyncScriptEvent() {
        registerCouldMatcher("player pre syncs data");
        registerCouldMatcher("husksync player pre syncs data");
        registerCouldMatcher("husksync player data pre loads");
    }

    public PreSyncEvent event;
    public PlayerTag player;
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
            case "user_uuid" -> new ElementTag(userUUID);
            case "user_username" -> new ElementTag(userName);
            default -> super.getContext(name);
        };
    }

    @EventHandler
    public void onPlayerDataPreSync(PreSyncEvent event) {
        // Check if there's an associated player
        if (event.getUser().getPlayer().isEmpty()) {
            return;
        }
        
        org.bukkit.entity.Player bukkitPlayer = event.getUser().getPlayer().get();
        if (EntityTag.isNPC(bukkitPlayer)) {
            return;
        }
        
        player = PlayerTag.mirrorBukkitPlayer(bukkitPlayer);
        userUUID = event.getUser().getUuid().toString();
        userName = event.getUser().getUsername();
        this.event = event;
        fire(event);
    }
}