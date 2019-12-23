package com.asasan.ordermanagement.queue;

import java.io.IOException;

public interface ChangeStatusQueue {
    void put(ChangeStatusTask task) throws IOException;

    ChangeStatusTask get() throws IOException;
}
