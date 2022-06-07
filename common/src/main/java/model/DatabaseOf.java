package model;

import controller.Tickable;
import model.objects.Creature;
import model.objects.CreatureInterface;
import tools.Pair;
import tools.Trio;
import tools.Vector2D;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DatabaseOf<BaseCreatureObject extends CreatureInterface> implements Tickable {
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

    public Stream<Trio<Integer, Vector2D, BaseCreatureObject>> streamTrio() {
        return streamTrio(dataBaseOwner.getId(), new Vector2D(0, 0));
    }
    public Stream<Trio<Integer, Vector2D, BaseCreatureObject>> streamTrioOfOwned() {
        var onlyOwnerCreature = new ArrayList<Trio<Integer, Vector2D, BaseCreatureObject>>();
        onlyOwnerCreature.add(new Trio<>(dataBaseOwner.getId(), new Vector2D(0, 0), dataBaseOwner));

        var newAbsoluteDrawPosition = dataBaseOwner.getPosition();
        var newIdOfParent = dataBaseOwner.getId();

        return creaturesOwned.stream().map(creatureObject -> {{
                var onlyOwnedCreature = new ArrayList<Trio<Integer, Vector2D, BaseCreatureObject>>();
                onlyOwnedCreature.add(new Trio<>(newIdOfParent, newAbsoluteDrawPosition, creatureObject));
                return onlyOwnedCreature.stream();
            }
        }).reduce(onlyOwnerCreature.stream(), Stream::concat);
    }

    public Stream<Trio<Integer, Vector2D, BaseCreatureObject>> streamTrio(Integer idOfParent, Vector2D absoluteDrawPosition) {
        var onlyOwnerCreature = new ArrayList<Trio<Integer, Vector2D, BaseCreatureObject>>();
        onlyOwnerCreature.add(new Trio<>(idOfParent, absoluteDrawPosition, dataBaseOwner));

        var newAbsoluteDrawPosition = absoluteDrawPosition.add(dataBaseOwner.getPosition());
        var newIdOfParent = dataBaseOwner.getId();

        return creaturesOwned.stream().map(creatureObject -> {
            if (creatureObject instanceof Transport) {
                return ((Transport<BaseCreatureObject>) creatureObject).getLocalDataBase().streamTrio(
                        newIdOfParent,
                        newAbsoluteDrawPosition
                );
            } else {
                var onlyOwnedCreature = new ArrayList<Trio<Integer, Vector2D, BaseCreatureObject>>();
                onlyOwnedCreature.add(new Trio<>(newIdOfParent, newAbsoluteDrawPosition, creatureObject));
                return onlyOwnedCreature.stream();
            }
        }).reduce(onlyOwnerCreature.stream(), Stream::concat);
    }

    public <SubType extends BaseCreatureObject> Stream<SubType> streamOf(Class<SubType> creatureToFind) {
        return streamTrio().map(Trio::getThird)
                .filter(creature -> creatureToFind.isAssignableFrom(creature.getClass()))
                .map(creature -> (SubType) creature);
    }

    public <SubType extends BaseCreatureObject> Stream<SubType> streamOfOnlyOwned(Class<SubType> creatureToFind) {
        return streamTrioOfOwned().map(Trio::getThird)
                .filter(creature -> creatureToFind.isAssignableFrom(creature.getClass()))
                .map(creature -> (SubType) creature);
    }

    public <SubType extends BaseCreatureObject> Stream<Pair<Integer, SubType>> streamWithParentsOf(Class<SubType> creatureToFind) {
        return streamTrio().map(Trio::getFirstAndThird)
                .filter(creature -> creatureToFind.isAssignableFrom(creature.getSecond().getClass()))
                .map(creature -> new Pair<>(creature.getFirst(), (SubType) creature.getSecond()));
    }

    public void removeIf(Predicate<CreatureInterface> predicate) {
        if (predicate.test(dataBaseOwner)) {
            throw new RuntimeException("Cannot kill owner of database");
        }
        creaturesOwned.removeIf(predicate);
    }

    public void addCreature(BaseCreatureObject creature) {
        if (Arrays.stream(possibleTypes).noneMatch(possibleTypes -> possibleTypes.isAssignableFrom(creature.getClass()))) {
            throw new RuntimeException("Cannot add creature because there is no registrated " + creature.getClass().getName());
        }
        if (creature instanceof Transportable) {
            ((Transportable) creature).setTransport((Transport) dataBaseOwner);
        }
        creaturesOwned.add(creature);
    }

    public long countOf(Class<? extends BaseCreatureObject> classToCount) {
        return streamOf(classToCount).count();
    }

    public void moveCreatureInto(Integer moveCreatureId, Transport<BaseCreatureObject> whereTransport) {
        add(whereTransport, release(moveCreatureId));
    }

    private Pair<Vector2D, Transportable<BaseCreatureObject>> release(Integer idToRelease) {
        var object = get(idToRelease);
        removeIf(creature -> creature.getId() == idToRelease);
        return new Pair<>(object.getFirst(), (Transportable<BaseCreatureObject>) object.getSecond());
    }

    private void add(Transport<BaseCreatureObject> whereTransport,
                     Pair<Vector2D, Transportable<BaseCreatureObject>> absolutePositionAndTransportable) {
        var parent = get(whereTransport.getId());
        var deltaInParentPositions = absolutePositionAndTransportable
                .getFirst().sub(absolutePositionAndTransportable.getSecond().getPosition()).sub(parent.getFirst());
        absolutePositionAndTransportable.getSecond().applyDelta(deltaInParentPositions);
        whereTransport.add((BaseCreatureObject) absolutePositionAndTransportable.getSecond());
    }


    public Pair<Vector2D, BaseCreatureObject> get(Integer creatureId) {
        return streamTrio().map(Trio::getSecondAndThird)
                .filter(positionAndCreature -> positionAndCreature.getSecond().getId() == creatureId)
                .findFirst().get();
    }

    public <ToReturnType> Pair<Vector2D, ToReturnType> get
            (Integer creatureId, Class<ToReturnType> creatureType) {
        return streamTrio().map(Trio::getSecondAndThird)
                .filter(positionAndCreature -> positionAndCreature.getSecond().getId() == creatureId)
                .findFirst().get().cast(Vector2D.class, creatureType);
    }

    @Override
    public void tick(double deltaTime) {
        streamTrio().filter(trio -> trio.getThird() != dataBaseOwner).
                forEach(trio -> trio.getThird().tick(deltaTime));
    }

}
