package net.skycade.tanks.board.renderer;

import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.tanks.board.TankGameBoard;

/**
 * Represents a tank game renderer.
 *
 * @param <C> the type of context.
 */
public abstract class TankGameRenderer<C> {

  /**
   * The game space to render the board to.
   */
  private final GameSpace gameSpace;

  /**
   * The board to render.
   */
  private final TankGameBoard board;

  /**
   * Creates a new renderer
   *
   * @param gameSpace the game space to render the board to.
   * @param board     the board to render.
   */
  public TankGameRenderer(GameSpace gameSpace, TankGameBoard board) {
    this.gameSpace = gameSpace;
    this.board = board;
  }

  /**
   * Gets the game space to render the board to.
   *
   * @return the game space to render the board to.
   */
  public GameSpace gameSpace() {
    return gameSpace;
  }

  /**
   * Gets the board to render.
   *
   * @return the board to render.
   */
  public TankGameBoard board() {
    return board;
  }

  /**
   * Renders based on the context of the subclass.
   *
   * @param context the context.
   */
  public abstract void render(C context);
}
