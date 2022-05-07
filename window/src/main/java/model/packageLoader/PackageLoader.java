package model.packageLoader;

import configs.CanvasSettings.MainSettings;
import drawable.drawableAbstract.DrawableRemoteCreature;
import drawable.drawableObjectsConcrete.customer.DrawableCustomer;
import drawable.drawableObjectsConcrete.elevator.DrawableElevator;
import model.objects.Creature;

import java.util.LinkedList;
import java.util.List;

public class PackageLoader {
    public static void ApplyCustomers(
            List<Creature> customers, List<DrawableCustomer> drawableCustomers, MainSettings settings) {
        applyArrivedData(customers, drawableCustomers);
        customers.forEach(
                creatureA -> {
                    if (drawableCustomers.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        drawableCustomers.add(new DrawableCustomer(creatureA, settings));
                    }
                }
        );
    }

    public static void ApplyElevators(
            List<Creature> elevators, List<DrawableElevator> drawableElevators, MainSettings settings) {
        applyArrivedData(elevators, drawableElevators);
        // Add
        elevators.forEach(
                creatureA -> {
                    if (drawableElevators.stream()
                            .noneMatch(creatureB -> creatureA.getId() == creatureB.getId())) {
                        drawableElevators.add(
                                new DrawableElevator(creatureA, settings));
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
