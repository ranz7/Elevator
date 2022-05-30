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
        return currentTimeInMillisec <= 0;
    }

    public void restart(double startTime) {
        currentTimeInMillisec = startTime;
        startTimeInMillisec = startTime;
    }

    public void restart() {
        currentTimeInMillisec = startTimeInMillisec;
    }

    public double getPercent() {
        if (currentTimeInMillisec <= 0) {
            return 0;
        }
        return currentTimeInMillisec * 1. / startTimeInMillisec;
    }
}
