package net.skycade.tanks.terrain;

public class BoardTerrainGenerator {

  /**
   * Generates a random double between the specified min and max.
   *
   * @param min the minimum value.
   * @param max the maximum value.
   * @return a random double between the specified min and max.
   */
  private static double random(double min, double max) {
    return Math.random() * (max - min) + min;
  }

  /**
   * F(x) equation.
   *
   * @param x              the x value.
   * @param randomModifier the random modifier.
   * @return the y value.
   */
  private static double fOfX(double x, int randomModifier) {
    return (Math.sin((x - randomModifier) * 2) * 8) + 9;
  }

  /**
   * G(x) equation.
   *
   * @param x the x value.
   * @return the y value.
   */
  private static double gOfX(double x) {
    return (Math.cos(x / 50f) * 6);
  }

  /**
   * Generates the terrain for the specified board.
   *
   * @param width the width of the board.
   * @return the heights of the terrain.
   */
  public static int[] generateHeights(int width) {
    int[] heights = new int[width];
    int randomModifier = (int) random(0, 1000);
    for (int i = 0; i < heights.length; i++) {
      double r = fOfX(gOfX(i), randomModifier);
      heights[i] = (int) Math.floor(r);
    }
    return heights;
  }
}
