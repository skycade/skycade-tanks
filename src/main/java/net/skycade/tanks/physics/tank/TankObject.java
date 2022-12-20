package net.skycade.tanks.physics.tank;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.skycade.tanks.physics.GravitationallyAffectedPhysicsObject;

public class TankObject extends GravitationallyAffectedPhysicsObject {

  /**
   * The constructor for the physics object.
   *
   * @param position     the position of the object.
   * @param velocity     the velocity of the object.
   * @param acceleration the acceleration of the object.
   */
  public TankObject(Pos position, Vec velocity, Vec acceleration) {
    super(position, velocity, acceleration, 1D);
  }
}
