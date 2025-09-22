package com.denizenscript.depenizen.bukkit.bridges;

import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.properties.PropertyParser;
import com.denizenscript.depenizen.bukkit.Bridge;
import com.denizenscript.depenizen.bukkit.events.husksync.PlayerDataSyncScriptEvent;
import com.denizenscript.depenizen.bukkit.properties.husksync.HuskSyncPlayerProperties;
import net.william278.husksync.api.HuskSyncAPI;

public class HuskSyncBridge extends Bridge {

    // <--[language]
    // @name HuskSync Bridge
    // @group Depenizen Bridges
    // @plugin Depenizen, HuskSync
    // @description
    // HuskSync is a modern, cross-server player data synchronization system.
    // 
    // This bridge provides access to HuskSync's data synchronization features,
    // allowing scripts to interact with player data across multiple servers.
    // 
    // You can listen for data synchronization events and access player sync data
    // through properties and tags.
    //
    // -->

    public static HuskSyncAPI huskSyncAPI;

    @Override
    public void init() {
        huskSyncAPI = HuskSyncAPI.getInstance();
        
        // Register properties for PlayerTag to access HuskSync data
        PropertyParser.registerProperty(HuskSyncPlayerProperties.class, PlayerTag.class);
        
        // Register script events for HuskSync data synchronization
        ScriptEvent.registerScriptEvent(PlayerDataSyncScriptEvent.class);
    }
}