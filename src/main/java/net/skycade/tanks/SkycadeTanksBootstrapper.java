package net.skycade.tanks;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.utils.NamespaceID;
import net.skycade.serverruntime.api.Game;
import net.skycade.serverruntime.api.ServerRuntime;
import net.skycade.serverruntime.api.event.EventHandler;
import net.skycade.tanks.command.GamemodeCommand;
import net.skycade.tanks.handler.MinecraftSignHandler;
import net.skycade.tanks.session.SessionCoordinator;
import net.skycade.tanks.session.TankGameSession;
import net.skycade.tanks.space.SessionGameSpaceCoordinator;
import net.skycade.tanks.space.TankGameSpace;

public class SkycadeTanksBootstrapper extends Game {

  /**
   * Main method for the game.
   *
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    new SkycadeTanksBootstrapper();
  }

  /**
   * Constructor for the game.
   */
  public SkycadeTanksBootstrapper() {
    this.init();
  }

  @Override
  public void onCompleteStartup() {
    MinecraftServer.getBlockManager()
        .registerHandler(NamespaceID.from("minecraft:sign"), MinecraftSignHandler::new);
  }

  /**
   * Session game space coordinator.
   */
  private final SessionGameSpaceCoordinator sessionGameSpaceCoordinator =
      new SessionGameSpaceCoordinator();

  /**
   * Session coordinator.
   */
  private final SessionCoordinator sessionCoordinator = new SessionCoordinator();

  @Override
  protected List<Command> commands() {
    ServerRuntime.LOGGER.info("Registering commands...");
    return List.of(new GamemodeCommand());
  }

  @Override
  protected List<EventNode<Event>> eventNodes() {
    return List.of(new EventHandler<>(EventNode.all("global-" + UUID.randomUUID())) {
      @Override
      protected void init() {
      }
    }.node());
  }

  @Override
  protected List<InstanceContainer> instances() {
    return Collections.emptyList();
  }

  @Override
  protected InstanceContainer provideSpawningInstance(Player player) {
    // if the player is not in a session, create a new session and a new game space for them
    if (sessionCoordinator.isPlayerInSession(player.getUuid())) {
      TankGameSession session =
          sessionCoordinator.getSessionByPlayer(player.getUuid()).orElseThrow();

      return sessionGameSpaceCoordinator.getGameSpace(session.id());
    }

    TankGameSession tankGameSession =
        sessionCoordinator.createSession(player.getUuid(), UUID.randomUUID());
    TankGameSpace tankGameSpace = sessionGameSpaceCoordinator.createGameSpace(tankGameSession.id(),
        tankGameSession.gameBoard());
    // register the game space
    MinecraftServer.getInstanceManager().registerInstance(tankGameSpace);

    return tankGameSpace;
  }
}