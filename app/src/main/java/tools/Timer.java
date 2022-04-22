package tools;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Timer {
    @Getter
    private long currentTimer;
    private long counterTime;

    public void tick(long deltaTime) {
        currentTimer -= deltaTime;
    }


    public boolean isReady() {
        return currentTimer <= 0;
    }

    public void restart(long timeToCount) {
        currentTimer = timeToCount;
        counterTime = timeToCount;
    }

    public double getPercent() {
        if (currentTimer <= 0) {
            return 0;
        }
        return currentTimer * 1. / counterTime;
    }
}
