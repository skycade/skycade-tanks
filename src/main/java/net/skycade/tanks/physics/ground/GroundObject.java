package net.skycade.tanks.physics.ground;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.physics.PhysicsObject;

/**
 * The ground object is responsible for representing the ground in the game. It is a physics object
 * that IS affected by gravity, but is not affected by any other forces.
 * It can be broken by bullets, and is destroyed when it is broken.
 *
 * @author Jacob Cohen
 */
public class GroundObject extends PhysicsObject {

  /**
   * The constructor for the physics object.
   *
   * @param position the position of the object.
   */
  public GroundObject(Pos position) {
    super(position, Vec.ZERO, Vec.ZERO, 1D, 0.5D);
  }

  @Override
  public void update(TankGameBoard board) {
    // no-op
  }
}
