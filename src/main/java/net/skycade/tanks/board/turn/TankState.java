package net.skycade.tanks.board.turn;

import java.util.UUID;
import net.minestom.server.coordinate.Vec;

public class TankState {

  /**
   * The tank's physics object id.
   */
  private UUID objectId;

  /**
   * The tank's turret physics object id.
   */
  private Vec turretVector;

  /**
   * Constructs a new tank state.
   *
   * @param objectId     the tank's physics object id.
   * @param turretVector the tank's turret physics object id.
   */
  public TankState(UUID objectId, Vec turretVector) {
    this.objectId = objectId;
    this.turretVector = turretVector;
  }

  /**
   * Constructs a new tank state.
   */
  public TankState() {
    this(UUID.randomUUID(), Vec.ZERO);
  }

  /**
   * Gets the tank's physics object id.
   *
   * @return the tank's physics object id.
   */
  public UUID objectId() {
    return objectId;
  }

  /**
   * Gets the tank's turret physics object id.
   *
   * @return the tank's turret physics object id.
   */
  public Vec turretVector() {
    return turretVector;
  }

  /**
   * Sets the tank's physics object id.
   *
   * @param objectId the tank's physics object id.
   */
  public void objectId(UUID objectId) {
    this.objectId = objectId;
  }

  /**
   * Sets the tank's turret physics object id.
   *
   * @param turretVector the tank's turret physics object id.
   */
  public void turretVector(Vec turretVector) {
    this.turretVector = turretVector;
  }
}
