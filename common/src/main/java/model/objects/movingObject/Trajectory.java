package model.objects.movingObject;

import tools.Timer;
import tools.Vector2D;

public class Trajectory {

    private final MoveFunction moveFunction;

    private final Timer endTimer;

    public Trajectory(MoveFunction moveFunction) {
        this.moveFunction = moveFunction;
        endTimer = moveFunction.getEstimationEndTime();
    }

    public Vector2D tickAndGet(long deltaTime) {
        var oldPos = moveFunction.getPosition();
        moveFunction.tick(deltaTime);
        if(endTimer!=null) {
            endTimer.tick(deltaTime);
        }
        return moveFunction.getPosition().getSubbed(oldPos);
    }

    public boolean reached() {
        if(endTimer ==null){
            return moveFunction.reached();
        }
        return endTimer.isReady();
    }

    public double getConstSpeed() {
        if(!moveFunction.isConstantSpeed()){
            throw new RuntimeException("Try to get constant speed from non constant function.");
        }
        return moveFunction.getDerivative();
    }
}
