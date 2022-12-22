package net.skycade.tanks.space;

import dev.emortal.nbstom.NBS;
import java.nio.file.Path;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.InstanceTickEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.serverruntime.space.dimension.FullbrightDimension;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.board.renderer.GameSpaceTankGameBoardRenderer;
import net.skycade.tanks.calculator.PlayerMovementDirection;
import net.skycade.tanks.calculator.PlayerMovementDirectionCalculator;

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
      Pos oldPosition = event.getPlayer().getPosition();
      Pos newPosition = event.getNewPosition();
      Pos diff = newPosition.sub(oldPosition);

      PlayerMovementDirection direction =
          PlayerMovementDirectionCalculator.calculate(oldPosition, newPosition);

      // if the player moved in the x, z, or y direction, update the physics
      if (diff.x() != 0 || diff.z() != 0 || diff.y() != 0) {
        event.setCancelled(true);
      }

      // if the player moved forward
      if (direction == PlayerMovementDirection.FORWARD) {
        // rotate the turret on the positive unit circle
        board.player1TankTurretAngle(board.player1TankTurretAngle().add(0, 0, 4));
      } else if (direction == PlayerMovementDirection.BACKWARD) {
        // rotate the turret on the negative unit circle
        board.player1TankTurretAngle(board.player1TankTurretAngle().add(0, 0, -4));
      } else if (direction == PlayerMovementDirection.LEFT) {
        // push the tank left
        board.player1Tank().velocity(board.player1Tank().velocity().add(-0.2, 0, 0));
      } else if (direction == PlayerMovementDirection.RIGHT) {
        // push the tank right
        board.player1Tank().velocity(board.player1Tank().velocity().add(0.2, 0, 0));
      }

      // if the player moved in the y direction
      if (oldPosition.y() != newPosition.y()) {
        // fire the tank turret
        Pos explosionPosition = board.player1Tank().position();

        ParticlePacket particlePacket =
            ParticleCreator.createParticlePacket(Particle.EXPLOSION, explosionPosition.x(),
                explosionPosition.y(), explosionPosition.z(), 0f, 0f, 0f, 1);
        sendGroupedPacket(particlePacket);
        // play the explosion sound
        playSound(Sound.sound(Key.key("entity.generic.explode"), Sound.Source.MASTER, 1f, 1f));
      }
    });
  }
}
