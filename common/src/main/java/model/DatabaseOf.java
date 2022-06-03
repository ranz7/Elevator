package model;

import controller.Tickable;
import model.objects.Creature;
import model.objects.CreatureInterface;
import tools.Pair;
import tools.Vector2D;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DatabaseOf<BaseCreatureObject extends CreatureInterface> implements Tickable {
    final private List<DatabaseOf<BaseCreatureObject>> databases = new ArrayList<>();
    final private List<BaseCreatureObject> creaturesOwned = new ArrayList<>();
    final private BaseCreatureObject dataBaseOwner;
    final private Class<? extends BaseCreatureObject>[] possibleTypes;

    public DatabaseOf(BaseCreatureObject dataBaseOwner, Class<? extends BaseCreatureObject>... possibleTypes) {
        this.dataBaseOwner = dataBaseOwner;
        if (possibleTypes.length == 0) {
            throw new RuntimeException("There is no need to create database if you are not going to store objects in it");
        }
        this.possibleTypes = possibleTypes;
    }

    public Stream<Pair<Vector2D, BaseCreatureObject>> stream() {
        return stream(new Vector2D(0, 0));
    }

    public Stream<Pair<Vector2D, BaseCreatureObject>> stream(Vector2D positionOfParent) {
        var onlyOwnerCreature = new ArrayList<Pair<Vector2D, BaseCreatureObject>>();
        onlyOwnerCreature.add(new Pair<>(positionOfParent, dataBaseOwner));
        var absoluteDrawPosition = positionOfParent.add(dataBaseOwner.getPosition());
        return Stream.concat(onlyOwnerCreature.stream(),
                databases.stream()
                        .map(databaseOfCreatures -> databaseOfCreatures.stream(absoluteDrawPosition))
                        .reduce(creaturesOwned.stream().map(creature -> new Pair<>(absoluteDrawPosition, creature))
                                , Stream::concat));
    }

    public <SubType extends BaseCreatureObject> Stream<SubType> streamOf(Class<SubType> creatureToFind) {
        return stream()
                .filter(positionAndCreature -> creatureToFind.isAssignableFrom(positionAndCreature.getSecond().getClass()))
                .map(positionAndCreatureToFind -> (SubType) positionAndCreatureToFind.getSecond());
    }

    public Stream<Pair<Vector2D, BaseCreatureObject>> streamOfPairs(Class<?> creatureToFind) {
        return stream()
                .filter(positionAndCreature -> creatureToFind.isAssignableFrom(positionAndCreature.getSecond().getClass()))
                .map(positionAndCreatureToFind ->
                        new Pair<>(
                                positionAndCreatureToFind.getFirst(),
                                positionAndCreatureToFind.getSecond())
                );
    }

    public void removeIf(Predicate<CreatureInterface> predicate) {
        if (predicate.test(dataBaseOwner)) {
            throw new RuntimeException("Cannot kill owner of database");
        }
        creaturesOwned.removeIf(predicate);
        databases.removeIf(dataBase -> dataBase.dataBaseOwner.isDead());
        databases.forEach(dataBase -> dataBase.removeIf(predicate));
    }

    private void register(DatabaseOf<BaseCreatureObject> dataBaseOf) {
        databases.add(dataBaseOf);
    }

    public void addCreature(BaseCreatureObject creature) {
        if (Arrays.stream(possibleTypes).noneMatch(possibleTypes -> possibleTypes.isAssignableFrom(creature.getClass()))) {
            throw new RuntimeException("Cannot add creature because there is no registrated " + creature.getClass().getName());
        }
        if (creature instanceof Transportable) {
            ((Transportable) creature).setTransport((Transport) dataBaseOwner);
        }

        if (creature instanceof Transport) {
            register(((Transport<BaseCreatureObject>) creature).getLocalDataBase());
        }
        creaturesOwned.add(creature);
    }

    public List<Pair<Integer, BaseCreatureObject>> toIdAndCreaturesList() {
        var list = new ArrayList<Pair<Integer, BaseCreatureObject>>();
        var ownerId = dataBaseOwner.getId();
        list.add(new Pair<>(ownerId, dataBaseOwner));
        creaturesOwned.forEach(creatureOwned -> list.add(new Pair<>(ownerId, creatureOwned)));
        databases.forEach(collection -> list.addAll(collection.toIdAndCreaturesList()));
        return list;
    }

    public long countOf(Class<? extends BaseCreatureObject> classToCount) {
        return streamOf(classToCount).count();
    }

    public Pair<Vector2D, BaseCreatureObject> get(Class<?> creatureClass, Integer creatureId) {
        return streamOfPairs(creatureClass)
                .filter(positionAndCreature -> positionAndCreature.getSecond().getId() == creatureId)
                .findFirst().get();
    }

    public void moveCreatureInto(Integer moveCreatureId, Transport<BaseCreatureObject> whereTransport) {
        Pair<Vector2D, Transportable<BaseCreatureObject>> absolutePositionAndCreature = release(moveCreatureId);
        add(whereTransport, absolutePositionAndCreature);
    }

    private void add(Transport<BaseCreatureObject> whereTransport, Pair<Vector2D, Transportable<BaseCreatureObject>> absolutePositionAndCreature) {
        var parent = get(Creature.class, whereTransport.getId());
        var deltaInParentPositions = absolutePositionAndCreature
                .getFirst().sub(absolutePositionAndCreature.getSecond().getPosition()).sub(parent.getFirst());
        absolutePositionAndCreature.getSecond().applyDelta(deltaInParentPositions);
        whereTransport.add((BaseCreatureObject) absolutePositionAndCreature.getSecond());
    }

    private Pair<Vector2D, Transportable<BaseCreatureObject>> release(Integer idToRelease) {
        var object = get(Creature.class, idToRelease);
        remove(idToRelease);
        return new Pair<>(object.getFirst(), (Transportable<BaseCreatureObject>) object.getSecond());
    }

    private void remove(Integer idToRelease) {
        removeIf(creature -> creature.getId() == idToRelease);
    }

    public Transport<BaseCreatureObject> get(Integer id) {
        return (Transport<BaseCreatureObject>) (stream().filter(creature -> creature.getSecond().getId() == id).findFirst().get().getSecond());
    }

    @Override
    public void tick(double deltaTime) {
        stream().forEach(posAndCreature -> posAndCreature.getSecond().tick(deltaTime));
    }

    public synchronized List<Pair<Vector2D, BaseCreatureObject>> toAbsolutePositionAndObjects() {
        return stream().collect(Collectors.toList());
    }
}
