package com.asasan.ordermanagement.app.util;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Streams {
    public static <T> Stream<T> of(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
