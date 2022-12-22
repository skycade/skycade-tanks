package net.skycade.tanks.board.renderer;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.skycade.tanks.board.TankGameBoard;
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
        new TankObject(tank1Spawn, Vec.ZERO, Vec.ZERO, board.player1TankObjectId(), null, null));
    board.addPhysicsObject(
        new TankObject(tank2Spawn, Vec.ZERO, Vec.ZERO, board.player2TankObjectId(), null, null));
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

      Entity turretEntity =
          gameSpace.getEntities().stream().filter(e -> e.getUuid() == tankObject.turretRefId())
              .findFirst().orElse(null);

      // if the turret entity doesn't exist, create it
      if (turretEntity == null) {
        // add a cannon to the tank
        LivingEntity turret = new LivingEntity(EntityType.ARMOR_STAND);
        turret.setHelmet(ItemStack.of(Material.STICK));
//        turret.setInvisible(true);
        turret.setNoGravity(true);

        turret.setInstance(gameSpace, determineTurretPosition(tankObject));
        // update the reference id
        tankObject.turretRefId(turret.getUuid());
        continue;
      }

      // set the turret's angle
      ArmorStandMeta turretEntityMeta = (ArmorStandMeta) turretEntity.getEntityMeta();
      turretEntityMeta.setHeadRotation(
          isPlayer1Tank(tankObject) ? board.player1TankTurretAngle() :
              board.player2TankTurretAngle());

      // if the turret entity does exist, update its position
      turretEntity.teleport(determineTurretPosition(tankObject));
    }
  }

  /**
   * Determines the turret position in relation to the tank.
   *
   * @param tankObject the tank object.
   * @return the turret position.
   */
  private Pos determineTurretPosition(TankObject tankObject) {
    return tankObject.position().add(isPlayer1Tank(tankObject) ? 0.3 : -0.3, -1.5, 0)
        .withYaw(isPlayer1Tank(tankObject) ? 0 : 180);
  }

  /**
   * If the tank belongs to player 1.
   *
   * @param tankObject the tank object.
   * @return if the tank belongs to player 1.
   */
  private boolean isPlayer1Tank(TankObject tankObject) {
    return tankObject.objectId() == board.player1TankObjectId();
  }
}
