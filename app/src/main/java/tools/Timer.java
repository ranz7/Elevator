package tools;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Timer {
    @Getter
    private long currentTime;
    private long startTime;

    public void tick(long deltaTime) {
        currentTime -= deltaTime;
    }

    public boolean isReady() {
        return currentTime <= 0;
    }

    public void restart(long timeToCount) {
        startTime = currentTime = timeToCount;
    }

    public double getPercent() {
        if (currentTime <= 0) {
            return 0;
        }
        return currentTime * 1. / startTime;
    }
}