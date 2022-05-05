package model.objects.movingObject;

import lombok.Getter;
import lombok.Setter;
import common.tools.Vector2D;

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
    protected Point size = new Point();
    protected Vector2D position;
    protected long id;

    public Creature(Creature creatureA) {
        this.position = new Vector2D(creatureA.position);
        this.size = new Point(creatureA.size);
        this.isVisible = creatureA.isVisible;
        this.id = creatureA.id;
    }

    public Creature(Vector2D position) {
        this.position = position;
        id = next_id++;
    }

    public Creature(Vector2D position, Point size) {
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
