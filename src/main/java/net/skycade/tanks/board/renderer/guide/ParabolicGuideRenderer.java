package net.skycade.tanks.board.renderer.guide;

import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.board.renderer.TankGameRenderer;
import net.skycade.tanks.physics.PhysicsConstants;
import net.skycade.tanks.physics.tank.TankObject;

public class ParabolicGuideRenderer extends TankGameRenderer<TankObject> {

  /**
   * Creates a new renderer
   *
   * @param gameSpace the game space to render the board to.
   * @param board     the board to render.
   */
  public ParabolicGuideRenderer(GameSpace gameSpace, TankGameBoard board) {
    super(gameSpace, board);
  }

  @Override
  public void render(TankObject tankObject) {
    double angleOfTurret = board().tankStateOfTank(tankObject).turretVector().z();
    // render a thick parabola for the trajectory using 50 particles
    // using the angle of the turret above
    // ... shooting on the x-axis, so only the x & y are affected
    double v0 = board().turnTracker().firingPower();
    double particleSpacing = 0.5;

    for (int i = 0; i < 50; i++) {
      double t = i * particleSpacing;
      double x = v0 * Math.cos(angleOfTurret) * t + tankObject.position().x();
      double y = v0 * Math.sin(angleOfTurret) * t - 0.5 * PhysicsConstants.GRAVITY * t * t +
          tankObject.position().y();


      ParticlePacket particlePacket =
          ParticleCreator.createParticlePacket(Particle.DRIPPING_WATER, x, y,
              tankObject.position().z(), 0f, 0f, 0f, 1);
      gameSpace().sendGroupedPacket(particlePacket);
    }
  }
}
