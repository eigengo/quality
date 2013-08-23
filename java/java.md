#Java coding conventions
This guide summarises the authors' experience with modern Java. It starts with description of the "visual" conventions (whitespaces, braces, line breaks, et cetera). It then adds naming conventions for variables, parameters, methods, classes, interfaces, enumerations and generic parameters. Further on, this guide explores contemporary Java programming style (immutability, behaviour instead of data, composition over inheritance, and other topics of modern Java systems).

#Visual layout
It is important to follow the same visual layout rules. Even though the whitespaces, brace position and line breaks do not matter as much as the overall design of the software, codebase that follows consistent layout removes the need to mentally reformat the code. This allows your programmers to focus on the intent of the code without any intermediate steps.

Let's jump right into the fray and create a simple ``HttpWorker`` class. 

#####Listing 1. Java source
```java
package org.eigengo.styleguide

/**
 * Performs non-blocking HTTP operations.
 */
public class HttpWorker {
	private final String userAgent;

	/**
	 * Instantiates the worker, specifying the userAgent for the HTTP operations in the
	 * {@link #}
	 */
	public HttpWorker(final String userAgent) {
		if (userAgent == null) throw new IllegalArgumentException("The 'userAgent' argument must not be null.");
		if (userAgent.isEmpty) throw new IllegalArgumentException("The 'userAgent' argument must not be empty.");

		this.userAgent = userAgent;
	}
	
	public Future<HttpResponse> sendReceive(final HttpRequest request) {
		if (request == null) 
			return Futures.unsuccessful(
				new IllegalArgumentException("The 'request' argument must not be null."));

		return Futures.unsuccessful(new RuntimeException("Not implemented."));
	}

}
```
