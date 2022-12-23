package net.skycade.tanks.space;

import dev.emortal.nbstom.NBS;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.InstanceTickEvent;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.AnvilLoader;
import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.serverruntime.space.dimension.FullbrightDimension;
import net.skycade.tanks.board.BoardAndSpaceConstants;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.board.renderer.GameSpaceTankGameBoardRenderer;
import net.skycade.tanks.board.turn.BoardTurn;
import net.skycade.tanks.board.turn.TankState;
import net.skycade.tanks.board.turn.TankTurnTracker;
import net.skycade.tanks.calculator.PlayerMovementDirection;
import net.skycade.tanks.calculator.PlayerMovementDirectionCalculator;
import net.skycade.tanks.physics.tank.TankObject;
import net.skycade.tanks.space.utils.TankGameSpaceCalculationUtils;

/**
 * This game space is used for the tank game.
 */
public class TankGameSpace extends GameSpace {

  /**
   * Creates a new tank game board renderer.
   */
  private final GameSpaceTankGameBoardRenderer boardRenderer;

  /**
   * The board that is being rendered.
   */
  private final TankGameBoard board;

  private BossBar previousBossBar;

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
      player.teleport(board.getPlayerSpawn(player.getUuid()));
      player.setGameMode(GameMode.ADVENTURE);

      // todo: make this a manageable resource, not hardcoded
      NBS nbs = new NBS(Path.of("./tanks_game_song.nbs"));
      NBS.Companion.play(nbs, player);
    });

    scheduleNextTick((instance) -> {
      // initialize the board through the board renderer
      boardRenderer.initializeBoard();

      MinecraftServer.getSchedulerManager().buildTask(() -> {
        final long ramUsage =
            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024;
        if (this.previousBossBar != null) {
          Audiences.players().hideBossBar(this.previousBossBar);
        }
        this.previousBossBar = BossBar.bossBar(
            Component.text("RAM usage: " + ramUsage + " MB", NamedTextColor.RED),
            1.0f,
            BossBar.Color.RED,
            BossBar.Overlay.PROGRESS
        );
        Audiences.players().showBossBar(this.previousBossBar);
      }).delay(Duration.ofMillis(500)).schedule();

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
        event.setCancelled(true);
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

        Vec newTurretVector = targetState.turretVector().add(0, 0, -Math.PI / 48);

        double zTranslatedToContextOfPlayer =
            TankGameSpaceCalculationUtils.translateTurretAngleIntoContextOfPlayerTurn(
                board.currentTurn(), newTurretVector.z());

        if (zTranslatedToContextOfPlayer > BoardAndSpaceConstants.MAX_Z_TURRET_ANGLE ||
            zTranslatedToContextOfPlayer < BoardAndSpaceConstants.MIN_Z_TURRET_ANGLE) {
          return;
        }

        // rotate the turret on the negative unit circle
        targetState.turretVector(targetState.turretVector().add(0, 0, -Math.PI / 48));
        turnTracker.incrementTurretRotationMoves();
      } else if (direction == PlayerMovementDirection.BACKWARD) {
        if (turnTracker.hasMaxTurretRotationMoves()) {
          return;
        }

        Vec newTurretVector = targetState.turretVector().add(0, 0, Math.PI / 48);

        double zTranslatedToContextOfPlayer =
            TankGameSpaceCalculationUtils.translateTurretAngleIntoContextOfPlayerTurn(
                board.currentTurn(), newTurretVector.z());

        if (zTranslatedToContextOfPlayer > BoardAndSpaceConstants.MAX_Z_TURRET_ANGLE ||
            zTranslatedToContextOfPlayer < BoardAndSpaceConstants.MIN_Z_TURRET_ANGLE) {
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
    }).addListener(PlayerHandAnimationEvent.class, event -> {
      Player player = event.getPlayer();
      // if it's not the player's turn, do nothing
      if (!board.isTurn(player.getUuid())) {
        return;
      }

      // get the block location that the player is looking at
      Point point = player.getTargetBlockPosition(100);

      if (point == null) {
        return;
      }

      // if the player clicked on the firing power bar
      if (TankGameSpaceCalculationUtils.clickedBlockIsOnFiringPowerBar(point,
          board.currentTurn())) {
        // get the firing power that the player clicked on
        double firingPower = TankGameSpaceCalculationUtils.getFiringPowerBasedOnBlock(point,
            board.currentTurn());

        // set the firing power
        board.turnTracker().setFiringPower(firingPower);
      }
    });
  }
}
