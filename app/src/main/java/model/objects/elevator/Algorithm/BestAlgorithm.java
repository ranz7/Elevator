package model.objects.elevator.Algorithm;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
public class BestAlgorithm implements ElevatorAlgorithm {
    private static final int unexistingFloor = 1111;
    private boolean isGoUp = false;

    private int customersCount = 0;
    private int bookedCount = 0;
    private final long timeToSpendOfFloor;
    private final long maxCustomersCount;
    private final Function<Integer,Double> getRawTimeToGetOnFloor;
    // how many people are waiting
    private final TreeMap<Integer, Integer> PICK_UP_TOP = new TreeMap<>();
    private final TreeMap<Integer, Integer> PICK_UP_BOTTOM = new TreeMap<>(Comparator.reverseOrder());
    private final TreeSet<Integer> THROW_OUT_TOP = new TreeSet<>();
    private final TreeSet<Integer> THROW_OUT_BOTTOM = new TreeSet<>();


    @Override
    public int findBestFloor() {
        int floorToGetUp = unexistingFloor;
        if (!PICK_UP_TOP.isEmpty()) {
            floorToGetUp = Math.min(PICK_UP_TOP.firstKey(), floorToGetUp);
        }
        if (!THROW_OUT_TOP.isEmpty()) {
            floorToGetUp = Math.min(THROW_OUT_TOP.first(), floorToGetUp);
        }
        if (isGoUp && floorToGetUp != unexistingFloor) {
            return floorToGetUp;
        }
        int floorToGetDown = -unexistingFloor;
        if (!PICK_UP_BOTTOM.isEmpty()) {
            floorToGetDown = Math.max(PICK_UP_BOTTOM.firstKey(), floorToGetDown);
        }
        if (!THROW_OUT_BOTTOM.isEmpty()) {
            floorToGetDown = Math.max(THROW_OUT_BOTTOM.first(), floorToGetDown);
        }
        if (!isGoUp && floorToGetDown != -unexistingFloor) {
            return floorToGetDown;
        }
        if (!isGoUp && floorToGetUp != unexistingFloor) {
            isGoUp = true;
            return floorToGetUp;
        }
        if (isGoUp && floorToGetDown != -unexistingFloor) {
            isGoUp = false;
            return floorToGetDown;
        }
        return -1;
    }

    @Override
    public void addCustomer() {
        customersCount++;
    }

    @Override
    public void removeCustomer() {
        customersCount--;
    }

    @Override
    public void addFloorToPick(int floorToPick, int currentFloor) {
        bookedCount++;
        TreeMap<Integer, Integer> mapToWorkWith;
        if (floorToPick > currentFloor) {
            mapToWorkWith = PICK_UP_TOP;
        } else {
            mapToWorkWith = PICK_UP_BOTTOM;
        }
        if (mapToWorkWith.containsKey(floorToPick)) {
            mapToWorkWith.put(floorToPick, mapToWorkWith.get(floorToPick) + 1);
        } else {
            mapToWorkWith.put(floorToPick, 1);
        }
        findBestFloor();
    }

    @Override
    public boolean isAvailable() {
        return bookedCount + customersCount <= maxCustomersCount;
    }

    @Override
    public boolean isFree() {
        return customersCount <= maxCustomersCount;
    }

    @Override
    public void arrived(int currentFloor) {

        Map<Integer, Integer> mapToWorkWith = isGoUp ? PICK_UP_TOP : PICK_UP_BOTTOM;
        if (mapToWorkWith.containsKey(currentFloor)) {
            bookedCount -= mapToWorkWith.get(currentFloor);
            mapToWorkWith.remove(currentFloor);
        }

        SortedSet<Integer> setToWorkWith = isGoUp ? THROW_OUT_TOP : THROW_OUT_BOTTOM;
        setToWorkWith.remove(currentFloor);
    }

    @Override
    public void addFloorToThrowOut(int floorEnd, int currentFloor) {
        SortedSet<Integer> setToWorkWith = floorEnd > currentFloor ? THROW_OUT_TOP : THROW_OUT_BOTTOM;
        setToWorkWith.add(floorEnd);
        //update go up go down
        findBestFloor();
    }

    @Override
    public double getTimeToBeHere(int florEnd, int currentFloor) {
        if (getBooking() == 0) {
            return getRawTimeToGetOnFloor.apply(florEnd);
        }
        TreeSet<Integer> allTop = new TreeSet<>();
        TreeSet<Integer> allBot = new TreeSet<>();

        PICK_UP_TOP.forEach((key, value) -> allTop.add(key));
        allTop.addAll(THROW_OUT_TOP.stream().toList());
        PICK_UP_BOTTOM.forEach((key, value) -> allBot.add(key));
        allBot.addAll(THROW_OUT_BOTTOM.stream().toList());

        allTop.add(florEnd);
        allBot.add(florEnd);
        Integer[] allTopSorted = (allTop).toArray(new Integer[allTop.size()]);
        Integer[] allBotSorted = (allBot).toArray(new Integer[allBot.size()]);

        // 3 5 6 7  9  on the way
        // 1 2 3 4  not on the way
        double penalty;
        if (isGoUp) {
            if (currentFloor<florEnd) {
                penalty = Arrays.binarySearch(allTopSorted, florEnd) * timeToSpendOfFloor;
            } else {
                penalty = getRawTimeToGetOnFloor.apply(allTopSorted[allTopSorted.length - 1]) * 2;
                penalty += (allBotSorted.length - 1 - (Arrays.binarySearch(allBotSorted, florEnd))
                        + allTopSorted.length - 1)
                        * timeToSpendOfFloor;
            }
        } else {
            if (currentFloor>florEnd ) {
                penalty = (allBotSorted.length - Arrays.binarySearch(allBotSorted, florEnd) - 1)
                        * timeToSpendOfFloor;
            } else {
                penalty = getRawTimeToGetOnFloor.apply(allBotSorted[allBotSorted.length - 1]) * 2;
                penalty += ((Arrays.binarySearch(allTopSorted, florEnd) + allBotSorted.length - 1)
                        * timeToSpendOfFloor);
            }
        }
        return getRawTimeToGetOnFloor.apply(florEnd) + penalty;
    }


    private int getBooking() {
        return bookedCount + customersCount;
    }

}

