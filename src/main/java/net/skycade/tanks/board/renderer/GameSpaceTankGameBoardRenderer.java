package net.skycade.tanks.board.renderer;

import java.util.List;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.board.renderer.guide.ParabolicGuideRenderer;
import net.skycade.tanks.board.renderer.power.FiringPowerBarRenderer;
import net.skycade.tanks.board.renderer.tank.TankRenderer;
import net.skycade.tanks.physics.PhysicsObject;
import net.skycade.tanks.physics.edge.BoardEdgeObject;
import net.skycade.tanks.physics.ground.GroundObject;
import net.skycade.tanks.physics.tank.TankObject;
import net.skycade.tanks.space.TankGameSpace;
import net.skycade.tanks.terrain.BoardTerrainGenerator;

public class GameSpaceTankGameBoardRenderer extends TankGameRenderer<List<PhysicsObject>> {

  /**
   * The game space to render the board to.
   */
  private final TankGameSpace gameSpace;

  /**
   * The board to render.
   */
  private final TankGameBoard board;

  /**
   * The tank renderer.
   */
  private final TankRenderer tankRenderer;

  /**
   * The guide renderer.
   */
  private final ParabolicGuideRenderer parabolicGuideRenderer;

  /**
   * The power bar renderer.
   */
  private final FiringPowerBarRenderer firingPowerBarRenderer;

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
    super(gameSpace, board);
    this.gameSpace = gameSpace;
    this.board = board;
    this.tankRenderer = new TankRenderer(gameSpace, board);
    this.parabolicGuideRenderer = new ParabolicGuideRenderer(gameSpace, board);
    this.firingPowerBarRenderer = new FiringPowerBarRenderer(gameSpace, board);
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

    // add the board edges
    int xStart = board.bottomLeft().blockX();
    int xEnd = board.topRight().blockX();
    int yStart = board.bottomLeft().blockY();
    int yEnd = board.topRight().blockY();

    // add the left edge
    for (int y = yStart; y <= yEnd; y++) {
      board.addPhysicsObject(
          new BoardEdgeObject(new Pos(xStart, y + 0.5, board.bottomLeft().blockZ() + 0.5)));
    }

    // add the right edge
    for (int y = yStart; y <= yEnd; y++) {
      board.addPhysicsObject(
          new BoardEdgeObject(new Pos(xEnd + 1, y + 0.5, board.bottomLeft().blockZ() + 0.5)));
    }

    // add the bottom edge
    for (int x = xStart; x <= xEnd; x++) {
      board.addPhysicsObject(
          new BoardEdgeObject(new Pos(x + 0.5, yStart, board.bottomLeft().blockZ() + 0.5)));
    }

    // add the top edge
    for (int x = xStart; x <= xEnd; x++) {
      board.addPhysicsObject(
          new BoardEdgeObject(new Pos(x + 0.5, yEnd + 1, board.bottomLeft().blockZ() + 0.5)));
    }

    // add the tanks
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
    render(board.physicsObjects());
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
  @Override
  public void render(List<PhysicsObject> context) {
    // render each physics object (tanks only at the moment)
    for (TankObject tankObject : context.stream()
        .filter(physicsObject -> physicsObject instanceof TankObject)
        .map(physicsObject -> (TankObject) physicsObject).toList()) {
      this.tankRenderer.render(tankObject);
      this.parabolicGuideRenderer.render(tankObject);
    }

    this.firingPowerBarRenderer.render(null);
  }
}
