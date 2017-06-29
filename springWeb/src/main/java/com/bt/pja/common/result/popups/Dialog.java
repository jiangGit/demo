package com.bt.pja.common.result.popups;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjiapeng on 2015/6/26 0026.
 */
public class Dialog extends Popups {
    private String title;
    private List<Button> buttons;

    public Dialog(String title, String content) {
        super(content);
        this.title = title;
        super.type = 2;
        buttons = new ArrayList<>();
    }

    public Dialog(String title, String content, List<Button> buttons) {
        super(content);
        this.title = title;
        this.buttons = buttons;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public Dialog addButtons(Button button) {
        buttons.add(button);
        return this;
    }

    public static class Button {
        public static final Button BUTTON_CLOSE = new Button("好的", "close");
        private String name;
        private boolean keep;
        private String action;

        public Button(String name, String action, boolean keep) {
            this.name = name;
            this.action = action;
            this.keep = keep;
        }

        public Button(String name, String action) {
            this(name, action, false);
        }

        public boolean isKeep() {
            return keep;
        }

        public void setKeep(boolean keep) {
            this.keep = keep;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }
}
