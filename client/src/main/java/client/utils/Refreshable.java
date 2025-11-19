package client.utils;

import commons.Event;

public interface Refreshable {
    /**
     * Refreshes controller from long polling
     */
    void refresh();
}
