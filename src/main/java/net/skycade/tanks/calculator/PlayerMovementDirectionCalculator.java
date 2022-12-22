package net.skycade.tanks.calculator;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

public class PlayerMovementDirectionCalculator {

  /**
   * Calculates the direction in which the player is moving.
   *
   * @param newPosition the new position of the player.
   * @param oldPosition the old position of the player.
   * @return the direction in which the player is moving.
   */
  public static PlayerMovementDirection calculate(Pos newPosition, Pos oldPosition) {
    // get the difference in position
    Vec deltaPosition = newPosition.asVec().sub(oldPosition.asVec());
    // get the angle between the delta and the x-axis
    double angle = -1 * Math.atan2(deltaPosition.z(), deltaPosition.x());
    // convert negative angles to positive
    if (angle < 0) {
      angle += 2 * Math.PI;
    }

    // get the yaw
    double yaw = -1 * Math.toRadians(oldPosition.yaw());
    // rotate -90 degrees
    yaw -= Math.PI / 2;
    // convert negative angles to positive
    if (yaw < 0) {
      yaw += 2 * Math.PI;
    }

    // get the angle between the delta-position and the yaw
    double angleDelta = angle - yaw;
    // convert the angle delta to a value between -pi and pi
    if (angleDelta > Math.PI) {
      angleDelta -= 2 * Math.PI;
    } else if (angleDelta < -Math.PI) {
      angleDelta += 2 * Math.PI;
    }

    // get the angle delta in degrees
    double angleDeltaDegrees = Math.toDegrees(angleDelta);
    // make sure the angle is between 0 and 360
    if (angleDeltaDegrees < 0) {
      angleDeltaDegrees += 360;
    }

    // if the angle is within 0.2 of 0, the player is moving forward
    if (isWithinRange(angleDeltaDegrees, 0, 5) || isWithinRange(angleDeltaDegrees, 360, 5)) {
      return PlayerMovementDirection.BACKWARD;
    } else if (isWithinRange(angleDeltaDegrees, 90, 5)) {
      return PlayerMovementDirection.RIGHT;
    } else if (isWithinRange(angleDeltaDegrees, 180, 5)) {
      return PlayerMovementDirection.FORWARD;
    } else if (isWithinRange(angleDeltaDegrees, 270, 5)) {
      return PlayerMovementDirection.LEFT;
    } else {
      return PlayerMovementDirection.CANT_CALCULATE;
    }
  }

  /**
   * If a value is within a certain range of a target, given padding.
   *
   * @param value   the value to check.
   * @param target  the target value.
   * @param padding the padding.
   * @return if the value is within the range.
   */
  private static boolean isWithinRange(double value, double target, double padding) {
    return value > target - padding && value < target + padding;
  }
}
