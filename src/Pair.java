/**
 * Class for creating matches between strings and the number of times they were repeated in the file/console.
 */
public class Pair<M,S> {
    private M matches;
    private S string;

    public Pair(M matches, S string) {
        this.matches = matches;
        this.string = string;
    }

    public M getMatches() {
        return matches;
    }

    public S getString() {
        return string;
    }

    public void setMatches(M matches) {
        this.matches = matches;
    }

    @Override
    public String toString() {
        return matches + " " + string;
    }
}
