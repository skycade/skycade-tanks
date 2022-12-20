package net.skycade.tanks.board.renderer;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.block.Block;
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
        board.addPhysicsObject(
            new GroundObject(new Pos(xCoord + 0.5, yCoord - 0.5, board.bottomLeft().blockZ() + 0.5)));
      }
    }

    // spawn the tanks
    Pos tank1Spawn =
        new Pos(board.bottomLeft().blockX() + 3, board.bottomLeft().blockY() + heights[2] + 3,
            board.bottomLeft().blockZ() + 0.5);
    Pos tank2Spawn = new Pos(board.topRight().blockX() - 3,
        board.bottomLeft().blockY() + heights[heights.length - 4] + 3,
        board.bottomLeft().blockZ() + 0.5);

    // add the tanks to the board
    board.addPhysicsObject(
        new TankObject(tank1Spawn, Vec.ZERO, Vec.ZERO, board.player1TankObjectId(), null));
//    board.addPhysicsObject(
//        new TankObject(tank2Spawn, Vec.ZERO, Vec.ZERO, board.player2TankObjectId(), null));
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

      Entity entity =
          gameSpace.getEntities().stream().filter(e -> e.getUuid() == tankObject.previousRefId())
              .findFirst().orElse(null);

      // if the entity doesn't exist, create it
      if (entity == null) {
        Entity tank = new Entity(EntityType.FURNACE_MINECART);
        tank.setNoGravity(true);
        tank.setInstance(gameSpace, tankObject.position());
        // update the reference id
        tankObject.refId(tank.getUuid());
        continue;
      }

      // if the entity does exist, update its position
      entity.teleport(tankObject.position());
//      // add a cannon to the tank
//      LivingEntity cannon2 = new LivingEntity(EntityType.ARMOR_STAND);
//      cannon2.setItemInMainHand(ItemStack.of(Material.STICK));
//      cannon2.setInvisible(true);
//      ArmorStandMeta cannon2Meta = (ArmorStandMeta) cannon2.getEntityMeta();
//      cannon2Meta.setHasArms(true);
//      cannon2.setInstance(gameSpace, tank2Spawn.add(0, 1, 0.5).withYaw(90));
    }
  }
}
