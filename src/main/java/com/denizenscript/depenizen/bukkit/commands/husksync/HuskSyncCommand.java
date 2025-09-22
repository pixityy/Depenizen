package com.denizenscript.depenizen.bukkit.commands.husksync;

import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsRuntimeException;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.depenizen.bukkit.bridges.HuskSyncBridge;
import net.william278.husksync.user.OnlineUser;
import org.bukkit.entity.Player;

import java.util.Optional;

public class HuskSyncCommand extends AbstractCommand {

    public HuskSyncCommand() {
        setName("husksync");
        setSyntax("husksync [save]");
        setRequiredArguments(1, 1);
        autoCompile();
    }

    // <--[command]
    // @Name HuskSync
    // @Syntax husksync [save]
    // @Group Depenizen
    // @Plugin Depenizen, HuskSync
    // @Required 1
    // @Maximum 1
    // @Short Triggers HuskSync operations for a player.
    //
    // @Description
    // This command allows you to trigger HuskSync operations for a player.
    // Currently supports:
    // - save: Forces a save of the player's data to the synchronization backend
    //
    // @Tags
    // <PlayerTag.husksync_is_syncing>
    // <PlayerTag.husksync_user_exists>
    // <husksync.is_enabled>
    // <husksync.version>
    //
    // @Usage
    // Use to force save a player's data.
    // - husksync save
    //
    // -->

    public enum Action {SAVE}

    public static void autoExecute(ScriptEntry scriptEntry,
                                   @ArgName("action") Action action) {
        if (!Utilities.entryHasPlayer(scriptEntry)) {
            throw new InvalidArgumentsRuntimeException("Missing linked player.");
        }
        
        if (HuskSyncBridge.huskSyncAPI == null) {
            Debug.echoError("HuskSync API is not available!");
            return;
        }
        
        Player player = Utilities.getEntryPlayer(scriptEntry).getPlayerEntity();
        Optional<OnlineUser> onlineUser = HuskSyncBridge.huskSyncAPI.getUser(player);
        
        if (onlineUser.isEmpty()) {
            Debug.echoError("Player " + player.getName() + " is not a valid HuskSync user!");
            return;
        }
        
        switch (action) {
            case SAVE -> {
                try {
                    HuskSyncBridge.huskSyncAPI.saveUserData(onlineUser.get());
                } catch (Exception e) {
                    Debug.echoError("Failed to save player data: " + e.getMessage());
                }
            }
        }
    }
}