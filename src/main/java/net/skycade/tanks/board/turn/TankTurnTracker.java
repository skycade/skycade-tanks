package net.skycade.tanks.board.turn;

import net.skycade.tanks.board.BoardAndSpaceConstants;

public class TankTurnTracker {

  /**
   * The maximum amount of position moves that can be made in a turn.
   */
  private static final int MAX_POSITION_MOVES = 20;

  /**
   * The maximum amount of rotation moves that can be made in a turn.
   */
  private static final int MAX_TURRET_ROTATION_MOVES = 50;

  /**
   * The number of position moves that have been made in the current turn.
   */
  private int positionMovesDone;

  /**
   * The number of turret rotation moves that have been made.
   */
  private int turretRotationMovesDone;

  /**
   * The firing power of the tank.
   */
  private double firingPower;

  /**
   * Creates a new turn tracker.
   */
  public TankTurnTracker() {
    this.positionMovesDone = 0;
    this.turretRotationMovesDone = 0;
    this.firingPower = BoardAndSpaceConstants.MIN_FIRING_POWER;
  }

  /**
   * Increments the number of position moves that have been made.
   */
  public void incrementPositionMoves() {
    this.positionMovesDone++;
  }

  /**
   * Increments the number of turret rotation moves that have been made.
   */
  public void incrementTurretRotationMoves() {
    this.turretRotationMovesDone++;
  }

  /**
   * Gets the number of position moves left in the current turn.
   */
  public int positionMovesLeft() {
    return MAX_POSITION_MOVES - positionMovesDone;
  }

  /**
   * Gets the number of turret rotation moves left in the current turn.
   */
  public int turretRotationMovesLeft() {
    return MAX_TURRET_ROTATION_MOVES - turretRotationMovesDone;
  }

  /**
   * Checks if the maximum amount of position moves have been made.
   *
   * @return true if the maximum amount of position moves have been made.
   */
  public boolean hasMaxPositionMoves() {
    return this.positionMovesDone >= MAX_POSITION_MOVES;
  }

  /**
   * Checks if the maximum amount of turret rotation moves have been made.
   *
   * @return true if the maximum amount of turret rotation moves have been made.
   */
  public boolean hasMaxTurretRotationMoves() {
    return this.turretRotationMovesDone >= MAX_TURRET_ROTATION_MOVES;
  }

  /**
   * Gets the firing power of the tank.
   */
  public double firingPower() {
    return firingPower;
  }

  /**
   * If the firing power is at max
   *
   * @return true if the firing power is at max
   */
  public boolean isAtMaxFiringPower() {
    return firingPower >= BoardAndSpaceConstants.MAX_FIRING_POWER;
  }

  /**
   * If the firing power is at min
   *
   * @return true if the firing power is at min
   */
  public boolean isAtMinFiringPower() {
    return firingPower <= BoardAndSpaceConstants.MIN_FIRING_POWER;
  }

  /**
   * Increments the firing power.
   */
  public void incrementFiringPower() {
    // get the max and min, and increment the firing power by 1/10th of the difference
    double max = BoardAndSpaceConstants.MAX_FIRING_POWER;
    double min = BoardAndSpaceConstants.MIN_FIRING_POWER;
    firingPower += (max - min) / 10;
  }

  /**
   * Decrements the firing power.
   */
  public void decrementFiringPower() {
    // get the max and min, and decrement the firing power by 1/10th of the difference
    double max = BoardAndSpaceConstants.MAX_FIRING_POWER;
    double min = BoardAndSpaceConstants.MIN_FIRING_POWER;
    firingPower -= (max - min) / 10;
  }

  /**
   * Resets the turn tracker.
   */
  public void reset() {
    this.positionMovesDone = 0;
    this.turretRotationMovesDone = 0;
    this.firingPower = 0;
  }
}
