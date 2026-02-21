package com.descodeuses.planit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.descodeuses.planit.entity.ActionEntity;

class ActionEntityTest {

    @Test
    void shouldMarkTaskAsCompleted() {

        // GIVEN
        ActionEntity action = new ActionEntity();

        // WHEN
        action.setCompleted(true);

        // THEN
        assertTrue(action.isCompleted());
    }

    @Test
    void shouldBeFalseByDefault() {

        // GIVEN
        ActionEntity action = new ActionEntity();

        // THEN
        assertFalse(action.isCompleted());
    }
}
