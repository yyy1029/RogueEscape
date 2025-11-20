import java.util.*;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Rogue class represents a player-controlled character in the game.
 * It implements MoveStrategy and Graph interfaces.
 */
public class Rogue extends Player implements MoveStrategy, Graph {

    private boolean isInCycle;
    private List<Site> currentPath;
    private List<Site> visitedStart = new ArrayList<>();
    private int vs = 0;
    private List<Site> StartCorridor = new ArrayList<>();

    /**
     * Constructs a Rogue object with the specified game and start site.
     * @param game The game instance.
     * @param startSite The starting site for the rogue.
     */
    public Rogue(Game game, Site startSite) {
        super(game, startSite);
        this.isInCycle = false;
        this.currentPath = new ArrayList<>();
    }

    /**
     * Retrieves the current path of the rogue.
     * @return The current path.
     */
    public List<Site> getCurrentPath() {
        return currentPath;
    }

    /**
     * Retrieves the start corridor for the rogue.
     * @return The start corridor.
     */
    public List<Site> getStartCorridor() {
        return StartCorridor;
    }

    @Override
    public Site move(Site current, Site target) {
        return current;
    }

    @Override
    public Site move() {
        Site rogue = game.getRogueSite();
        Site monster = game.getMonsterSite();
        List<Site> path = getCurrentPath();

        if (!path.isEmpty() && path.contains(rogue) && dungeon.isCorridor(rogue)) {
            isInCycle = true;
        }

        if (path.isEmpty()) {
            System.out.println("Move logic 1");
            return moveAwayFromMonster(rogue, monster);
        } else if (!path.isEmpty() && isInCycle) {
            System.out.println("Move logic 2");
            return moveAwayFromMonsterInCycle(rogue, path.get(0));
        }

        System.out.println("Move logic 3");
        return moveToTarget(rogue, path.get(0));
    }

    /**
     * Finds immediate corridors reachable from the given start site.
     * @param start The start site.
     */
    public void findImmediateCorridors(Site start) {
        visitedStart.add(start);
        StartCorridor.clear();

        for (Site neighbor : dungeon.getNeighbors(start)) {
            vs++;
            char c = dungeon.board[neighbor.i()][neighbor.j()];
            if (c == '+' && !visitedStart.contains(neighbor)) {
                StartCorridor.add(neighbor);
            } else if (c == '.' && !visitedStart.contains(neighbor) && vs > 4) {
                StartCorridor = new ArrayList<>();
                break;
            }
        }
    }

    /**
     * Finds the shortest path to the start site.
     * @param start The start site.
     * @param StartCorridor The list of start corridors.
     * @return The shortest path to the start site.
     */
    public List<Site> findShortestPathToStart(Site start, List<Site> StartCorridor) {
        findImmediateCorridors(start);
        this.StartCorridor = StartCorridor;
        if (StartCorridor.isEmpty()) {
            this.currentPath.clear();
            return StartCorridor;
        }

        Map<Site, Site> parentMap = new HashMap<>();
        Queue<Site> queue = new LinkedList<>();
        Set<Site> visited = new HashSet<>();

        visited.add(start);

        if (StartCorridor.size() >= 2) {
            StartCorridor = Collections.singletonList(StartCorridor.get(0));
        }
        for (Site site : StartCorridor) {
            if (!site.equals(start)) {
                queue.add(site);
                visited.add(site);
                parentMap.put(site, null);
            }
        }

        int i = 0;
        while (!queue.isEmpty()) {
            Site current = queue.poll();
            for (Site neighbor : dungeon.getNeighbors(current)) {
                i++;
                if (!visited.contains(neighbor) && dungeon.isLegalMove(current, neighbor)) {
                    parentMap.put(neighbor, current);
                    visited.add(neighbor);
                    if (i > 8) {
                        visited.remove(start);
                    }
                    queue.add(neighbor);

                    if (neighbor.equals(start)) {
                        LinkedList<Site> path = new LinkedList<>();
                        for (Site at = neighbor; at != null; at = parentMap.get(at)) {
                            path.addFirst(at);
                        }
                        this.currentPath = path;
                        System.out.println("Shortest path to start found:");
                        System.out.println(this.currentPath);
                        return StartCorridor;
                    }
                }
            }
        }

        return attemptNewStarts(StartCorridor, start);
    }

    /**
     * Attempts to find new start sites.
     * @param previousStartCorridors The previous start corridors.
     * @param start The start site.
     * @return The new path to the start site.
     */
    private List<Site> attemptNewStarts(List<Site> previousStartCorridors, Site start) {
        previousStartCorridors = StartCorridor;
        if (previousStartCorridors.size() >= 2) {
            previousStartCorridors = Collections.singletonList(previousStartCorridors.get(0));
        }
        for (Site corridor : previousStartCorridors) {
            start = corridor;
            List<Site> newCorridors = StartCorridor;
            List<Site> newPath = findShortestPathToStart(start, newCorridors);
            if (!newPath.isEmpty()) {
                return newPath;
            } else {
                return new ArrayList<>();
            }
        }

        return new ArrayList<>();
    }

    /**
     * Finds accessible corridors from the start site.
     * @param start The start site.
     * @return The list of accessible corridors.
     */
    public List<Site> findAccessibleCorridors(Site start) {
        List<Site> accessibleCorridors = new ArrayList<>();
        Queue<Site> queue = new LinkedList<>();
        Set<Site> visited = new HashSet<>();
        if (dungeon.board[start.i()][start.j()] != '.') {
            return accessibleCorridors;
        }

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Site current = queue.poll();
            for (Site neighbor : dungeon.getNeighbors(current)) {
                if (!visited.contains(neighbor) && dungeon.board[neighbor.i()][neighbor.j()] == '.') {
                    queue.add(neighbor);
                    visited.add(neighbor);
                } else if (!visited.contains(neighbor) && dungeon.board[neighbor.i()][neighbor.j()] == '+') {
                    if (!accessibleCorridors.contains(neighbor)) {
                        accessibleCorridors.add(neighbor);
                    }
                }
            }
        }

        return accessibleCorridors;
    }

    /**
     * Filters the corridors based on the distance to monster and rogue start positions.
     * @param rogueStart The start position of the rogue.
     * @param monsterStart The start position of the monster.
     * @param corridors The list of corridors to filter.
     * @return The filtered list of start corridors.
     */
    public List<Site> filterCorridors(Site rogueStart, Site monsterStart, List<Site> corridors)
    {
        List<Site> StartCorridors = new ArrayList<>();

        Map<Site, Integer> rogueDistances = dungeon.bfsDistance(rogueStart);
        Map<Site, Integer> monsterDistances = dungeon.bfsDistance(monsterStart);

        for (Site corridor : corridors) {
            if (rogueDistances.get(corridor) < monsterDistances.get(corridor) && hasAdjacentCorridor(corridor)) {
                StartCorridors.add(corridor);
            }
        }
        return StartCorridors;
    }

    /**
     * Checks if a site has an adjacent corridor.
     * @param site The site to check.
     * @return True if the site has an adjacent corridor, false otherwise.
     */
    private boolean hasAdjacentCorridor(Site site) {
        for (Site neighbor : dungeon.getNeighbors(site)) {
            if (dungeon.isCorridor(neighbor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the rogue away from the monster.
     * @param rogue The rogue's current site.
     * @param monster The monster's current site.
     * @return The site where the rogue should move.
     */
    private Site moveAwayFromMonster(Site rogue, Site monster) {
        rogue = game.getRogueSite();
        monster = game.getMonsterSite();
        List<Site> neighbors = dungeon.getNeighbors(rogue);
        Site bestMove = rogue;
        int maxDistance = -1;

        for (Site neighbor : neighbors) {
            if (dungeon.isLegalMove(rogue, neighbor)) {
                int distance = computeManhattanDistance(neighbor, monster);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    bestMove = neighbor;
                }
            }
        }

        return bestMove;
    }

    /**
     * Moves the rogue away from the monster when inside a cycle.
     * @param rogue The rogue's current site.
     * @param monster The monster's current site.
     * @return The site where the rogue should move.
     */
    private Site moveAwayFromMonsterInCycle(Site rogue, Site monster) {
        List<Site> path = getCurrentPath();
        rogue = game.getRogueSite();
        monster = game.getMonsterSite();
        List<Site> neighbors = dungeon.getNeighbors(rogue);
        Site bestMove = rogue;
        int maxDistance = -1;

        for (Site neighbor : neighbors) {
            if (dungeon.isLegalMove(rogue, neighbor) && path.contains(neighbor)) {
                int distance = computeManhattanDistance(neighbor, monster);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    bestMove = neighbor;
                } else if (distance == maxDistance) {
                    return rogue;
                }
            }
        }

        return bestMove;
    }

    /**
     * Moves the rogue towards the target site.
     * @param start The start site.
     * @param target The target site.
     * @return The site where the rogue should move.
     */
    private Site moveToTarget(Site start, Site target) {
        if (start.equals(target)) {
            return start;
        }

        Queue<Site> queue = new LinkedList<>();
        Map<Site, Site> cameFrom = new HashMap<>();
        Map<Site, Integer> distance = new HashMap<>();

        queue.add(start);
        cameFrom.put(start, null);
        distance.put(start, 0);

        while (!queue.isEmpty()) {
            Site current = queue.poll();

            for (Site neighbor : dungeon.getNeighbors(current)) {
                if (!cameFrom.containsKey(neighbor) && dungeon.isLegalMove(current, neighbor)) {
                    cameFrom.put(neighbor, current);
                    distance.put(neighbor, distance.get(current) + 1);
                    queue.add(neighbor);

                    if (neighbor.equals(target)) {
                        LinkedList<Site> path = new LinkedList<>();
                        Site pathCurrent = neighbor;
                        while (pathCurrent != null && !pathCurrent.equals(start)) {
                            path.addFirst(pathCurrent);
                            pathCurrent = cameFrom.get(pathCurrent);
                        }
                        path.addFirst(start);
                        return path.size() > 1 ? path.get(1) : start;
                    }
                }
            }
        }

        return start;
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
