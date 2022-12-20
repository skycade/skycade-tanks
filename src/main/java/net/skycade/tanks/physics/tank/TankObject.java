package net.skycade.tanks.physics.tank;

import java.util.List;
import java.util.UUID;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.physics.GravitationallyAffectedPhysicsObject;
import net.skycade.tanks.physics.PhysicsObject;
import net.skycade.tanks.physics.ground.GroundObject;

public class TankObject extends GravitationallyAffectedPhysicsObject {

  /**
   * The entity id that represents the tank.
   */
  private UUID refId;

  /**
   * The constructor for the physics object.
   *
   * @param position     the position of the object.
   * @param velocity     the velocity of the object.
   * @param acceleration the acceleration of the object.
   * @param referenceId  the entity id that represents the tank.
   */
  public TankObject(Pos position, Vec velocity, Vec acceleration, UUID referenceId) {
    super(position, velocity, acceleration, 1D, 1D);
    this.refId = referenceId;
  }

  /**
   * The constructor for the physics object.
   *
   * @param position     the position of the object.
   * @param velocity     the velocity of the object.
   * @param acceleration the acceleration of the object.
   * @param objectId     the object's id.
   * @param referenceId  the entity id that represents the tank.
   */
  public TankObject(Pos position, Vec velocity, Vec acceleration, UUID objectId, UUID referenceId) {
    super(position, velocity, acceleration, 1D, 0.5D, objectId);
    this.refId = referenceId;
  }

  /**
   * Gets the reference id that represents the tank.
   *
   * @return the reference id that represents the tank.
   */
  public UUID previousRefId() {
    return refId;
  }

  /**
   * Sets the entity id that represents the tank.
   *
   * @param previousRefId the entity id that represents the tank.
   */
  public void refId(UUID previousRefId) {
    this.refId = previousRefId;
  }

  @Override
  public void applyForces(TankGameBoard board) {
    // get the objects that are colliding with the tank.
    List<PhysicsObject> collidingObjects = board.getCollidingObjects(this);

    // cancel any z movement
    this.velocity(this.velocity().withZ(0D));

    if (collidingObjects.stream().noneMatch(object -> object instanceof GroundObject)) {
      return;
    }

    // get the direction of the wall.
    Pos direction = collidingObjects.stream().filter(object -> object instanceof GroundObject)
        .map(object -> object.position().sub(this.position())).reduce(Pos::add)
        .orElse(new Pos(0, 0, 0));

    // get the position of the colliding object.
    Pos collidingObjectPosition = collidingObjects.stream()
        .filter(object -> object instanceof GroundObject).map(PhysicsObject::position)
        .reduce(Pos::add).orElse(new Pos(0, 0, 0));

    // if the tank is colliding left/right, call jump.
    if (direction.x() > 1.3 || direction.x() < -1.3) {
      this.jumpUp();
    }


    // if the wall is below, then apply a force to the tank to push it away from the wall.
    if (direction.y() < 0) {
      this.velocity(new Vec(this.velocity().x(), 0, this.velocity().z()));

      // if there is lateral movement, then apply friction to the tank.
      if (this.velocity().x() != 0) {
        this.velocity(new Vec(this.velocity().x() * 0.3, this.velocity().y(), this.velocity().z()));
      }
    }
  }

  /**
   * Jumps the tank upwards.
   */
  private void jumpUp() {
//    this.velocity(this.velocity().add(new Vec(0, 2, 0)));
    this.position(this.position().add(new Pos(0, 2, 0)));
  }
}
