package sudokuSolver.server.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Cors filter allowing cross-domain requests
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NoCorsFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(NoCorsFilter.class);

    public NoCorsFilter() {
        LOG.debug("NoCorsFilter was successfully loaded");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, x-auth-token");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if (!(request.getMethod().equalsIgnoreCase("OPTIONS"))) {
            try {
                chain.doFilter(req, res);
            } catch (Exception ex) {
                LOG.error(ex.getMessage());
            }
        } else { // handle pre-flight - https://developer.mozilla.org/en-US/docs/Glossary/Preflight_request
            response.setHeader("Access-Control-Allowed-Methods", "POST, GET, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "authorization, content-type,x-auth-token, " +
                    "access-control-request-headers, access-control-request-method, accept, origin, authorization, x-requested-with");

            response.setStatus(HttpServletResponse.SC_OK);
        }

    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}