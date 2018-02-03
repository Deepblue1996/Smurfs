package com.prohua.smurfs.listener;

import android.view.KeyEvent;
import android.widget.TextView;

/**
 * Created by Deep on 2017/4/28 0028.
 */

public interface CircleEditActionListener {
    boolean onEditorAction(TextView v, int actionId, KeyEvent event);
}
