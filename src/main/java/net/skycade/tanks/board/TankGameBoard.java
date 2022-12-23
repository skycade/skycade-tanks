package net.skycade.tanks.board;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.skycade.tanks.board.turn.BoardTurn;
import net.skycade.tanks.board.turn.TankState;
import net.skycade.tanks.board.turn.TankTurnTracker;
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
   * Player 1's tank state.
   */
  private final TankState player1TankState;

  /**
   * Player 2's tank state.
   */
  private final TankState player2TankState;

  /**
   * Player 1's uuid.
   */
  private final UUID player1Uuid;

  /**
   * Player 2's uuid.
   */
  private final UUID player2Uuid;

  /**
   * The current turn.
   */
  private BoardTurn currentTurn;

  /**
   * Tank turn tracker.
   */
  private TankTurnTracker turnTracker;

  /**
   * Creates a new tank game board.
   *
   * @param bottomLeft  the bottom left corner of the board.
   * @param topRight    the top right corner of the board.
   * @param player1Uuid player 1's UUID.
   * @param player2Uuid player 2's UUID.
   */
  public TankGameBoard(Pos bottomLeft, Pos topRight, UUID player1Uuid, UUID player2Uuid) {
    this.bottomLeft = bottomLeft;
    this.topRight = topRight;
    this.player1Uuid = player1Uuid;
    this.player2Uuid = player2Uuid;
    this.player1TankState = new TankState();
    this.player2TankState = new TankState();
    this.physicsObjects = new ArrayList<>();
    this.currentTurn = BoardTurn.PLAYER_1;
    this.turnTracker = new TankTurnTracker();
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
        .filter(physicsObject -> physicsObject.objectId().equals(player1TankState.objectId()))
        .findFirst().orElse(null);
  }

  /**
   * Gets the player 2 tank.
   *
   * @return the player 2 tank.
   */
  public TankObject player2Tank() {
    return physicsObjects.stream().filter(physicsObject -> physicsObject instanceof TankObject)
        .map(physicsObject -> (TankObject) physicsObject)
        .filter(physicsObject -> physicsObject.objectId().equals(player2TankState.objectId()))
        .findFirst().orElse(null);
  }

  /**
   * Gets the player 1 tank state.
   *
   * @return the player 1 tank state.
   */
  public TankState player1TankState() {
    return player1TankState;
  }

  /**
   * Gets the player 2 tank state.
   *
   * @return the player 2 tank state.
   */
  public TankState player2TankState() {
    return player2TankState;
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
   * Gets the current turn.
   *
   * @return the current turn.
   */
  public BoardTurn currentTurn() {
    return currentTurn;
  }

  /**
   * Sets the current turn.
   *
   * @param currentTurn the current turn.
   */
  public void setCurrentTurn(BoardTurn currentTurn) {
    this.currentTurn = currentTurn;
  }

  /**
   * Gets the turn tracker.
   *
   * @return the turn tracker.
   */
  public TankTurnTracker turnTracker() {
    return turnTracker;
  }

  /**
   * Gets the player 1 uuid.
   *
   * @return the player 1 uuid.
   */
  public UUID player1Uuid() {
    return player1Uuid;
  }

  /**
   * Gets the player 2 uuid.
   *
   * @return the player 2 uuid.
   */
  public UUID player2Uuid() {
    return player2Uuid;
  }

  /**
   * Gets the uuid of the player whose turn it is.
   *
   * @return the uuid of the player whose turn it is.
   */
  public UUID uuidOfCurrentTurn() {
    return currentTurn == BoardTurn.PLAYER_1 ? player1Uuid : player2Uuid;
  }

  /**
   * If it's a player's turn.
   *
   * @param uuid the uuid of the player.
   * @return if it's a player's turn.
   */
  public boolean isTurn(UUID uuid) {
    return uuidOfCurrentTurn().equals(uuid);
  }

  /**
   * Gets the tank of the player whose turn it is.
   *
   * @return the tank of the player whose turn it is.
   */
  public TankObject tankOfCurrentTurn() {
    return currentTurn == BoardTurn.PLAYER_1 ? player1Tank() : player2Tank();
  }

  /**
   * Gets the tank state of the player whose turn it is.
   *
   * @return the tank state of the player whose turn it is.
   */
  public TankState tankStateOfCurrentTurn() {
    return currentTurn == BoardTurn.PLAYER_1 ? player1TankState : player2TankState;
  }

  /**
   * Gets the tank state of a tank object.
   *
   * @param tank the tank object.
   * @return the tank state of a tank object.
   */
  public TankState tankStateOfTank(TankObject tank) {
    return tank.objectId().equals(player1TankState.objectId()) ? player1TankState :
        player2TankState;
  }

  /**
   * Gets the spawn point of a player.
   *
   * @param playerUuid the player uuid.
   * @return the spawn point of a player.
   */
  public Pos getPlayerSpawn(UUID playerUuid) {
    return playerUuid.equals(player1Uuid) ? BoardAndSpaceConstants.PLAYER_1_SPAWN_POSITION :
        BoardAndSpaceConstants.PLAYER_2_SPAWN_POSITION;
  }
}
