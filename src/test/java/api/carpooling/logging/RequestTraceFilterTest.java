package api.carpooling.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.AfterAll;
import org.slf4j.MDC;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyString;

/**
 * Unit tests for {@link RequestTraceFilter}.
 * <p>
 * These tests verify that the filter generates a unique trace ID
 * if none exists, reuses an existing trace ID if provided,
 * and correctly sets the trace ID in the MDC and response header.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("RequestTraceFilter Test")
@Slf4j
class RequestTraceFilterTest {

    /**
     * Filter instance used to trace incoming HTTP requests during tests.
     */
    private static RequestTraceFilter filter;

    /**
     * Displays start message before all tests.
     */
    @BeforeAll
    static void setUpAll() {
        filter = new RequestTraceFilter();
        log.info("Starting RequestTraceFilter tests");
    }

    /**
     * Displays message after all tests.
     */
    @AfterAll
    static void tearDownAll() {
        log.info("Finished RequestTraceFilter tests");
    }

    /**
     * Verifies that the filter generates a new trace ID if none is provided in the header.
     */
    @Test
    @Order(1)
    @DisplayName("Should generate new traceId if header is missing")
    void testGenerateTraceIdIfMissing() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("X-Request-Trace")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        verify(response).setHeader(eq("X-Request-Trace"), anyString());
        assertNull(MDC.get("traceId"), "MDC traceId should be cleared after filter execution");
    }

    /**
     * Verifies that the filter reuses an existing trace ID from the header.
     */
    @Test
    @Order(2)
    @DisplayName("Should reuse existing traceId from header")
    void testReuseExistingTraceId() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        String existingTraceId = "existing-trace-id";
        when(request.getHeader("X-Request-Trace")).thenReturn(existingTraceId);

        filter.doFilterInternal(request, response, chain);

        verify(response).setHeader("X-Request-Trace", existingTraceId);
        assertNull(MDC.get("traceId"), "MDC traceId should be cleared after filter execution");
    }

    /**
     * Verifies that the filter always clears the trace ID from MDC after execution.
     */
    @Test
    @Order(3)
    @DisplayName("Should clear MDC traceId after execution")
    void testMdcCleared() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getHeader("X-Request-Trace")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        assertNull(MDC.get("traceId"), "MDC traceId should be null after filter execution");
    }
}
