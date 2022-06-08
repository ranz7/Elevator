package model.objects.floor;

import model.objects.Creature;
import tools.Vector2D;

public class Painting extends Creature {
    int randomSeed;

    public Painting(Vector2D position, int randomSeed) {
        super(position, new Vector2D(1,1));
        this.randomSeed = randomSeed;
    }
}
