package com.jmmxp.android.reactiontime;

import android.support.v4.app.Fragment;

/**
 * Created by jmmxp on 18/01/17.
 */

public class MenuActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new MenuFragment();
    }
}
