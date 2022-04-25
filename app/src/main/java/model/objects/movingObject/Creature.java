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
    @Setter
    protected boolean isVisible = true;
    protected Point size = new Point();
    protected Vector2D position;

    public Creature(Creature creatureA) {
        this.position = new Vector2D(creatureA.position);
        this.size = new Point(creatureA.size);
        this.isVisible = creatureA.isVisible;
    }

    public Creature(Vector2D position, Point size) {
        this.position = position;
        this.size = size;
    }

}
