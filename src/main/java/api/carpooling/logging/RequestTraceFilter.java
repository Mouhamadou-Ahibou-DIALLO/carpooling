package api.carpooling.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter that assigns a unique trace ID to each HTTP request.
 * <p>
 * The trace ID is either reused from the "X-Request-Trace" header
 * or generated if absent. It is added to the MDC for logging
 * and included in the response header.
 */
@Component
@Slf4j
public class RequestTraceFilter extends OncePerRequestFilter {

    /**
     * Name of the HTTP header used to carry the trace ID.
     */
    private static final String TRACE_HEADER = "X-Request-Trace";

    /**
     * Assigns a unique trace ID to each HTTP request and adds it
     * to the response header and logging context (MDC).
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String traceId = request.getHeader(TRACE_HEADER);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
            log.debug("Generated new traceId: {}", traceId);
        } else {
            log.debug("Reusing existing traceId from header: {}", traceId);
        }

        MDC.put("traceId", traceId);
        response.setHeader(TRACE_HEADER, traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("traceId");
        }
    }
}
