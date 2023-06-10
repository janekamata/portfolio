import java.awt.Color;
import java.util.ArrayList;

import javalib.funworld.World;
import javalib.funworld.WorldScene;
import javalib.worldimages.AboveImage;
import javalib.worldimages.ComputedPixelImage;
import javalib.worldimages.FontStyle;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

// represents a world in which Sokoban is played
class SokobanWorld extends World {
  // represents the state of the Sokoban board
  SokobanBoard boardState;

  // represents the history of the Sokoban world
  ArrayList<SokobanBoard> boardHistory = new ArrayList<SokobanBoard>();

  // represents the number of steps a player has made
  int counter;

  SokobanWorld(SokobanBoard boardState) {
    this.boardState = boardState;
    this.counter = 0;
  }

  SokobanWorld(SokobanBoard boardState, ArrayList<SokobanBoard> prevHistory, int counter) {
    this.boardState = boardState;
    this.boardHistory = prevHistory;
    this.counter = counter;
  }

  // renders this world's board into a scene
  public WorldScene makeScene() {
    return this.boardState.render(this.counter);
  }

  // allows the player to move around based on a key input
  // by producing a new world based on their input
  // stops when player is not found (fell into black hole) OR when level is won
  public World onKeyEvent(String key) {
    if (this.boardState.shouldEnd()) {
      if (this.boardState.levelWon()) {
        return this.endOfWorld("Level Won");
      }
      else {
        return this.endOfWorld("Level Lost");
      }
    }
    this.counter += 1;
    if (key.equals("u")) {
      if (this.boardHistory.size() == 0) {
        return this;
      }
      else {
        SokobanBoard newBoard = this.boardHistory.get(0);
        this.boardHistory.remove(0);
        return new SokobanWorld(newBoard, this.boardHistory, this.counter);
      }
    }
    this.boardHistory.add(0, this.boardState.historic());
    if (key.equals(">") || key.equals("d") || key.equals("right")) {
      return new SokobanWorld(this.boardState.playerMove("right"), this.boardHistory,
          this.counter);
    }
    else if ((key.equals("<") || key.equals("a")) || key.equals("left")) {
      return new SokobanWorld(this.boardState.playerMove("left"), this.boardHistory,
          this.counter);
    }
    else if ((key.equals("^") || key.equals("w")) || key.equals("up")) {
      return new SokobanWorld(this.boardState.playerMove("up"), this.boardHistory, this.counter);
    }
    else if ((key.equals("v") || key.equals("s")) || key.equals("down")) {
      return new SokobanWorld(this.boardState.playerMove("down"), this.boardHistory,
          this.counter);
    }
    else {
      return this;
    }
  }

  // overrides lastScene to return an appropriate image based on the message.
  // sends the message to lastScene within SokobanGame class to access the
  // appropriate size
  // to ensure no field of field access
  public WorldScene lastScene(String msg) {
    return this.boardState.lastScene(msg, this.counter);
  }
}

// tests and examples for SokobanWorld
class ExamplesSokobanWorld {

  // tests and examples for the SokobanWorld score
  boolean testOnKeyEvent_SokobanWorldCOUNTER(Tester t) {
    ArrayList<ICell> shortExLevelGround = new ArrayList<ICell>();
    shortExLevelGround.add(new Target(new Posn(1, 1), Color.red));
    shortExLevelGround.add(new Blank(new Posn(2, 1)));
    shortExLevelGround.add(new Blank(new Posn(1, 2)));
    shortExLevelGround.add(new Blank(new Posn(2, 2)));
    ArrayList<ICell> shortExLevelContents0 = new ArrayList<ICell>();
    shortExLevelContents0.add(new Player(new Posn(1, 1)));
    shortExLevelContents0.add(new Blank(new Posn(2, 1)));
    shortExLevelContents0.add(new Blank(new Posn(1, 2)));
    shortExLevelContents0.add(new Blank(new Posn(2, 2)));
    SokobanBoard shortExB0 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents0);
    SokobanWorld shortExW0 = new SokobanWorld(shortExB0);

    boolean beforeDownEXWO = t.checkExpect(shortExW0.counter, 0);
    shortExW0.onKeyEvent("down");
    boolean afterDownEXWO = t.checkExpect(shortExW0.counter, 1);
    shortExW0.onKeyEvent("right");
    boolean afterRightEXWO = t.checkExpect(shortExW0.counter, 2);
    shortExW0.onKeyEvent("left");
    boolean afterLeftEXWO = t.checkExpect(shortExW0.counter, 3);
    shortExW0.onKeyEvent("up");
    boolean afterUpEXWO = t.checkExpect(shortExW0.counter, 4);
    shortExW0.onKeyEvent("u");
    boolean afterUndoEXWO = t.checkExpect(shortExW0.counter, 5);

    return beforeDownEXWO && afterDownEXWO && afterRightEXWO && afterLeftEXWO && afterUpEXWO
        && afterUndoEXWO;
  }

  // tests and examples for undo in a SokobanWorld
  boolean testOnKeyEvent_SokobanWorldUNDO(Tester t) {
    ArrayList<ICell> shortExLevelGround = new ArrayList<ICell>();
    shortExLevelGround.add(new Target(new Posn(1, 1), Color.red));
    shortExLevelGround.add(new Blank(new Posn(2, 1)));
    shortExLevelGround.add(new Blank(new Posn(1, 2)));
    shortExLevelGround.add(new Blank(new Posn(2, 2)));
    ArrayList<ICell> shortExLevelContents0 = new ArrayList<ICell>();
    shortExLevelContents0.add(new Player(new Posn(1, 1)));
    shortExLevelContents0.add(new Blank(new Posn(2, 1)));
    shortExLevelContents0.add(new Blank(new Posn(1, 2)));
    shortExLevelContents0.add(new Blank(new Posn(2, 2)));
    SokobanBoard shortExB0 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents0);

    SokobanWorld shortExW0 = new SokobanWorld(shortExB0);

    // player moves down
    ArrayList<ICell> shortExLevelContents1 = new ArrayList<ICell>();
    shortExLevelContents1.add(new Blank(new Posn(2, 1)));
    shortExLevelContents1.add(new Blank(new Posn(1, 2)));
    shortExLevelContents1.add(new Blank(new Posn(2, 2)));
    shortExLevelContents1.add(new Player(new Posn(1, 2)));
    shortExLevelContents1.add(new Blank(new Posn(1, 1)));
    SokobanBoard shortExB1 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents1);

    ArrayList<SokobanBoard> history = new ArrayList<SokobanBoard>();
    history.add(shortExB0);

    SokobanWorld shortExW1 = new SokobanWorld(shortExB1, history, 1);

    SokobanWorld shortExW1_UNDO = new SokobanWorld(shortExB0, new ArrayList<SokobanBoard>(), 2);

    return t.checkExpect(shortExW0.onKeyEvent("u"), shortExW0)
        && t.checkExpect(shortExW1.onKeyEvent("u"), shortExW1_UNDO);
  }

  // tests and examples for makeScene in SokobanWorld
  boolean testMakeScene_SokobanWorld(Tester t) {
    WorldImage BLANK = new ComputedPixelImage(120, 120);
    WorldImage RTARGET = new FromFileImage("src/SokobanImages/RedTarget.png");
    WorldImage YTARGET = new FromFileImage("src/SokobanImages/YellowTarget.png");
    WorldImage BTARGET = new FromFileImage("src/SokobanImages/BlueTarget.png");
    WorldImage GTARGET = new FromFileImage("src/SokobanImages/GreenTarget.png");
    WorldImage PLAYER = new FromFileImage("src/SokobanImages/Player.png");
    WorldImage WALL = new FromFileImage("src/SokobanImages/Wall.png");
    WorldImage BOX = new FromFileImage("src/SokobanImages/Box.png");
    WorldImage RTROPHY = new FromFileImage("src/SokobanImages/RedTrophy.png");
    WorldImage YTROPHY = new FromFileImage("src/SokobanImages/YellowTrophy.png");
    WorldImage BTROPHY = new FromFileImage("src/SokobanImages/BlueTrophy.png");
    WorldImage GTROPHY = new FromFileImage("src/SokobanImages/GreenTrophy.png");
    WorldImage HOLE = new FromFileImage("src/SokobanImages/Hole.png");
    WorldImage ICE = new FromFileImage("src/SokobanImages/Ice.png");

    ArrayList<ICell> shortExLevelGround = new ArrayList<ICell>();
    shortExLevelGround.add(new Blank(new Posn(1, 1)));
    shortExLevelGround.add(new Ice(new Posn(2, 1)));
    shortExLevelGround.add(new Blank(new Posn(3, 1)));
    shortExLevelGround.add(new Blank(new Posn(4, 1)));
    shortExLevelGround.add(new Target(new Posn(5, 1), Color.red));
    shortExLevelGround.add(new Target(new Posn(1, 2), Color.yellow));
    shortExLevelGround.add(new Target(new Posn(2, 2), Color.blue));
    shortExLevelGround.add(new Target(new Posn(3, 2), Color.green));
    shortExLevelGround.add(new Blank(new Posn(4, 2)));
    shortExLevelGround.add(new Blank(new Posn(5, 2)));

    ArrayList<ICell> shortExLevelContents0 = new ArrayList<ICell>();
    shortExLevelContents0.add(new Player(new Posn(1, 1)));
    shortExLevelContents0.add(new Blank(new Posn(2, 1)));
    shortExLevelContents0.add(new Wall(new Posn(3, 1)));
    shortExLevelContents0.add(new Box(new Posn(4, 1)));
    shortExLevelContents0.add(new Trophy(new Posn(5, 1), Color.red));
    shortExLevelContents0.add(new Trophy(new Posn(1, 2), Color.yellow));
    shortExLevelContents0.add(new Trophy(new Posn(2, 2), Color.blue));
    shortExLevelContents0.add(new Trophy(new Posn(3, 2), Color.green));
    shortExLevelContents0.add(new Hole(new Posn(4, 2)));
    shortExLevelContents0.add(new Hole(new Posn(5, 2)));

    SokobanBoard shortExBoard = new SokobanBoard(new Posn(9, 1), shortExLevelGround,
        shortExLevelContents0);

    WorldScene result = new WorldScene(9 * 120, 1 * 120);
    result = result.placeImageXY(BLANK, 60, 60);
    result = result.placeImageXY(ICE, 180, 60);
    result = result.placeImageXY(BLANK, 300, 60);
    result = result.placeImageXY(BLANK, 420, 60);
    result = result.placeImageXY(RTARGET, 540, 60);

    result = result.placeImageXY(YTARGET, 60, 180);
    result = result.placeImageXY(BTARGET, 180, 180);
    result = result.placeImageXY(GTARGET, 300, 180);
    result = result.placeImageXY(BLANK, 420, 180);
    result = result.placeImageXY(BLANK, 540, 180);

    result = result.placeImageXY(PLAYER, 60, 60);
    result = result.placeImageXY(BLANK, 180, 60);
    result = result.placeImageXY(WALL, 300, 60);
    result = result.placeImageXY(BOX, 420, 60);
    result = result.placeImageXY(RTROPHY, 540, 60);

    result = result.placeImageXY(YTROPHY, 60, 180);
    result = result.placeImageXY(BTROPHY, 180, 180);
    result = result.placeImageXY(GTROPHY, 300, 180);
    result = result.placeImageXY(HOLE, 420, 180);
    result = result.placeImageXY(HOLE, 540, 180);

    TextImage counterImage = new TextImage("Score: " + 0, 24, FontStyle.BOLD, Color.BLACK);
    result = result.placeImageXY(
        counterImage.overlayImages(new RectangleImage(160, 50, OutlineMode.SOLID, Color.white)),
        9 + 85, 1 + 30);

    SokobanWorld shortExWorld = new SokobanWorld(shortExBoard);

    return t.checkExpect(shortExWorld.makeScene(), result);
  }

  // tests and examples for onKeyEvent down in SokobanWorld
  boolean testOnKeyEventDown_SokobanWorld(Tester t) {

    ArrayList<ICell> shortExLevelGround = new ArrayList<ICell>();
    shortExLevelGround.add(new Target(new Posn(1, 1), Color.red));
    shortExLevelGround.add(new Blank(new Posn(2, 1)));
    shortExLevelGround.add(new Blank(new Posn(1, 2)));
    shortExLevelGround.add(new Blank(new Posn(2, 2)));
    ArrayList<ICell> shortExLevelContents0 = new ArrayList<ICell>();
    shortExLevelContents0.add(new Player(new Posn(1, 1)));
    shortExLevelContents0.add(new Blank(new Posn(2, 1)));
    shortExLevelContents0.add(new Blank(new Posn(1, 2)));
    shortExLevelContents0.add(new Blank(new Posn(2, 2)));
    SokobanBoard shortExB0 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents0);
    SokobanWorld shortExW0 = new SokobanWorld(shortExB0);
    // player moves down
    ArrayList<ICell> shortExLevelContents1 = new ArrayList<ICell>();
    shortExLevelContents1.add(new Blank(new Posn(2, 1)));
    shortExLevelContents1.add(new Blank(new Posn(1, 2)));
    shortExLevelContents1.add(new Blank(new Posn(2, 2)));
    shortExLevelContents1.add(new Player(new Posn(1, 2)));
    shortExLevelContents1.add(new Blank(new Posn(1, 1)));
    SokobanBoard shortExB1 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents1);
    ArrayList<SokobanBoard> history1 = new ArrayList<>();
    history1.add(shortExB0.historic());
    SokobanWorld shortExW1 = new SokobanWorld(shortExB1, history1, 1);

    return t.checkExpect(shortExW0.onKeyEvent("down"), shortExW1);
  }

  //tests and examples for onKeyEvent left in SokobanWorld
  boolean testOnKeyEventLeft_SokobanWorld(Tester t) {

    ArrayList<ICell> shortExLevelGround = new ArrayList<ICell>();
    shortExLevelGround.add(new Target(new Posn(1, 1), Color.red));
    shortExLevelGround.add(new Blank(new Posn(2, 1)));
    shortExLevelGround.add(new Blank(new Posn(1, 2)));
    shortExLevelGround.add(new Blank(new Posn(2, 2)));
    ArrayList<ICell> shortExLevelContents0 = new ArrayList<ICell>();
    shortExLevelContents0.add(new Player(new Posn(2, 1)));
    shortExLevelContents0.add(new Blank(new Posn(1, 1)));
    shortExLevelContents0.add(new Blank(new Posn(1, 2)));
    shortExLevelContents0.add(new Blank(new Posn(2, 2)));
    SokobanBoard shortExB0 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents0);
    SokobanWorld shortExW0 = new SokobanWorld(shortExB0);
    // player moves left
    ArrayList<ICell> shortExLevelContents1 = new ArrayList<ICell>();
    shortExLevelContents1.add(new Blank(new Posn(1, 1)));
    shortExLevelContents1.add(new Blank(new Posn(1, 2)));
    shortExLevelContents1.add(new Blank(new Posn(2, 2)));
    shortExLevelContents1.add(new Player(new Posn(1, 1)));
    shortExLevelContents1.add(new Blank(new Posn(2, 1)));
    SokobanBoard shortExB1 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents1);
    ArrayList<SokobanBoard> history1 = new ArrayList<>();
    history1.add(shortExB0.historic());
    SokobanWorld shortExW1 = new SokobanWorld(shortExB1, history1, 1);

    return t.checkExpect(shortExW0.onKeyEvent("left"), shortExW1);
  }

  //tests and examples for onKeyEvent right in SokobanWorld
  boolean testOnKeyEventRight_SokobanWorld(Tester t) {

    ArrayList<ICell> shortExLevelGround = new ArrayList<ICell>();
    shortExLevelGround.add(new Target(new Posn(1, 1), Color.red));
    shortExLevelGround.add(new Blank(new Posn(2, 1)));
    shortExLevelGround.add(new Blank(new Posn(1, 2)));
    shortExLevelGround.add(new Blank(new Posn(2, 2)));
    ArrayList<ICell> shortExLevelContents0 = new ArrayList<ICell>();
    shortExLevelContents0.add(new Player(new Posn(1, 1)));
    shortExLevelContents0.add(new Blank(new Posn(2, 1)));
    shortExLevelContents0.add(new Blank(new Posn(1, 2)));
    shortExLevelContents0.add(new Blank(new Posn(2, 2)));
    SokobanBoard shortExB0 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents0);
    SokobanWorld shortExW0 = new SokobanWorld(shortExB0);
    // player moves right
    ArrayList<ICell> shortExLevelContents1 = new ArrayList<ICell>();
    shortExLevelContents1.add(new Blank(new Posn(2, 1)));
    shortExLevelContents1.add(new Blank(new Posn(1, 2)));
    shortExLevelContents1.add(new Blank(new Posn(2, 2)));
    shortExLevelContents1.add(new Player(new Posn(2, 1)));
    shortExLevelContents1.add(new Blank(new Posn(1, 1)));
    SokobanBoard shortExB1 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents1);
    ArrayList<SokobanBoard> history1 = new ArrayList<>();
    history1.add(shortExB0.historic());
    SokobanWorld shortExW1 = new SokobanWorld(shortExB1, history1, 1);

    return t.checkExpect(shortExW0.onKeyEvent("right"), shortExW1);
  }

  //tests and examples for onKeyEvent up in SokobanWorld
  boolean testOnKeyEventUp_SokobanWorld(Tester t) {

    ArrayList<ICell> shortExLevelGround = new ArrayList<ICell>();
    shortExLevelGround.add(new Target(new Posn(1, 1), Color.red));
    shortExLevelGround.add(new Blank(new Posn(2, 1)));
    shortExLevelGround.add(new Blank(new Posn(1, 2)));
    shortExLevelGround.add(new Blank(new Posn(2, 2)));
    ArrayList<ICell> shortExLevelContents0 = new ArrayList<ICell>();
    shortExLevelContents0.add(new Player(new Posn(1, 2)));
    shortExLevelContents0.add(new Blank(new Posn(2, 1)));
    shortExLevelContents0.add(new Blank(new Posn(1, 1)));
    shortExLevelContents0.add(new Blank(new Posn(2, 2)));
    SokobanBoard shortExB0 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents0);
    SokobanWorld shortExW0 = new SokobanWorld(shortExB0);
    // player moves up
    ArrayList<ICell> shortExLevelContents1 = new ArrayList<ICell>();
    shortExLevelContents1.add(new Blank(new Posn(2, 1)));
    shortExLevelContents1.add(new Blank(new Posn(1, 1)));
    shortExLevelContents1.add(new Blank(new Posn(2, 2)));
    shortExLevelContents1.add(new Player(new Posn(1, 1)));
    shortExLevelContents1.add(new Blank(new Posn(1, 2)));
    SokobanBoard shortExB1 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents1);
    ArrayList<SokobanBoard> history1 = new ArrayList<>();
    history1.add(shortExB0.historic());
    SokobanWorld shortExW1 = new SokobanWorld(shortExB1, history1, 1);

    return t.checkExpect(shortExW0.onKeyEvent("up"), shortExW1);
  }

  boolean testLastScene(Tester t) {
    WorldImage BLANK = new ComputedPixelImage(120, 120);
    WorldImage RTARGET = new FromFileImage("src/SokobanImages/RedTarget.png");
    WorldImage YTARGET = new FromFileImage("src/SokobanImages/YellowTarget.png");
    WorldImage BTARGET = new FromFileImage("src/SokobanImages/BlueTarget.png");
    WorldImage GTARGET = new FromFileImage("src/SokobanImages/GreenTarget.png");
    WorldImage PLAYER = new FromFileImage("src/SokobanImages/Player.png");
    WorldImage WALL = new FromFileImage("src/SokobanImages/Wall.png");
    WorldImage BOX = new FromFileImage("src/SokobanImages/Box.png");
    WorldImage RTROPHY = new FromFileImage("src/SokobanImages/RedTrophy.png");
    WorldImage YTROPHY = new FromFileImage("src/SokobanImages/YellowTrophy.png");
    WorldImage BTROPHY = new FromFileImage("src/SokobanImages/BlueTrophy.png");
    WorldImage GTROPHY = new FromFileImage("src/SokobanImages/GreenTrophy.png");
    WorldImage HOLE = new FromFileImage("src/SokobanImages/Hole.png");

    ArrayList<ICell> shortExLevelGround = new ArrayList<ICell>();
    shortExLevelGround.add(new Blank(new Posn(1, 1)));
    shortExLevelGround.add(new Blank(new Posn(2, 1)));
    shortExLevelGround.add(new Blank(new Posn(3, 1)));
    shortExLevelGround.add(new Blank(new Posn(4, 1)));
    shortExLevelGround.add(new Target(new Posn(5, 1), Color.red));
    shortExLevelGround.add(new Target(new Posn(1, 2), Color.yellow));
    shortExLevelGround.add(new Target(new Posn(2, 2), Color.blue));
    shortExLevelGround.add(new Target(new Posn(3, 2), Color.green));
    shortExLevelGround.add(new Blank(new Posn(4, 2)));
    shortExLevelGround.add(new Blank(new Posn(5, 2)));

    ArrayList<ICell> shortExLevelContents0 = new ArrayList<ICell>();
    shortExLevelContents0.add(new Player(new Posn(1, 1)));
    shortExLevelContents0.add(new Blank(new Posn(2, 1)));
    shortExLevelContents0.add(new Wall(new Posn(3, 1)));
    shortExLevelContents0.add(new Box(new Posn(4, 1)));
    shortExLevelContents0.add(new Trophy(new Posn(5, 1), Color.red));
    shortExLevelContents0.add(new Trophy(new Posn(1, 2), Color.yellow));
    shortExLevelContents0.add(new Trophy(new Posn(2, 2), Color.blue));
    shortExLevelContents0.add(new Trophy(new Posn(3, 2), Color.green));
    shortExLevelContents0.add(new Hole(new Posn(4, 2)));
    shortExLevelContents0.add(new Hole(new Posn(5, 2)));

    SokobanBoard shortExBoard = new SokobanBoard(new Posn(9, 1), shortExLevelGround,
        shortExLevelContents0);

    WorldScene result = new WorldScene(9 * 120, 1 * 120);
    result = result.placeImageXY(BLANK, 60, 60);
    result = result.placeImageXY(BLANK, 180, 60);
    result = result.placeImageXY(BLANK, 300, 60);
    result = result.placeImageXY(BLANK, 420, 60);
    result = result.placeImageXY(RTARGET, 540, 60);

    result = result.placeImageXY(YTARGET, 60, 180);
    result = result.placeImageXY(BTARGET, 180, 180);
    result = result.placeImageXY(GTARGET, 300, 180);
    result = result.placeImageXY(BLANK, 420, 180);
    result = result.placeImageXY(BLANK, 540, 180);

    result = result.placeImageXY(PLAYER, 60, 60);
    result = result.placeImageXY(BLANK, 180, 60);
    result = result.placeImageXY(WALL, 300, 60);
    result = result.placeImageXY(BOX, 420, 60);
    result = result.placeImageXY(RTROPHY, 540, 60);

    result = result.placeImageXY(YTROPHY, 60, 180);
    result = result.placeImageXY(BTROPHY, 180, 180);
    result = result.placeImageXY(GTROPHY, 300, 180);
    result = result.placeImageXY(HOLE, 420, 180);
    result = result.placeImageXY(HOLE, 540, 180);

    SokobanWorld shortExWorld = new SokobanWorld(shortExBoard);

    WorldScene scene = new WorldScene(9 * 120, 1 * 120);

    AboveImage levelWonImage = new AboveImage(new TextImage("Level Won", 24, FontStyle.BOLD,
        Color.BLACK),
        new TextImage("Score: " + 0, 14, FontStyle.BOLD,
            Color.BLACK));
    WorldScene levelWon = scene.placeImageXY(levelWonImage, (9 * 120) / 2,
        60);

    AboveImage levelLostImage = new AboveImage(new TextImage("Level Lost", 24, FontStyle.BOLD,
        Color.BLACK), new TextImage("Score: " + 0, 14, FontStyle.BOLD, Color.BLACK));
    WorldScene levelLost = scene.placeImageXY(levelLostImage, (9 * 120) / 2,
        60);

    return t.checkExpect(shortExWorld.lastScene("Level Won"), levelWon)
        && t.checkExpect(shortExWorld.lastScene("Level Lost"), levelLost);

  }
}

//tests and examples for SokobanWorld (basic)
class ExamplesSokobanWorldBasic {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "________\n" + "___R____\n" + "________\n" + "_B____Y_\n"
        + "________\n" + "___G____\n" + "________";

    String givenExLevelContents = "__WWW___\n" + "__W_WW__\n" + "WW___WWW\n" + "W>g____W\n"
        + "WWH_WWWW\n" + "_WW_W___\n" + "__WWW___";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

//tests and examples as the first level of sokobon
class ExamplesSokobanWorldLevelWon {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "________\n" + "________\n" + "_B______\n" + "_____G__\n"
        + "_R______\n" + "____Y___\n" + "______R_\n" + "____G___\n" + "________";

    String givenExLevelContents = "__WWWWW_\n" + "WWW___W_\n" + "W_<b__W_\n" + "WWW_g_W_\n"
        + "W_WWy_W_\n" + "W_W___WW\n" + "Wr_bgr_W\n" + "W______W\n" + "WWWWWWWW";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

// tests and examples for specifically holes
class ExamplesSokobanWorldHoles {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "_______\n" + "_______\n" + "_______\n" + "__R____\n"
        + "_______\n"
        + "_______\n" + "_______";

    String givenExLevelContents = "WWWWWWW\n" + "W_>___W\n" + "W_H_r_W\n" + "WH_HB_W\n"
        + "W_H___W\n" + "W_____W\n" + "WWWWWWW";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

//tests and examples for SokobanWorld (Ice)
// Testing simple world with ice when the trophy doesn't start next to the player
// To see if it matters where on the ice a trophy starts and what happens when
// it starts on ice
class ExamplesSokobanWorldIceSimpleTrophy1 {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "_II_B";
    String givenExLevelContents = ">_g__";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

//tests and examples for SokobanWorld (Ice)
//Testing simple world with ice when the trophy starts next to the player
//To see if it matters where on the ice a trophy starts and what happens when
//it starts on ice
class ExamplesSokobanWorldIceSimpleTrophy2 {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "_II_B";
    String givenExLevelContents = ">g___";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

//tests and examples for SokobanWorld (Ice)
//Testing simple world with ice when the box doesn't start next to the player
//To see if it matters where on the ice a box starts and what happens when
//it starts on ice
class ExamplesSokobanWorldIceSimpleBox1 {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "_II_B";
    String givenExLevelContents = ">_B__";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

//tests and examples for SokobanWorld (Ice)
//Testing simple world with ice when the box starts next to the player
//To see if it matters where on the ice a box starts and what happens when
//it starts on ice
class ExamplesSokobanWorldIceSimpleBox2 {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "_II_B";
    String givenExLevelContents = ">B___";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

//tests and examples for SokobanWorld (Ice)
// Testing world with ice when the trophy and player both do not start on ice
// To see if both the player and the trophy will slide when the player moves
class ExamplesSokobanWorldIceDemo1 {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "_________\n" + "___II__B_\n" + "_________";
    String givenExLevelContents = "WWWWWWWWW\n" + "_>b______\n" + "WWWWWWWWW";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

//tests and examples for SokobanWorld (Ice)
// Testing world with ice when the trophy starts at the end of the ice and the
// player starts at the beginning
// To see if the player will push the trophy when it reaches the end
class ExamplesSokobanWorldIceDemo2 {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "_________\n" + "___II__B_\n" + "_________";
    String givenExLevelContents = "WWWWWWWWW\n" + "_>___b___\n" + "WWWWWWWWW";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

//tests and examples for SokobanWorld (Ice)
// Testing world with ice when the trophy starts in front of the player and is on ice
//To see if it matters where on the ice a trophy starts and what happens when
//it starts on ice
class ExamplesSokobanWorldIceIntro {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "________\n" + "__YI____\n" + "__II____\n" + "________\n"
        + "________";
    String givenExLevelContents = "_WWWWWWW\n" + "WW_____W\n" + "W>y____W\n" + "WW___WWW\n"
        + "_WWWWW__";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

// tests and examples for SokobanWorld (Ice)
// Testing world with ice when two moveable objects are adjacent but only one is on the ice
// To see if the objects are considered too heavy when only one is on the ice
// (nothing should move because it's too heavy)
class ExamplesSokobanWorldHeavy {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "________\n" + "__YI____\n" + "__II___\n" + "________\n"
        + "________";
    String givenExLevelContents = "_WWWWWWW\n" + "WW_____W\n" + ">_yB___W\n" + "WW___WWW\n"
        + "_WWWWW__";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

//tests and examples for SokobanWorld (Ice) with a bunch of objects in a row
// Testing world with ice when there are many trophies and boxes in front of the player
// and a hole at the end
// To see what happens with a slide-n-slide of an ice runway followed by a pit trap
// (nothing should move because it's too heavy)
class ExamplesSokobanWorldIceSlipAndSlide {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "_________\n" + "___IIII_B\n" + "_________";
    String givenExLevelContents = "WWWWWWWWW\n" + "W_>gBrBHW\n" + "WWWWWWWWW";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}

//tests and examples for SokobanWorld (Ice)
// Testing world with ice where there are no moveable or non-moveable objects so
// the player is able to move freely
// To see that the player slides correctly on the ice
// (should move to the corners)
class ExamplesSokobanWorldPUREICE {
  boolean testsWorld(Tester t) {
    String givenExLevelGround = "IIIII\n" + "IIIII\n" + "IIBII\n" + "IIIII\n" + "IIIII\n"
        + "IIIII";
    String givenExLevelContents = "WWWWW\n" + "W_>_W\n" + "W___W\n" + "W___W\n" + "W___W\n"
        + "WWWWW";

    SokobanBoard shortExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);
    SokobanWorld shortExW = new SokobanWorld(shortExB);

    return shortExW.bigBang(shortExB.size.x * 120, shortExB.size.y * 120, 0.1);
  }
}