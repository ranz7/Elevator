package common.tools;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Timer {
    @Getter
    private long currentMiliSeconds;
    private long startMiliSeconds;

    public Timer(long startMiliSeconds) {
        restart(startMiliSeconds);
    }

    public void tick(long deltaTime) {
        currentMiliSeconds -= deltaTime;
    }


    public boolean isReady() {
        return currentMiliSeconds <= 0;
    }

    public void restart(long miliSeconds) {
        currentMiliSeconds = miliSeconds;
        startMiliSeconds = miliSeconds;
    }

    public void restart() {
        currentMiliSeconds = startMiliSeconds;
    }

    public double getPercent() {
        if (currentMiliSeconds <= 0) {
            return 0;
        }
        return currentMiliSeconds * 1. / startMiliSeconds;
    }
}
