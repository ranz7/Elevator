package tools;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Pair<First, Second> implements Comparable, Serializable {
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

    public <FirstCasted, SecondCasted> Pair<FirstCasted, SecondCasted>
    cast(Class<FirstCasted> first, Class<SecondCasted> second) {
        return new Pair<>(first.cast(getFirst()), second.cast(getSecond()));
    }
}
