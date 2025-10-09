package api.carpooling.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.slf4j.MDC;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RequestTraceFilter}.
 * <p>
 * These tests verify that the filter generates a unique trace ID
 * if none exists, reuses an existing trace ID if provided,
 * and correctly sets the trace ID in the MDC and response header.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("RequestTraceFilter Test")
class RequestTraceFilterTest {

    private static RequestTraceFilter filter;

    @BeforeAll
    static void setUpAll() {
        filter = new RequestTraceFilter();
        System.out.println("Starting RequestTraceFilter tests");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finished RequestTraceFilter tests");
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
