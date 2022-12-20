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
            new GroundObject(new Pos(xCoord, yCoord, board.bottomLeft().blockZ())));
      }
    }

    // spawn the tanks
    Pos tank1Spawn =
        new Pos(board.bottomLeft().blockX() + 3, board.bottomLeft().blockY() + heights[2] + 1,
            board.bottomLeft().blockZ() + 0.5);
    Pos tank2Spawn = new Pos(board.topRight().blockX() - 3,
        board.bottomLeft().blockY() + heights[heights.length - 4] + 1,
        board.bottomLeft().blockZ() + 0.5);

    // add the tanks to the board
    board.addPhysicsObject(new TankObject(tank1Spawn, Vec.ZERO, Vec.ZERO));
    board.addPhysicsObject(new TankObject(tank2Spawn, Vec.ZERO, Vec.ZERO));

    // spawn the tanks
    Entity tank1 = new Entity(EntityType.FURNACE_MINECART);
    tank1.setInstance(gameSpace, tank1Spawn);
    // add a cannon to the tank
    LivingEntity cannon1 = new LivingEntity(EntityType.ARMOR_STAND);
    cannon1.setItemInMainHand(ItemStack.of(Material.STICK));
    cannon1.setInvisible(true);
    ArmorStandMeta cannon1Meta = (ArmorStandMeta) cannon1.getEntityMeta();
    cannon1Meta.setHasArms(true);
    cannon1.setInstance(gameSpace, tank1Spawn.add(0, 1, -0.5).withYaw(-90));

    Entity tank2 = new Entity(EntityType.FURNACE_MINECART);
    tank2.setInstance(gameSpace, tank2Spawn);
    // add a cannon to the tank
    LivingEntity cannon2 = new LivingEntity(EntityType.ARMOR_STAND);
    cannon2.setItemInMainHand(ItemStack.of(Material.STICK));
    cannon2.setInvisible(true);
    ArmorStandMeta cannon2Meta = (ArmorStandMeta) cannon2.getEntityMeta();
    cannon2Meta.setHasArms(true);
    cannon2.setInstance(gameSpace, tank2Spawn.add(0, 1, 0.5).withYaw(90));
  }

  /**
   * Renders the next frame of the board.
   */
  public void tick() {
  }
}
