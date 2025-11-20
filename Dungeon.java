import java.util.*;

/**
 * Represents the dungeon where the game takes place.
 */
public class Dungeon implements Graph {

    private boolean[][] isRoom;        // Indicates if a site is a room.
    private boolean[][] isCorridor;    // Indicates if a site is a corridor.
    private int N; // Size of the dungeon.
    public char[][] board; // Representation of the dungeon.
    private Site[][] sites; // Sites in the dungeon.
    private Map<Integer, List<Integer>> adjMap; // Adjacency map.
    private List<Site> adjacentToStarsSites = new ArrayList<>(); // Sites adjacent to stars.

    /**
     * Constructs a dungeon with the provided board.
     * @param board The board representing the dungeon.
     */
    public Dungeon(char[][] board) {
        this.N = board.length;
        this.board = new char[N][];
        isRoom = new boolean[N][N];
        isCorridor = new boolean[N][N];
        adjMap = new HashMap<>();
        sites = new Site[N][N];
        initializeBoard(board);
    }

    /**
     * Gets the list of sites adjacent to stars.
     * @return List of sites adjacent to stars.
     */
    public List<Site> getAdjacentToStarsSites() {
        return adjacentToStarsSites;
    }

    private void initializeBoard(char[][] inputBoard) {
        this.board = new char[N][];
        for (int i = 0; i < N; i++) {
            this.board[i] = new char[N];
            for (int j = 0; j < N; j++) {
                this.board[i][j] = inputBoard[i][j];
                sites[i][j] = new Site(i, j);
                if (inputBoard[i][j] == '.') {
                    isRoom[i][j] = true;
                } else if (inputBoard[i][j] == '+') {
                    isCorridor[i][j] = true;
                }
            }
        }
    }

    /**
     * Gets the size of the dungeon.
     * @return The size of the dungeon.
     */
    public int size() {
        return N;
    }

    /**
     * Checks if a site is a corridor.
     * @param v The site to check.
     * @return True if the site is a corridor, false otherwise.
     */
    public boolean isCorridor(Site v) {
        int i = v.i();
        int j = v.j();
        if (i < 0 || j < 0 || i >= N || j >= N) return false;
        return isCorridor[i][j];
    }

    /**
     * Checks if a site is a room.
     * @param v The site to check.
     * @return True if the site is a room, false otherwise.
     */
    public boolean isRoom(Site v) {
        int i = v.i();
        int j = v.j();
        if (i < 0 || j < 0 || i >= N || j >= N) return false;
        return isRoom[i][j];
    }

    /**
     * Checks if a site is a wall.
     * @param v The site to check.
     * @return True if the site is a wall, false otherwise.
     */
    public boolean isWall(Site v) {
        return (!isRoom(v) && !isCorridor(v));
    }

    /**
     * Checks if a move between two sites is legal.
     * @param v The source site.
     * @param w The target site.
     * @return True if the move is legal, false otherwise.
     */
    public boolean isLegalMove(Site v, Site w) {
        if (v == null || w == null) {
            System.out.println("Illegal move attempted due to null site.");
            return false;
        }
        int i1 = v.i();
        int j1 = v.j();
        int i2 = w.i();
        int j2 = w.j();
        if (i1 < 0 || j1 < 0 || i1 >= N || j1 >= N) return false;
        if (i2 < 0 || j2 < 0 || i2 >= N || j2 >= N) return false;
        if (isWall(v) || isWall(w)) return false;
        if (Math.abs(i1 - i2) > 1) return false;
        if (Math.abs(j1 - j2) > 1) return false;
        if (isRoom(v) && isRoom(w)) return true;
        if (i1 == i2) return true;
        return j1 == j2;
    }

    /**
     * Computes the BFS distance from a start site to all other sites.
     * @param start The start site.
     * @return A map containing distances from the start site to all other sites.
     */
    public Map<Site, Integer> bfsDistance(Site start) {
        Queue<Site> queue = new LinkedList<>();
        Map<Site, Integer> distances = new HashMap<>();
        Map<Site, Boolean> visited = new HashMap<>();

        queue.add(start);
        visited.put(start, true);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            Site current = queue.poll();
            int Distance = distances.get(current);

            for (Site neighbor : getNeighbors(current)) {
                if (!visited.getOrDefault(neighbor, false)) {
                    visited.put(neighbor, true);
                    queue.add(neighbor);
                    distances.put(neighbor, Distance + 1);
                }
            }
        }

        return distances;
    }

    /**
     * Gets the neighbors of a site.
     * @param site The site to get neighbors for.
     * @return A list of neighboring sites.
     */
    public List<Site> getNeighbors(Site site) {
        List<Site> neighbors = new ArrayList<>();
        int i = site.i();
        int j = site.j();

        int[][] directions;
        if (board[i][j] == '+') {
            directions = new int[][]{
                    {1, 0}, {0, 1}, {-1, 0}, {0, -1}
            };
        } else {
            directions = new int[][]{
                    {1, 0}, {0, 1}, {-1, 0}, {0, -1},
                    {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
            };
        }

        for (int[] direction : directions) {
            int newX = i + direction[0];
            int newY = j + direction[1];
            if (newX >= 0 && newX < board.length && newY >= 0 && newY < board[0].length &&
                    (board[newX][newY] == '.' || board[newX][newY] == '+')) {
                neighbors.add(new Site(newX, newY));
            }
        }

        return neighbors;
    }

    @Override
    public Map bfsDistance(Object start) {
        return null;
    }

    @Override
    public List getNeighbors(Object vertex) {
        return null;
    }

    @Override
    public List findAccessibleCorridors(Object vertex) {
        return null;
    }

    @Override
    public void findImmediateCorridors(Object vertex) {

    }

    @Override
    public List findShortestPathToStart(Object vertex, List point) {
        return null;
    }

    @Override
    public List calculateBFSDistanceToRogue(Object vertex, List point) {
        return null;
    }
}

