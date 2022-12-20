package net.skycade.tanks.physics;

import java.util.UUID;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.skycade.tanks.board.TankGameBoard;

/**
 * This is an object that represents a physical object in the game. This includes the position,
 * velocity, and acceleration of the object.
 *
 * @author Jacob Cohen
 */
public abstract class PhysicsObject {

  /**
   * The object's id.
   */
  private final UUID objectId;

  /**
   * The position of the object.
   */
  private Pos position;

  /**
   * The velocity of the object.
   */
  private Vec velocity;

  /**
   * The acceleration of the object.
   * <p>
   * The acceleration is the rate of change of the velocity of the object.
   * The force of gravity is not included in the acceleration of the object, because it
   * is applied in the game loop.
   */
  private Vec acceleration;

  /**
   * The mass of the object, in kilograms.
   */
  private double mass;

  /**
   * The radius of the object, in meters.
   */
  private final double radius;

  /**
   * The constructor for the physics object.
   *
   * @param position     the position of the object.
   * @param velocity     the velocity of the object.
   * @param acceleration the acceleration of the object.
   * @param mass         the mass of the object.
   * @param radius       the radius of the object.
   */
  public PhysicsObject(Pos position, Vec velocity, Vec acceleration, double mass, double radius) {
    this(position, velocity, acceleration, mass, radius, UUID.randomUUID());
  }

  /**
   * The constructor for the physics object.
   *
   * @param position     the position of the object.
   * @param velocity     the velocity of the object.
   * @param acceleration the acceleration of the object.
   * @param mass         the mass of the object.
   * @param objectId     the object's id.
   * @param radius       the radius of the object.
   */
  public PhysicsObject(Pos position, Vec velocity, Vec acceleration, double mass, double radius,
                       UUID objectId) {
    this.objectId = objectId;
    this.position = position;
    this.velocity = velocity;
    this.acceleration = acceleration;
    this.mass = mass;
    this.radius = radius;
  }

  /**
   * Gets the object's id.
   *
   * @return the object's id.
   */
  public UUID objectId() {
    return objectId;
  }

  /**
   * Get the position of the object.
   *
   * @return the position of the object.
   */
  public Pos position() {
    return position;
  }

  /**
   * Set the position of the object.
   *
   * @param position the new position of the object.
   */
  public void position(Pos position) {
    this.position = position;
  }

  /**
   * Get the velocity of the object.
   *
   * @return the velocity of the object.
   */
  public Vec velocity() {
    return velocity;
  }

  /**
   * Set the velocity of the object.
   *
   * @param velocity the new velocity of the object.
   */
  public void velocity(Vec velocity) {
    this.velocity = velocity;
  }

  /**
   * Get the acceleration of the object.
   *
   * @return the acceleration of the object.
   */
  public Vec acceleration() {
    return acceleration;
  }

  /**
   * Set the acceleration of the object.
   *
   * @param acceleration the new acceleration of the object.
   */
  public void acceleration(Vec acceleration) {
    this.acceleration = acceleration;
  }

  /**
   * Get the mass of the object.
   *
   * @return the mass of the object.
   */
  public double mass() {
    return mass;
  }

  /**
   * Set the mass of the object.
   *
   * @param mass the new mass of the object.
   */
  public void mass(double mass) {
    this.mass = mass;
  }

  /**
   * Get the radius of the object.
   *
   * @return the radius of the object.
   */
  public double radius() {
    return radius;
  }

  /**
   * Updates the object physics.
   */
  public void tickPhysics(TankGameBoard board) {
    // update the velocity of the object based on the acceleration.
    velocity = velocity.add(acceleration);
    // update the position of the object based on the velocity.
    position = position.add(velocity);
    this.update(board);
  }

  /**
   * Updates the physics object by applying the acceleration to the velocity, and the velocity to the
   * position.
   */
  public abstract void update(TankGameBoard board);

  /**
   * If the object is colliding with another object.
   *
   * @param other the other object.
   * @return if the object is colliding with another object.
   */
  public boolean collidesWith(PhysicsObject other) {
    return this.position.distance(other.position) <= this.radius + other.radius;
  }
}
