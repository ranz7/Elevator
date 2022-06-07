package tools;

import controller.Tickable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Timer implements Tickable {
    private double currentTimeInMillisec;
    private double startTimeInMillisec;

    public Timer(double startTimerTime) {
        restart(startTimerTime);
    }

    @Override
    public void tick(double deltaTime) {
        currentTimeInMillisec -= deltaTime;
    }

    public boolean isReady() {
        if (isOff) {
            return false;
        }
        return currentTimeInMillisec <= 0;
    }

    public void restart(double startTime) {
        isOff = false;
        currentTimeInMillisec = startTime;
        startTimeInMillisec = startTime;
    }

    public void restart() {
        isOff = false;
        currentTimeInMillisec = startTimeInMillisec;
    }

    public double getPercent() {
        if (currentTimeInMillisec <= 0) {
            return 0;
        }
        return currentTimeInMillisec * 1. / startTimeInMillisec;
    }

    private boolean isOff = true;

    public void turnOff() {
        isOff = true;
    }

    public boolean isOff() {
        return isOff;
    }
}
