package model.packageLoader;

import databases.CombienedDrawDataBase;
import drawable.abstracts.DrawableRemoteCreature;
import drawable.concretes.customer.DrawableCustomerCreature;
import drawable.concretes.elevator.DrawableElevatorCreature;
import model.objects.Creature;

import java.util.List;

public class PackageLoader {
    public static void ApplyCustomers(
            List<Creature> customers, List<DrawableCustomerCreature> drawableCustomers, CombienedDrawDataBase settings) {
        applyArrivedData(customers, drawableCustomers);
        customers.forEach(
                creatureA -> {
                    if (drawableCustomers.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        drawableCustomers.add(new DrawableCustomerCreature(creatureA, settings));
                    }
                }
        );
    }

    public static void ApplyElevators(
            List<Creature> elevators, List<DrawableElevatorCreature> drawableElevators, CombienedDrawDataBase settings) {
        applyArrivedData(elevators, drawableElevators);
        // Add
        elevators.forEach(
                creatureA -> {
                    if (drawableElevators.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        drawableElevators.add(
                                new DrawableElevatorCreature(creatureA, settings));
                    }
                }
        );
    }

    private static void applyArrivedData(List<Creature> creaturesThatRemotlyCame,
                                         List<? extends DrawableRemoteCreature> creaturesToApplyData) {
        // erase
        creaturesToApplyData.removeIf(creatureA -> creaturesThatRemotlyCame.stream().noneMatch(
                creatureB -> creatureA.getId() == creatureB.getId()));
        // update
        creaturesToApplyData.forEach(creatureA -> {
                    creatureA.set(
                            creaturesThatRemotlyCame.stream().filter(
                                    creatureB -> creatureA.getId() == creatureB.getId()
                            ).findFirst().get());
                }
        );
    }

}
