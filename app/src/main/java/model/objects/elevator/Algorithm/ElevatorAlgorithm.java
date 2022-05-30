package model.objects.elevator.Algorithm;

public interface ElevatorAlgorithm {
    int findBestFloor();

    void addCustomer();

    void removeCustomer();

    void addFloorToPick(int floorToPickUp, int currentFloor);

    boolean isAvailable();

    boolean isFree();

    void arrived(int currentFloor);

    void addFloorToThrowOut(int floorEnd, int currentFloor);

    double getTimeToBeHere(int florEnd, int currentFloor);
}
