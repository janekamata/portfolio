
import java.awt.Color;
import java.util.ArrayList;

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

// represents the state of the Sokoban game's board
class SokobanBoard {
  // represents the width and height of this board
  Posn size;
  // represents the ground cells of this board
  ArrayList<ICell> levelGroundCells = new ArrayList<ICell>();
  // represents the content cells of this board
  ArrayList<ICell> levelContentsCells = new ArrayList<ICell>();

  SokobanBoard(Posn size, ArrayList<ICell> levelGroundCells,
      ArrayList<ICell> levelContentsCells) {
    this.size = size;
    this.levelGroundCells = levelGroundCells;
    this.levelContentsCells = levelContentsCells;
  }

  // constructor to create a board based on two strings
  SokobanBoard(String levelGround, String levelContents) {
    if (!(new Utils().samePosn(new Utils().findSize(levelContents),
        (new Utils().findSize(levelGround))))) {
      throw new IllegalArgumentException(
          "Dimensions of given level ground do not match dimensions of given level contents");
    }

    int playerCount = 0;
    // goes through the length of the contents to count all instances of a player
    // character
    // starts at an index of 0 in the arraylist of string contents
    // checks if any of the characters are a player character, adds one the player
    // count
    for (int i = 0; i < levelContents.length(); i++) {
      char c = levelContents.charAt(i);
      if ((c == '>') || (c == '<') || (c == '^') || (c == 'v')) {
        playerCount += 1;
      }
    }
    if (playerCount != 1) {
      throw new IllegalArgumentException("The board should contain exactly one player.");
    }

    this.size = new Utils().findSize(levelContents);
    this.levelGroundCells.addAll(new Utils().toLevelCells(levelGround, true));
    this.levelContentsCells.addAll(new Utils().toLevelCells(levelContents, false));
  }

  // produces a new SokobanBoard with this board's size, a list of ground cells with the
  // same contents as this board, and a list of content cells with the same contents as this board
  SokobanBoard historic() {
    ArrayList<ICell> historicGroundCells = new ArrayList<ICell>();
    historicGroundCells.addAll(this.levelGroundCells);
    ArrayList<ICell> historicContentCells = new ArrayList<ICell>();
    historicContentCells.addAll(this.levelContentsCells);
    return new SokobanBoard(this.size, historicGroundCells, historicContentCells);
  }

  // renders this Sokoban board into an image
  WorldScene render(int counter) {
    WorldScene result = new WorldScene(this.size.x * 120, this.size.y * 120);
    ArrayList<ICell> fullBoard = new ArrayList<ICell>();
    fullBoard.addAll(this.levelGroundCells);
    fullBoard.addAll(this.levelContentsCells);
    // for every cell in the list,
    // places the image at the given coordinates in the resulting world
    for (ICell cell : fullBoard) {
      int x = cell.accept(new CellPosnVisitor()).x;
      int y = cell.accept(new CellPosnVisitor()).y;
      result = result.placeImageXY(cell.drawICell(), (x * 120) - 60, (y * 120) - 60);
    }
    TextImage counterImage = new TextImage("Score: " + counter, 24, FontStyle.BOLD, Color.BLACK);
    return result.placeImageXY(
        counterImage.overlayImages(new RectangleImage(160, 50, OutlineMode.SOLID, Color.white)),
        this.size.x + 85, this.size.y + 30);
  }

  // produces a new board based on this board with the player moved in the given
  // direction
  // if the player is able to move there
  SokobanBoard playerMove(String direction) {
    // gets that player from the contents
    ICell player = this.levelContentsCells
        .get(new Utils().findPlayerIndex(this.levelContentsCells));
    // fills the place the player was
    ICell fillPlace = new Blank(player.accept(new CellPosnVisitor()));
    // finds the next content block from the player in the direction
    ICell nextContent = new Utils().findNext(this.levelContentsCells, direction,
        player.accept(new CellPosnVisitor()), this.size);
    // finds the next ground block from the player in the direction
    ICell nextGround = new Utils().findNext(this.levelGroundCells, direction,
        player.accept(new CellPosnVisitor()), this.size);
    // moves the player to the next block based on ground
    ICell newPlayerGround = nextGround
        .accept(new MovePlayerVisitor(player, direction,
            this.levelGroundCells, this.levelContentsCells, this.size));
    // moves the player to the next block based on content
    ICell newPlayerContent = nextContent
        .accept(new MovePlayerVisitor(player, direction, this.levelGroundCells,
            this.levelContentsCells, this.size));
    // removes the player from the contents
    this.levelContentsCells.remove(new Utils().findPlayerIndex(this.levelContentsCells));
    // adds the moved player to the contents
    if ((nextContent.accept(new MoveableObjectCanMoveToVisitor())
        && (!new Utils().samePosn(newPlayerGround.accept(new CellPosnVisitor()),
            newPlayerContent.accept(new CellPosnVisitor()))))
        || new Utils().samePosn(newPlayerGround.accept(new CellPosnVisitor()),
            player.accept(new CellPosnVisitor()))) {
      this.levelContentsCells.add(newPlayerGround);
    }
    else {
      this.levelContentsCells.add(newPlayerContent);
    }
    // removes any duplicate cells to the given
    this.levelContentsCells = new Utils().noDupes(this.levelContentsCells, fillPlace);
    // returns the new board
    return new SokobanBoard(this.size, this.levelGroundCells, this.levelContentsCells);
  }

  // determines if this board has been won
  // (every target has a trophy on top with the correct color)
  boolean levelWon() {
    boolean result = true;
    ICell contentCell;
    // for every cell in the list, determines if the cell is a good pair (winnable
    // pair) with the
    // cell at the same location in this board's list of contents
    for (ICell cell : this.levelGroundCells) {
      contentCell = new Utils().findCell(this.levelContentsCells,
          cell.accept(new CellPosnVisitor()).x, cell.accept(new CellPosnVisitor()).y);
      result = result && cell.accept(new GoodPairVisitor(contentCell));
    }
    return result;
  }

  // checks if the level should end
  // under the conditions that level is won or that no player is found
  boolean shouldEnd() {
    boolean levelWon = this.levelWon();
    boolean noPlayer = new Utils().findPlayerIndex(this.levelContentsCells) == -1;
    return levelWon || noPlayer;
  }

  // displays the appropriate ending screening based on the given string
  // returns an image of level won if all trophies are on the targets
  // returns an image of level lost if there is no player found
  // is called within lastScene in SokobanWorld class so it is displayed properly
  public WorldScene lastScene(String msg, int counter) {
    WorldScene scene = new WorldScene(this.size.x * 120, this.size.y * 120);

    AboveImage levelWonImage = new AboveImage(new TextImage("Level Won", 24, FontStyle.BOLD,
        Color.BLACK),
        new TextImage("Score: " + counter, 14, FontStyle.BOLD,
            Color.BLACK));
    WorldScene levelWon = scene.placeImageXY(levelWonImage, (this.size.x * 120) / 2,
        (this.size.y * 120) / 2);

    AboveImage levelLostImage = new AboveImage(new TextImage("Level Lost", 24, FontStyle.BOLD,
        Color.BLACK),
        new TextImage("Score: " + counter, 14, FontStyle.BOLD,
            Color.BLACK));
    WorldScene levelLost = scene.placeImageXY(levelLostImage, (this.size.x * 120) / 2,
        (this.size.y * 120) / 2);

    if (msg.equals("Level Won")) {
      return levelWon;
    }
    else {
      return levelLost;
    }
  }
}

// represents a cell in the board
interface ICell {

  // produces an image of this cell
  WorldImage drawICell();

  // to return the result of applying the given visitor to this Cell
  <T> T accept(ICellVisitor<T> visitor);

  // helps determine the location of the player in the board
  Posn findPlayer();

  // produces a new cell moved to the new coordinates
  // based on the given direction and this cell
  ICell move(String direction);

  // produces a new cell moved to the new coordinates
  // based on the given coordinates
  ICell move(Posn location);

  // determines if this cell is a good pair with the given color
  // (good pair is only a trophy with the corresponding color)
  boolean goodPair(Color color);
}

// represents any cell in the board
abstract class AICell implements ICell {
  // represents the location of this Cell
  Posn coord;
  // represents whether this cell is a ground cell
  boolean isGround;

  AICell(Posn coord, boolean isGround) {
    this.coord = coord;
    this.isGround = isGround;
  }

  // produces an image of this cell
  public abstract WorldImage drawICell();

  // to return the result of applying the given visitor to this Cell
  public abstract <T> T accept(ICellVisitor<T> visitor);

  // helps determine the location of the player in the board
  public Posn findPlayer() {
    return new Posn(-1, -1);
  }

  // produces a new cell moved to the new coordinates
  // based on the given direction and this cell
  public ICell move(String direction) {
    return this;
  }

  // produces a new cell moved to the new coordinates
  // based on the given coordinates
  public ICell move(Posn location) {
    this.coord = location;
    return this;
  }

  // determines if this cell is a good pair with the given color
  // (good pair is only a trophy with the corresponding color)
  public boolean goodPair(Color color) {
    return false;
  }
}

// represents a blank cell in the board
class Blank extends AICell {

  Blank(Posn coord) {
    super(coord, true);
  }

  // produces an image of this Blank
  public WorldImage drawICell() {
    return new ComputedPixelImage(120, 120);
  }

  // to return the result of applying the given visitor to this Blank
  public <T> T accept(ICellVisitor<T> visitor) {
    return visitor.visitBlank(this);
  }

}

//represents a wall cell in the board
class Wall extends AICell {

  Wall(Posn coord) {
    super(coord, false);
  }

  // produces an image of this Wall
  public WorldImage drawICell() {
    return new FromFileImage("src/SokobanImages/Wall.png");
  }

  // to return the result of applying the given visitor to this Wall
  public <T> T accept(ICellVisitor<T> visitor) {
    return visitor.visitWall(this);
  }

}

// represents a box cell in the board
class Box extends AICell {

  Box(Posn coord) {
    super(coord, false);
  }

  // produces an image of this Box
  public WorldImage drawICell() {
    return new FromFileImage("src/SokobanImages/Box.png");
  }

  // to return the result of applying the given visitor to this Box
  public <T> T accept(ICellVisitor<T> visitor) {
    return visitor.visitBox(this);
  }

  // produces a new box moved to the new coordinates
  // based on the given direction and this box cell
  public ICell move(String direction) {
    if (direction.equals("right")) {
      return new Box(new Posn(this.coord.x + 1, this.coord.y));
    }
    else if (direction.equals("left")) {
      return new Box(new Posn(this.coord.x - 1, this.coord.y));
    }
    else if (direction.equals("up")) {
      return new Box(new Posn(this.coord.x, this.coord.y - 1));

    }
    else if (direction.equals("down")) {
      return new Box(new Posn(this.coord.x, this.coord.y + 1));
    }
    else {
      return this;
    }
  }

}

//represents a player cell in the board
class Player extends AICell {

  Player(Posn coord) {
    super(coord, false);
  }

  // produces an image of this Player
  public WorldImage drawICell() {
    return new FromFileImage("src/SokobanImages/Player.png");
  }

  // to return the result of applying the given visitor to this Player
  public <T> T accept(ICellVisitor<T> visitor) {
    return visitor.visitPlayer(this);
  }

  // to determines the location of this player
  public Posn findPlayer() {
    return this.coord;
  }

  // produces a new player moved to the new coordinates
  // based on the given direction and this player cell
  public ICell move(String direction) {
    if (direction.equals("right")) {
      return new Player(new Posn(this.coord.x + 1, this.coord.y));
    }
    else if (direction.equals("left")) {
      return new Player(new Posn(this.coord.x - 1, this.coord.y));
    }
    else if (direction.equals("up")) {
      return new Player(new Posn(this.coord.x, this.coord.y - 1));

    }
    else if (direction.equals("down")) {
      return new Player(new Posn(this.coord.x, this.coord.y + 1));
    }
    else {
      return this;
    }
  }

}

//represents a target cell in the board
class Target extends AICell {
  // represents this target's color
  Color color;

  Target(Posn coord, Color color) {
    super(coord, true);
    this.color = color;
  }

  // produces an image of this Target based on the color
  public WorldImage drawICell() {
    if (this.color.equals(Color.yellow)) {
      return new FromFileImage("src/SokobanImages/YellowTarget.png");
    }
    else if (this.color.equals(Color.green)) {
      return new FromFileImage("src/SokobanImages/GreenTarget.png");
    }
    else if (this.color.equals(Color.blue)) {
      return new FromFileImage("src/SokobanImages/BlueTarget.png");
    }
    else {
      return new FromFileImage("src/SokobanImages/RedTarget.png");
    }
  }

  // to return the result of applying the given visitor to this Target
  public <T> T accept(ICellVisitor<T> visitor) {
    return visitor.visitTarget(this);
  }
}

//represents a trophy cell in the board
class Trophy extends AICell {
  // represents this trophy's color
  Color color;

  Trophy(Posn coord, Color color) {
    super(coord, true);
    this.color = color;
  }

  // produces an image of this Trophy based on the color
  public WorldImage drawICell() {
    if (this.color.equals(Color.yellow)) {
      return new FromFileImage("src/SokobanImages/YellowTrophy.png");
    }
    else if (this.color.equals(Color.green)) {
      return new FromFileImage("src/SokobanImages/GreenTrophy.png");
    }
    else if (this.color.equals(Color.blue)) {
      return new FromFileImage("src/SokobanImages/BlueTrophy.png");
    }
    else {
      return new FromFileImage("src/SokobanImages/RedTrophy.png");
    }
  }

  // to return the result of applying the given visitor to this Trophy
  public <T> T accept(ICellVisitor<T> visitor) {
    return visitor.visitTrophy(this);
  }

  // produces a new Trophy moved to the new coordinates
  // based on the given direction and this Trophy cell
  public ICell move(String direction) {
    if (direction.equals("right")) {
      return new Trophy(new Posn(this.coord.x + 1, this.coord.y), this.color);
    }
    else if (direction.equals("left")) {
      return new Trophy(new Posn(this.coord.x - 1, this.coord.y), this.color);
    }
    else if (direction.equals("up")) {
      return new Trophy(new Posn(this.coord.x, this.coord.y - 1), this.color);

    }
    else if (direction.equals("down")) {
      return new Trophy(new Posn(this.coord.x, this.coord.y + 1), this.color);
    }
    else {
      return this;
    }
  }

  // determines if this trophy cell is a good pair with the given color
  // (good pair is only a trophy with the corresponding color)
  public boolean goodPair(Color color) {
    return this.color.equals(color);
  }

}

// represents a hole cell in the board
class Hole extends AICell {

  Hole(Posn coord) {
    super(coord, false);
  }

  // produces an image of this Hole
  public WorldImage drawICell() {
    return new FromFileImage("src/SokobanImages/Hole.png");
  }

  // to return the result of applying the given visitor to this Hole
  public <T> T accept(ICellVisitor<T> visitor) {
    return visitor.visitHole(this);
  }
}

//represents a ice cell in the board
class Ice extends AICell {

  Ice(Posn coord) {
    super(coord, true);
  }

  // produces an image of this Hole
  public WorldImage drawICell() {
    return new FromFileImage("src/SokobanImages/Ice.png");
  }

  // to return the result of applying the given visitor to this Hole
  public <T> T accept(ICellVisitor<T> visitor) {
    return visitor.visitIce(this);
  }

  //helps determine the location of the player in the board
  public Posn findPlayer() {
    return new Posn(-2, -2);
  }
}

// to represent a visitor that visit (implements a function over ICell objects)
// an ICell and produces a result of type T
interface ICellVisitor<T> {

  // to represent a visitor that visits a blank ICell and produces a result of
  // type T
  T visitBlank(Blank blank);

  // to represent a visitor that visits a wall ICell and produces a result of type
  // T
  T visitWall(Wall wall);

  // to represent a visitor that visits a box ICell and produces a result of type
  // T
  T visitBox(Box box);

  // to represent a visitor that visits a player ICell and produces a result of
  // type T
  T visitPlayer(Player player);

  // to represent a visitor that visits a target ICell and produces a result of
  // type T
  T visitTarget(Target target);

  // to represent a visitor that visits a trophy ICell and produces a result of
  // type T
  T visitTrophy(Trophy trophy);

  // to represent a visitor that visits a hole ICell and produces a result of type
  // T
  T visitHole(Hole hole);

  //to represent a visitor that visits a ice ICell and produces a result of type
  // T
  T visitIce(Ice ice);
}

// to represent an ICellVisitor that visits an ICell and evaluates the cell to a Posn
// representing the location of the visited cell
class CellPosnVisitor implements ICellVisitor<Posn> {

  // to represent a visitor that visits a blank ICell and produces a Posn
  // representing the location of the visited blank cell
  public Posn visitBlank(Blank blank) {
    return blank.coord;
  }

  // to represent a visitor that visits a wall ICell and produces a Posn
  // representing the location of the visited wall cell
  public Posn visitWall(Wall wall) {
    return wall.coord;
  }

  // to represent a visitor that visits a box ICell and produces a Posn
  // representing the location of the visited box cell
  public Posn visitBox(Box box) {
    return box.coord;
  }

  // to represent a visitor that visits a player ICell and produces a Posn
  // representing the location of the visited player cell
  public Posn visitPlayer(Player player) {
    return player.coord;
  }

  // to represent a visitor that visits a target ICell and produces a Posn
  // representing the location of the visited target cell
  public Posn visitTarget(Target target) {
    return target.coord;
  }

  // to represent a visitor that visits a trophy ICell and produces a Posn
  // representing the location of the visited trophy cell
  public Posn visitTrophy(Trophy trophy) {
    return trophy.coord;
  }

  // to represent a visitor that visits a hole ICell and produces a Posn
  // representing the location of the visited hole cell
  public Posn visitHole(Hole hole) {
    return hole.coord;
  }

  //to represent a visitor that visits a ice ICell and produces a Posn
  // representing the location of the visited ice cell
  public Posn visitIce(Ice ice) {
    return ice.coord;
  }
}

// to represent an abstract ICellVisitor that visits an ICell and moves it to the given ICell
//if an only if it should be able to be moved
abstract class AMoveVisitor implements ICellVisitor<ICell> {
  // to represent a cell of this board
  ICell cell;
  // to represent the direction of movement
  String direction;
  //to represent the level ground cells of this board
  ArrayList<ICell> levelGroundCells;
  // to represent the level content cells of this board
  ArrayList<ICell> levelContentsCells;
  // to represent the size of the board
  Posn size;

  AMoveVisitor(ICell cell, String direction, ArrayList<ICell> levelGroundCells,
      ArrayList<ICell> levelContentsCells, Posn size) {
    this.cell = cell;
    this.direction = direction;
    this.levelGroundCells = levelGroundCells;
    this.levelContentsCells = levelContentsCells;
    this.size = size;
  }

  // moves the cell onto a blank space
  public ICell visitBlank(Blank blank) {
    return this.cell.move(this.direction);
  }

  // returns a wall as a cell is unable to move onto a wall
  public ICell visitWall(Wall wall) {
    return this.cell;
  }

  // returns a box as a cell is unable to move onto a box
  public ICell visitBox(Box box) {
    return this.cell;
  }

  // returns a cell as a cell is unable to move onto a player
  public ICell visitPlayer(Player player) {
    return this.cell;
  }

  // moves a cell as it moves onto a target
  public ICell visitTarget(Target target) {
    return this.cell.move(this.direction);
  }

  // returns a trophy as a cell is unable to move onto a trophy
  public ICell visitTrophy(Trophy trophy) {
    return this.cell;
  }

  // returns a blank as the cell is now lost forever and the hole is gone
  public ICell visitHole(Hole hole) {
    return new Blank(hole.coord);
  }

  //to represent a visitor that visits an ice ICell and produces an ICell
  // representing a new cell  moved on the ice in the given direction
  // will slide the cell in that direction until they collide with an immovable
  // item,
  // or until they land on a non-ice tile.
  // If, while colliding, they crash into a movable item, then the movable item
  // starts sliding too
  public ICell visitIce(Ice ice) {
    return this.cell.move(this.direction);
  }
}

//to represent an ICellVisitor that visits an ICell and moves trophy to the given ICell
//if an only if it should be able to be moved
class MoveBoxVisitor extends AMoveVisitor {

  MoveBoxVisitor(ICell box, String direction, ArrayList<ICell> levelGroundCells,
      ArrayList<ICell> levelContentsCells,
      Posn size) {
    super(box, direction, levelGroundCells, levelContentsCells, size);
  }
}

//to represent an ICellVisitor that visits an ICell and moves trophy to the given ICell
//if an only if it should be able to be moved
class MoveTrophyVisitor extends AMoveVisitor {

  MoveTrophyVisitor(ICell trophy, String direction, ArrayList<ICell> levelGroundCells,
      ArrayList<ICell> levelContentsCells,
      Posn size) {
    super(trophy, direction, levelGroundCells, levelContentsCells, size);
  }
}

// to represent an ICellVisitor that visits an ICell and evaluates the cell to an ICell
// representing a new player based on the given player either moved to a new location
// based on the given direction or in the same place
class MovePlayerVisitor extends AMoveVisitor {

  MovePlayerVisitor(ICell player, String direction, ArrayList<ICell> levelGroundCells,
      ArrayList<ICell> levelContentsCells,
      Posn size) {
    super(player, direction, levelGroundCells, levelContentsCells, size);
  }

  // to represent a visitor that visits a box ICell and produces an ICell
  // representing a new player moved in the given direction
  // will also move the box and the player concurrently in the given direction of
  // the player
  // under the condition that there is not a wall or other object next to the box
  // in the direction the player is moving
  public ICell visitBox(Box box) {

    // Find the next content tile in the direction of movement
    ICell nextContents = new Utils().findNext(this.levelContentsCells, this.direction,
        box.accept(new CellPosnVisitor()), this.size);

    // Find the next ground tile in the direction of movement
    ICell nextGround = new Utils().findNext(this.levelGroundCells, this.direction,
        this.cell.accept(new CellPosnVisitor()), this.size);

    // checks that the box has a free space to move, else does not move the
    // player or the box
    if (nextContents.accept(new MoveableObjectCanMoveToVisitor())
        && (new Utils().samePosn(nextGround.findPlayer(), new Posn(-2, -2)))) {
      ICell newTrophy = nextGround.accept(new MoveBoxVisitor(box, this.direction,
          this.levelGroundCells, this.levelContentsCells, this.size));
      ICell newPlayer = this.cell.move(this.direction);
      if (this.levelContentsCells.indexOf(box) == -1) {
        newPlayer = nextGround.accept(new MovePlayerVisitor(this.cell, this.direction,
            this.levelGroundCells, this.levelContentsCells, this.size)).move(this.direction);
        newPlayer = newPlayer.move(new Utils().opposite(this.direction));
      }
      else {
        this.levelContentsCells.set(this.levelContentsCells.indexOf(box),
            new Blank(box.accept(new CellPosnVisitor())));
        this.levelContentsCells.set(this.levelContentsCells.indexOf(nextContents), newTrophy);
      }
      return newPlayer;
    }
    if (nextContents.accept(new MoveableObjectCanMoveToVisitor())) {
      ICell newTrophy = nextContents.accept(new MoveBoxVisitor(box, this.direction,
          this.levelGroundCells, this.levelContentsCells, this.size));
      ICell newPlayer = this.cell.move(this.direction);
      this.levelContentsCells.set(this.levelContentsCells.indexOf(box),
          new Blank(box.accept(new CellPosnVisitor())));
      this.levelContentsCells.set(this.levelContentsCells.indexOf(nextContents), newTrophy);
      return newPlayer;
    }
    else {
      return this.cell;
    }
  }

  // to represent a visitor that visits a trophy ICell and produces an ICell
  // representing a new player moved in the given direction
  // will also move the trophy and the player concurrently in the given direction
  // of the player
  // under the condition that there is not a wall or other object next to the
  // trophy
  // in the direction the player is moving
  public ICell visitTrophy(Trophy trophy) {

    // Find the next content tile in the direction of movement
    ICell nextContents = new Utils().findNext(this.levelContentsCells, this.direction,
        trophy.accept(new CellPosnVisitor()), this.size);

    // Find the next ground tile in the direction of movement
    ICell nextGround = new Utils().findNext(this.levelGroundCells, this.direction,
        this.cell.accept(new CellPosnVisitor()), this.size);

    // checks that the trophy has a free space to move, else does not move the
    // player or the trophy
    if (nextContents.accept(new MoveableObjectCanMoveToVisitor())
        && (new Utils().samePosn(nextGround.findPlayer(), new Posn(-2, -2)))) {
      ICell newTrophy = nextGround.accept(new MoveTrophyVisitor(trophy, this.direction,
          this.levelGroundCells, this.levelContentsCells, this.size));
      ICell newPlayer = this.cell.move(this.direction);
      if (this.levelContentsCells.indexOf(trophy) == -1) {
        newPlayer = nextGround.accept(new MovePlayerVisitor(this.cell, this.direction,
            this.levelGroundCells, this.levelContentsCells, this.size)).move(this.direction);
        newPlayer = newPlayer.move(new Utils().opposite(this.direction));
      }
      else {
        this.levelContentsCells.set(this.levelContentsCells.indexOf(trophy),
            new Blank(trophy.accept(new CellPosnVisitor())));
        this.levelContentsCells.set(this.levelContentsCells.indexOf(nextContents), newTrophy);
      }
      return newPlayer;
    }
    if (nextContents.accept(new MoveableObjectCanMoveToVisitor())) {
      ICell newTrophy = nextContents.accept(new MoveTrophyVisitor(trophy, this.direction,
          this.levelGroundCells, this.levelContentsCells, this.size));
      ICell newPlayer = this.cell.move(this.direction);

      this.levelContentsCells.set(this.levelContentsCells.indexOf(trophy),
          new Blank(trophy.accept(new CellPosnVisitor())));
      this.levelContentsCells.set(this.levelContentsCells.indexOf(nextContents), newTrophy);
      return newPlayer;
    }
    else {
      return this.cell;
    }
  }

  //to represent a visitor that visits an ice ICell and produces a player
  // representing a new player moved on the ice in the given direction
  // will slide the player in that direction until they collide with an immovable
  // item,
  // or until they land on a non-ice tile.
  // If, while colliding, they crash into a movable item, then the movable item
  // starts sliding too
  public ICell visitIce(Ice ice) {

    // Find the next ground tile in the direction of movement
    ICell nextGround = new Utils().findNext(this.levelGroundCells, this.direction,
        this.cell.accept(new CellPosnVisitor()), this.size);

    // Find the next content tile in the direction of movement
    ICell nextContent = new Utils().findNext(this.levelContentsCells, this.direction,
        this.cell.accept(new CellPosnVisitor()), this.size);

    // checks that the player has a free space to move, else does not move the player
    ICell movedCell = this.cell;
    if (nextContent.accept(new PlayerCanMoveToVisitor())
        && (new Utils().samePosn(nextGround.findPlayer(), new Posn(-2, -2)))) {
      movedCell = nextContent.accept(new MovePlayerVisitor(movedCell, this.direction,
          this.levelGroundCells, this.levelContentsCells, this.size));
      movedCell = nextGround
          .accept(new MovePlayerVisitor(movedCell, this.direction, this.levelGroundCells,
              this.levelContentsCells, this.size));
    }
    else if (nextContent.accept(new PlayerCanMoveToVisitor())) {
      movedCell = nextContent
          .accept(new MovePlayerVisitor(movedCell, this.direction, this.levelGroundCells,
              this.levelContentsCells, this.size));
    }
    return movedCell;
  }
}

//to represent an ICellVisitor that visits an ICell
//returns whether or not the given ICell should be able to be moved
//used on boxes and trophies to check if there is another ICell next to them
//that makes them unable to be moved
class MoveableObjectCanMoveToVisitor implements ICellVisitor<Boolean> {
  // returns true because an ICell can move to a blank
  public Boolean visitBlank(Blank blank) {
    return true;
  }

  // returns false because an ICell (more specifically Boxes and Trophies) cannot
  // move to a wall
  public Boolean visitWall(Wall wall) {
    return false;
  }

  // returns false because an ICell (more specifically Boxes and Trophies) cannot
  // move to a Box
  public Boolean visitBox(Box box) {
    return false;
  }

  // returns false because an ICell (more specifically Boxes and Trophies) cannot
  // move to a Player
  public Boolean visitPlayer(Player player) {
    return false;
  }

  // returns true because an ICell can move to a Target
  public Boolean visitTarget(Target target) {
    return true;
  }

  // returns false because an ICell (more specifically boxes and Trophies) cannot
  // move to a Trophy
  public Boolean visitTrophy(Trophy trophy) {
    return false;
  }

  // returns true because an ICell can move to Hole
  // but will then be lost
  public Boolean visitHole(Hole hole) {
    return true;
  }

  // returns true because an ICell can move to a Ice
  public Boolean visitIce(Ice ice) {
    return true;
  }
}

//to represent an ICellVisitor that visits an ICell
//returns whether or not the given ICell (player) should be able to be moved
//used on the player to see if it can move to a spot
class PlayerCanMoveToVisitor implements ICellVisitor<Boolean> {
  //returns true because an ICell can move to a blank
  public Boolean visitBlank(Blank blank) {
    return true;
  }

  //returns false because an ICell (player) cannot
  //move to a wall
  public Boolean visitWall(Wall wall) {
    return false;
  }

  //returns true because an ICell (player) can
  //move to a Box
  public Boolean visitBox(Box box) {
    return true;
  }

  //returns false because an ICell (player) cannot
  //move to a Player
  public Boolean visitPlayer(Player player) {
    return false;
  }

  //returns true because an ICell (player) can move to a Target
  public Boolean visitTarget(Target target) {
    return true;
  }

  //returns true because an ICell (player) can
  //move to a Trophy
  public Boolean visitTrophy(Trophy trophy) {
    return true;
  }

  //returns true because an ICell (player) can move to Hole
  //but will then be lost
  public Boolean visitHole(Hole hole) {
    return true;
  }

  //returns true because an ICell can move to a Ice
  public Boolean visitIce(Ice ice) {
    return true;
  }
}

// to represent an ICellVisitor that visits an ICell and evaluates the cell to a boolean
// representing whether the given cell and the visited cell
// are a good pair (target has the correct color)
class GoodPairVisitor implements ICellVisitor<Boolean> {
  // to represent a cell in the game
  ICell cell;

  GoodPairVisitor(ICell cell) {
    this.cell = cell;
  }

  // to represent a visitor that visits a blank ICell and produces a boolean
  // representing that the given cell and the visited blank cell are a good pair
  public Boolean visitBlank(Blank blank) {
    return true;
  }

  // to represent a visitor that visits a wall ICell and produces a boolean
  // representing that the given cell and the visited wall cell are a good pair
  public Boolean visitWall(Wall wall) {
    return true;
  }

  // to represent a visitor that visits a box ICell and produces a boolean
  // representing that the given cell and the visited box cell are a good pair
  public Boolean visitBox(Box box) {
    return true;
  }

  // to represent a visitor that visits a player ICell and produces a boolean
  // representing that the given cell and the visited player cell are a good pair
  public Boolean visitPlayer(Player player) {
    return true;
  }

  // to represent a visitor that visits a target ICell and produces a boolean
  // representing if this visitor's cell is a match for the target's color
  public Boolean visitTarget(Target target) {
    return this.cell.goodPair(target.color);
  }

  // to represent a visitor that visits a trophy ICell and produces a boolean
  // representing that the given cell and the visited trophy cell are a good pair
  // since only the target's pair is important
  public Boolean visitTrophy(Trophy trophy) {
    return true;
  }

  // to represent a visitor that visits a hole ICell and produces a boolean
  // representing that the given cell and the visited hole cell are a good pair
  public Boolean visitHole(Hole hole) {
    return true;
  }

  // to represent a visitor that visits a ice ICell and produces a boolean
  // representing that the given cell and the visited ice cell are a good pair
  public Boolean visitIce(Ice ice) {
    return true;
  }
}

// tests and examples for SokobanGame
class ExamplesSokobanGame {

  // tests and examples for the SokobanBoard constructor
  boolean testSokobanBoardConstructor_SokobanBoard(Tester t) {
    String givenExLevelGround = "________\n" + "__IR____\n" + "________\n" + "_B____Y_\n"
        + "________\n" + "___G____\n" + "________";

    String givenExLevelContents = "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "W_b>yB_W\n"
        + "WW_gWWWW\n" + "_WW_W___\n" + "__WWW___";

    String givenExLevelContentsTwoPlayers = "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n"
        + "W<b>yB_W\n"
        + "WW_gWWWW\n" + "_WW_W___\n" + "__WWW___";

    SokobanBoard givenExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);

    ArrayList<ICell> givenExLevelGroundList = new ArrayList<ICell>();
    givenExLevelGroundList.add(new Blank(new Posn(1, 1)));
    givenExLevelGroundList.add(new Blank(new Posn(2, 1)));
    givenExLevelGroundList.add(new Blank(new Posn(3, 1)));
    givenExLevelGroundList.add(new Blank(new Posn(4, 1)));
    givenExLevelGroundList.add(new Blank(new Posn(5, 1)));
    givenExLevelGroundList.add(new Blank(new Posn(6, 1)));
    givenExLevelGroundList.add(new Blank(new Posn(7, 1)));
    givenExLevelGroundList.add(new Blank(new Posn(8, 1)));
    givenExLevelGroundList.add(new Blank(new Posn(1, 2)));
    givenExLevelGroundList.add(new Blank(new Posn(2, 2)));
    givenExLevelGroundList.add(new Ice(new Posn(3, 2)));
    givenExLevelGroundList.add(new Target(new Posn(4, 2), Color.red));
    givenExLevelGroundList.add(new Blank(new Posn(5, 2)));
    givenExLevelGroundList.add(new Blank(new Posn(6, 2)));
    givenExLevelGroundList.add(new Blank(new Posn(7, 2)));
    givenExLevelGroundList.add(new Blank(new Posn(8, 2)));
    givenExLevelGroundList.add(new Blank(new Posn(1, 3)));
    givenExLevelGroundList.add(new Blank(new Posn(2, 3)));
    givenExLevelGroundList.add(new Blank(new Posn(3, 3)));
    givenExLevelGroundList.add(new Blank(new Posn(4, 3)));
    givenExLevelGroundList.add(new Blank(new Posn(5, 3)));
    givenExLevelGroundList.add(new Blank(new Posn(6, 3)));
    givenExLevelGroundList.add(new Blank(new Posn(7, 3)));
    givenExLevelGroundList.add(new Blank(new Posn(8, 3)));
    givenExLevelGroundList.add(new Blank(new Posn(1, 4)));
    givenExLevelGroundList.add(new Target(new Posn(2, 4), Color.blue));
    givenExLevelGroundList.add(new Blank(new Posn(3, 4)));
    givenExLevelGroundList.add(new Blank(new Posn(4, 4)));
    givenExLevelGroundList.add(new Blank(new Posn(5, 4)));
    givenExLevelGroundList.add(new Blank(new Posn(6, 4)));
    givenExLevelGroundList.add(new Target(new Posn(7, 4), Color.yellow));
    givenExLevelGroundList.add(new Blank(new Posn(8, 4)));
    givenExLevelGroundList.add(new Blank(new Posn(1, 5)));
    givenExLevelGroundList.add(new Blank(new Posn(2, 5)));
    givenExLevelGroundList.add(new Blank(new Posn(3, 5)));
    givenExLevelGroundList.add(new Blank(new Posn(4, 5)));
    givenExLevelGroundList.add(new Blank(new Posn(5, 5)));
    givenExLevelGroundList.add(new Blank(new Posn(6, 5)));
    givenExLevelGroundList.add(new Blank(new Posn(7, 5)));
    givenExLevelGroundList.add(new Blank(new Posn(8, 5)));
    givenExLevelGroundList.add(new Blank(new Posn(1, 6)));
    givenExLevelGroundList.add(new Blank(new Posn(2, 6)));
    givenExLevelGroundList.add(new Blank(new Posn(3, 6)));
    givenExLevelGroundList.add(new Target(new Posn(4, 6), Color.green));
    givenExLevelGroundList.add(new Blank(new Posn(5, 6)));
    givenExLevelGroundList.add(new Blank(new Posn(6, 6)));
    givenExLevelGroundList.add(new Blank(new Posn(7, 6)));
    givenExLevelGroundList.add(new Blank(new Posn(8, 6)));
    givenExLevelGroundList.add(new Blank(new Posn(1, 7)));
    givenExLevelGroundList.add(new Blank(new Posn(2, 7)));
    givenExLevelGroundList.add(new Blank(new Posn(3, 7)));
    givenExLevelGroundList.add(new Blank(new Posn(4, 7)));
    givenExLevelGroundList.add(new Blank(new Posn(5, 7)));
    givenExLevelGroundList.add(new Blank(new Posn(6, 7)));
    givenExLevelGroundList.add(new Blank(new Posn(7, 7)));
    givenExLevelGroundList.add(new Blank(new Posn(8, 7)));

    ArrayList<ICell> givenExLevelContentsList = new ArrayList<ICell>();
    givenExLevelContentsList.add(new Blank(new Posn(1, 1)));
    givenExLevelContentsList.add(new Blank(new Posn(2, 1)));
    givenExLevelContentsList.add(new Wall(new Posn(3, 1)));
    givenExLevelContentsList.add(new Wall(new Posn(4, 1)));
    givenExLevelContentsList.add(new Wall(new Posn(5, 1)));
    givenExLevelContentsList.add(new Blank(new Posn(6, 1)));
    givenExLevelContentsList.add(new Blank(new Posn(7, 1)));
    givenExLevelContentsList.add(new Blank(new Posn(8, 1)));
    givenExLevelContentsList.add(new Blank(new Posn(1, 2)));
    givenExLevelContentsList.add(new Blank(new Posn(2, 2)));
    givenExLevelContentsList.add(new Wall(new Posn(3, 2)));
    givenExLevelContentsList.add(new Blank(new Posn(4, 2)));
    givenExLevelContentsList.add(new Wall(new Posn(5, 2)));
    givenExLevelContentsList.add(new Wall(new Posn(6, 2)));
    givenExLevelContentsList.add(new Blank(new Posn(7, 2)));
    givenExLevelContentsList.add(new Blank(new Posn(8, 2)));
    givenExLevelContentsList.add(new Wall(new Posn(1, 3)));
    givenExLevelContentsList.add(new Wall(new Posn(2, 3)));
    givenExLevelContentsList.add(new Wall(new Posn(3, 3)));
    givenExLevelContentsList.add(new Trophy(new Posn(4, 3), Color.red));
    givenExLevelContentsList.add(new Blank(new Posn(5, 3)));
    givenExLevelContentsList.add(new Wall(new Posn(6, 3)));
    givenExLevelContentsList.add(new Wall(new Posn(7, 3)));
    givenExLevelContentsList.add(new Wall(new Posn(8, 3)));
    givenExLevelContentsList.add(new Wall(new Posn(1, 4)));
    givenExLevelContentsList.add(new Blank(new Posn(2, 4)));
    givenExLevelContentsList.add(new Trophy(new Posn(3, 4), Color.blue));
    givenExLevelContentsList.add(new Player(new Posn(4, 4)));
    givenExLevelContentsList.add(new Trophy(new Posn(5, 4), Color.yellow));
    givenExLevelContentsList.add(new Box(new Posn(6, 4)));
    givenExLevelContentsList.add(new Blank(new Posn(7, 4)));
    givenExLevelContentsList.add(new Wall(new Posn(8, 4)));
    givenExLevelContentsList.add(new Wall(new Posn(1, 5)));
    givenExLevelContentsList.add(new Wall(new Posn(2, 5)));
    givenExLevelContentsList.add(new Blank(new Posn(3, 5)));
    givenExLevelContentsList.add(new Trophy(new Posn(4, 5), Color.green));
    givenExLevelContentsList.add(new Wall(new Posn(5, 5)));
    givenExLevelContentsList.add(new Wall(new Posn(6, 5)));
    givenExLevelContentsList.add(new Wall(new Posn(7, 5)));
    givenExLevelContentsList.add(new Wall(new Posn(8, 5)));
    givenExLevelContentsList.add(new Blank(new Posn(1, 6)));
    givenExLevelContentsList.add(new Wall(new Posn(2, 6)));
    givenExLevelContentsList.add(new Wall(new Posn(3, 6)));
    givenExLevelContentsList.add(new Blank(new Posn(4, 6)));
    givenExLevelContentsList.add(new Wall(new Posn(5, 6)));
    givenExLevelContentsList.add(new Blank(new Posn(6, 6)));
    givenExLevelContentsList.add(new Blank(new Posn(7, 6)));
    givenExLevelContentsList.add(new Blank(new Posn(8, 6)));
    givenExLevelContentsList.add(new Blank(new Posn(1, 7)));
    givenExLevelContentsList.add(new Blank(new Posn(2, 7)));
    givenExLevelContentsList.add(new Wall(new Posn(3, 7)));
    givenExLevelContentsList.add(new Wall(new Posn(4, 7)));
    givenExLevelContentsList.add(new Wall(new Posn(5, 7)));
    givenExLevelContentsList.add(new Blank(new Posn(6, 7)));
    givenExLevelContentsList.add(new Blank(new Posn(7, 7)));
    givenExLevelContentsList.add(new Blank(new Posn(8, 7)));

    return t.checkConstructorNoException("testSokobanBoardConstructor", "SokobanBoard",
        givenExLevelGround, givenExLevelContents)
        && t.checkExpect(givenExB,
            new SokobanBoard(new Posn(8, 7), givenExLevelGroundList, givenExLevelContentsList))
        && t.checkConstructorException(
            new IllegalArgumentException(
                "Dimensions of given level ground do not match dimensions of given level contents"),
            "SokobanBoard", givenExLevelGround, "")
        && t.checkConstructorException(
            new IllegalArgumentException(
                "The board should contain exactly one player."),
            "SokobanBoard", givenExLevelGround, givenExLevelContentsTwoPlayers);
  }

  // tests and examples for historic in SokobanBoard
  boolean testHistoric(Tester t) {
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

    SokobanBoard newShortExBoard = new SokobanBoard(new Posn(9, 1), shortExLevelGround,
        shortExLevelContents0);

    return t.checkExpect(shortExBoard.historic(), newShortExBoard);
  }

  // tests and examples for render in SokobanBoard
  boolean testRender_SokobanBoard(Tester t) {

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

    return t.checkExpect(shortExBoard.render(0), result);
  }

  // tests and examples for playerMove in SokobanBoard
  boolean testPlayerMove_SokobanBoard(Tester t) {

    ArrayList<ICell> shortExLevelGround = new ArrayList<ICell>();
    shortExLevelGround.add(new Blank(new Posn(1, 1)));
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

    // player moves down
    ArrayList<ICell> shortExLevelContents1 = new ArrayList<ICell>();
    shortExLevelContents1.add(new Blank(new Posn(2, 1)));
    shortExLevelContents1.add(new Blank(new Posn(1, 2)));
    shortExLevelContents1.add(new Blank(new Posn(2, 2)));
    shortExLevelContents1.add(new Player(new Posn(1, 2)));
    shortExLevelContents1.add(new Blank(new Posn(1, 1)));

    SokobanBoard shortExB1 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents1);

    // player moves right
    ArrayList<ICell> shortExLevelContents2 = new ArrayList<ICell>();
    shortExLevelContents2.add(new Blank(new Posn(2, 1)));
    shortExLevelContents2.add(new Blank(new Posn(1, 2)));
    shortExLevelContents2.add(new Blank(new Posn(2, 2)));
    shortExLevelContents2.add(new Blank(new Posn(1, 1)));
    shortExLevelContents2.add(new Player(new Posn(2, 2)));

    SokobanBoard shortExB2 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents2);

    // player moves up
    ArrayList<ICell> shortExLevelContents3 = new ArrayList<ICell>();
    shortExLevelContents3.add(new Blank(new Posn(2, 1)));
    shortExLevelContents3.add(new Blank(new Posn(1, 2)));
    shortExLevelContents3.add(new Blank(new Posn(2, 2)));
    shortExLevelContents3.add(new Blank(new Posn(1, 1)));
    shortExLevelContents3.add(new Player(new Posn(2, 1)));

    SokobanBoard shortExB3 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents3);

    // player moves left
    ArrayList<ICell> shortExLevelContents4 = new ArrayList<ICell>();
    shortExLevelContents4.add(new Blank(new Posn(2, 1)));
    shortExLevelContents4.add(new Blank(new Posn(1, 2)));
    shortExLevelContents4.add(new Blank(new Posn(2, 2)));
    shortExLevelContents4.add(new Blank(new Posn(1, 1)));
    shortExLevelContents4.add(new Player(new Posn(1, 1)));

    SokobanBoard shortExB4 = new SokobanBoard(new Posn(2, 2), shortExLevelGround,
        shortExLevelContents4);

    return t.checkExpect(shortExB0.playerMove("down"), shortExB1)
        && t.checkExpect(shortExB1.playerMove("right"), shortExB2)
        && t.checkExpect(shortExB2.playerMove("up"), shortExB3)
        && t.checkExpect(shortExB3.playerMove("left"), shortExB4);
  }

  // tests and examples for levelWon in SokobanBoard
  boolean testLevelWon_SokobanBoard(Tester t) {

    String wonInexactExLevelGround = "___R____\n" + "_B____Y_\n" + "___G____";

    String wonInexactExLevelContents = "_>_r__g_\n" + "_b_b__y_\n" + "r__g_y__";

    SokobanBoard wonInexact = new SokobanBoard(wonInexactExLevelGround,
        wonInexactExLevelContents);

    String givenExLevelGround = "________\n" + "___R____\n" + "________\n" + "_B____Y_\n"
        + "________\n" + "___G____\n" + "________";

    String givenExLevelContents = "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "W_b>yB_W\n"
        + "WW_gWWWW\n" + "_WW_W___\n" + "__WWW___";

    SokobanBoard givenExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);

    return t.checkExpect(wonInexact.levelWon(), true)
        && t.checkExpect(givenExB.levelWon(), false);
  }

  // tests and examples for drawICell for ICell
  boolean testDrawICell_ICell(Tester t) {
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

    Posn ex = new Posn(0, 0);
    ICell blank = new Blank(ex);
    ICell yTarget = new Target(ex, Color.yellow);
    ICell gTarget = new Target(ex, Color.green);
    ICell bTarget = new Target(ex, Color.blue);
    ICell rTarget = new Target(ex, Color.red);
    ICell yTrophy = new Trophy(ex, Color.yellow);
    ICell gTrophy = new Trophy(ex, Color.green);
    ICell bTrophy = new Trophy(ex, Color.blue);
    ICell rTrophy = new Trophy(ex, Color.red);
    ICell wall = new Wall(ex);
    ICell box = new Box(ex);
    ICell player = new Player(ex);
    ICell hole = new Hole(ex);
    ICell ice = new Ice(ex);

    return t.checkExpect(blank.drawICell(), BLANK)
        && t.checkExpect(rTarget.drawICell(), RTARGET)
        && t.checkExpect(yTarget.drawICell(), YTARGET)
        && t.checkExpect(bTarget.drawICell(), BTARGET)
        && t.checkExpect(gTarget.drawICell(), GTARGET)
        && t.checkExpect(player.drawICell(), PLAYER)
        && t.checkExpect(wall.drawICell(), WALL)
        && t.checkExpect(box.drawICell(), BOX)
        && t.checkExpect(rTrophy.drawICell(), RTROPHY)
        && t.checkExpect(yTrophy.drawICell(), YTROPHY)
        && t.checkExpect(bTrophy.drawICell(), BTROPHY)
        && t.checkExpect(gTrophy.drawICell(), GTROPHY)
        && t.checkExpect(hole.drawICell(), HOLE)
        && t.checkExpect(ice.drawICell(), ICE);
  }

  // tests and examples for accept for ICell
  boolean testAccept_ICell(Tester t) {
    Posn ex = new Posn(1, 1);
    ICell blank = new Blank(ex);
    ICell yTarget = new Target(ex, Color.yellow);
    ICell gTarget = new Target(ex, Color.green);
    ICell bTarget = new Target(ex, Color.blue);
    ICell rTarget = new Target(ex, Color.red);
    ICell yTrophy = new Trophy(ex, Color.yellow);
    ICell gTrophy = new Trophy(ex, Color.green);
    ICell bTrophy = new Trophy(ex, Color.blue);
    ICell rTrophy = new Trophy(ex, Color.red);
    ICell wall = new Wall(ex);
    ICell box = new Box(ex);
    ICell player = new Player(new Posn(0, 0));
    ICell hole = new Hole(ex);
    ICell ice = new Ice(ex);

    return t.checkExpect(blank.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(yTarget.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(gTarget.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(bTarget.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(rTarget.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(yTrophy.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(gTrophy.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(bTrophy.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(rTrophy.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(wall.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(box.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(player.accept(new CellPosnVisitor()), new Posn(0, 0))
        && t.checkExpect(hole.accept(new CellPosnVisitor()), ex)
        && t.checkExpect(ice.accept(new CellPosnVisitor()), ex);
  }

  // tests and examples for findPlayer for ICell
  boolean testFindPlayer_ICell(Tester t) {
    Posn ex = new Posn(0, 0);
    Posn bad = new Posn(-1, -1);
    ICell blank = new Blank(ex);
    ICell yTarget = new Target(ex, Color.yellow);
    ICell gTarget = new Target(ex, Color.green);
    ICell bTarget = new Target(ex, Color.blue);
    ICell rTarget = new Target(ex, Color.red);
    ICell yTrophy = new Trophy(ex, Color.yellow);
    ICell gTrophy = new Trophy(ex, Color.green);
    ICell bTrophy = new Trophy(ex, Color.blue);
    ICell rTrophy = new Trophy(ex, Color.red);
    ICell wall = new Wall(ex);
    ICell box = new Box(ex);
    ICell player = new Player(new Posn(1, 2));
    ICell hole = new Hole(ex);
    ICell ice = new Ice(ex);

    return t.checkExpect(blank.findPlayer(), bad) && t.checkExpect(yTarget.findPlayer(), bad)
        && t.checkExpect(gTarget.findPlayer(), bad) && t.checkExpect(bTarget.findPlayer(), bad)
        && t.checkExpect(rTarget.findPlayer(), bad) && t.checkExpect(yTrophy.findPlayer(), bad)
        && t.checkExpect(gTrophy.findPlayer(), bad) && t.checkExpect(bTrophy.findPlayer(), bad)
        && t.checkExpect(rTrophy.findPlayer(), bad) && t.checkExpect(wall.findPlayer(), bad)
        && t.checkExpect(box.findPlayer(), bad)
        && t.checkExpect(player.findPlayer(), new Posn(1, 2))
        && t.checkExpect(hole.findPlayer(), bad)
        && t.checkExpect(ice.findPlayer(), new Posn(-2, -2));
  }

  // tests and examples for move for ICell
  boolean testMove_ICell(Tester t) {
    Posn ex = new Posn(1, 1);
    ICell blank = new Blank(ex);
    ICell yTarget = new Target(ex, Color.yellow);
    ICell gTarget = new Target(ex, Color.green);
    ICell bTarget = new Target(ex, Color.blue);
    ICell rTarget = new Target(ex, Color.red);
    ICell yTrophy = new Trophy(ex, Color.yellow);
    ICell gTrophy = new Trophy(ex, Color.green);
    ICell bTrophy = new Trophy(ex, Color.blue);
    ICell rTrophy = new Trophy(ex, Color.red);
    ICell wall = new Wall(ex);
    ICell box = new Box(ex);
    ICell player = new Player(ex);
    ICell hole = new Hole(ex);
    ICell ice = new Ice(ex);

    return t.checkExpect(blank.move("right"), blank)
        && t.checkExpect(yTarget.move("left"), yTarget)
        && t.checkExpect(gTarget.move("right"), gTarget)
        && t.checkExpect(bTarget.move("left"), bTarget)
        && t.checkExpect(rTarget.move("up"), rTarget) && t.checkExpect(wall.move("down"), wall)
        && t.checkExpect(hole.move("up"), hole)
        && t.checkExpect(ice.move("up"), ice)
        && t.checkExpect(yTrophy.move("right"), new Trophy(new Posn(2, 1), Color.yellow))
        && t.checkExpect(gTrophy.move("left"), new Trophy(new Posn(0, 1), Color.green))
        && t.checkExpect(bTrophy.move("down"), new Trophy(new Posn(1, 2), Color.blue))
        && t.checkExpect(rTrophy.move("up"), new Trophy(new Posn(1, 0), Color.red))
        && t.checkExpect(box.move("right"), new Box(new Posn(2, 1)))
        && t.checkExpect(box.move("left"), new Box(new Posn(0, 1)))
        && t.checkExpect(box.move("down"), new Box(new Posn(1, 2)))
        && t.checkExpect(box.move("up"), new Box(new Posn(1, 0)))
        && t.checkExpect(player.move("right"), new Player(new Posn(2, 1)))
        && t.checkExpect(player.move("left"), new Player(new Posn(0, 1)))
        && t.checkExpect(player.move("down"), new Player(new Posn(1, 2)))
        && t.checkExpect(player.move("up"), new Player(new Posn(1, 0)));
  }

  // tests and examples for goodPair for ICell
  boolean testGoodPair_ICell(Tester t) {
    Posn ex = new Posn(1, 1);
    ICell blank = new Blank(ex);
    ICell yTarget = new Target(ex, Color.yellow);
    ICell gTarget = new Target(ex, Color.green);
    ICell bTarget = new Target(ex, Color.blue);
    ICell rTarget = new Target(ex, Color.red);
    ICell yTrophy = new Trophy(ex, Color.yellow);
    ICell gTrophy = new Trophy(ex, Color.green);
    ICell bTrophy = new Trophy(ex, Color.blue);
    ICell rTrophy = new Trophy(ex, Color.red);
    ICell wall = new Wall(ex);
    ICell box = new Box(ex);
    ICell player = new Player(ex);
    ICell hole = new Hole(ex);
    ICell ice = new Ice(ex);

    return t.checkExpect(blank.goodPair(Color.yellow), false)
        && t.checkExpect(yTarget.goodPair(Color.green), false)
        && t.checkExpect(gTarget.goodPair(Color.blue), false)
        && t.checkExpect(bTarget.goodPair(Color.red), false)
        && t.checkExpect(rTarget.goodPair(Color.yellow), false)
        && t.checkExpect(wall.goodPair(Color.green), false)
        && t.checkExpect(box.goodPair(Color.blue), false)
        && t.checkExpect(ice.goodPair(Color.blue), false)
        && t.checkExpect(player.goodPair(Color.red), false)
        && t.checkExpect(hole.goodPair(Color.yellow), false)
        && t.checkExpect(yTrophy.goodPair(Color.yellow), true)
        && t.checkExpect(gTrophy.goodPair(Color.red), false)
        && t.checkExpect(bTrophy.goodPair(Color.blue), true)
        && t.checkExpect(rTrophy.goodPair(Color.yellow), false)
        && t.checkExpect(rTrophy.goodPair(Color.green), false)
        && t.checkExpect(rTrophy.goodPair(Color.red), true)
        && t.checkExpect(rTrophy.goodPair(Color.blue), false);
  }

  // tests and examples for CellPosnVisitor
  boolean test_CellPosnVisitor(Tester t) {
    Posn ex = new Posn(1, 1);

    Blank blankO = new Blank(ex);
    Target yTargetO = new Target(ex, Color.yellow);
    Target gTargetO = new Target(ex, Color.green);
    Target bTargetO = new Target(ex, Color.blue);
    Target rTargetO = new Target(ex, Color.red);
    Trophy yTrophyO = new Trophy(ex, Color.yellow);
    Trophy gTrophyO = new Trophy(ex, Color.green);
    Trophy bTrophyO = new Trophy(ex, Color.blue);
    Trophy rTrophyO = new Trophy(ex, Color.red);
    Wall wallO = new Wall(ex);
    Box boxO = new Box(ex);
    Player playerO = new Player(ex);
    Hole holeO = new Hole(ex);
    Ice iceO = new Ice(ex);

    CellPosnVisitor visitor = new CellPosnVisitor();

    return t.checkExpect(visitor.visitBlank(blankO), ex)
        && t.checkExpect(visitor.visitTarget(yTargetO), ex)
        && t.checkExpect(visitor.visitTarget(gTargetO), ex)
        && t.checkExpect(visitor.visitTarget(bTargetO), ex)
        && t.checkExpect(visitor.visitTarget(rTargetO), ex)
        && t.checkExpect(visitor.visitTrophy(yTrophyO), ex)
        && t.checkExpect(visitor.visitTrophy(gTrophyO), ex)
        && t.checkExpect(visitor.visitTrophy(bTrophyO), ex)
        && t.checkExpect(visitor.visitTrophy(rTrophyO), ex)
        && t.checkExpect(visitor.visitWall(wallO), ex)
        && t.checkExpect(visitor.visitBox(boxO), ex)
        && t.checkExpect(visitor.visitPlayer(playerO), ex)
        && t.checkExpect(visitor.visitHole(holeO), ex)
        && t.checkExpect(visitor.visitIce(iceO), ex);
  }

  // tests and examples for GoodPairVisitor
  boolean test_GoodPairVisitor(Tester t) {
    Posn ex = new Posn(1, 1);

    Blank blankO = new Blank(ex);
    Target yTargetO = new Target(ex, Color.yellow);
    Target gTargetO = new Target(ex, Color.green);
    Target bTargetO = new Target(ex, Color.blue);
    Target rTargetO = new Target(ex, Color.red);
    Trophy yTrophyO = new Trophy(ex, Color.yellow);
    Trophy gTrophyO = new Trophy(ex, Color.green);
    Trophy bTrophyO = new Trophy(ex, Color.blue);
    Trophy rTrophyO = new Trophy(ex, Color.red);
    Wall wallO = new Wall(ex);
    Box boxO = new Box(ex);
    Player playerO = new Player(ex);
    Hole holeO = new Hole(ex);
    Ice iceO = new Ice(ex);

    GoodPairVisitor visitorTrophy = new GoodPairVisitor(yTrophyO);
    GoodPairVisitor visitorWall = new GoodPairVisitor(wallO);

    return t.checkExpect(visitorTrophy.visitBlank(blankO), true)
        && t.checkExpect(visitorTrophy.visitTarget(yTargetO), true)
        && t.checkExpect(visitorTrophy.visitTarget(gTargetO), false)
        && t.checkExpect(visitorTrophy.visitTarget(bTargetO), false)
        && t.checkExpect(visitorTrophy.visitTarget(rTargetO), false)
        && t.checkExpect(visitorTrophy.visitTrophy(yTrophyO), true)
        && t.checkExpect(visitorTrophy.visitTrophy(gTrophyO), true)
        && t.checkExpect(visitorTrophy.visitTrophy(bTrophyO), true)
        && t.checkExpect(visitorTrophy.visitTrophy(rTrophyO), true)
        && t.checkExpect(visitorTrophy.visitWall(wallO), true)
        && t.checkExpect(visitorTrophy.visitBox(boxO), true)
        && t.checkExpect(visitorTrophy.visitPlayer(playerO), true)
        && (t.checkExpect(visitorTrophy.visitHole(holeO), true)
            & t.checkExpect(visitorTrophy.visitIce(iceO), true))
        && t.checkExpect(visitorWall.visitBlank(blankO), true)
        && t.checkExpect(visitorWall.visitTarget(yTargetO), false)
        && t.checkExpect(visitorWall.visitTarget(gTargetO), false)
        && t.checkExpect(visitorWall.visitTarget(bTargetO), false)
        && t.checkExpect(visitorWall.visitTarget(rTargetO), false)
        && t.checkExpect(visitorWall.visitTrophy(yTrophyO), true)
        && t.checkExpect(visitorWall.visitTrophy(gTrophyO), true)
        && t.checkExpect(visitorWall.visitTrophy(bTrophyO), true)
        && t.checkExpect(visitorWall.visitTrophy(rTrophyO), true)
        && t.checkExpect(visitorWall.visitWall(wallO), true)
        && t.checkExpect(visitorWall.visitBox(boxO), true)
        && t.checkExpect(visitorWall.visitPlayer(playerO), true)
        && t.checkExpect(visitorWall.visitIce(iceO), true)
        && t.checkExpect(visitorWall.visitHole(holeO), true);
  }

  // tests on shouldEnd()
  boolean testShouldEnd(Tester t) {

    String emptyGround = "__";
    String emptyContents = "_>";
    SokobanBoard empty = new SokobanBoard(emptyGround, emptyContents);

    String wonExactExLevelGround = "___R____\n" + "_B____Y_\n" + "___G____";
    String wonExactExLevelContents = "___r____\n" + "_b____y_\n" + "_>_g____";
    SokobanBoard wonExact = new SokobanBoard(wonExactExLevelGround, wonExactExLevelContents);

    String wonInexactExLevelGround = "___R____\n" + "_B____Y_\n" + "___G____";
    String wonInexactExLevelContents = "_<_r__g_\n" + "_b_b__y_\n" + "r__g_y__";
    SokobanBoard wonInexact = new SokobanBoard(wonInexactExLevelGround,
        wonInexactExLevelContents);

    String givenExLevelGround = "________\n" + "___R____\n" + "________\n" + "_B____Y_\n"
        + "________\n" + "___G____\n" + "________";
    String givenExLevelContents = "__WWW___\n" + "__W_WW__\n" + "WWWr_WWW\n" + "W_b>yB_W\n"
        + "WW_gWWWW\n" + "_WW_W___\n" + "__WWW___";
    SokobanBoard givenExB = new SokobanBoard(givenExLevelGround, givenExLevelContents);

    return t.checkExpect(empty.shouldEnd(), true) && t.checkExpect(wonExact.shouldEnd(), true)
        && t.checkExpect(wonInexact.shouldEnd(), true)
        && t.checkExpect(givenExB.shouldEnd(), false);
  }

  // tests the visitBlank method of the MovePlayer class
  boolean testVisitBlankMovePlayerLeft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(0, 1)));
    ICell player = new Player(new Posn(1, 1));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));

    ICell newPlayer = visitor.visitBlank(new Blank(new Posn(0, 0)));

    return t.checkExpect(newPlayer, new Player(new Posn(0, 1)));
  }

  // tests the visitWall method of the MovePlayer class
  boolean testVisitWallMovePlayerLeft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Wall(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(0, 1)));
    ICell player = new Player(new Posn(1, 1));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));

    ICell newPlayer = visitor.visitWall(new Wall(new Posn(0, 0)));

    return t.checkExpect(newPlayer, new Player(new Posn(1, 1)));
  }

  // Tests the visitPlayer method of the MovePlayer class
  boolean testVisitPlayerMovePlayerLeft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(0, 1)));
    ICell player = new Player(new Posn(1, 1));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));

    ICell newPlayer = visitor.visitPlayer(new Player(new Posn(1, 1)));

    return t.checkExpect(newPlayer, new Player(new Posn(1, 1)));
  }

  // tests the visitTarget method of the MovePlayer class
  boolean testVisitTargetMovePlayerLeft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(0, 1), new Color(1)));
    ICell player = new Player(new Posn(1, 1));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));

    ICell newPlayer = visitor.visitTarget(new Target(new Posn(0, 1), new Color(1)));

    return t.checkExpect(newPlayer, new Player(new Posn(0, 1)));
  }

  // tests the visitBlank method of the MovePlayer class
  boolean testVisitBlankMovePlayerRight(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(0, 1)));
    ICell player = new Player(new Posn(0, 0));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitBlank(new Blank(new Posn(0, 1)));

    return t.checkExpect(newPlayer, new Player(new Posn(1, 0)));
  }

  // tests the visitWall method of the MovePlayer class
  boolean testVisitWallMovePlayerRight(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Wall(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(0, 1)));
    ICell player = new Player(new Posn(0, 0));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitWall(new Wall(new Posn(0, 1)));

    return t.checkExpect(newPlayer, new Player(new Posn(0, 0)));
  }

  // Tests the visitPlayer method of the MovePlayer class
  boolean testVisitPlayerMovePlayerRight(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(1, 0)));
    ICell player = new Player(new Posn(0, 0));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(1, 0)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 1));
    ICell newPlayer = visitor.visitPlayer(new Player(new Posn(0, 1)));

    return t.checkExpect(newPlayer, new Player(new Posn(0, 0)));
  }

  // tests the visitTarget method of the MovePlayer class
  boolean testVisitTargetMovePlayerRight(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(0, 1), new Color(1)));
    ICell player = new Player(new Posn(0, 0));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitTarget(new Target(new Posn(0, 1), new Color(1)));

    return t.checkExpect(newPlayer, new Player(new Posn(1, 0)));
  }

  // tests the visitBlank method of the MovePlayer class
  boolean testVisitBlankMovePlayerDown(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(1, 0)));
    ICell player = new Player(new Posn(0, 0));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitBlank(new Blank(new Posn(1, 0)));

    return t.checkExpect(newPlayer, new Player(new Posn(0, 1)));
  }

  // tests the visitWall method of the MovePlayer class
  boolean testVisitWallMovePlayerDown(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Wall(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(1, 0)));
    ICell player = new Player(new Posn(0, 0));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitWall(new Wall(new Posn(1, 0)));

    return t.checkExpect(newPlayer, new Player(new Posn(0, 0)));
  }

  // Tests the visitPlayer method of the MovePlayer class
  boolean testVisitPlayerMovePlayerDown(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(1, 0)));
    levelContentsCells.add(new Blank(new Posn(0, 1)));
    levelContentsCells.add(new Blank(new Posn(1, 1)));
    ICell player = new Player(new Posn(1, 1));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitPlayer(new Player(new Posn(1, 1)));

    return t.checkExpect(newPlayer, new Player(new Posn(1, 1)));
  }

  // tests the visitTarget method of the MovePlayer class
  boolean testVisitTargetMovePlayerDown(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ICell player = new Player(new Posn(0, 0));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitTarget(new Target(new Posn(1, 0), new Color(1)));

    return t.checkExpect(newPlayer, new Player(new Posn(0, 1)));
  }

  // tests the visitBlank method of the MovePlayer class
  boolean testVisitBlankMovePlayerUp(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(1, 0)));
    ICell player = new Player(new Posn(1, 1));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitBlank(new Blank(new Posn(1, 0)));

    return t.checkExpect(newPlayer, new Player(new Posn(1, 0)));
  }

  // tests the visitWall method of the MovePlayer class
  boolean testVisitWallMovePlayerUp(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Wall(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(1, 0)));
    ICell player = new Player(new Posn(1, 1));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitWall(new Wall(new Posn(1, 0)));

    return t.checkExpect(newPlayer, new Player(new Posn(1, 1)));
  }

  // Tests the visitPlayer method of the MovePlayer class
  boolean testVisitPlayerMovePlayerUp(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Blank(new Posn(1, 0)));
    levelContentsCells.add(new Blank(new Posn(0, 1)));
    levelContentsCells.add(new Blank(new Posn(1, 1)));
    ICell player = new Player(new Posn(1, 1));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitPlayer(new Player(new Posn(1, 0)));

    return t.checkExpect(newPlayer, new Player(new Posn(1, 1)));
  }

  // tests the visitTarget method of the MovePlayer class
  boolean testVisitTargetMovePlayerUp(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ICell player = new Player(new Posn(1, 1));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MovePlayerVisitor visitor = new MovePlayerVisitor(player, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    ICell newPlayer = visitor.visitTarget(new Target(new Posn(1, 0), new Color(1)));

    return t.checkExpect(newPlayer, new Player(new Posn(1, 0)));
  }

  // Tests the visitBlank method of the MoveBoxVisitor class
  boolean testVisitBlankMoveBoxVisitorleft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "left", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Blank blank = new Blank(new Posn(1, 0));

    ICell newBox = visitor.visitBlank(blank);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(0, 1));
  }

  // Tests the visitWall method of the MoveBoxVisitor class
  boolean testVisitWallMoveBoxVisitorleft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "left", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Wall wall = new Wall(new Posn(1, 0));

    ICell newBox = visitor.visitWall(wall);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitBox method of the MoveBoxVisitor class
  boolean testVisitBoxMoveBoxVisitorleft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "left", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Box otherBox = new Box(new Posn(1, 0));

    ICell newBox = visitor.visitBox(otherBox);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitPlayer method of the MoveBoxVisitor class
  boolean testVisitPlayerMoveBoxVisitorleft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "left", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Player player = new Player(new Posn(1, 0));

    ICell newBox = visitor.visitPlayer(player);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitTarget method of the MoveBoxVisitor class
  boolean testVisitTargetMoveBoxVisitorleft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "left", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Target target = new Target(new Posn(1, 0), new Color(1));

    ICell newBox = visitor.visitTarget(target);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(0, 1));
  }

  // Tests the visitTrophy method of the MoveBoxVisitor class
  boolean testVisitTrophyMoveBoxVisitorleft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "left", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Trophy trophy = new Trophy(new Posn(1, 0), new Color(1));

    ICell newBox = visitor.visitTrophy(trophy);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitHole method of the MoveBoxVisitor class
  boolean testVisitHoleMoveBoxVisitorleft(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "left", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Hole hole = new Hole(new Posn(1, 0));

    ICell newCell = visitor.visitHole(hole);

    return t.checkExpect(newCell, new Blank(new Posn(1, 0)));
  }

  // Tests the visitBlank method of the MoveBoxVisitor class for up motion
  boolean testVisitBlankMoveBoxVisitorUp(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "up", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Blank blank = new Blank(new Posn(0, 1));

    ICell newBox = visitor.visitBlank(blank);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(1, 0));
  }

  // Tests the visitWall method of the MoveBoxVisitor class for up motion
  boolean testVisitWallMoveBoxVisitorUp(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "up", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Wall wall = new Wall(new Posn(0, 1));

    ICell newBox = visitor.visitWall(wall);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitBox method of the MoveBoxVisitor class for up motion
  boolean testVisitBoxMoveBoxVisitorUp(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "up", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Box otherBox = new Box(new Posn(0, 1));

    ICell newBox = visitor.visitBox(otherBox);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitPlayer method of the MoveBoxVisitor class for up motion
  boolean testVisitPlayerMoveBoxVisitorUp(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "up", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Player player = new Player(new Posn(0, 1));

    ICell newBox = visitor.visitPlayer(player);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitTarget method of the MoveBoxVisitor class for up motion
  boolean testVisitTargetMoveBoxVisitorUp(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "up", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Target target = new Target(new Posn(0, 1), new Color(1));

    ICell newBox = visitor.visitTarget(target);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(1, 0));
  }

  // Tests the visitTrophy method of the MoveBoxVisitor class for up motion
  boolean testVisitTrophyMoveBoxVisitorUp(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "up", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Trophy trophy = new Trophy(new Posn(0, 1), new Color(1));

    ICell newBox = visitor.visitTrophy(trophy);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitHole method of the MoveBoxVisitor class for up motion
  boolean testVisitHoleMoveBoxVisitorUp(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "up", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Hole hole = new Hole(new Posn(0, 1));

    ICell newCell = visitor.visitHole(hole);

    return t.checkExpect(newCell, new Blank(new Posn(0, 1)));
  }

  // Tests the visitBlank method of the MoveBoxVisitor class
  boolean testVisitBlankMoveBoxVisitorDown(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "down", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Blank blank = new Blank(new Posn(2, 1));

    ICell newBox = visitor.visitBlank(blank);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(1, 2));
  }

  // Tests the visitWall method of the MoveBoxVisitor class
  boolean testVisitWallMoveBoxVisitorDown(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "down", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Wall wall = new Wall(new Posn(2, 1));

    ICell newBox = visitor.visitWall(wall);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitBox method of the MoveBoxVisitor class
  boolean testVisitBoxMoveBoxVisitorDown(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "down", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Box otherBox = new Box(new Posn(2, 1));

    ICell newBox = visitor.visitBox(otherBox);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitPlayer method of the MoveBoxVisitor class
  boolean testVisitPlayerMoveBoxVisitorDown(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "down", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Player player = new Player(new Posn(2, 1));

    ICell newBox = visitor.visitPlayer(player);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitTarget method of the MoveBoxVisitor class
  boolean testVisitTargetMoveBoxVisitorDown(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "down", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Target target = new Target(new Posn(2, 1), new Color(1));

    ICell newBox = visitor.visitTarget(target);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(1, 2));
  }

  // Tests the visitTrophy method of the MoveBoxVisitor class
  boolean testVisitTrophyMoveBoxVisitorDown(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "down", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Trophy trophy = new Trophy(new Posn(2, 1), new Color(1));

    ICell newBox = visitor.visitTrophy(trophy);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitHole method of the MoveBoxVisitor class
  boolean testVisitHoleMoveBoxVisitorDown(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Box box = new Box(new Posn(1, 1));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "down", levelGroundCells, levelContentsCells,
        new Posn(2, 2));
    Hole hole = new Hole(new Posn(2, 1));

    ICell newCell = visitor.visitHole(hole);

    return t.checkExpect(newCell, new Blank(new Posn(2, 1)));
  }

  // Tests the visitBlank method of the MoveBoxVisitor class
  boolean testVisitBlankMoveBoxVisitorRight(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Blank blank = new Blank(new Posn(1, 2));

    ICell newBox = visitor.visitBlank(blank);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(2, 1));
  }

  // Tests the visitWall method of the MoveBoxVisitor class
  boolean testVisitWallMoveBoxVisitorRight(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Wall wall = new Wall(new Posn(1, 2));

    ICell newBox = visitor.visitWall(wall);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitBox method of the MoveBoxVisitor class
  boolean testVisitBoxMoveBoxVisitorRight(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Box otherBox = new Box(new Posn(1, 2));

    ICell newBox = visitor.visitBox(otherBox);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitPlayer method of the MoveBoxVisitor class
  boolean testVisitPlayerMoveBoxVisitorRight(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Player player = new Player(new Posn(1, 2));

    ICell newBox = visitor.visitPlayer(player);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitTarget method of the MoveBoxVisitor class
  boolean testVisitTargetMoveBoxVisitorRight(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Target target = new Target(new Posn(1, 2), new Color(1));

    ICell newBox = visitor.visitTarget(target);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(2, 1));
  }

  // Tests the visitTrophy method of the MoveBoxVisitor class
  boolean testVisitTrophyMoveBoxVisitorRight(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Trophy trophy = new Trophy(new Posn(1, 2), new Color(1));

    ICell newBox = visitor.visitTrophy(trophy);

    return t.checkExpect(newBox, box);
  }

  // Tests the visitHole method of the MoveBoxVisitor class
  boolean testVisitHoleMoveBoxVisitorRight(Tester t) {
    Box box = new Box(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveBoxVisitor visitor = new MoveBoxVisitor(box, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Hole hole = new Hole(new Posn(1, 2));

    ICell newCell = visitor.visitHole(hole);

    return t.checkExpect(newCell, new Blank(new Posn(1, 2)));
  }

  // Tests the visitBlank method of the MoveTrophyVisitor class
  boolean testVisitBlankMoveTrophyVisitorleft(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Blank blank = new Blank(new Posn(1, 0));

    ICell newTrophy = visitor.visitBlank(blank);

    return t.checkExpect(newTrophy.accept(new CellPosnVisitor()), new Posn(0, 1));
  }

  // Tests the visitWall method of the MoveTrophyVisitor class
  boolean testVisitWallMoveTrophyVisitorleft(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Wall wall = new Wall(new Posn(1, 0));

    ICell newTrophy = visitor.visitWall(wall);

    return t.checkExpect(newTrophy, trophy);
  }

  // Tests the visitBox method of the MoveTrophyVisitor class
  boolean testVisitBoxMoveTrophyVisitorleft(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Box box = new Box(new Posn(1, 0));

    ICell newTrophy = visitor.visitBox(box);

    return t.checkExpect(newTrophy, trophy);
  }

  // Tests the visitPlayer method of the MoveTrophyVisitor class
  boolean testVisitPlayerMoveTrophyVisitorleft(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Player player = new Player(new Posn(1, 0));

    ICell newTrophy = visitor.visitPlayer(player);

    return t.checkExpect(newTrophy, trophy);
  }

  // Tests the visitTarget method of the MoveTrophyVisitor class
  boolean testVisitTargetMoveTrophyVisitorleft(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Target target = new Target(new Posn(1, 0), new Color(1));

    ICell newTrophy = visitor.visitTarget(target);

    return t.checkExpect(newTrophy.accept(new CellPosnVisitor()), new Posn(0, 1));
  }

  // Tests the visitTrophy method of the MoveTrophyVisitor class
  boolean testVisitTrophyMoveTrophyVisitorleft(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Trophy otherTrophy = new Trophy(new Posn(1, 0), new Color(2));

    ICell newTrophy = visitor.visitTrophy(otherTrophy);

    return t.checkExpect(newTrophy, trophy);
  }

  // Tests the visitHole method of the MoveTrophyVisitor class
  boolean testVisitHoleMoveTrophyVisitorleft(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "left", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Hole hole = new Hole(new Posn(1, 0));

    ICell newCell = visitor.visitHole(hole);

    return t.checkExpect(newCell, new Blank(new Posn(1, 0)));
  }

  //Tests the visitBlank method of the MoveTrophyVisitor class
  boolean testVisitBlankMoveTrophyVisitorRight(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Blank blank = new Blank(new Posn(1, 2));

    ICell newTrophy = visitor.visitBlank(blank);

    return t.checkExpect(newTrophy.accept(new CellPosnVisitor()), new Posn(2, 1));
  }

  //Tests the visitWall method of the MoveTrophyVisitor class
  boolean testVisitWallMoveTrophyVisitorRight(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Wall wall = new Wall(new Posn(1, 2));

    ICell newTrophy = visitor.visitWall(wall);

    return t.checkExpect(newTrophy, trophy);
  }

  //Tests the visitBox method of the MoveTrophyVisitor class
  boolean testVisitBoxMoveTrophyVisitorRight(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Box box = new Box(new Posn(1, 2));

    ICell newTrophy = visitor.visitBox(box);

    return t.checkExpect(newTrophy, trophy);
  }

  //Tests the visitPlayer method of the MoveTrophyVisitor class
  boolean testVisitPlayerMoveTrophyVisitorRight(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Player player = new Player(new Posn(1, 2));

    ICell newTrophy = visitor.visitPlayer(player);

    return t.checkExpect(newTrophy, trophy);
  }

  //Tests the visitTarget method of the MoveTrophyVisitor class
  boolean testVisitTargetMoveTrophyVisitorRight(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Target target = new Target(new Posn(1, 2), new Color(1));

    ICell newTrophy = visitor.visitTarget(target);

    return t.checkExpect(newTrophy.accept(new CellPosnVisitor()), new Posn(2, 1));
  }

  //Tests the visitTrophy method of the MoveTrophyVisitor class
  boolean testVisitTrophyMoveTrophyVisitorRight(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Trophy otherTrophy = new Trophy(new Posn(1, 2), new Color(2));

    ICell newTrophy = visitor.visitTrophy(otherTrophy);

    return t.checkExpect(newTrophy, trophy);
  }

  //Tests the visitHole method of the MoveTrophyVisitor class
  boolean testVisitHoleMoveTrophyVisitorRight(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "right", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Hole hole = new Hole(new Posn(1, 2));

    ICell newCell = visitor.visitHole(hole);

    return t.checkExpect(newCell, new Blank(new Posn(1, 2)));
  }

  // Tests the visitBlank method of the MoveTrophyVisitor class for up motion
  boolean testVisitBlankMoveTrophyVisitorUp(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Blank blank = new Blank(new Posn(0, 1));

    ICell newBox = visitor.visitBlank(blank);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(1, 0));
  }

  // Tests the visitWall method of the MoveTrophyVisitor class for up motion
  boolean testVisitWallMoveTrophyVisitorUp(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Wall wall = new Wall(new Posn(0, 1));

    ICell newBox = visitor.visitWall(wall);

    return t.checkExpect(newBox, trophy);
  }

  // Tests the visitBox method of the MoveTrophyVisitor class for up motion
  boolean testVisitBoxMoveTrophyVisitorUp(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Box otherBox = new Box(new Posn(0, 1));

    ICell newBox = visitor.visitBox(otherBox);

    return t.checkExpect(newBox, trophy);
  }

  // Tests the visitPlayer method of the MoveTrophyVisitor class for up motion
  boolean testVisitPlayerMoveTrophyVisitorUp(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Player player = new Player(new Posn(0, 1));

    ICell newBox = visitor.visitPlayer(player);

    return t.checkExpect(newBox, trophy);
  }

  // Tests the visitTarget method of the MoveTrophyVisitor class for up motion
  boolean testVisitTargetMoveTrophyVisitorUp(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Target target = new Target(new Posn(0, 1), new Color(1));

    ICell newBox = visitor.visitTarget(target);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(1, 0));
  }

  // Tests the visitTrophy method of the MoveTrophyVisitor class for up motion
  boolean testVisitTrophyMoveTrophyVisitorUp(Tester t) {
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));

    ICell newBox = visitor.visitTrophy(trophy);

    return t.checkExpect(newBox, trophy);
  }

  // Tests the visitHole method of the MoveTrophyVisitor class for up motion
  boolean testVisitHoleMoveTrophyVisitorUp(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "up", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Hole hole = new Hole(new Posn(0, 1));

    ICell newCell = visitor.visitHole(hole);

    return t.checkExpect(newCell, new Blank(new Posn(0, 1)));
  }

  // Tests the visitBlank method of the MoveTrophyVisitor class
  boolean testVisitBlankMoveTrophyVisitorDown(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Blank blank = new Blank(new Posn(2, 1));

    ICell newBox = visitor.visitBlank(blank);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(1, 2));
  }

  // Tests the visitWall method of the MoveTrophyVisitor class
  boolean testVisitWallMoveTrophyVisitorDown(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Wall wall = new Wall(new Posn(2, 1));

    ICell newBox = visitor.visitWall(wall);

    return t.checkExpect(newBox, trophy);
  }

  // Tests the visitBox method of the MoveTrophyVisitor class
  boolean testVisitBoxMoveTrophyVisitorDown(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Box otherBox = new Box(new Posn(2, 1));

    ICell newBox = visitor.visitBox(otherBox);

    return t.checkExpect(newBox, trophy);
  }

  // Tests the visitPlayer method of the MoveTrophyVisitor class
  boolean testVisitPlayerMoveTrophyVisitorDown(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Player player = new Player(new Posn(2, 1));

    ICell newBox = visitor.visitPlayer(player);

    return t.checkExpect(newBox, trophy);
  }

  // Tests the visitTarget method of the MoveTrophyVisitor class
  boolean testVisitTargetMoveTrophyVisitorDown(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Target target = new Target(new Posn(2, 1), new Color(1));

    ICell newBox = visitor.visitTarget(target);

    return t.checkExpect(newBox.accept(new CellPosnVisitor()), new Posn(1, 2));
  }

  // Tests the visitTrophy method of the MoveTrophyVisitor class
  boolean testVisitTrophyMoveTrophyVisitorDown(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));

    ICell newBox = visitor.visitTrophy(trophy);

    return t.checkExpect(newBox, trophy);
  }

  // Tests the visitHole method of the MoveTrophyVisitor class
  boolean testVisitHoleMoveTrophyVisitorDown(Tester t) {
    Trophy trophy = new Trophy(new Posn(1, 1), new Color(1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Blank(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 1)));
    MoveTrophyVisitor visitor = new MoveTrophyVisitor(trophy, "down", levelGroundCells,
        levelContentsCells,
        new Posn(2, 2));
    Hole hole = new Hole(new Posn(2, 1));

    ICell newCell = visitor.visitHole(hole);

    return t.checkExpect(newCell, new Blank(new Posn(2, 1)));
  }

  // tests the visitIce method for visitors
  boolean testVisitIce(Tester t) {
    Ice ice = new Ice(new Posn(1, 1));
    Box box = new Box(new Posn(1, 1));
    Trophy trophy = new Trophy(new Posn(1, 1), Color.red);
    Player player = new Player(new Posn(1, 1));
    ArrayList<ICell> levelContentsCells = new ArrayList<>();
    levelContentsCells.add(new Player(new Posn(1, 1)));
    levelContentsCells.add(new Blank(new Posn(0, 1)));
    levelContentsCells.add(new Target(new Posn(1, 0), new Color(1)));
    levelContentsCells.add(new Blank(new Posn(0, 0)));
    ArrayList<ICell> levelGroundCells = new ArrayList<>();
    levelGroundCells.add(new Ice(new Posn(0, 0)));
    levelGroundCells.add(new Blank(new Posn(0, 1)));
    levelGroundCells.add(new Blank(new Posn(1, 0)));
    levelGroundCells.add(new Ice(new Posn(1, 1)));
    MoveBoxVisitor moveBoxVisitor = new MoveBoxVisitor(box, "right", levelGroundCells,
        levelContentsCells, new Posn(2, 2));
    MoveTrophyVisitor moveTrophyVisitor = new MoveTrophyVisitor(trophy, "right", levelGroundCells,
        levelContentsCells, new Posn(2, 2));
    //MovePlayerVisitor movePlayerVisitor = new MovePlayerVisitor(player, "right", levelGroundCells,
    //levelContentsCells, new Posn(2, 2));

    ICell newCellBox = moveBoxVisitor.visitIce(ice);
    ICell newCellTrophy = moveTrophyVisitor.visitIce(ice);
    //ICell newCellPlayer = movePlayerVisitor.visitIce(ice);

    return t.checkExpect(newCellBox, new Box(new Posn(2, 1)))
        && t.checkExpect(newCellTrophy, new Trophy(new Posn(2, 1), Color.red));
    // && t.checkExpect(newCellPlayer, player);
  }

  // tests for visitBlank method of the MoveableObjectCanMoveToVisitor
  boolean testVisitBlankMoveableObjectCanMoveToVisitor(Tester t) {
    MoveableObjectCanMoveToVisitor visitor = new MoveableObjectCanMoveToVisitor();
    Blank blank = new Blank(new Posn(0, 0));
    return t.checkExpect(blank.accept(visitor), true);
  }

  //tests for visitBlank method of the PlayerCanMoveToVisitor
  boolean testVisitBlankPlayerCanMoveToVisitor(Tester t) {
    PlayerCanMoveToVisitor visitor = new PlayerCanMoveToVisitor();
    Blank blank = new Blank(new Posn(0, 0));
    return t.checkExpect(blank.accept(visitor), true);
  }

  // tests for visitWall method of the MoveableObjectCanMoveToVisitor
  boolean testVisitWallCanMoveToVisitor(Tester t) {
    MoveableObjectCanMoveToVisitor visitor = new MoveableObjectCanMoveToVisitor();
    Wall wall = new Wall(new Posn(0, 0));
    return t.checkExpect(wall.accept(visitor), false);
  }

  //tests for visitWall method of the PlayerCanMoveToVisitor
  boolean testVisitWallPlayerCanMoveToVisitor(Tester t) {
    PlayerCanMoveToVisitor visitor = new PlayerCanMoveToVisitor();
    Wall wall = new Wall(new Posn(0, 0));
    return t.checkExpect(wall.accept(visitor), false);
  }

  // tests for visitBox method of the MoveableObjectCanMoveToVisitor
  boolean testVisitBoxCanMoveToVisitor(Tester t) {
    MoveableObjectCanMoveToVisitor visitor = new MoveableObjectCanMoveToVisitor();
    Box box = new Box(new Posn(0, 0));
    return t.checkExpect(box.accept(visitor), false);
  }

  //tests for visitBox method of the PlayerCanMoveToVisitor
  boolean testVisitBoxPlayerCanMoveToVisitor(Tester t) {
    PlayerCanMoveToVisitor visitor = new PlayerCanMoveToVisitor();
    Box box = new Box(new Posn(0, 0));
    return t.checkExpect(box.accept(visitor), true);
  }

  // tests for visitPlayer method of the MoveableObjectCanMoveToVisitor
  boolean testVisitPlayerCanMoveToVisitor(Tester t) {
    MoveableObjectCanMoveToVisitor visitor = new MoveableObjectCanMoveToVisitor();
    Player player = new Player(new Posn(0, 0));
    return t.checkExpect(player.accept(visitor), false);
  }

  //tests for visitPlayer method of the PlayerCanMoveToVisitor
  boolean testVisitPlayerPlayerCanMoveToVisitor(Tester t) {
    PlayerCanMoveToVisitor visitor = new PlayerCanMoveToVisitor();
    Player player = new Player(new Posn(0, 0));
    return t.checkExpect(player.accept(visitor), false);
  }

  // tests for visitTarget method of the MoveableObjectCanMoveToVisitor
  boolean testVisitTargetCanMoveToVisitor(Tester t) {
    MoveableObjectCanMoveToVisitor visitor = new MoveableObjectCanMoveToVisitor();
    Target target = new Target(new Posn(0, 0), new Color(1));
    return t.checkExpect(target.accept(visitor), true);
  }

  //tests for visitTarget method of the PlayerCanMoveToVisitor
  boolean testVisitTargetPlayerCanMoveToVisitor(Tester t) {
    PlayerCanMoveToVisitor visitor = new PlayerCanMoveToVisitor();
    Target target = new Target(new Posn(0, 0), new Color(1));
    return t.checkExpect(target.accept(visitor), true);
  }

  // tests for visitTrophy method of the MoveableObjectCanMoveToVisitor
  boolean testVisitTrophyCanMoveToVisitor(Tester t) {
    MoveableObjectCanMoveToVisitor visitor = new MoveableObjectCanMoveToVisitor();
    Trophy trophy = new Trophy(new Posn(0, 0), new Color(1));
    return t.checkExpect(trophy.accept(visitor), false);
  }

  //tests for visitTrophy method of the PlayerCanMoveToVisitor
  boolean testVisitTrophyPlayerCanMoveToVisitor(Tester t) {
    PlayerCanMoveToVisitor visitor = new PlayerCanMoveToVisitor();
    Trophy trophy = new Trophy(new Posn(0, 0), new Color(1));
    return t.checkExpect(trophy.accept(visitor), true);
  }

  // tests for visitHole method of the MoveableObjectCanMoveToVisitor
  boolean testVisitHoleCanMoveToVisitor(Tester t) {
    MoveableObjectCanMoveToVisitor visitor = new MoveableObjectCanMoveToVisitor();
    Hole hole = new Hole(new Posn(0, 0));
    return t.checkExpect(hole.accept(visitor), true);
  }

  //tests for visitHole method of the PlayerCanMoveToVisitor
  boolean testVisitHolePlayerCanMoveToVisitor(Tester t) {
    PlayerCanMoveToVisitor visitor = new PlayerCanMoveToVisitor();
    Hole hole = new Hole(new Posn(0, 0));
    return t.checkExpect(hole.accept(visitor), true);
  }

  //tests for visitIce method of the MoveableObjectCanMoveToVisitor
  boolean testVisitIceCanMoveToVisitor(Tester t) {
    MoveableObjectCanMoveToVisitor visitor = new MoveableObjectCanMoveToVisitor();
    Ice ice = new Ice(new Posn(0, 0));
    return t.checkExpect(ice.accept(visitor), true);
  }

  //tests for visitIce method of the PlayerCanMoveToVisitor
  boolean testVisitIcePlayerCanMoveToVisitor(Tester t) {
    PlayerCanMoveToVisitor visitor = new PlayerCanMoveToVisitor();
    Ice ice = new Ice(new Posn(0, 0));
    return t.checkExpect(ice.accept(visitor), true);
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