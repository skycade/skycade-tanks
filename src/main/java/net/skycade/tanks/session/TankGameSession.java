package net.skycade.tanks.session;

import java.util.UUID;
import net.skycade.tanks.board.BoardConstants;
import net.skycade.tanks.board.TankGameBoard;

/**
 * The session object is responsible for managing the state of a game session. This includes
 * players in the session and the game state itself.
 * <p>
 * The session contains a reference to the game board, which is the contents and state of the board
 * itself. The session also contains the unique IDs of the players in the session (which are used
 * to identify the players in the game board).
 *
 * @author Jacob Cohen
 */
public class TankGameSession {

  /**
   * The unique id of the session
   */
  private final UUID sessionUniqueId;

  /**
   * The unique ID of the first player in the session.
   */
  private final UUID player1UniqueId;

  /**
   * The unique ID of the second player in the session.
   */
  private final UUID player2UniqueId;

  /**
   * The game board in the session.
   */
  private final TankGameBoard gameBoard;

  /**
   * The constructor for the session object.
   *
   * @param player1UniqueId the unique ID of the first player in the session.
   * @param player2UniqueId the unique ID of the second player in the session.
   */
  public TankGameSession(UUID player1UniqueId, UUID player2UniqueId) {
    this.sessionUniqueId = UUID.randomUUID();
    this.player1UniqueId = player1UniqueId;
    this.player2UniqueId = player2UniqueId;
    this.gameBoard = new TankGameBoard(BoardConstants.BOTTOM_LEFT, BoardConstants.TOP_RIGHT, player1UniqueId, player2UniqueId);
  }

  /**
   * Get the unique ID of the first player in the session.
   *
   * @return the unique ID of the first player in the session.
   */
  public UUID player1UniqueId() {
    return player1UniqueId;
  }

  /**
   * Get the unique ID of the second player in the session.
   *
   * @return the unique ID of the second player in the session.
   */
  public UUID player2UniqueId() {
    return player2UniqueId;
  }

  /**
   * Get the game board in the session.
   *
   * @return the game board in the session.
   */
  public TankGameBoard gameBoard() {
    return gameBoard;
  }

  /**
   * Get the unique ID of the session.
   *
   * @return the unique ID of the session.
   */
  public UUID id() {
    return sessionUniqueId;
  }

  /**
   * If the session contains a player with the specified unique ID.
   *
   * @param playerUniqueId the unique ID of the player.
   * @return if the session contains a player with the specified unique ID.
   */
  public boolean isPlayerInSession(UUID playerUniqueId) {
    return player1UniqueId.equals(playerUniqueId) || player2UniqueId.equals(playerUniqueId);
  }
}
