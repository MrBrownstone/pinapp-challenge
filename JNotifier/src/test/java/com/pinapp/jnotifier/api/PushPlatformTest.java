package com.pinapp.jnotifier.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PushPlatformTest {

    @Test
    void enumContainsExpectedValues() {
        assertEquals(PushPlatform.ANDROID, PushPlatform.valueOf("ANDROID"));
        assertEquals(PushPlatform.IOS, PushPlatform.valueOf("IOS"));
    }
}
