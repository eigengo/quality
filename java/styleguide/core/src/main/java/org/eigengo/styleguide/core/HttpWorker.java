package org.eigengo.styleguide.core;

import akka.dispatch.Futures;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import scala.concurrent.Future;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Performs non-blocking HTTP operations.
 */
public class HttpWorker {
    private final String userAgent;

    /**
     * Instantiates the worker, specifying the userAgent for the HTTP operations in the
     * {@link #sendReceive(com.sun.deploy.net.HttpRequest)}.
     *
     * @param userAgent the value for the {@code user-agent} HTTP header. Never {@code null} or empty.
     */
    public HttpWorker(final String userAgent) {
        if (userAgent == null) throw new IllegalArgumentException("The 'userAgent' argument must not be null.");
        if (userAgent.isEmpty()) throw new IllegalArgumentException("The 'userAgent' argument must not be empty.");

        this.userAgent = userAgent;
    }

    /**
     * Begin peforming the HTTP {@code request}.
     *
     * Note that the returned future will be failed if the preconditions fail; this method
     * will not throw synchronous exceptions.
     *
     * @param request the request to be performed. Never {@code null}.
     * @return Future of the {@code HttpResponse}.
     */
    public Future<HttpResponse> sendReceive(final HttpRequest request) {
        if (request == null)
            return Futures.failed(
                    new IllegalArgumentException("The 'request' argument must not be null."));

        return Futures.failed(new RuntimeException("Not implemented."));
    }

}

class X{
    public enum RetryMode {
        FailOnFirstError,
        FixedRetryThenFail,
        KeepRetrying
    }

    void x() {
        for (int i = 0; i < 100; i++) {
            final String prefix;
            if (i % 15 == 0) prefix = "FizzBuzz";
            else if (i % 3 == 0) prefix = "Fizz";
            else if (i % 5 == 0) prefix = "Buzz";
            else prefix = "";

            System.out.println(String.format("%s The number is %d", prefix, i));
        }

    }
}