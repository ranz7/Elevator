package model.objects;

import lombok.Getter;
import lombok.Setter;
import tools.Vector2D;

import java.io.Serializable;

/**
 * Basic object of all objects in project
 */
@Getter
public class Creature implements Serializable {
    // used in sent data
    private static Integer next_id = 0;

    @Setter
    protected boolean isVisible = true;
    protected Vector2D size;
    protected Vector2D position;
    protected long id;

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
        this.position = creature.position;
        this.isVisible = creature.isVisible;
        this.size = creature.size;
    }
}
