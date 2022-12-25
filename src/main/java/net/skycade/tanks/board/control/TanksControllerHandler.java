package net.skycade.tanks.board.control;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.tanks.board.BoardAndSpaceConstants;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.board.turn.BoardTurn;
import net.skycade.tanks.board.turn.TankState;
import net.skycade.tanks.board.turn.TankTurnTracker;
import net.skycade.tanks.physics.tank.TankObject;
import net.skycade.tanks.space.utils.TankGameSpaceCalculationUtils;

/**
 * Handles control events given the context of the board.
 *
 * @author Jacob Cohen
 */
public class TanksControllerHandler {

  /**
   * The game board.
   */
  private final TankGameBoard board;

  /**
   * The game space.
   */
  private final GameSpace gameSpace;

  /**
   * Creates a new {@link TanksControllerHandler}.
   *
   * @param board     the game board.
   * @param gameSpace the game space.
   */
  public TanksControllerHandler(TankGameBoard board, GameSpace gameSpace) {
    this.board = board;
    this.gameSpace = gameSpace;
  }

  /**
   * Handles when a player clicks a button.
   *
   * @param clickedButtonPoint the point that was clicked
   * @param whoClicked         player that clicked the button
   */

  public void onButtonClick(Point clickedButtonPoint, Player whoClicked) {
    // if it's not the player's turn, do nothing
    if (!board.isTurn(whoClicked.getUuid())) {
      return;
    }

    // the turn tracker
    TankTurnTracker turnTracker = board.turnTracker();

    // the target tank + state for the player who's turn it is
    TankState targetState = board.currentTurn() == BoardTurn.PLAYER_1 ? board.player1TankState() :
        board.player2TankState();
    TankObject targetTank =
        board.currentTurn() == BoardTurn.PLAYER_1 ? board.player1Tank() : board.player2Tank();

    // get the control for the button that was clicked
    TankBoardControl control =
        TankBoardControlUtils.getControlFromClickedButtonPosition(clickedButtonPoint,
            board.currentTurn());

    if (control == TankBoardControl.AIM_DOWN) {
      handleAimDownControl(turnTracker, targetState);
    } else if (control == TankBoardControl.AIM_UP) {
      handleAimUpControl(turnTracker, targetState);
    } else if (control == TankBoardControl.FIRE) {
      handleFireControl(turnTracker, targetState, targetTank);
    } else if (control == TankBoardControl.MOVE_RIGHT) {
      handleMoveRightControl(turnTracker, targetTank);
    } else if (control == TankBoardControl.MOVE_LEFT) {
      handleMoveLeftControl(turnTracker, targetTank);
    } else if (control == TankBoardControl.INCREASE_POWER) {
      handleIncreasePowerControl(turnTracker);
    } else if (control == TankBoardControl.DECREASE_POWER) {
      handleDecreasePowerControl(turnTracker);
    }

    playUpdateSound(whoClicked);
  }

  private void handleAimDownControl(TankTurnTracker turnTracker, TankState targetState) {
    if (turnTracker.hasMaxTurretRotationMoves()) {
      return;
    }

    Vec newTurretVector = targetState.turretVector().add(0, 0, -Math.PI / 48);

    double zTranslatedToContextOfPlayer =
        TankGameSpaceCalculationUtils.translateTurretAngleIntoContextOfPlayerTurn(
            board.currentTurn(), newTurretVector.z());

    if (zTranslatedToContextOfPlayer > BoardAndSpaceConstants.MAX_Z_TURRET_ANGLE ||
        zTranslatedToContextOfPlayer < BoardAndSpaceConstants.MIN_Z_TURRET_ANGLE) {
      return;
    }

    // rotate the turret on the negative unit circle
    targetState.turretVector(targetState.turretVector().add(0, 0, -Math.PI / 48));
    turnTracker.incrementTurretRotationMoves();
  }

  private void handleAimUpControl(TankTurnTracker turnTracker, TankState targetState) {
    if (turnTracker.hasMaxTurretRotationMoves()) {
      return;
    }

    Vec newTurretVector = targetState.turretVector().add(0, 0, Math.PI / 48);

    double zTranslatedToContextOfPlayer =
        TankGameSpaceCalculationUtils.translateTurretAngleIntoContextOfPlayerTurn(
            board.currentTurn(), newTurretVector.z());

    if (zTranslatedToContextOfPlayer > BoardAndSpaceConstants.MAX_Z_TURRET_ANGLE ||
        zTranslatedToContextOfPlayer < BoardAndSpaceConstants.MIN_Z_TURRET_ANGLE) {
      return;
    }

    // rotate the turret on the positive unit circle
    targetState.turretVector(targetState.turretVector().add(0, 0, Math.PI / 48));
    turnTracker.incrementTurretRotationMoves();
  }

  private void handleMoveRightControl(TankTurnTracker turnTracker, TankObject targetTank) {
    if (turnTracker.hasMaxPositionMoves()) {
      return;
    }

    // push the tank right
    targetTank.velocity(targetTank.velocity().add(0.5, 0, 0));
    turnTracker.incrementPositionMoves();
  }

  private void handleMoveLeftControl(TankTurnTracker turnTracker, TankObject targetTank) {
    if (turnTracker.hasMaxPositionMoves()) {
      return;
    }

    // push the tank left
    targetTank.velocity(targetTank.velocity().add(-0.5, 0, 0));
    turnTracker.incrementPositionMoves();
  }

  private void handleFireControl(TankTurnTracker turnTracker, TankState targetState,
                                 TankObject targetTank) {
    ParticlePacket particlePacket =
        ParticleCreator.createParticlePacket(Particle.EXPLOSION, targetTank.position().x(),
            targetTank.position().y(), targetTank.position().z(), 0f, 0f, 0f, 5);

    gameSpace.sendGroupedPacket(particlePacket);
    gameSpace.getPlayers().forEach(player -> player.playSound(
        Sound.sound(Key.key("entity.generic.explode"), Sound.Source.MASTER, 1f, 1f)));

//    if (turnTracker.hasFired()) {
//      return;
//    }
//
//    // fire the tank
//    targetState.firing(true);
//    targetTank.velocity(targetState.turretVector().multiply(0.5));
//    turnTracker.fired(true);
  }

  private void handleIncreasePowerControl(TankTurnTracker turnTracker) {
    if (turnTracker.isAtMaxFiringPower()) {
      return;
    }

    turnTracker.incrementFiringPower();
  }

  private void handleDecreasePowerControl(TankTurnTracker turnTracker) {
    if (turnTracker.isAtMinFiringPower()) {
      return;
    }

    turnTracker.decrementFiringPower();
  }

  private void playUpdateSound(Player player) {
    player.playSound(
        Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 0.5f, 1));
  }
}
