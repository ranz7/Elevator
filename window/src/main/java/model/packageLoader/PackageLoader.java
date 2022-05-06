package model.packageLoader;

import configs.CanvasSettings.MainSettings;
import drawable.drawableObjectsConcrete.customer.DrawableCustomer;
import drawable.drawableObjectsConcrete.elevator.DrawableElevator;
import model.objects.movingObject.Creature;

import java.util.LinkedList;
import java.util.List;

public class PackageLoader {
    public static void ApplyCustomers(
            List<Creature> customers, LinkedList<DrawableCustomer> drawableCustomers, MainSettings settings) {
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
            List<Creature> elevators, LinkedList<DrawableElevator> drawableElevators, MainSettings settings) {
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

    private static void applyArrivedData(List<Creature> creatures_came, LinkedList<? extends Creature> creatures_to_apply) {
        // erase
        creatures_to_apply.removeIf(
                creatureA -> creatures_came.stream().noneMatch(
                        creatureB -> creatureA.getId() == creatureB.getId()));
        // update
        creatures_to_apply.forEach(
                creatureA -> {
                    creatureA.set(creatures_came.stream().filter(
                            creatureB -> creatureA.getId() == creatureB.getId()
                    ).findFirst().get());
                }
        );
    }

}
