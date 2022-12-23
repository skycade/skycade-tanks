package net.skycade.tanks.space.utils;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.block.Block;
import net.skycade.tanks.board.BoardAndSpaceConstants;
import net.skycade.tanks.board.turn.BoardTurn;

public class TankGameSpaceCalculationUtils {

  /**
   * Converts the given vector to a comparable number in radians that can be used to calculate
   * if the vector is pointing in a certain direction based on the current turn.
   *
   * @param turn          the current turn.
   * @param initialZAngle the vector to convert.
   * @return the converted vector.
   */
  public static double translateTurretAngleIntoContextOfPlayerTurn(BoardTurn turn,
                                                                   double initialZAngle) {
    double zTranslatedToContextOfPlayer =
        turn == BoardTurn.PLAYER_1 ? initialZAngle : initialZAngle + Math.PI;
    // convert the z value (it's in radians) from (0, 360) to (-180, 180)
    return zTranslatedToContextOfPlayer > Math.PI ? zTranslatedToContextOfPlayer - 2 * Math.PI :
        zTranslatedToContextOfPlayer;
  }

  /**
   * If a clicked block is on the firing power bar
   *
   * @param clicked clicked
   * @param turn    turn
   * @return if clicked block is on the firing power bar
   */
  public static boolean clickedBlockIsOnFiringPowerBar(Point clicked, BoardTurn turn) {
    // get the x and z coordinates of the clicked block
    double xClicked = clicked.x();
    double zClicked = clicked.z();

    // get the x and z coordinates of the firing power bar
    double xFiringPowerBar =
        turn == BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_BOTTOM.x() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_BOTTOM.x();

    double zFiringPowerBar =
        turn == BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_BOTTOM.z() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_BOTTOM.z();

    // if the clicked block is on the firing power bar
    return xClicked == xFiringPowerBar && zClicked == zFiringPowerBar;
  }

  /**
   * Gets the firing power based on the clicked block.
   *
   * @param clicked the clicked block.
   * @param turn    the current turn.
   * @return the firing power.
   */
  public static double getFiringPowerBasedOnBlock(Point clicked, BoardTurn turn) {
    // get the y coordinate of the clicked block
    double yClicked = clicked.y();

    // get the y coordinates of the firing power bar
    double yBottomFiringPowerBar =
        turn == BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_BOTTOM.y() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_BOTTOM.y();

    double yTopFiringPowerBar =
        turn == BoardTurn.PLAYER_1 ? BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_TOP.y() :
            BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_TOP.y();

    // subtract to find how many steps the firing power bar is
    double firingPowerBarSteps = yTopFiringPowerBar - yBottomFiringPowerBar;
    // subtract to find how many steps the clicked block is
    double clickedBlockSteps = yClicked - yBottomFiringPowerBar;

    double minFiringPower = BoardAndSpaceConstants.MIN_FIRING_POWER;
    double maxFiringPower = BoardAndSpaceConstants.MAX_FIRING_POWER;

    // calculate the firing power based on the clicked block
    return minFiringPower +
        (maxFiringPower - minFiringPower) * (clickedBlockSteps / firingPowerBarSteps);
  }

  /**
   * Gets the block to use (low, med, high) based on the firing power.
   * <p>
   * Split into three sections: low, med, high.
   *
   * @param firingPower the firing power.
   * @return the block to use.
   */
  public static Block getFiringPowerBlock(double firingPower) {
    double minFiringPower = BoardAndSpaceConstants.MIN_FIRING_POWER;
    double maxFiringPower = BoardAndSpaceConstants.MAX_FIRING_POWER;

    // calculate the firing power based on the clicked block
    double firingPowerPercentage =
        (firingPower - minFiringPower) / (maxFiringPower - minFiringPower);

    // if the firing power is less than 33% of the max firing power
    if (firingPowerPercentage < 0.33) {
      return BoardAndSpaceConstants.FIRING_POWER_SPECIFIED_LOW;
    }
    // if the firing power is less than 66% of the max firing power
    else if (firingPowerPercentage < 0.66) {
      return BoardAndSpaceConstants.FIRING_POWER_SPECIFIED_MEDIUM;
    }
    // if the firing power is less than 100% of the max firing power
    else {
      return BoardAndSpaceConstants.FIRING_POWER_SPECIFIED_HIGH;
    }
  }
}
