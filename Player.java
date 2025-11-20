import java.util.*;

public abstract class Player {
    protected Game game; // The game instance associated with the player
    protected Dungeon dungeon; // The dungeon instance associated with the player
    protected int N; // The size of the dungeon grid
    protected Site currentSite; // The current position of the player

    /**
     * Constructor for Player class.
     * @param game The Game instance associated with the player.
     * @param startSite The initial site where the player starts.
     */
    public Player(Game game, Site startSite) {
        this.game = game;
        this.dungeon = game.getDungeon();
        this.N = dungeon.size();
        this.currentSite = startSite;
    }

    /**
     * Abstract method representing the movement strategy of the player.
     * Subclasses will override this method to define specific movement behavior.
     * @return The next site where the player intends to move.
     */
    abstract public Site move();

    /**
     * Computes the Manhattan distance between two sites on the dungeon grid.
     * @param a The first site.
     * @param b The second site.
     * @return The Manhattan distance between the two sites.
     */
    protected int computeManhattanDistance(Site a, Site b) {
        return Math.abs(a.i() - b.i()) + Math.abs(a.j() - b.j());
    }
}
