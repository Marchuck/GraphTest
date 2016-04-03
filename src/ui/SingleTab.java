package ui;

import javax.swing.*;

/**
 * @author  lukasz
 * @since 24.03.16.
 */
public  class SingleTab {
    public JComponent jComponent;
    public String tabTitle;

    public SingleTab() {
    }

    public SingleTab(String tabTitle, JComponent jComponent) {
        this.jComponent = jComponent;
        this.tabTitle = tabTitle;
    }
}
