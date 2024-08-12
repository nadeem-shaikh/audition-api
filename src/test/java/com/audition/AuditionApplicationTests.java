package com.audition;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test class for AuditionApplication.
 * This class contains tests to verify the Spring context loads correctly
 * and the main method behaves as expected.
 *
 * @author Nadeem Shaikh
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuditionApplicationTests {

    /**
     * Tests that the Spring application context loads successfully.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void contextLoads() {
    }

    /**
     * Tests that the main method calls SpringApplication.run.
     */
    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void mainCallsSpringApplicationRun() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            final String[] args = {};
            AuditionApplication.main(args);

            mocked.verify(() ->
                SpringApplication.run(AuditionApplication.class, args),
                Mockito.times(1)
            );
        }
    }

}