import java.util.Scanner;

public class MoneyScoutBotVer1Skeleton {

  public static void main(String[] args) {

    int fieldCols = 50;
    int fieldRows = 20;

    int[][] coins = getCoins();
    int[][] walls = getWalls();
    int scoutPocketCoinsCounter = 0;

    int robotPosCol = 2, robotPosRow = 6;

    Scanner scanner = new Scanner(System.in);

    int movesCounter = 0;

    while (true) {

      int[][] visibleCoins = getVisibleObjects(coins, robotPosRow, robotPosCol);
      int[][] visibleWalls = getVisibleObjects(walls, robotPosRow, robotPosCol);

      showLevel(fieldRows, fieldCols, robotPosRow, robotPosCol, visibleWalls, visibleCoins);

      delay();

      String yourMove = thinkWhatsYourNextMove(visibleCoins, visibleWalls,
          robotPosCol, robotPosRow, fieldCols, fieldRows);

      if (isCanMakeThisMove(fieldRows, fieldCols, robotPosRow, robotPosCol, visibleWalls,
          yourMove)) {

        robotPosCol = calculateNewPosCol(robotPosCol, yourMove);
        robotPosRow = calculateNewPosRow(robotPosRow, yourMove);

        int indexOfCoin = isCoordinateInsideAnyObject(robotPosRow, robotPosCol, visibleCoins);

        if (indexOfCoin >= 0) {
          coins = pickCoin(indexOfCoin, coins);
          scoutPocketCoinsCounter++;
        }

      } else {
        System.out.println("This move is not possible! Your move was: " + yourMove);
      }

      movesCounter++;

      if (coins == null) {
        showLevel(fieldRows, fieldCols, robotPosRow, robotPosCol, walls, coins);
        System.out.println(
            "Congradulations! \nNo more coins on the field! You gathered all! \nYou did it in "
                + movesCounter + " moves!");
        break;
      }

    }


  }

  private static void delay() {
    for (long i = 0; i < 5_000_000_000L; i++) {
      ;
    }
  }

  // TODO: этот метод всего лишь пример, его нужно сделать полностью самостоятельно, НО изменять
  // в этом методе можно только логику метода, сигнатуру (название и входные/выходные параметры) -
  // менять НЕЛЬЗЯ!
  // можно использовать внешние статические переменные уровня класса - как здесь использована пере-
  // менная movesCounterBotMemory - причем неограничено, только нельзя в таких переменных хранить
  // изначальные копии массивов coins и walls, но можно например создать собственный массив
  // wallsMemory например, в который будете накапливать всю информацию по уже увиденным ботом стенам

  private static String thinkWhatsYourNextMove(int[][] visibleCoins, int[][] visibleWalls,
      int robotPosCol, int robotPosRow, int fieldCols, int fieldRows) {

    //TODO
    return "a";
  }

  private static int[][] getVisibleObjects(int[][] allObjects, int robotPosRow, int robotPosCol) {

    if (allObjects == null || allObjects.length == 0) {
      return null;
    }

    boolean[] visibleObjectsIndexes = new boolean[allObjects.length];

    for (int r = robotPosRow - 2; r < robotPosRow + 2; r++) {
      for (int c = robotPosCol - 2; c < robotPosCol + 2; c++) {

        for (int i = 0; i < allObjects.length; i++) {

          if (isCoordinateInsideObject(
              r, c,
              allObjects[i][0], allObjects[i][1],
              allObjects[i][2], allObjects[i][3])) {

            visibleObjectsIndexes[i] = true;
            break;
          }

        }

      }
    }

    int visiblesCount = 0;

    for (int i = 0; i < visibleObjectsIndexes.length; i++) {
      if (visibleObjectsIndexes[i] == true) {
        visiblesCount++;
      }
    }

    int[][] visibleObjects = new int[visiblesCount][4];

    for (int i = 0, j = 0; i < visibleObjectsIndexes.length; i++) {
      if (visibleObjectsIndexes[i] == true) {
        visibleObjects[j] = allObjects[i];
        j++;
      }
    }

    return visibleObjects;
  }

  private static int[][] pickCoin(int indexOfCoin, int[][] coins) {

    if (coins.length > 1) {

      int[][] tmpCoins = new int[coins.length - 1][coins[0].length];

      for (int i = 0, j = 0; i < coins.length; i++) {
        if (i != indexOfCoin) {
          tmpCoins[j] = coins[i];
          j++;
        }
      }

      coins = tmpCoins;

    } else {
      coins = null;
    }

    return coins;

  }

  private static boolean isCanMakeThisMove(int fieldRows, int fieldCols, int robotPosRow,
      int robotPosCol, int[][] walls, String yourMove) {

    final int newRobotPosRow = calculateNewPosRow(robotPosRow, yourMove);
    final int newRobotPosCol = calculateNewPosCol(robotPosCol, yourMove);

    if (isCoordinateInsideAnyObject(newRobotPosRow, newRobotPosCol, walls) >= 0 ||
        isBorder(newRobotPosRow, newRobotPosCol, fieldRows, fieldCols)) {
      return false;
    } else {
      return true;
    }

  }

  private static boolean isBorder(int r, int c, int fieldRows, int fieldCols) {
    return r == fieldRows - 1 || c == fieldCols - 1 || r == 0 || c == 0;
  }

  private static void showLevel(int fieldRows, int fieldCols, int robotPosRow, int robotPosCol,
      int[][] walls, int[][] coins) {

    System.out.println();
    for (int r = 0; r < fieldRows; r++) {
      for (int c = 0; c < fieldCols; c++) {
        if (r == robotPosRow && c == robotPosCol) {
          System.out.print('M');
        } else if (isCoordinateInsideAnyObject(r, c, walls) >= 0) {
          System.out.print('x');
        } else if (isBorder(r, c, fieldRows, fieldCols)) {
          System.out.print('+');
        } else if (isCoordinateInsideAnyObject(r, c, coins) >= 0) {
          System.out.print('$');
        } else {
          if (isCoordinateInsideObject(r, c, robotPosRow - 2, robotPosCol - 2, robotPosRow + 2,
              robotPosCol + 2)) {
            System.out.print(' ');
          } else {
            System.out.print('*');
          }
        }
      }
      System.out.println();
    }

  }

  private static int isCoordinateInsideAnyObject(int r, int c, int[][] objects) {

    if (objects == null) {
      return -1;
    }

    for (int i = 0; i < objects.length; i++) {
      if (isCoordinateInsideObject(r, c, objects[i][0], objects[i][1], objects[i][2],
          objects[i][3])) {
        return i;
      }
    }

    return -1;
  }

  private static boolean isCoordinateInsideObject(int r, int c, int r1, int c1, int height,
      int width) {

    return r >= r1 && r <= (r1 + height - 1) && c >= c1 && c <= (c1 + width - 1);
  }


  private static int[][] getWalls() {
    return new int[][]{{10, 10, 2, 2}, {14, 14, 5, 1}};
  }

  private static int[][] getCoins() {
    return new int[][]{{6, 6, 1, 1}, {6, 8, 1, 1}, {7, 8, 1, 1},};
  }

  private static int calculateNewPosRow(int robotPosRow, String yourMove) {

    switch (yourMove) {
      case "w":
        return robotPosRow - 1;
      case "s":
        return robotPosRow + 1;
      default:
        return robotPosRow;
    }
  }

  private static int calculateNewPosCol(int robotPosCol, String yourMove) {

    switch (yourMove) {
      case "a":
        return robotPosCol - 1;
      case "d":
        return robotPosCol + 1;
      default:
        return robotPosCol;
    }
  }

}
