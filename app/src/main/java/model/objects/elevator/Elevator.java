package model.objects.elevator;

import databases.configs.ElevatorSystemConfig;
import lombok.Getter;
import lombok.Setter;
import model.objects.movingObject.MovingCreature;
import model.objects.movingObject.trajectory.Trajectory;
import tools.Timer;
import tools.Vector2D;

import java.util.*;

/*
 * Elevator is storing all requests under and behind his way, the algorithm finds the closest floor by
 * calculating distance to came from floor A to floor B and all intermediate floors.
 */
public class Elevator extends MovingCreature {
    public static final int UNEXISTING_FLOOR = 999;
    public final Timer TIMER = new Timer();

    private final long TIME_TO_STOP_ON_FLOOR;
    private final int MAX_HUMAN_CAPACITY;

    @Setter
    private double wallSize;

    @Getter
    @Setter
    private ElevatorState state;
    private boolean isGoUp = false;

    private int currentCustomersCount = 0;
    private int currentBookedCount = 0;

    // how many people are waiting
    private final TreeMap<Integer, Integer> PICK_UP_TOP = new TreeMap<>();
    private final TreeMap<Integer, Integer> PICK_UP_BOTTOM = new TreeMap<>(Comparator.reverseOrder());
    private final TreeSet<Integer> THROW_OUT_TOP = new TreeSet<>();
    private final TreeSet<Integer> THROW_OUT_BOTTOM = new TreeSet<>();


    public Elevator(Vector2D position, ElevatorSystemConfig settings) {
        super(position, settings.elevatorSize,
                Trajectory.StayOnPlaceWithDefaultConstantSpeed(settings.elevatorSpeed));
        this.TIME_TO_STOP_ON_FLOOR = settings.elevatorOpenCloseTime * 2 +
                settings.elevatorAfterCloseAfkTime + settings.elevatorWaitAsOpenedTime;
        this.MAX_HUMAN_CAPACITY = settings.elevatorMaxHumanCapacity;
        this.state = ElevatorState.WAIT;
    }

    @Override
    public void tick(double deltaTime) {
        super.tick(deltaTime);
        TIMER.tick(deltaTime);
    }

    public int findBestFloor() {
        int floorToGetUp = UNEXISTING_FLOOR;
        if (!PICK_UP_TOP.isEmpty()) {
            floorToGetUp = Math.min(PICK_UP_TOP.firstKey(), floorToGetUp);
        }
        if (!THROW_OUT_TOP.isEmpty()) {
            floorToGetUp = Math.min(THROW_OUT_TOP.first(), floorToGetUp);
        }
        if (isGoUp && floorToGetUp != UNEXISTING_FLOOR) {
            return floorToGetUp;
        }
        int floorToGetDown = -UNEXISTING_FLOOR;
        if (!PICK_UP_BOTTOM.isEmpty()) {
            floorToGetDown = Math.max(PICK_UP_BOTTOM.firstKey(), floorToGetDown);
        }
        if (!THROW_OUT_BOTTOM.isEmpty()) {
            floorToGetDown = Math.max(THROW_OUT_BOTTOM.first(), floorToGetDown);
        }
        if (!isGoUp && floorToGetDown != -UNEXISTING_FLOOR) {
            return floorToGetDown;
        }
        if (!isGoUp && floorToGetUp != UNEXISTING_FLOOR) {
            isGoUp = true;
            return floorToGetUp;
        }
        if (isGoUp && floorToGetDown != -UNEXISTING_FLOOR) {
            isGoUp = false;
            return floorToGetDown;
        }
        return UNEXISTING_FLOOR;
    }

    public boolean isAvailable() {
        return currentBookedCount + currentCustomersCount <= this.MAX_HUMAN_CAPACITY;
    }

    public boolean isFree() {
        return currentCustomersCount <= this.MAX_HUMAN_CAPACITY;
    }


    public int getCurrentFloor() {
        return (int) Math.round(position.y / wallSize);
    }

    public boolean isOpened() {
        return ElevatorState.OPENED.equals(state);
    }

    public void addFloorToPickUp(int toAddFloor) {
        currentBookedCount++;
        TreeMap<Integer, Integer> mapToWorkWith;
        if (toAddFloor > getCurrentFloor()) {
            mapToWorkWith = PICK_UP_TOP;
        } else {
            mapToWorkWith = PICK_UP_BOTTOM;
        }
        if (mapToWorkWith.containsKey(toAddFloor)) {
            mapToWorkWith.put(toAddFloor, mapToWorkWith.get(toAddFloor) + 1);
        } else {
            mapToWorkWith.put(toAddFloor, 1);
        }
    }

    public void put() {
        currentCustomersCount++;
    }

    public void remove() {
        currentCustomersCount--;
    }

    public void setFloorDestination(int bestFloor) {
        setMoveTrajectory(Trajectory.WithOldSpeedToTheDestination(new Vector2D(position.x, bestFloor * wallSize)));
    }

    public void arrived() {
        Map<Integer, Integer> mapToWorkWith = isGoUp ? PICK_UP_TOP : PICK_UP_BOTTOM;
        int currentFloor = getCurrentFloor();
        if (mapToWorkWith.containsKey(currentFloor)) {
            currentBookedCount -= mapToWorkWith.get(currentFloor);
            mapToWorkWith.remove(getCurrentFloor());
        }

        SortedSet<Integer> setToWorkWith = isGoUp ? THROW_OUT_TOP : THROW_OUT_BOTTOM;
        setToWorkWith.remove(currentFloor);
    }

    public boolean isInMotion() {
        return state.equals(ElevatorState.IN_MOTION);
    }

    public void addFloorToThrowOut(int floorEnd) {
        SortedSet<Integer> setToWorkWith = floorEnd > getCurrentFloor() ? THROW_OUT_TOP : THROW_OUT_BOTTOM;
        setToWorkWith.add(floorEnd);
    }

    public double getTimeToBeHere(int requestFloor) {
        if (getBooking() == 0) {
            return getTimeToGetTo(requestFloor);
        }
        TreeSet<Integer> allTop = new TreeSet<>();
        TreeSet<Integer> allBot = new TreeSet<>();

        PICK_UP_TOP.forEach((key, value) -> allTop.add(key));
        allTop.addAll(THROW_OUT_TOP.stream().toList());
        PICK_UP_BOTTOM.forEach((key, value) -> allBot.add(key));
        allBot.addAll(THROW_OUT_BOTTOM.stream().toList());

        allTop.add(requestFloor);
        allBot.add(requestFloor);
        Integer[] allTopSorted = (allTop).toArray(new Integer[allTop.size()]);
        Integer[] allBotSorted = (allBot).toArray(new Integer[allBot.size()]);

        // 3 5 6 7  9  on the way
        // 1 2 3 4  not on the way
        double penalty;
        if (isGoUp) {
            if (position.y < getPositionForFloor(requestFloor)) {
                penalty = Arrays.binarySearch(allTopSorted, requestFloor) * TIME_TO_STOP_ON_FLOOR;
            } else {
                penalty = getTimeToGetTo(allTopSorted[allTopSorted.length - 1]) * 2;
                penalty += (allBotSorted.length - 1 - (Arrays.binarySearch(allBotSorted, requestFloor))
                        + allTopSorted.length - 1)
                        * TIME_TO_STOP_ON_FLOOR;
            }
        } else {
            if (position.y > getPositionForFloor(requestFloor)) {
                penalty = (allBotSorted.length - Arrays.binarySearch(allBotSorted, requestFloor) - 1)
                        * TIME_TO_STOP_ON_FLOOR;
            } else {
                penalty = getTimeToGetTo(allBotSorted[allBotSorted.length - 1]) * 2;
                penalty += ((Arrays.binarySearch(allTopSorted, requestFloor) + allBotSorted.length - 1)
                        * TIME_TO_STOP_ON_FLOOR);
            }
        }
        return getTimeToGetTo(requestFloor) + penalty;
    }

    private double getPositionForFloor(int requestFloor) {
        return requestFloor * wallSize;
    }

    private double getTimeToGetTo(int requestFloor) {
        return Math.abs(getPositionForFloor(requestFloor) - position.y) / getConstSpeed();
    }

    public int getBooking() {
        return currentBookedCount + currentCustomersCount;
    }

    public void reset() {
        PICK_UP_TOP.clear();
        THROW_OUT_TOP.clear();
        PICK_UP_BOTTOM.clear();
        THROW_OUT_BOTTOM.clear();
        TIMER.restart(0);
        // >???
    }
}
