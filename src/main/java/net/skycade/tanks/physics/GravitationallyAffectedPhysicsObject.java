package net.skycade.tanks.physics;

import java.util.UUID;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.skycade.tanks.board.TankGameBoard;

/**
 * The gravitationally affected physics object is responsible for representing a physics object
 * that is affected by gravity, but is not affected by any other forces.
 *
 * @author Jacob Cohen
 */
public abstract class GravitationallyAffectedPhysicsObject extends PhysicsObject {

  /**
   * The constructor for the physics object.
   *
   * @param position     the position of the object.
   * @param velocity     the velocity of the object.
   * @param acceleration the acceleration of the object.
   * @param mass         the mass of the object.
   * @param radius       the radius of the object.
   */

  public GravitationallyAffectedPhysicsObject(Pos position, Vec velocity, Vec acceleration,
                                              double mass, double radius) {
    super(position, velocity, acceleration, mass, radius);
  }

  /**
   * The constructor for the physics object.
   *
   * @param position     the position of the object.
   * @param velocity     the velocity of the object.
   * @param acceleration the acceleration of the object.
   * @param mass         the mass of the object.
   * @param radius       the radius of the object.
   * @param objectId     the object's id.
   */
  public GravitationallyAffectedPhysicsObject(Pos position, Vec velocity, Vec acceleration,
                                              double mass, double radius, UUID objectId) {
    super(position, velocity, acceleration, mass, radius, objectId);
  }

  @Override
  public void update(TankGameBoard board) {
    // Apply gravity.
    this.velocity(this.velocity().add(0, -0.8, 0));
    this.applyForces(board);
  }

  /**
   * Applies the forces to the object.
   */
  public abstract void applyForces(TankGameBoard board);
}
