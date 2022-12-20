package net.skycade.tanks.space;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.skycade.tanks.board.TankGameBoard;

public class SessionGameSpaceCoordinator {

  /**
   * A map of the session id mapped to the game space.
   */
  private final Map<UUID, TankGameSpace> sessionIdToGameSpaceMap;

  /**
   * Creates a new session game space coordinator.
   */
  public SessionGameSpaceCoordinator() {
    this.sessionIdToGameSpaceMap = new HashMap<>();
  }

  /**
   * Creates a new game space for the session.
   *
   * @param sessionId the session id.
   * @return the game space.
   */
  public TankGameSpace createGameSpace(UUID sessionId, TankGameBoard board) {
    TankGameSpace gameSpace = new TankGameSpace(board);
    sessionIdToGameSpaceMap.put(sessionId, gameSpace);
    return gameSpace;
  }

  /**
   * Gets the game space for the session.
   *
   * @param sessionId the session id.
   * @return the game space.
   */
  public TankGameSpace getGameSpace(UUID sessionId) {
    return sessionIdToGameSpaceMap.get(sessionId);
  }

  /**
   * Removes the game space for the session.
   *
   * @param sessionId the session id.
   */
  public void removeGameSpace(UUID sessionId) {
    sessionIdToGameSpaceMap.remove(sessionId);
  }
}
