package net.skycade.tanks.space;

import dev.emortal.nbstom.NBS;
import java.nio.file.Path;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
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

      // if the player moved in the x, z, or y direction, update the physics
      if (oldPosition.blockX() != newPosition.blockX() ||
          oldPosition.blockZ() != newPosition.blockZ() ||
          oldPosition.blockY() != newPosition.blockY()) {
        event.setCancelled(true);
      }

      // if the player moved in the x direction
      if (oldPosition.x() != newPosition.x()) {
        Vec velocity = board.player1Tank().velocity();

        // if the player moved to the right
        if (oldPosition.x() < newPosition.x()) {
          // move the tank to the right
          board.player1Tank().velocity(new Vec(0.4, velocity.y(), velocity.z()));
        } else {
          // move the tank to the left
          board.player1Tank().velocity(new Vec(-0.4, velocity.y(), velocity.z()));
        }
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
