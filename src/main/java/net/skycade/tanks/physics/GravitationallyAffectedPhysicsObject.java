package net.skycade.tanks.physics;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

/**
 * The gravitationally affected physics object is responsible for representing a physics object
 * that is affected by gravity, but is not affected by any other forces.
 *
 * @author Jacob Cohen
 */
public class GravitationallyAffectedPhysicsObject extends PhysicsObject {

  /**
   * The constructor for the physics object.
   *
   * @param position     the position of the object.
   * @param velocity     the velocity of the object.
   * @param acceleration the acceleration of the object.
   * @param mass         the mass of the object.
   */
  public GravitationallyAffectedPhysicsObject(Pos position, Vec velocity, Vec acceleration,
                                              double mass) {
    super(position, velocity, acceleration, mass);
  }
}
