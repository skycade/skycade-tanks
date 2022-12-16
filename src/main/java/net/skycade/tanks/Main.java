package net.skycade.tanks;

import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.InstanceContainer;
import net.skycade.serverruntime.api.Game;
import net.skycade.serverruntime.api.ServerRuntime;
import net.skycade.serverruntime.api.event.GameEventHandler;
import net.skycade.tanks.command.GamemodeCommand;
import net.skycade.tanks.space.TestGameSpace;

public class Main extends Game {

  /**
   * Main method for the game.
   *
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    new Main();
  }

  /**
   * Constructor for the game.
   */
  public Main() {
    this.init();
  }

  @Override
  protected List<Command> commands() {
    ServerRuntime.LOGGER.info("Registering commands...");
    return List.of(new GamemodeCommand());
  }

  @Override
  protected List<EventNode<Event>> eventNodes() {
    return List.of(new GameEventHandler("tanks-test") {
      @Override
      protected void init() {
        this.on(PlayerLoginEvent.class, event -> {
          event.getPlayer().setRespawnPoint(new Pos(0, 20, 0));
        });
        this.on(PlayerChatEvent.class, event -> {
          event.setChatFormat(
              (e) -> Component.text(e.getPlayer().getUsername(), NamedTextColor.GRAY)
                  .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                  .append(Component.text(e.getMessage())));
        });
      }
    }.node());
  }

  @Override
  protected List<InstanceContainer> instances() {
    return Collections.singletonList(TestGameSpace.INSTANCE);
  }

  @Override
  protected InstanceContainer provideSpawningInstance(Player player) {
    return TestGameSpace.INSTANCE;
  }
}