package tools;

import lombok.Getter;
import lombok.Setter;

public class Pair<Thirst, Second> {
    @Getter
    @Setter
    Thirst thirst;

    @Getter
    @Setter
    Second second;

    public Pair(Thirst thirst, Second second) {
        this.thirst = thirst;
        this.second = second;
    }

}
