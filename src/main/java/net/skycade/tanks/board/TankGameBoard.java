package net.skycade.tanks.board;

import java.util.ArrayList;
import java.util.List;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.skycade.tanks.physics.PhysicsObject;

public class TankGameBoard {

  /**
   * The bottom left corner of the board.
   */
  private final Point bottomLeft;

  /**
   * The top right corner of the board.
   */
  private final Point topRight;

  /**
   * The physics objects on the board.
   */
  private final List<PhysicsObject> physicsObjects;

  /**
   * Creates a new tank game board.
   *
   * @param bottomLeft the bottom left corner of the board.
   * @param topRight   the top right corner of the board.
   */
  public TankGameBoard(Pos bottomLeft, Pos topRight) {
    this.bottomLeft = bottomLeft;
    this.topRight = topRight;

    this.physicsObjects = new ArrayList<>();
  }

  /**
   * Adds a physics object to the board.
   *
   * @param physicsObject the physics object to add.
   */
  public void addPhysicsObject(PhysicsObject physicsObject) {
    this.physicsObjects.add(physicsObject);
  }

  /**
   * Gets the bottom left corner of the board.
   *
   * @return the bottom left corner of the board.
   */
  public Point bottomLeft() {
    return bottomLeft;
  }

  /**
   * Gets the top right corner of the board.
   *
   * @return the top right corner of the board.
   */
  public Point topRight() {
    return topRight;
  }

  /**
   * Gets the physics objects on the board.
   *
   * @return the physics objects on the board.
   */
  public List<PhysicsObject> physicsObjects() {
    return physicsObjects;
  }
}
