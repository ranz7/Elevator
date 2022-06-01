package tools;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class Trio<First, Second, Third> implements Comparable, Serializable {
    @Getter
    @Setter
    First first;

    @Getter
    @Setter
    Second second;

    @Getter
    @Setter
    Third third;

    public Trio(First first, Second second, Third third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public int compareTo(Object o) {
        Trio<First, Second, Third> pair = (Trio<First, Second, Third>) o;
        return hashCode() - pair.hashCode();
    }
}
