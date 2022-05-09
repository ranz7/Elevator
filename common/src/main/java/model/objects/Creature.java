package model.objects;

import lombok.Getter;
import lombok.Setter;
import tools.Vector2D;

import java.io.Serializable;

/**
 * Basic object of all objects in project
 */

public class Creature implements Serializable {
    // used in sent data
    private static int next_id = 0;

    @Setter
    @Getter
    private boolean isVisible = true;
    @Setter
    @Getter
    private Vector2D size;

    @Getter
    protected Vector2D position;

    @Getter
    @Setter
    protected boolean isDead = false;

    @Getter
    private long id;

    public Creature(Creature creatureA) {
        position = new Vector2D(creatureA.position);
        size = new Vector2D(creatureA.size);
        isVisible = creatureA.isVisible;
        id = creatureA.id;
    }

    public Creature(Vector2D position, Vector2D size) {
        this.position = position;
        this.size = size;
        id = next_id++;
    }

    // sets object in window, by copy it into created instance
    // to be used
    public void set(Creature creature) {
        this.position.set(creature.position);
        this.size.set(creature.size);
        this.isVisible = creature.isVisible;
    }
}
