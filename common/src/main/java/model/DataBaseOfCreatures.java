package model;

import lombok.RequiredArgsConstructor;
import model.objects.Creature;
import tools.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@RequiredArgsConstructor
public final class DataBaseOfCreatures {
    final private LinkedList<DataBaseOfCreatures> collectionsOfCreatures = new LinkedList<>();
    final private LinkedList<Creature> creaturesOwned = new LinkedList<>();
    final private Creature dataBaseOwner;


    public Stream<Creature> stream() {
        var onlyOwnerCreature = new LinkedList<Creature>();
        onlyOwnerCreature.add(dataBaseOwner);
        return Stream.concat(onlyOwnerCreature.stream(), collectionsOfCreatures.stream()
                .map(DataBaseOfCreatures::stream).reduce(
                        creaturesOwned.stream(), Stream::concat));
    }

    public <T extends Creature> Stream<T> streamOf(Class<T> creatureToFind) {
        return (Stream<T>) stream().filter(creature -> creatureToFind.isAssignableFrom(creature.getClass()));
    }

    public void removeIf(Predicate predicate) {
        creaturesOwned.removeIf(predicate);
        collectionsOfCreatures.forEach(
                dataBaseOfCreatures -> dataBaseOfCreatures.removeIf(predicate));
    }

    public void register(DataBaseOfCreatures dataBaseOfCreatures) {
        collectionsOfCreatures.add(dataBaseOfCreatures);
    }

    public void add(Creature creature) {
        creaturesOwned.add(creature);
    }

    public List<Pair<Long, Creature>> toIdAndCreaturesList() {
        var list = new LinkedList<Pair<Long, Creature>>();
        var ownerId = dataBaseOwner.getId();
        list.add(new Pair<>(ownerId, dataBaseOwner));
        creaturesOwned.forEach(creatureOwned -> list.add(new Pair<>(ownerId, creatureOwned)));
        collectionsOfCreatures.forEach(collection -> list.addAll(collection.toIdAndCreaturesList()));
    }
}
