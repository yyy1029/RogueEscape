import java.util.List;
import java.util.Map;

public interface Graph<V> {
    Map<V, Integer> bfsDistance(V start);
    List<V> getNeighbors(V vertex);
    List<V> findAccessibleCorridors(V vertex);
    void findImmediateCorridors(V vertex);
    List<V> findShortestPathToStart(V vertex, List<V> point);
    List<V> calculateBFSDistanceToRogue(V vertex, List<V> point);
}
