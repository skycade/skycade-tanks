package net.skycade.tanks.board.renderer.power;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.tanks.board.BoardAndSpaceConstants;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.board.renderer.TankGameRenderer;
import net.skycade.tanks.board.turn.BoardTurn;
import net.skycade.tanks.space.utils.TankGameSpaceCalculationUtils;

public class FiringPowerBarRenderer extends TankGameRenderer<Void> {

  /**
   * Creates a new renderer
   *
   * @param gameSpace the game space to render the board to.
   * @param board     the board to render.
   */
  public FiringPowerBarRenderer(GameSpace gameSpace, TankGameBoard board) {
    super(gameSpace, board);
  }

  @Override
  public void render(Void context) {
    // first render for the player whose turn it is not
    BoardTurn turn = board().currentTurn();

    int xMinNotTurn =
        turn != BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_BOTTOM.blockX() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_BOTTOM.blockX();
    int xMaxNotTurn =
        turn != BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_TOP.blockX() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_TOP.blockX();

    int yMinNotTurn =
        turn != BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_BOTTOM.blockY() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_BOTTOM.blockY();
    int yMaxNotTurn =
        turn != BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_TOP.blockY() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_TOP.blockY();

    int zNotTurn =
        turn != BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_BOTTOM.blockZ() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_BOTTOM.blockZ();

    // then render for the player whose turn it is
    int xMinTurn =
        turn == BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_BOTTOM.blockX() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_BOTTOM.blockX();
    int xMaxTurn =
        turn == BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_TOP.blockX() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_TOP.blockX();

    int yMinTurn =
        turn == BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_BOTTOM.blockY() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_BOTTOM.blockY();
    int yMaxTurn =
        turn == BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_TOP.blockY() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_TOP.blockY();

    int zTurn =
        turn == BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_BOTTOM.blockZ() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_BOTTOM.blockZ();

    // render the not turn player's bar (vertical)
    for (int x = xMinNotTurn; x <= xMaxNotTurn; x++) {
      for (int y = yMinNotTurn; y <= yMaxNotTurn; y++) {
        gameSpace().setBlock(x, y, zNotTurn, BoardAndSpaceConstants.FIRING_POWER_UNSPECIFIED);
      }
    }

    double firingPower = board().turnTracker().firingPower();

    // render the turn player's bar (vertical)
    for (int x = xMinTurn; x <= xMaxTurn; x++) {
      for (int y = yMinTurn; y <= yMaxTurn; y++) {
        // get the firing power for this block
        double power =
            TankGameSpaceCalculationUtils.getFiringPowerBasedOnBlock(new Pos(x, y, zTurn), turn);
        // if the power is greater than the firing power, render the block as specified
        if (power > firingPower) {
          gameSpace().setBlock(x, y, zTurn, BoardAndSpaceConstants.FIRING_POWER_UNSPECIFIED);
        } else {
          // calculate the block to use (low, med, high)
          Block block = TankGameSpaceCalculationUtils.getFiringPowerBlock(power);
          gameSpace().setBlock(x, y, zTurn, block);
        }
      }
    }
  }
}
