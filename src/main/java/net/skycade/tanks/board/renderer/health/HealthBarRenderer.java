package net.skycade.tanks.board.renderer.health;

import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.board.renderer.TankGameRenderer;

public class HealthBarRenderer extends TankGameRenderer<Void> {

  /**
   * Creates a new renderer
   *
   * @param gameSpace the game space to render the board to.
   * @param board     the board to render.
   */
  public HealthBarRenderer(GameSpace gameSpace, TankGameBoard board) {
    super(gameSpace, board);
  }

  @Override
  public void render(Void context) {
  }
}
