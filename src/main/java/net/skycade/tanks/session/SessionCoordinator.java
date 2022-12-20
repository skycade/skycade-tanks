package net.skycade.tanks.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The session coordinator is responsible for managing the sessions of the game. It is responsible
 * for creating new sessions, removing sessions, and managing the sessions.
 *
 * @author Jacob Cohen
 */
public class SessionCoordinator {

  /**
   * The sessions map, which maps the session ID to the session.
   */
  private final Map<UUID, TankGameSession> sessionIdToSessionMap;

  /**
   * Creates a new session coordinator.
   */
  public SessionCoordinator() {
    this.sessionIdToSessionMap = new HashMap<>();
  }

  /**
   * Creates a new session.
   *
   * @param player1UniqueId the unique ID of the first player in the session.
   * @param player2UniqueId the unique ID of the second player in the session.
   * @return the session.
   */
  public TankGameSession createSession(UUID player1UniqueId, UUID player2UniqueId) {
    TankGameSession session = new TankGameSession(player1UniqueId, player2UniqueId);
    sessionIdToSessionMap.put(session.id(), session);
    return session;
  }

  /**
   * Gets the session for the session ID.
   *
   * @param sessionId the session ID.
   * @return the session.
   */
  public TankGameSession getSession(UUID sessionId) {
    return sessionIdToSessionMap.get(sessionId);
  }

  /**
   * Removes the session for the session ID.
   *
   * @param sessionId the session ID.
   */
  public void removeSession(UUID sessionId) {
    sessionIdToSessionMap.remove(sessionId);
  }

  /**
   * Gets the session for the player.
   *
   * @param playerUniqueId the unique ID of the player.
   * @return the session.
   */
  public Optional<TankGameSession> getSessionByPlayer(UUID playerUniqueId) {
    return sessionIdToSessionMap.values().stream().filter(
        session -> session.player1UniqueId().equals(playerUniqueId) ||
            session.player2UniqueId().equals(playerUniqueId)).findFirst();
  }

  /**
   * If the player is in a session.
   *
   * @param playerUniqueId the unique ID of the player.
   * @return if the player is in a session.
   */
  public boolean isPlayerInSession(UUID playerUniqueId) {
    return getSessionByPlayer(playerUniqueId).isPresent();
  }
}
