package com.sdsu.cs646.shameetha.draganddraw;

import android.support.v4.app.Fragment;

public class DragAndDrawActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new DragAndDrawFragment();
    }
}
