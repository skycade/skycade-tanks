package net.skycade.tanks.physics.edge;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.skycade.tanks.board.TankGameBoard;
import net.skycade.tanks.physics.PhysicsObject;

public class BoardEdgeObject extends PhysicsObject {

  /**
   * Creates a new board edge object.
   *
   * @param position the position of the object.
   */
  public BoardEdgeObject(Pos position) {
    super(position, Vec.ZERO, Vec.ZERO, 1000, 0.5D);
  }

  @Override
  public void update(TankGameBoard board) {
    // no-op
  }
}
