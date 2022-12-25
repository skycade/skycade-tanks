package net.skycade.tanks.board.control;

import net.minestom.server.coordinate.Point;
import net.skycade.tanks.board.BoardAndSpaceConstants;
import net.skycade.tanks.board.turn.BoardTurn;

public class TankBoardControlUtils {

  /**
   * Gets the tank board control for the given point.
   *
   * @param buttonPoint the button point that was clicked.
   * @param turn        the turn.
   * @return the tank board control.
   */
  public static TankBoardControl getControlFromClickedButtonPosition(Point buttonPoint,
                                                                     BoardTurn turn) {
    if (turn == BoardTurn.PLAYER_1) {
      if (buttonPoint.sameBlock(BoardAndSpaceConstants.PLAYER_1_MOVE_LEFT_BUTTON_POINT)) {
        return TankBoardControl.MOVE_LEFT;
      } else if (buttonPoint.sameBlock(BoardAndSpaceConstants.PLAYER_1_MOVE_RIGHT_BUTTON_POINT)) {
        return TankBoardControl.MOVE_RIGHT;
      } else if (buttonPoint.sameBlock(BoardAndSpaceConstants.PLAYER_1_AIM_UP_BUTTON_POINT)) {
        return TankBoardControl.AIM_UP;
      } else if (buttonPoint.sameBlock(BoardAndSpaceConstants.PLAYER_1_AIM_DOWN_BUTTON_POINT)) {
        return TankBoardControl.AIM_DOWN;
      } else if (buttonPoint.sameBlock(BoardAndSpaceConstants.PLAYER_1_FIRE_BUTTON_POINT)) {
        return TankBoardControl.FIRE;
      } else if (buttonPoint.sameBlock(
          BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_INCREASE_BUTTON_POINT)) {
        return TankBoardControl.INCREASE_POWER;
      } else if (buttonPoint.sameBlock(
          BoardAndSpaceConstants.PLAYER_1_FIRING_POWER_DECREASE_BUTTON_POINT)) {
        return TankBoardControl.DECREASE_POWER;
      }
    } else {
      if (buttonPoint.sameBlock(BoardAndSpaceConstants.PLAYER_2_MOVE_LEFT_BUTTON_POINT)) {
        return TankBoardControl.MOVE_LEFT;
      } else if (buttonPoint.sameBlock(BoardAndSpaceConstants.PLAYER_2_MOVE_RIGHT_BUTTON_POINT)) {
        return TankBoardControl.MOVE_RIGHT;
      } else if (buttonPoint.sameBlock(BoardAndSpaceConstants.PLAYER_2_AIM_UP_BUTTON_POINT)) {
        return TankBoardControl.AIM_UP;
      } else if (buttonPoint.sameBlock(BoardAndSpaceConstants.PLAYER_2_AIM_DOWN_BUTTON_POINT)) {
        return TankBoardControl.AIM_DOWN;
      } else if (buttonPoint.sameBlock(BoardAndSpaceConstants.PLAYER_2_FIRE_BUTTON_POINT)) {
        return TankBoardControl.FIRE;
      } else if (buttonPoint.sameBlock(
          BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_INCREASE_BUTTON_POINT)) {
        return TankBoardControl.INCREASE_POWER;
      } else if (buttonPoint.sameBlock(
          BoardAndSpaceConstants.PLAYER_2_FIRING_POWER_DECREASE_BUTTON_POINT)) {
        return TankBoardControl.DECREASE_POWER;
      }
    }

    return null;
  }
}
