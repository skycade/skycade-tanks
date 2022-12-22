package net.skycade.tanks.board;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.skycade.tanks.physics.PhysicsObject;
import net.skycade.tanks.physics.tank.TankObject;

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
   * Player 1's tank.
   */
  private final UUID player1TankObjectId;

  /**
   * Player 2's tank.
   */
  private final UUID player2TankObjectId;

  /**
   * The turret angle of player 1's tank.
   */
  private Vec player1TankTurretAngle;

  /**
   * The turret angle of player 2's tank.
   */
  private Vec player2TankTurretAngle;

  /**
   * Creates a new tank game board.
   *
   * @param bottomLeft the bottom left corner of the board.
   * @param topRight   the top right corner of the board.
   */
  public TankGameBoard(Pos bottomLeft, Pos topRight) {
    this.bottomLeft = bottomLeft;
    this.topRight = topRight;
    this.player1TankObjectId = UUID.randomUUID();
    this.player2TankObjectId = UUID.randomUUID();
    this.player1TankTurretAngle = new Vec(0, 0, 0);
    this.player2TankTurretAngle = new Vec(0, 0, 0);

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

  /**
   * Gets the player 1 tank object id.
   *
   * @return the player 1 tank object id.
   */
  public TankObject player1Tank() {
    return physicsObjects.stream().filter(physicsObject -> physicsObject instanceof TankObject)
        .map(physicsObject -> (TankObject) physicsObject)
        .filter(physicsObject -> physicsObject.objectId().equals(player1TankObjectId)).findFirst()
        .orElse(null);
  }

  /**
   * Gets the player 2 tank.
   *
   * @return the player 2 tank.
   */
  public PhysicsObject player2Tank() {
    return physicsObjects.stream().filter(physicsObject -> physicsObject instanceof TankObject)
        .map(physicsObject -> (TankObject) physicsObject)
        .filter(physicsObject -> physicsObject.objectId().equals(player2TankObjectId)).findFirst()
        .orElse(null);
  }

  /**
   * Gets the player 1 tank object id.
   *
   * @return the player 1 tank object id.
   */
  public UUID player1TankObjectId() {
    return player1TankObjectId;
  }

  /**
   * Gets the player 2 tank object id.
   *
   * @return the player 2 tank object id.
   */
  public UUID player2TankObjectId() {
    return player2TankObjectId;
  }

  /**
   * Gets the physics object colliding with the given physics object.
   *
   * @param physicsObject the physics object to check for collisions.
   * @return the physics object colliding with the given physics object.
   */
  public List<PhysicsObject> getCollidingObjects(PhysicsObject physicsObject) {
    return physicsObjects.stream().filter(physicsObject1 -> physicsObject1 != physicsObject)
        .filter(physicsObject1 -> physicsObject1.collidesWith(physicsObject)).toList();
  }

  /**
   * Gets the player 1 tank turret angle.
   *
   * @return the player 1 tank turret angle.
   */
  public Vec player1TankTurretAngle() {
    return player1TankTurretAngle;
  }

  /**
   * Gets the player 2 tank turret angle.
   *
   * @return the player 2 tank turret angle.
   */
  public Vec player2TankTurretAngle() {
    return player2TankTurretAngle;
  }

  /**
   * Sets the player 1 tank turret angle.
   *
   * @param player1TankTurretAngle the player 1 tank turret angle.
   */
  public void player1TankTurretAngle(Vec player1TankTurretAngle) {
    this.player1TankTurretAngle = player1TankTurretAngle;
  }

  /**
   * Sets the player 2 tank turret angle.
   *
   * @param player2TankTurretAngle the player 2 tank turret angle.
   */
  public void player2TankTurretAngle(Vec player2TankTurretAngle) {
    this.player2TankTurretAngle = player2TankTurretAngle;
  }

  /**
   * Gets the player 1 tank turret position.
   *
   * @return the player 1 tank turret position.
   */
  public Pos player1TankTurretPosition() {
    return player1Tank().position().add(player1TankTurretAngle);
  }
}
