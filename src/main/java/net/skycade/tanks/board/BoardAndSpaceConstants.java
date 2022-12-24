package net.skycade.tanks.board;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.Direction;

public class BoardAndSpaceConstants {


  /**
   * The spawn point for player 1.
   */
  public static final Pos PLAYER_1_SPAWN_POSITION = new Pos(0, 19.5, 24, -180, 0);

  /**
   * The spawn point for player 2.
   */
  public static final Pos PLAYER_2_SPAWN_POSITION = new Pos(0, 19.5, -24, 0, 0);

  /**
   * The bottom left corner of the board.
   */
  public static final Pos BOARD_BOTTOM_LEFT = new Pos(-23, 13, -1);

  /**
   * The top right corner of the board.
   */
  public static final Pos BOARD_TOP_RIGHT = new Pos(22, 41, -1);

  /**
   * The bottom left corner of player 1's health bar.
   */
  public static final Pos PLAYER_1_HEALTH_BAR_BOTTOM_LEFT = new Pos(-24, 42, 0);

  /**
   * The top right corner of player 1's health bar.
   */
  public static final Pos PLAYER_1_HEALTH_BAR_TOP_RIGHT = new Pos(23, 45, 0);

  /**
   * The bottom left corner of player 2's health bar.
   */
  public static final Pos PLAYER_2_HEALTH_BAR_BOTTOM_LEFT = new Pos(23, 43, -2);

  /**
   * The top right corner of player 2's health bar.
   */
  public static final Pos PLAYER_2_HEALTH_BAR_TOP_RIGHT = new Pos(-24, 45, -2);

  /**
   * The bottom of player 1's firing power indicator.
   */
  public static final Pos PLAYER_1_FIRING_POWER_BOTTOM = new Pos(-27, 13, -1);

  /**
   * The top of player 1's firing power indicator.
   */
  public static final Pos PLAYER_1_FIRING_POWER_TOP = new Pos(-27, 41, -1);

  /**
   * The bottom of player 2's firing power indicator.
   */
  public static final Pos PLAYER_2_FIRING_POWER_BOTTOM = new Pos(26, 13, -1);

  /**
   * The top of player 2's firing power indicator.
   */
  public static final Pos PLAYER_2_FIRING_POWER_TOP = new Pos(26, 41, -1);

  /**
   * The minimum firing power.
   */
  public static final double MIN_FIRING_POWER = 0.5;

  /**
   * The maximum firing power.
   */
  public static final double MAX_FIRING_POWER = 3.0;

  /**
   * The minimum angle of the tank's turret.
   */
  public static final double MIN_Z_TURRET_ANGLE = 0.0;

  /**
   * The maximum angle of the tank's turret.
   */
  public static final double MAX_Z_TURRET_ANGLE = Math.PI / 2;

  /**
   * The block to be used when the firing power is unspecified at a point.
   */
  public static final Block FIRING_POWER_UNSPECIFIED = Block.GRAY_CONCRETE;

  /**
   * The block to be used when the firing power is low.
   */
  public static final Block FIRING_POWER_SPECIFIED_LOW = Block.LIME_CONCRETE;

  /**
   * The block to be used when the firing power is medium.
   */
  public static final Block FIRING_POWER_SPECIFIED_MEDIUM = Block.YELLOW_CONCRETE;

  /**
   * The block to be used when the firing power is high.
   */
  public static final Block FIRING_POWER_SPECIFIED_HIGH = Block.RED_CONCRETE;

  /**
   * Player 2 move left sign position.
   */
  public static final Pos PLAYER_2_MOVE_LEFT_SIGN_POSITION = new Pos(1, 19, -23);

  /**
   * Player 2 aim up sign position.
   */
  public static final Pos PLAYER_2_AIM_UP_SIGN_POSITION = new Pos(0, 19, -23);

  /**
   * Player 2 aim down sign position.
   */
  public static final Pos PLAYER_2_AIM_DOWN_SIGN_POSITION = new Pos(-1, 19, -23);

  /**
   * Player 2 move right sign position.
   */
  public static final Pos PLAYER_2_MOVE_RIGHT_SIGN_POSITION = new Pos(-2, 19, -23);

  /**
   * Player 2 main sign direction.
   */
  public static final Direction PLAYER_2_MAIN_SIGN_DIRECTION = Direction.NORTH;

  /**
   * Player 2 fire sign direction.
   */
  public static final Direction PLAYER_2_FIRE_SIGN_DIRECTION = Direction.WEST;

  public static final Point PLAYER_2_MOVE_LEFT_BUTTON_POINT = new Pos(1, 20, -22);

  public static final Point PLAYER_2_AIM_UP_BUTTON_POINT = new Pos(0, 20, -22);

  public static final Point PLAYER_2_AIM_DOWN_BUTTON_POINT = new Pos(-1, 20, -22);

  public static final Point PLAYER_2_MOVE_RIGHT_BUTTON_POINT = new Pos(-2, 20, -22);

  public static final Point PLAYER_2_FIRE_BUTTON_POINT = new Pos(2, 20, -24);

  /**
   * Player 1 move left sign position.
   */
  public static final Pos PLAYER_1_MOVE_LEFT_SIGN_POSITION = new Pos(-2, 19, 22);

  /**
   * Player 1 aim up sign position.
   */
  public static final Pos PLAYER_1_AIM_UP_SIGN_POSITION = new Pos(-1, 19, 22);

  /**
   * Player 1 aim down sign position.
   */
  public static final Pos PLAYER_1_AIM_DOWN_SIGN_POSITION = new Pos(0, 19, 22);

  /**
   * Player 1 move right sign position.
   */
  public static final Pos PLAYER_1_MOVE_RIGHT_SIGN_POSITION = new Pos(1, 19, 22);

  /**
   * Player 1 main sign direction.
   */
  public static final Direction PLAYER_1_MAIN_SIGN_DIRECTION = Direction.SOUTH;

  /**
   * Player 1 fire sign direction.
   */
  public static final Direction PLAYER_1_FIRE_SIGN_DIRECTION = Direction.EAST;

  public static final Point PLAYER_1_MOVE_LEFT_BUTTON_POINT = new Pos(-2, 20, 21);

  public static final Point PLAYER_1_AIM_UP_BUTTON_POINT = new Pos(-1, 20, 21);

  public static final Point PLAYER_1_AIM_DOWN_BUTTON_POINT = new Pos(0, 20, 21);

  public static final Point PLAYER_1_MOVE_RIGHT_BUTTON_POINT = new Pos(1, 20, 21);

  public static final Point PLAYER_1_FIRE_BUTTON_POINT = new Pos(-3, 20, 23);
}