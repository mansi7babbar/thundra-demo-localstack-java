package io.thundra.demo.localstack.integration;

import io.thundra.demo.localstack.LocalStackTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class UserAppLocalStackTest extends LocalStackTest {
    @Test
    public void testCreateNewRequest() {
        assertThat(true);
    }
}
