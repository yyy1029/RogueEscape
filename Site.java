import java.util.*;

/**
 * Represents a site in the dungeon.
 */
public class Site {
    private int i;
    private int j;

    private Dungeon dungeon;

    /**
     * Initializes a new site with the given coordinates.
     * @param i The row index.
     * @param j The column index.
     */
    public Site(int i, int j) {
        this.i = i;
        this.j = j;
    }

    /**
     * Gets the row index of the site.
     * @return The row index.
     */
    public int i() {
        return i;
    }

    /**
     * Gets the column index of the site.
     * @return The column index.
     */
    public int j() {
        return j;
    }

    /**
     * Checks if this site is equal to another object.
     * @param obj The object to compare with.
     * @return True if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Site site = (Site) obj;
        return i == site.i && j == site.j;
    }

    /**
     * Generates a hash code for this site.
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int result = Integer.hashCode(i);
        result = 31 * result + Integer.hashCode(j);
        return result;
    }

    /**
     * Returns a string representation of this site.
     * @return The string representation.
     */
    public String toString() {
        return "(" + i + ", " + j + ")";
    }
}
