package net.skycade.tanks.physics;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

/**
 * This is an object that represents a physical object in the game. This includes the position,
 * velocity, and acceleration of the object.
 *
 * @author Jacob Cohen
 */
public class PhysicsObject {

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
   * The constructor for the physics object.
   *
   * @param position     the position of the object.
   * @param velocity     the velocity of the object.
   * @param acceleration the acceleration of the object.
   * @param mass         the mass of the object.
   */
  public PhysicsObject(Pos position, Vec velocity, Vec acceleration, double mass) {
    this.position = position;
    this.velocity = velocity;
    this.acceleration = acceleration;
    this.mass = mass;
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
}
