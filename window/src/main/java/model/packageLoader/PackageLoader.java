package model.packageLoader;

import configs.tools.CombienedDrawDataBase;
import drawable.abstracts.DrawableCreatureRemote;
import drawable.concretes.customer.DrawableCreatureCustomer;
import drawable.concretes.elevator.DrawableCreatureElevator;
import model.objects.Creature;

import java.util.List;

public class PackageLoader {
    public static void ApplyCustomers(
            List<Creature> customers, List<DrawableCreatureCustomer> drawableCustomers, CombienedDrawDataBase settings) {
        applyArrivedData(customers, drawableCustomers);
        customers.forEach(
                creatureA -> {
                    if (drawableCustomers.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        drawableCustomers.add(new DrawableCreatureCustomer(creatureA, settings));
                    }
                }
        );
    }

    public static void ApplyElevators(
            List<Creature> elevators, List<DrawableCreatureElevator> drawableElevators, CombienedDrawDataBase settings) {
        applyArrivedData(elevators, drawableElevators);
        // Add
        elevators.forEach(
                creatureA -> {
                    if (drawableElevators.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        drawableElevators.add(
                                new DrawableCreatureElevator(creatureA, settings));
                    }
                }
        );
    }

    private static void applyArrivedData(List<Creature> creaturesThatRemotlyCame,
                                         List<? extends DrawableCreatureRemote> creaturesToApplyData) {
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
