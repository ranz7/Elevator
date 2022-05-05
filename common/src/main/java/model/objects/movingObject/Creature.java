package model.objects.movingObject;

import lombok.Getter;
import lombok.Setter;
import tools.Vector2D;

import java.awt.*;
import java.io.Serializable;

/*
 * Basic object of all objects in project
 */
@Getter
public class Creature implements Serializable {
    // used in sent data
    private static Integer next_id = 0;

    @Setter
    protected boolean isVisible = true;
    protected Vector2D size = new Vector2D();
    protected Vector2D position;
    protected long id;

    public Creature(Creature creatureA) {
        this.position = new Vector2D(creatureA.position);
        this.size = new Vector2D(creatureA.size);
        this.isVisible = creatureA.isVisible;
        this.id = creatureA.id;
    }

    public Creature(Vector2D position) {
        this.position = position;
        id = next_id++;
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
