package com.shalom.itai.theservantexperience.moods;

import com.shalom.itai.theservantexperience.utils.Constants;

/**
 * Proudly written by Itai on 08/07/2017.
 */

public class MoodFactory {
    public static Mood getMoodStatus(int total, int lastActions) {
        if (total >= 18) {
            return lastActions > 0 ? Excited.getInstance() :Happy.getInstance();
        } else if (total < 18 && total >= 12) {
            return lastActions > 0 ? Optimistic.getInstance() : Fine.getInstance();
        } else if (total < 12 && total >= 7) {
            return lastActions > 0 ? Calm.getInstance() : Board.getInstance();
        } else if (total < 7) {
            return lastActions > 0 ? Angry.getInstance() : Sad.getInstance();
        }

        return null;
    }
}
