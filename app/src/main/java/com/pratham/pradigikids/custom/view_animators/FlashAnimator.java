package com.pratham.pradigikids.custom.view_animators;

import android.animation.ObjectAnimator;
import android.view.View;

public class FlashAnimator extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 1, 0, 1, 0, 1)
        );
    }
}
