import java.util.List;
import java.util.Scanner;

/**
 * The main class representing the game environment and logic.
 */
public class Game {
    // Constants
    private static final String NEWLINE = System.getProperty("line.separator");
    private static final String BASE_PATH = "./Dungeons/";

    // Instance variables
    private Dungeon dungeon;
    private char monsterSymbol;
    private static final char ROGUE_SYMBOL = '@';
    private int gridSize;
    private Site monsterPosition;
    private Site roguePosition;
    private Site initialMonsterPosition;
    private Site initialRoguePosition;
    private Monster monster;
    private Rogue rogue;

    /**
     * Constructor for the Game class.
     */
    public Game() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the dungeon you want to play: ");
        String dungeonIdentifier = scanner.next();
        String fullPath = BASE_PATH + "dungeon" + dungeonIdentifier + ".txt";
        initializeGame(fullPath);
        scanner.close();
    }

    // Private methods

    /**
     * Initializes the game environment.
     * @param fullPath The full path to the dungeon file.
     */
    private void initializeGame(String fullPath) {
        In in = new In(fullPath);
        gridSize = Integer.parseInt(in.readLine());
        char[][] board = new char[gridSize][gridSize];
        initializeBoard(board, in);
        dungeon = new Dungeon(board);
        monster = new Monster(this, monsterPosition);
        rogue = new Rogue(this, roguePosition);
        in.close();
    }

    /**
     * Initializes the game board.
     * @param board The game board array.
     * @param in The input stream for reading the dungeon file.
     */
    private void initializeBoard(char[][] board, In in) {
        for (int i = 0; i < gridSize; i++) {
            String line = in.readLine();
            for (int j = 0; j < gridSize; j++) {
                board[i][j] = line.charAt(2 * j);
                processBoardCell(board, i, j);
            }
        }
    }

    /**
     * Processes a cell on the game board.
     * @param board The game board array.
     * @param i The row index of the cell.
     * @param j The column index of the cell.
     */
    private void processBoardCell(char[][] board, int i, int j) {
        if (board[i][j] >= 'A' && board[i][j] <= 'Z') {
            monsterSymbol = board[i][j];
            board[i][j] = '.';
            monsterPosition = new Site(i, j);
            initialMonsterPosition = new Site(i, j);
        } else if (board[i][j] == ROGUE_SYMBOL) {
            board[i][j] = '.';
            roguePosition = new Site(i, j);
            initialRoguePosition = new Site(i, j);
        }
    }

    // Public methods

    /**
     * Gets the monster's current position.
     * @return The monster's position.
     */
    public Site getMonsterSite() { return monsterPosition; }

    /**
     * Gets the rogue's current position.
     * @return The rogue's position.
     */
    public Site getRogueSite() { return roguePosition; }

    /**
     * Gets the initial position of the monster.
     * @return The initial position of the monster.
     */
    public Site getInitialMonsterPosition() { return initialMonsterPosition; }

    /**
     * Gets the initial position of the rogue.
     * @return The initial position of the rogue.
     */
    public Site getInitialRoguePosition() { return initialRoguePosition; }

    /**
     * Gets the dungeon object representing the game environment.
     * @return The dungeon object.
     */
    public Dungeon getDungeon() { return dungeon; }

    /**
     * Initializes the game environment.
     */
    public void initialize() {
        List<Site> accessibleCorridors = rogue.findAccessibleCorridors(getInitialRoguePosition());
        List<Site> startCorridors = rogue.filterCorridors(getInitialRoguePosition(), getInitialMonsterPosition(), accessibleCorridors);
        if (!startCorridors.isEmpty()) {
            for (Site site : startCorridors) {
                rogue.findShortestPathToStart(site, rogue.getStartCorridor());
                if (!rogue.getCurrentPath().isEmpty()) {
                    break;
                }
            }
        }
    }

    /**
     * Starts the game loop.
     */
    public void play() {
        for (int turn = 1; true; turn++) {
            System.out.println("Step: " + turn);
            System.out.println();

            if (performMonsterMove()) break;
            System.out.println(this);

            if (performRogueMove()) break;
            System.out.println(this);

            if (turn >= gridSize * gridSize) {
                System.out.println("Rogue is in a winning circle, Rogue wins.");
                break;
            }
        }
    }

    // Private helper methods

    /**
     * Performs the monster's move.
     * @return True if the game is over, false otherwise.
     */
    private boolean performMonsterMove() {
        if (monsterPosition.equals(roguePosition)) {
            System.out.println("Caught by monster");
            return true;
        }
        Site nextMonsterMove = monster.move();
        if (dungeon.isLegalMove(monsterPosition, nextMonsterMove)) {
            monsterPosition = nextMonsterMove;
        } else {
            throw new RuntimeException("Monster caught cheating");
        }
        return false;
    }

    /**
     * Performs the rogue's move.
     * @return True if the game is over, false otherwise.
     */
    private boolean performRogueMove() {
        if (monsterPosition.equals(roguePosition)) {
            System.out.println("Caught by monster");
            return true;
        }
        Site nextRogueMove = rogue.move();
        if (dungeon.isLegalMove(roguePosition, nextRogueMove)) {
            roguePosition = nextRogueMove;
        } else {
            throw new RuntimeException("Rogue caught cheating");
        }
        return false;
    }

    /**
     * Generates the string representation of the game board.
     * @return The string representation of the game board.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Site site = new Site(i, j);
                appendSiteRepresentation(sb, site);
            }
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

    /**
     * Appends the representation of a site on the game board to the string builder.
     * @param sb The string builder.
     * @param site The site to represent.
     */
    private void appendSiteRepresentation(StringBuilder sb, Site site) {
        if (roguePosition.equals(monsterPosition) && roguePosition.equals(site)) {
            sb.append("* ");
        } else if (roguePosition.equals(site)) {
            sb.append(ROGUE_SYMBOL + " ");
        } else if (monsterPosition.equals(site)) {
            sb.append(monsterSymbol + " ");
        } else if (dungeon.isRoom(site)) {
            sb.append(". ");
        } else if (dungeon.isCorridor(site)) {
            sb.append("+ ");
        } else if (dungeon.isWall(site)) {
            sb.append("  ");
        }
    }

    /**
     * The main method to start the game.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.play();
    }
}
