package net.skycade.tanks.space;

import dev.emortal.nbstom.NBS;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.InstanceTickEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.TaskSchedule;
import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.serverruntime.space.dimension.FullbrightDimension;
import net.skycade.tanks.board.BoardAndSpaceConstants;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.board.control.TankBoardControl;
import net.skycade.tanks.board.control.TankBoardControlUtils;
import net.skycade.tanks.board.renderer.GameSpaceTankGameBoardRenderer;
import net.skycade.tanks.board.turn.BoardTurn;
import net.skycade.tanks.board.turn.TankState;
import net.skycade.tanks.board.turn.TankTurnTracker;
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
        this.previousBossBar =
            BossBar.bossBar(Component.text("RAM usage: " + ramUsage + " MB", NamedTextColor.RED),
                1.0f, BossBar.Color.RED, BossBar.Overlay.PROGRESS);
        Audiences.players().showBossBar(this.previousBossBar);
      }).delay(Duration.ofMillis(500)).schedule();

      // every tick, render the next frame of the board
      eventNode().addListener(InstanceTickEvent.class, event -> {
        boardRenderer.tick();
      });
    });

    eventNode().addListener(PlayerBlockInteractEvent.class, event -> {
      Player player = event.getPlayer();
      // if it's not the player's turn, do nothing
      if (!board.isTurn(player.getUuid())) {
        return;
      }

      Block block = event.getBlock();
      Point position = event.getBlockPosition();

      if (!isApplicableButton(block)) {
        return;
      }

      // handle button press behavior
      handleButtonClickBehavior(block, position);

      TankTurnTracker turnTracker = board.turnTracker();

      TankState targetState = board.currentTurn() == BoardTurn.PLAYER_1 ? board.player1TankState() :
          board.player2TankState();
      TankObject targetTank =
          board.currentTurn() == BoardTurn.PLAYER_1 ? board.player1Tank() : board.player2Tank();

      TankBoardControl control =
          TankBoardControlUtils.getControlFromClickedButtonPosition(position, board.currentTurn());

      // if the player moved forward
      if (control == TankBoardControl.AIM_UP) {
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
      } else if (control == TankBoardControl.AIM_DOWN) {
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
      } else if (control == TankBoardControl.MOVE_LEFT) {
        if (turnTracker.hasMaxPositionMoves()) {
          return;
        }

        // push the tank left
        targetTank.velocity(targetTank.velocity().add(-0.2, 0, 0));
        turnTracker.incrementPositionMoves();
      } else if (control == TankBoardControl.MOVE_RIGHT) {
        if (turnTracker.hasMaxPositionMoves()) {
          return;
        }

        // push the tank right
        targetTank.velocity(targetTank.velocity().add(0.2, 0, 0));
        turnTracker.incrementPositionMoves();
      }
    });
  }

  /**
   * Handles a clicked block
   *
   * @param block    block
   * @param position position
   */
  private void handleButtonClickBehavior(Block block, Point position) {
    if (!isApplicableButton(block)) {
      return;
    }

    // in the next tick, imitate vanilla button behavior
    scheduleNextTick((instance) -> {
      if (Objects.equals(block.getProperty("powered"), "false")) {
        instance.setBlock(position, block.withProperties(
            Map.ofEntries(Map.entry("face", block.getProperty("face")),
                Map.entry("powered", "true"))));

        instance.scheduler().buildTask(() -> {
          instance.setBlock(position, block.withProperties(
              Map.ofEntries(Map.entry("face", block.getProperty("face")),
                  Map.entry("powered", "false"))));
          Objects.requireNonNull(getChunkAt(position)).sendChunk();
        }).delay(TaskSchedule.millis(50)).schedule();
      }
    });
  }

  /**
   * If a block is applicable to the vanilla button behavior
   *
   * @param block block
   * @return true if applicable
   */
  private boolean isApplicableButton(Block block) {
    return block.compare(Block.OAK_BUTTON) || block.compare(Block.SPRUCE_BUTTON) ||
        block.compare(Block.BIRCH_BUTTON) || block.compare(Block.JUNGLE_BUTTON) ||
        block.compare(Block.ACACIA_BUTTON) || block.compare(Block.DARK_OAK_BUTTON) ||
        block.compare(Block.CRIMSON_BUTTON) || block.compare(Block.WARPED_BUTTON);
  }
}
