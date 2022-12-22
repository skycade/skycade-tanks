package net.skycade.tanks.board.renderer;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.physics.PhysicsConstants;
import net.skycade.tanks.physics.PhysicsObject;
import net.skycade.tanks.physics.ground.GroundObject;
import net.skycade.tanks.physics.tank.TankObject;
import net.skycade.tanks.space.TankGameSpace;
import net.skycade.tanks.terrain.BoardTerrainGenerator;

public class GameSpaceTankGameBoardRenderer {

  /**
   * The game space to render the board to.
   */
  private final TankGameSpace gameSpace;

  /**
   * The board to render.
   */
  private final TankGameBoard board;

  /**
   * The time at which the last render was performed.
   */
  @SuppressWarnings("FieldCanBeLocal")
  private long lastRender;

  /**
   * Creates a new game space tank game board renderer.
   *
   * @param gameSpace the game space to render the board to.
   * @param board     the board to render.
   */
  public GameSpaceTankGameBoardRenderer(TankGameSpace gameSpace, TankGameBoard board) {
    this.gameSpace = gameSpace;
    this.board = board;
  }

  /**
   * Initializes the board.
   */
  public void initializeBoard() {
    int[] heights = BoardTerrainGenerator.generateHeights(
        board.topRight().blockX() - board.bottomLeft().blockX() + 1);

    // generate the terrain
    for (int x = 0; x < heights.length; x++) {
      int xCoord = board.bottomLeft().blockX() + x;
      for (int y = 0; y < heights[x]; y++) {
        int yCoord = board.bottomLeft().blockY() + y;
        gameSpace.setBlock(xCoord, yCoord, board.bottomLeft().blockZ(), Block.SAND);
        // add the ground physics object to the board
        board.addPhysicsObject(new GroundObject(
            new Pos(xCoord + 0.5, yCoord + 0.5, board.bottomLeft().blockZ() + 0.5)));
      }
    }

    // spawn the tanks
    Pos tank1Spawn =
        new Pos(board.bottomLeft().blockX() + 3, board.bottomLeft().blockY() + heights[2] + 2,
            board.bottomLeft().blockZ() + 0.5);
    Pos tank2Spawn = new Pos(board.topRight().blockX() - 3,
        board.bottomLeft().blockY() + heights[heights.length - 4] + 2,
        board.bottomLeft().blockZ() + 0.5);

    // add the tanks to the board
    board.addPhysicsObject(
        new TankObject(tank1Spawn, Vec.ZERO, Vec.ZERO, board.player1TankState().objectId(), null));
    board.addPhysicsObject(
        new TankObject(tank2Spawn, Vec.ZERO, Vec.ZERO, board.player2TankState().objectId(), null));
  }

  /**
   * Renders the next frame of the board.
   */
  public void tick() {
    calculateNextPositions();
    render();
    // update the last render time
    lastRender = System.currentTimeMillis();
  }

  /**
   * Calculates the next positions of the physics objects.
   */
  private void calculateNextPositions() {
    // calculate the next positions of the physics objects
    for (PhysicsObject physicsObject : board.physicsObjects()) {
      physicsObject.tickPhysics(this.board);
    }
  }

  /**
   * Renders the board.
   */
  private void render() {
    // render each physics object (tanks only at the moment)
    for (TankObject tankObject : board.physicsObjects().stream()
        .filter(physicsObject -> physicsObject instanceof TankObject)
        .map(physicsObject -> (TankObject) physicsObject).toList()) {

      Entity tankEntity =
          gameSpace.getEntities().stream().filter(e -> e.getUuid() == tankObject.tankRefId())
              .findFirst().orElse(null);

      // if the entity doesn't exist, create it
      if (tankEntity == null) {
        Entity tank = new Entity(EntityType.FURNACE_MINECART);
        tank.setNoGravity(true);

        tank.setInstance(gameSpace, tankObject.position());
        // update the reference id
        tankObject.tankRefId(tank.getUuid());
        continue;
      }
      // if the entity does exist, update its position
      tankEntity.teleport(tankObject.position().sub(0, 0.3, 0));

      double angleOfTurret = board.tankStateOfTank(tankObject).turretVector().z();

      // render the turret using particles
      // being 1.5 blocks long, it will be composed of 30 particles
      for (int i = 0; i < 30; i++) {
        double x = Math.cos(angleOfTurret) * (i / 30.0) * 1.5 + tankObject.position().x();
        double y = Math.sin(angleOfTurret) * (i / 30.0) * 1.5 + tankObject.position().y();

        ParticlePacket particlePacket =
            ParticleCreator.createParticlePacket(Particle.DRIPPING_LAVA, x, y,
                tankObject.position().z(), 0f, 0f, 0f, 1);
        gameSpace.sendGroupedPacket(particlePacket);
      }

      // render a thick parabola for the trajectory using 50 particles
      // using the angle of the turret above
      // ... shooting on the x axis, so only the x & y are affected
      double v0 = 1.5;
      double particleSpacing = 0.5;

      for (int i = 0; i < 50; i++) {
        double t = i * particleSpacing;
        double x = v0 * Math.cos(angleOfTurret) * t + tankObject.position().x();
        double y = v0 * Math.sin(angleOfTurret) * t - 0.5 * PhysicsConstants.GRAVITY * t * t +
            tankObject.position().y();


        ParticlePacket particlePacket =
            ParticleCreator.createParticlePacket(Particle.DRIPPING_WATER, x, y,
                tankObject.position().z(), 0f, 0f, 0f, 1);
        gameSpace.sendGroupedPacket(particlePacket);
      }
    }
  }
}
