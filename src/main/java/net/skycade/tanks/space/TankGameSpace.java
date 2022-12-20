package net.skycade.tanks.space;

import dev.emortal.nbstom.NBS;
import java.nio.file.Path;
import java.util.UUID;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.instance.InstanceTickEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.AnvilLoader;
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

  public TankGameSpace(TankGameBoard board) {
    super(UUID.randomUUID(), FullbrightDimension.INSTANCE);
    this.boardRenderer = new GameSpaceTankGameBoardRenderer(this, board);
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
  }
}
