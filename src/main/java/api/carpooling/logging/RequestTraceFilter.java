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
 * RequestTraceFilter
 * - Associate a unique identifier to each HTTP request.
 * - Allows you to track the complete journey of a request in the logs.
 */
@Component
@Slf4j
public class RequestTraceFilter extends OncePerRequestFilter {

    private static final String TRACE_HEADER = "X-Request-Trace";

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
