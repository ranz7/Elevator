package tools;

import architecture.tickable.Tickable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Timer implements Tickable {
    private long currentTimeInMillisec;
    private long startTimeInMillisec;

    public Timer(long startTimerTime) {
        restart(startTimerTime);
    }

    @Override
    public void tick(long deltaTime) {
        currentTimeInMillisec -= deltaTime;
    }

    public boolean isReady() {
        return currentTimeInMillisec <= 0;
    }

    public void restart(long startTime) {
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
