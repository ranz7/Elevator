package tools;

import lombok.Getter;
import lombok.Setter;

public class Pair<First, Second> implements Comparable {
    @Getter
    @Setter
    First first;

    @Getter
    @Setter
    Second second;

    public Pair(First first, Second second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Object o) {
        Pair<First, Second> pair = (Pair<First, Second>) o;
        return hashCode() - pair.hashCode();
    }
}
