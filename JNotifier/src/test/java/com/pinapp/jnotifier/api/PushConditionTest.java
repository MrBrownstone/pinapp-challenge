package com.pinapp.jnotifier.api;

import com.pinapp.jnotifier.error.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PushConditionTest {

    @Test
    void createsConditionWithValue() {
        PushCondition condition = new PushCondition("topic == 'news'");
        assertEquals("topic == 'news'", condition.value());
        assertEquals(PushTarget.TargetType.CONDITION, condition.type());
    }

    @Test
    void rejectsBlankCondition() {
        ValidationException ex = assertThrows(ValidationException.class, () -> new PushCondition(" "));
        assertEquals("push condition blank", ex.getMessage());
    }
}
