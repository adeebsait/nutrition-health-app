package com.example.finalyear.common.data.firebasepojo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserActivityData {
    private int goal = 9000;
    private int calories = 0;

    private String goalText = "";


    private String baselineActivity = "";


    public UserActivityData() {
    }

    public UserActivityData(String goalText, String baselineActivity) {
        this.goalText = goalText;
        this.baselineActivity = baselineActivity;
    }



    public int getGoal() {
        return goal;
    }

    public int getCalories() {
        return calories;
    }

    public String getGoalText() {
        return goalText;
    }

    public String getBaselineActivity() {
        return baselineActivity;
    }


    @Override
    public String toString() {
        return "UserActivityData{" +
                "goal=" + goal +
                ", calories=" + calories +
                ", goalText='" + goalText + '\'' +
                ", baselineActivity='" + baselineActivity + '\'' +
                '}';
    }
}
