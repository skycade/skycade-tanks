package net.skycade.tanks.board.renderer.tank;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.skycade.serverruntime.api.space.GameSpace;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.board.renderer.TankGameRenderer;
import net.skycade.tanks.physics.tank.TankObject;

public class TankRenderer extends TankGameRenderer<TankObject> {

  /**
   * Creates a new renderer
   *
   * @param gameSpace the game space to render the board to.
   * @param board     the board to render.
   */
  public TankRenderer(GameSpace gameSpace, TankGameBoard board) {
    super(gameSpace, board);
  }

  @Override
  public void render(TankObject tankObject) {
    // todo: optimize this so rendering doesn't happen unless something has changed
    Entity tankEntity =
        gameSpace().getEntities().stream().filter(e -> e.getUuid() == tankObject.tankRefId())
            .findFirst().orElse(null);

    // if the entity doesn't exist, create it
    if (tankEntity == null) {
      Entity tank = new Entity(EntityType.FURNACE_MINECART);
      tank.setNoGravity(true);

      tank.setInstance(gameSpace(), tankObject.position());
      // update the reference id
      tankObject.tankRefId(tank.getUuid());
      return;
    }
    // if the entity does exist, update its position
    tankEntity.teleport(tankObject.position().sub(0, 0.3, 0));

    // render the turret
    double angleOfTurret = board().tankStateOfTank(tankObject).turretVector().z();

    // todo: make sure when optimizing to still render the turret because it requires constant drawing
    // render the turret using particles
    // being 1.5 blocks long, it will be composed of 30 particles
    for (int i = 0; i < 30; i++) {
      double x = Math.cos(angleOfTurret) * (i / 30.0) * 1.5 + tankObject.position().x();
      double y = Math.sin(angleOfTurret) * (i / 30.0) * 1.5 + tankObject.position().y();

      ParticlePacket particlePacket =
          ParticleCreator.createParticlePacket(Particle.DRIPPING_LAVA, x, y,
              tankObject.position().z(), 0f, 0f, 0f, 1);
      gameSpace().sendGroupedPacket(particlePacket);
    }
  }
}
