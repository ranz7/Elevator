package model;

import controller.Tickable;
import lombok.RequiredArgsConstructor;
import model.objects.Creature;
import model.objects.CreatureInterface;
import tools.Pair;
import tools.Vector2D;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public final class DatabaseOf<T extends CreatureInterface> implements Tickable {
    final private List<DatabaseOf<T>> databases = new LinkedList<>();
    final private List<T> creaturesOwned = new LinkedList<>();
    final private T dataBaseOwner;

    public Stream<Pair<Vector2D, T>> stream() {
        return stream(new Vector2D(0, 0));
    }

    public Stream<Pair<Vector2D, T>> stream(Vector2D positionOfParent) {
        var onlyOwnerCreature = new LinkedList<Pair<Vector2D, T>>();
        onlyOwnerCreature.add(new Pair<>(positionOfParent, dataBaseOwner));
        var absoluteDrawPosition = positionOfParent.add(dataBaseOwner.getPosition());
        return Stream.concat(onlyOwnerCreature.stream(),
                databases.stream()
                        .map(databaseOfCreatures -> databaseOfCreatures.stream(absoluteDrawPosition))
                        .reduce(creaturesOwned.stream().map(creature -> new Pair<>(absoluteDrawPosition, creature))
                                , Stream::concat));

    }

    public <X extends T> Stream<X> streamOf(Class<X> creatureToFind) {
        return stream()
                .filter(positionAndCreature -> creatureToFind.isAssignableFrom(positionAndCreature.getSecond().getClass()))
                .map(positionAndCreatureToFind -> (X) positionAndCreatureToFind.getSecond());
    }

    public Stream<Pair<Vector2D, T>> streamOfPairs(Class<?> creatureToFind) {
        return stream()
                .filter(positionAndCreature -> creatureToFind.isAssignableFrom(positionAndCreature.getSecond().getClass()))
                .map(positionAndCreatureToFind ->
                        new Pair<>(
                                positionAndCreatureToFind.getFirst(),
                                positionAndCreatureToFind.getSecond())
                );
    }

    public void removeIf(Predicate<T> predicate) {
        creaturesOwned.removeIf(predicate);
        databases.forEach(
                dataBaseOfCreatures -> dataBaseOfCreatures.removeIf(predicate));
    }

    private void register(DatabaseOf<T> dataBaseOf) {
        databases.add(dataBaseOf);
    }

    public void add(T creature) {
        if (creature instanceof Transportable) {
            ((Transportable) creature).setTransport((Transport) dataBaseOwner);
        }
        if (creature instanceof Transport) {
            register(((Transport) creature).getLocalDataBase());
        }
        creaturesOwned.add(creature);
    }

    public List<Pair<Integer, T>> toIdAndCreaturesList() {
        var list = new LinkedList<Pair<Integer, T>>();
        var ownerId = dataBaseOwner.getId();
        list.add(new Pair<>(ownerId, dataBaseOwner));
        creaturesOwned.forEach(creatureOwned -> list.add(new Pair<>(ownerId, creatureOwned)));
        databases.forEach(collection -> list.addAll(collection.toIdAndCreaturesList()));
        return list;
    }

    public long countOf(Class<? extends T> classToCount) {
        return streamOf(classToCount).count();
    }

    public Pair<Vector2D, T> get(Class<?> creatureClass, Integer creatureId) {
        return streamOfPairs(creatureClass)
                .filter(positionAndCreature -> positionAndCreature.getSecond().getId() == creatureId)
                .findFirst().get();
    }

    public void moveCreatureInto(Integer moveCreatureId, Transport whereTransport) {
        Pair<Vector2D, T> absolutePositionAndCreature = release(moveCreatureId);
        add(whereTransport, absolutePositionAndCreature);
    }

    private void add(Transport whereTransport, Pair<Vector2D, T> absolutePositionAndCreature) {
        var parent = get(Creature.class, whereTransport.getId());
        var deltaInParentPositions = absolutePositionAndCreature
                .getFirst().sub(absolutePositionAndCreature.getSecond().getPosition()).sub(parent.getFirst());
        absolutePositionAndCreature.getSecond().applyDelta(deltaInParentPositions);
        whereTransport.getLocalDataBase().add(absolutePositionAndCreature.getSecond());
    }

    private Pair<Vector2D, T> release(Integer idToRelease) {
        var object = get(Creature.class, idToRelease);
        remove(idToRelease);
        return object;
    }

    private void remove(Integer idToRelease) {
        removeIf(creature -> creature.getId() == idToRelease);
    }

    public Transport get(Integer id) {
        return (Transport) (stream().filter(creature -> creature.getSecond().getId() == id).findFirst().get().getSecond());
    }

    @Override
    public void tick(double deltaTime) {
        stream().filter(posAndCreature -> posAndCreature.getSecond() != dataBaseOwner)
                .forEach(posAndCreature -> posAndCreature.getSecond().tick(deltaTime));
    }

    public List<Pair<Vector2D, T>> toAbsolutePositionAndObjects() {
        return stream().collect(Collectors.toList());

    }
}
