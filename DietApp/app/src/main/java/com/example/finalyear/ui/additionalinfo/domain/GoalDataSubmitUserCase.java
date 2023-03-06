package com.example.finalyear.ui.additionalinfo.domain;

import com.example.finalyear.common.data.firebasepojo.UserActivityData;
import com.example.finalyear.ui.authentication.data.DietFireAuthRepository;
import com.example.finalyear.ui.authentication.data.DietFireDbRepository;
import com.example.finalyear.ui.authentication.domain.LoginHelpers;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;

public class GoalDataSubmitUserCase {
    private final DietFireDbRepository dietFireDbRepository;
    private final DietFireAuthRepository dietFireAuthRepository;
    private final LoginHelpers loginHelpers;

    @Inject
    public GoalDataSubmitUserCase(DietFireAuthRepository dietFireAuthRepository,
                                  DietFireDbRepository dietFireDbRepository, LoginHelpers loginHelpers) {
        this.dietFireAuthRepository = dietFireAuthRepository;
        this.dietFireDbRepository = dietFireDbRepository;
        this.loginHelpers = loginHelpers;
    }

    public @NonNull Single<String> invoke(String goal, String baselineActivity) {
        String uid = dietFireAuthRepository.getUid();
        return Single.create(emitter -> {
            UserActivityData userActivity = new UserActivityData(goal,baselineActivity);
            dietFireDbRepository.addGoalInformation(userActivity, uid, (error, ref) -> {
                if (error != null) {
                    emitter.onError(error.toException());
                    return;
                }
                emitter.onSuccess(uid);
            });
        });
    }
}
