import java.util.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Class representing the Monster character in the game.
 */
public class Monster extends Player implements MoveStrategy, Graph {

    /**
     * Constructor for Monster class.
     * @param game The instance of the game.
     * @param startSite The starting position of the monster.
     */
    public Monster(Game game, Site startSite) {
        super(game, startSite);
    }

    @Override
    public Site move(Site current, Site target) {
        return current;
    }

    @Override
    public Site move() {
        Site monster = game.getMonsterSite();
        Site rogue = game.getRogueSite();

        // Get all possible shortest paths
        List<List<Site>> paths = calculateBFSDistanceToRogue(monster, rogue);
        if (!paths.isEmpty()) {
            // If there are multiple paths, choose the best path based on Priority
            return paths.size() > 1 ? chooseBestPathBasedOnPriority(paths) : paths.get(0).get(1);
        } else {
            System.out.println("No path to Rogue, Rogue wins the game.");
            System.exit(0);
            return null;
        }
    }

    /**
     * Implements the Breadth-First Search algorithm to find the shortest paths from the monster to the rogue.
     * @param start The starting position.
     * @param end The target position.
     * @return List of shortest paths from start to end.
     */
    private List<List<Site>> calculateBFSDistanceToRogue(Site start, Site end) {
        Map<Site, List<List<Site>>> allPaths = new HashMap<>();
        Queue<Site> queue = new LinkedList<>();
        Map<Site, Integer> distance = new HashMap<>();
        int shortestDistance = Integer.MAX_VALUE;

        queue.offer(start);
        allPaths.put(start, Arrays.asList(Arrays.asList(start)));
        distance.put(start, 0);

        while (!queue.isEmpty()) {
            Site current = queue.poll();
            int Distance = distance.get(current);

            if (Distance > shortestDistance) {
                continue;
            }

            for (Site neighbor : game.getDungeon().getNeighbors(current)) {
                if (!game.getDungeon().isLegalMove(current, neighbor)) {
                    continue;
                }

                int newDistance = Distance + 1;
                if (newDistance > shortestDistance) {
                    continue;
                }

                if (!distance.containsKey(neighbor) || newDistance == distance.get(neighbor)) {
                    if (!distance.containsKey(neighbor)) {
                        distance.put(neighbor, newDistance);
                        queue.offer(neighbor);
                    }

                    List<List<Site>> newPaths = new ArrayList<>();
                    for (List<Site> path : allPaths.get(current)) {
                        List<Site> newPath = new ArrayList<>(path);
                        newPath.add(neighbor);
                        newPaths.add(newPath);
                    }
                    allPaths.computeIfAbsent(neighbor, k -> new ArrayList<>()).addAll(newPaths);

                    if (neighbor.equals(end) && newDistance < shortestDistance) {
                        shortestDistance = newDistance;
                    }
                }
            }
        }

        List<List<Site>> shortestPath = allPaths.getOrDefault(end, new ArrayList<>());
        int finalShortestDistanceToRogue = shortestDistance;
        return shortestPath.stream()
                .filter(path -> path.size() == finalShortestDistanceToRogue + 1)
                .collect(Collectors.toList());
    }



    /**
     * Chooses the best path based on priority to the rogue, integrating all related decisions within a single method.
     * @param paths List of paths.
     * @return The best move based on priority.
     */
    private Site chooseBestPathBasedOnPriority(List<List<Site>> paths) {
        Site rogueSite = game.getRogueSite();
        List<Site> adjacentStarSites = game.getDungeon().getAdjacentToStarsSites();

        if (adjacentStarSites.isEmpty()) {
            // Logic from chooseClosestStepToRogue directly included here when no star sites are adjacent
            Site bestMove = null;
            int manhattanDistance = Integer.MAX_VALUE;

            for (List<Site> path : paths) {
                Site nextStep = path.get(1);
                int distanceToRogue = computeManhattanDistance(nextStep, rogueSite);
                if (distanceToRogue < manhattanDistance) {
                    bestMove = nextStep;
                    manhattanDistance = distanceToRogue;
                }
            }

            return bestMove;
        }

        // Computes the distances from the rogue site to all adjacent star sites
        Map<Site, Integer> distancesFromRogue = new HashMap<>();
        Map<Site, Integer> fullDistances = game.getDungeon().bfsDistance(rogueSite);
        for (Site target : adjacentStarSites) {
            distancesFromRogue.put(target, fullDistances.getOrDefault(target, Integer.MAX_VALUE));
        }

        Site nearestStar = findNearestCorridor(distancesFromRogue, adjacentStarSites);

        // Check if there's a single corridor leading to the nearest star
        if (isSingleCorridor(distancesFromRogue, adjacentStarSites, nearestStar)) {
            return findBestMoveToNearestCorridor(paths, nearestStar);
        } else {
            // Reusing the logic for finding the closest step to the rogue when there's no single corridor
            Site bestMove = null;
            int manhattanDistance = Integer.MAX_VALUE;

            for (List<Site> path : paths) {
                Site nextStep = path.get(1);
                int distanceToRogue = computeManhattanDistance(nextStep, rogueSite);
                if (distanceToRogue < manhattanDistance) {
                    bestMove = nextStep;
                    manhattanDistance = distanceToRogue;
                }
            }

            return bestMove;
        }
    }

    /**
     * Checks if there is only one nearest star site to the rogue.
     * @param distancesFromRogue Map containing distances from the rogue to adjacent star sites.
     * @param starSites List of adjacent star sites.
     * @param nearestCorridor The nearest star site.
     * @return True if there is only one nearest star site, false otherwise.
     */
    private boolean isSingleCorridor(Map<Site, Integer> distancesFromRogue, List<Site> starSites, Site nearestCorridor) {
        int minDistance = distancesFromRogue.get(nearestCorridor);
        return starSites.stream()
                .mapToInt(star -> distancesFromRogue.getOrDefault(star, Integer.MAX_VALUE))
                .filter(dist -> dist == minDistance)
                .count() == 1;
    }

    /**
     * Finds the nearest star site to the rogue.
     * @param distancesFromRogue Map containing distances from the rogue to adjacent star sites.
     * @param corridorSites List of adjacent corridor sites.
     * @return The nearest star site.
     */
    private Site findNearestCorridor(Map<Site, Integer> distancesFromRogue, List<Site> corridorSites) {
        Site nearestStar = null;
        int nearestDistance = Integer.MAX_VALUE;

        for (Site corridorSite : corridorSites) {
            int distance = distancesFromRogue.getOrDefault(corridorSite, Integer.MAX_VALUE);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestStar = corridorSite;
            }
        }
        return nearestStar;
    }


    /**
     * Finds the best move to the nearest star site.
     * @param paths List of paths.
     * @param nearestCorridor The nearest star site.
     * @return The best move to the nearest star.
     */
    private Site findBestMoveToNearestCorridor(List<List<Site>> paths, Site nearestCorridor) {
        Site bestMove = null;
        int minDistanceToCorridor = Integer.MAX_VALUE;

        for (List<Site> path : paths) {
            Site nextStep = path.get(1);
            int distanceToNearestCorridor = computeManhattanDistance(nextStep, nearestCorridor);
            if (distanceToNearestCorridor < minDistanceToCorridor) {
                bestMove = nextStep;
                minDistanceToCorridor = distanceToNearestCorridor;
            }
        }

        return bestMove;
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

