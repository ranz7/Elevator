package model;

import drawable.abstracts.DrawableCreature;
import tools.Pair;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;

public class CollectionOfDrawableCreatures extends CollectionOfCreatures {
    default List<Pair<Vector2D, DrawableCreature>> getAbsolutePositionOfDrawables() {
        List<Pair<Vector2D, DrawableCreature>> absolutePositionOfDrawables = new LinkedList<>();
        collectionsOfCreatures.forEach(drawableCreature -> {
            if (drawableCreature instanceof CollectionOfDrawableCreatures) {
                absolutePositionOfDrawables.addAll(getAbsolutePositionOfDrawables())
            }
        });
        return absolutePositionOfDrawables;
    }

    default List<Pair<Vector2D, DrawableCreature>> getAbsolutePositionOfDrawables(Vector2D parentPosition) {
        var drawables = new LinkedList<Pair<Vector2D, DrawableCreature>>();
        drawables.add(new Pair<>(parentPosition, this));
        var doubleParentPosition = getPosition().getAdded(parentPosition);
        for (var subDrawable : subDrawableCreatures) {
            drawables.add(new Pair<>(doubleParentPosition, subDrawable));
        }
        return drawables;
    }


    default LinkedList<Pair<Vector2D, DrawableCreature>> getDrawableOjects() {
        var startGamePosition = new Vector2D(0, 0);
        LinkedList<Pair<Vector2D, DrawableCreature>> drawablesAndPositions = new LinkedList<>();
        autoDieObjects.forEach(drawable -> drawablesAndPositions.addAll(drawable.getDrawablesAndAbsoluteDrawPositions(startGamePosition)));
        customers.forEach(drawable -> drawablesAndPositions.addAll(drawable.getDrawablesAndAbsoluteDrawPositions(startGamePosition)));
        elevators.forEach(drawable -> drawablesAndPositions.addAll(drawable.getDrawablesAndAbsoluteDrawPositions(startGamePosition)));
        floors.forEach(drawable -> drawablesAndPositions.addAll(drawable.getDrawablesAndAbsoluteDrawPositions(startGamePosition)));
        return drawablesAndPositions;
    }

    default void register(DrawableCreature drawableCreature) {
        collectionsOfCreatures.add(drawableCreature);
    }

}
