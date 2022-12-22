package net.skycade.tanks.space;

import dev.emortal.nbstom.NBS;
import java.nio.file.Path;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.InstanceTickEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.AnvilLoader;
import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.serverruntime.space.dimension.FullbrightDimension;
import net.skycade.tanks.board.BoardTurn;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.board.TankState;
import net.skycade.tanks.board.TankTurnTracker;
import net.skycade.tanks.board.renderer.GameSpaceTankGameBoardRenderer;
import net.skycade.tanks.calculator.PlayerMovementDirection;
import net.skycade.tanks.calculator.PlayerMovementDirectionCalculator;
import net.skycade.tanks.physics.tank.TankObject;

/**
 * This game space is used for the tank game.
 */
public class TankGameSpace extends GameSpace {
  public static final Pos SPAWN_POSITION = new Pos(0, 25, 23, -180, 0);

  /**
   * Creates a new tank game board renderer.
   */
  private final GameSpaceTankGameBoardRenderer boardRenderer;

  /**
   * The board that is being rendered.
   */
  private final TankGameBoard board;

  public TankGameSpace(TankGameBoard board) {
    super(UUID.randomUUID(), FullbrightDimension.INSTANCE);
    this.boardRenderer = new GameSpaceTankGameBoardRenderer(this, board);
    this.board = board;
  }

  @Override
  public void init() {
    setChunkLoader(new AnvilLoader(Path.of("tanks")));
    setTimeRate(0);

    instanceBoundPlayerEventNode().addListener(PlayerSpawnEvent.class, event -> {
      Player player = event.getPlayer();
      player.teleport(SPAWN_POSITION);
      player.setGameMode(GameMode.CREATIVE);

      // todo: make this a manageable resource, not hardcoded
      NBS nbs = new NBS(Path.of("./tanks_game_song.nbs"));
      NBS.Companion.play(nbs, player);
    });

    scheduleNextTick((instance) -> {
      // initialize the board through the board renderer
      boardRenderer.initializeBoard();

      // every tick, render the next frame of the board
      eventNode().addListener(InstanceTickEvent.class, event -> {
        boardRenderer.tick();
      });
    });

    eventNode().addListener(PlayerMoveEvent.class, event -> {
      Player player = event.getPlayer();
      Pos oldPosition = event.getPlayer().getPosition();
      Pos newPosition = event.getNewPosition();
      Pos diff = newPosition.sub(oldPosition);

      PlayerMovementDirection direction =
          PlayerMovementDirectionCalculator.calculate(oldPosition, newPosition);

      // if the player moved in the x, z, or y direction, update the physics
      if (diff.x() != 0 || diff.z() != 0 || diff.y() != 0) {
//        event.setCancelled(true);
      }

      // if the player moving is not the current turn, do nothing
      if (event.getPlayer().getUuid() != board.uuidOfCurrentTurn()) {
        return;
      }

      // get the turn tracker
      TankTurnTracker turnTracker = board.turnTracker();

      player.sendActionBar(Component.text(
          "Moves left (pos, rot): " + turnTracker.positionMovesLeft() + ", " +
              turnTracker.turretRotationMovesLeft(), NamedTextColor.RED));

      TankState targetState = board.currentTurn() == BoardTurn.PLAYER_1 ? board.player1TankState() :
          board.player2TankState();
      TankObject targetTank =
          board.currentTurn() == BoardTurn.PLAYER_1 ? board.player1Tank() : board.player2Tank();

      // if the player moved forward
      if (direction == PlayerMovementDirection.FORWARD) {
        if (turnTracker.hasMaxTurretRotationMoves()) {
          return;
        }

        // rotate the turret on the negative unit circle
        targetState.turretVector(targetState.turretVector().add(0, 0, -Math.PI / 48));
        turnTracker.incrementTurretRotationMoves();
      } else if (direction == PlayerMovementDirection.BACKWARD) {
        if (turnTracker.hasMaxTurretRotationMoves()) {
          return;
        }

        // rotate the turret on the positive unit circle
        targetState.turretVector(targetState.turretVector().add(0, 0, Math.PI / 48));
        turnTracker.incrementTurretRotationMoves();
      } else if (direction == PlayerMovementDirection.LEFT) {
        if (turnTracker.hasMaxPositionMoves()) {
          return;
        }

        // push the tank left
        targetTank.velocity(targetTank.velocity().add(-0.2, 0, 0));
        turnTracker.incrementPositionMoves();
      } else if (direction == PlayerMovementDirection.RIGHT) {
        if (turnTracker.hasMaxPositionMoves()) {
          return;
        }

        // push the tank right
        targetTank.velocity(targetTank.velocity().add(0.2, 0, 0));
        turnTracker.incrementPositionMoves();
      }

      // if the player moved in the y direction
//      if (oldPosition.y() != newPosition.y()) {
//        // fire the tank turret
//        Pos explosionPosition = targetTank.position();
//
//        // fire the tank turret
//
//        ParticlePacket particlePacket =
//            ParticleCreator.createParticlePacket(Particle.EXPLOSION, explosionPosition.x(),
//                explosionPosition.y(), explosionPosition.z(), 0f, 0f, 0f, 1);
//        sendGroupedPacket(particlePacket);
//        // play the explosion sound
//        playSound(Sound.sound(Key.key("entity.generic.explode"), Sound.Source.MASTER, 1f, 1f));
//      }
    });
  }
}
