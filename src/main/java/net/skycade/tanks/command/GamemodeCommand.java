package net.skycade.tanks.command;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.skycade.serverruntime.api.message.Messenger;

public class GamemodeCommand extends Command {
  public GamemodeCommand() {
    super("gamemode");

    setCondition((sender, command) -> {
      if (!(sender instanceof ConsoleSender)) {
        if (command != null) {
          Messenger.warn(sender, "You must be the console to use this command.");
        }
        return false;
      }

      return true;
    });

    var playerArgument = ArgumentType.Word("player");
    var gamemodeArgument = ArgumentType.Word("gamemode");

    addSyntax((sender, context) -> {
      var player = context.get(playerArgument);
      var gamemode = context.get(gamemodeArgument);
      var target = MinecraftServer.getConnectionManager().getPlayer(player);
      if (target == null) {
        Messenger.warn(sender, "Player not found.");
        return;
      }

      try {
        target.setGameMode(GameMode.valueOf(gamemode.toUpperCase()));
      } catch (IllegalArgumentException e) {
        Messenger.warn(sender, "Invalid gamemode.");
      }

      Messenger.info(sender, "Set gamemode of " + target.getUsername() + " to " + gamemode + ".");
    }, playerArgument, gamemodeArgument);

    setDefaultExecutor((sender, context) ->

    {
      Messenger.warn(sender, "Usage: /gamemode <player> <gamemode>");
    });
  }
}
