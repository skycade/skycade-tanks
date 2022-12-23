package net.skycade.tanks.physics.tank;

import java.util.List;
import java.util.UUID;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.physics.GravitationallyAffectedPhysicsObject;
import net.skycade.tanks.physics.PhysicsObject;
import net.skycade.tanks.physics.edge.BoardEdgeObject;
import net.skycade.tanks.physics.ground.GroundObject;

public class TankObject extends GravitationallyAffectedPhysicsObject {

  /**
   * The entity id that represents the tank.
   */
  private UUID tankRefId;

  /**
   * The constructor for the physics object.
   *
   * @param position     the position of the object.
   * @param velocity     the velocity of the object.
   * @param acceleration the acceleration of the object.
   * @param objectId     the object's id.
   * @param tankRefId    the entity id that represents the tank.
   */
  public TankObject(Pos position, Vec velocity, Vec acceleration, UUID objectId, UUID tankRefId) {
    super(position, velocity, acceleration, 1D, 0.5D, objectId);
    this.tankRefId = tankRefId;
  }

  /**
   * Gets the reference id that represents the tank.
   *
   * @return the reference id that represents the tank.
   */
  public UUID tankRefId() {
    return tankRefId;
  }

  /**
   * Sets the entity id that represents the tank.
   *
   * @param refId the entity id that represents the tank.
   */
  public void tankRefId(UUID refId) {
    this.tankRefId = refId;
  }

  @Override
  public void applyForces(TankGameBoard board) {
    // get the objects that are colliding with the tank.
    List<PhysicsObject> collidingObjects = board.getCollidingObjects(this);

    // cancel any z movement
    this.velocity(this.velocity().withZ(0D));

    if (collidingObjects.stream()
        .noneMatch(object -> object instanceof GroundObject || object instanceof BoardEdgeObject)) {
      return;
    }

    // if one of the colliding objects is below the tank, cancel the y movement.
    if (collidingObjects.stream().anyMatch(object -> object.position().y() < this.position().y())) {
      this.velocity(this.velocity().withY(0D));

      // if there is lateral movement, then apply friction to the tank.
      if (this.velocity().x() != 0) {
        this.velocity(new Vec(this.velocity().x() * 0.6, this.velocity().y(), this.velocity().z()));
      }
    }

    // if one of the colliding objects is near the left or right of the tank, cancel the x movement.
    // also make sure it's near the same y level as the tank so that it doesn't cancel movement when
    // the tank is on the ground.
    if (collidingObjects.stream().anyMatch(
        object -> Math.abs(object.position().x() - this.position().x()) <= 0.5 &&
            Math.abs(object.position().y() - this.position().y()) <= 0.5)) {
      this.velocity(new Vec(0D, this.velocity().y(), this.velocity().z()));
      // if none of the objects is a board edge, then jump.
      if (collidingObjects.stream().noneMatch(object -> object instanceof BoardEdgeObject)) {
        jumpUp();
      }
    }
  }

  /**
   * Jumps the tank upwards.
   */
  private void jumpUp() {
//    this.velocity(this.velocity().add(new Vec(0, 2, 0)));
    this.position(this.position().add(new Pos(0, 1, 0)));
  }
}
