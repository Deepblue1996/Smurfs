package com.prohua.smurfs.event;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * StartBrotherResultEvent 事件总线数据回调
 * Created by Deep on 2017/4/21 0021.
 */

public class StartBrotherResultEvent {
    public SupportFragment targetFragment;
    public int requestCode;

    public StartBrotherResultEvent(SupportFragment targetFragment, int requestCode) {
        this.targetFragment = targetFragment;
        this.requestCode = requestCode;
    }
}
