package com.bt.pja.common.result.popups;

import java.util.List;

/**
 * Created by chenjiapeng on 2015/9/8 0008.
 */
public class UpgradeDailog extends Dialog {
    public UpgradeDailog(String title, String content) {
        super(title, content);
        type = 3;
    }

    public UpgradeDailog(String title, String content, List<Button> buttons) {
        super(title, content, buttons);
        type = 3;
    }
}
