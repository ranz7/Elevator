package model;

import model.objects.Creature;

import java.util.LinkedList;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CollectionOfCreatures {
    LinkedList<CollectionOfCreatures> collectionsOfCreatures = new LinkedList<>();
    LinkedList<Creature> creaturesOwned = new LinkedList<>();

    final public Stream<Creature> stream() {
        return collectionsOfCreatures.stream()
                .map(CollectionOfCreatures::stream).reduce(
                        creaturesOwned.stream(), Stream::concat);
    }

    final public <T extends Creature> Stream<T> streamOf(Class<T> creatureToFind) {
        return (Stream<T>) stream().filter(creature -> creatureToFind.isAssignableFrom(creature.getClass()));
    }

    public void removeIf(Predicate predicate) {
        creaturesOwned.removeIf(predicate);
        collectionsOfCreatures.forEach(
                collectionOfCreatures -> collectionOfCreatures.removeIf(predicate));
    }

    protected final void register(CollectionOfCreatures collectionOfCreatures) {
        collectionsOfCreatures.add(collectionOfCreatures);
    }

    protected final void add(Creature creature) {
        creaturesOwned.add(creature);
    }
}
