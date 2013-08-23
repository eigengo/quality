#Java coding conventions
This guide summarises the authors' experience with modern Java. It starts with description of the
"visual" conventions (whitespaces, braces, line breaks, et cetera). It then adds naming conventions
for variables, parameters, methods, classes, interfaces, enumerations and generic parameters.
Further on, this guide explores contemporary Java programming style (immutability, behaviour instead
of data, composition over inheritance, and other topics of modern Java systems).

#Visual layout
It is important to follow the same visual layout rules. Even though the whitespaces, brace position
and line breaks do not matter as much as the overall design of the software, codebase that follows
consistent layout removes the need to mentally reformat the code. This allows your programmers to
focus on the intent of the code without any intermediate steps.

Let's jump right into the fray and outline a simple Java source file that includes the main aspects of
the code

#####Listing 1. Java source
```java
package org.eigengo.styleguide

import java.util.List

/**
 * Intention and purpose of this class. All its methods are thread-safe.
 * Typical usage is
 * <pre><code>
 * SomeClass<> c = new SomeClass("foo");
 * c.call();
 * c.run();
 * c.getBean();
 * </code></pre>
 */
@Annotation
@AnnotationWithValue("value-for-value property")
@AnnotationWithValues(value = "value", other = false)
public class SomeClass<A extends Integer, B> implements Runnable, Callable<A> {
    private final B bean;

    /**
     * Instantiates the sample, recording the value of the {@code bean}.
     *
     * @param bean the bean to be remembered. Never {@code null}.
     */
    public SampleClass(@Hint final B bean) {
        if (b == null) throw new IllegalArgumentException("The argument 'bean' must not be null.");

        this.b = b;
    }

    @Override
    public void run() {
        final random = new Random()
        for (int i = 0; i < random.nextInt(100); i++) {
            final String prefix;
            if (i % 15 == 0) prefix = "FizzBuzz";
            else if (i % 3 == 0) prefix = "Fizz";
            else if (i % 5 == 0) prefix = "Buzz";
            else prefix = "";

            System.out.println(String.format("%s The number is %d", prefix, i));
        }
    }

    @Override
    public A call() throws Exception {
        return SomeClassSubcomponent.MEANING_OF_LIFE;
    }

    /**
     * Returns the value of the {@code bean} property.
     */
    B getBean() {
        return this.b;
    }

}

/**
 * Special constants to be used in this package
 */
final class SomeClassSubcomponent {
    private final Constants() { }

    /**
     * Contains the boxed meaning of life value
     */
    static Integer MEANING_OF_LIFE = 42
}

```

This source code outlines some aspects of the source code you should be writing. Notice the naming
of types; the fact that the ``public`` members appear first, followed by default members. (That are
followed by ``protected`` and ``private`` members; these are not shown here.) Notice that the
indentation uses four spaces; there are spaces after the control-flow statements (``if``, ``for``);
before the opening brace ``{``. The opening brace is on the same line. The ``return`` keyword does not
take its value in braces.

All accessible members include Javadoc that describes their usage, the accepted values of the
parameters; and include any notes that are not immediately obvious from the type signatures. In the
Javadoc comments, favour plain text wherever possible using HTML tags only when you absolutely need
to assert certain formatting. The notable usage of the HTML tags are code examples, which should
be wrapped in ``<pre><code>``.

Annotations are on the previous line for classes, interfaces and annotations; and just in front of the
parameter in case of parameter annotations. The annotations on classes, interfaces and annotations
may appear in any order, but we prefer the layered effect.

> First you must find... another shrubbery! Then, when you have found the shrubbery, you must place
> it here, beside this shrubbery, only slightly higher so you get a two layer effect with a little
> path running down the middle. ("A path! A path!") Then, you must cut down the mightiest tree in the
> forrest... with... a herring!

Generic parameters are either single upper-case letter, typically ``A``, ``B``, ...; or descriptive
words with first letter in upper-case (``Result``, ``Output`` or such like). You should always be
careful to correctly express the type variance using the upper and lower bounds wherever appropriate.
(We shall explore this later; for now, a reader-like types are usually covariant; writer-like types
are usually contravariant; all others are invariant.)

Before we start exploring the details, be sure to be as strict as possible with member accessibility:
ideally, all members wound be private (though the authors accept that such program would not be
particularly useful). A rule of thumb is to start with low accessibility level, and only increase
it when necessary. You should also satisfy yourself that you are not violating the
[Law of Demeter](http://en.wikipedia.org/wiki/Law_of_Demeter) or the
[Liskov substitution principle](http://en.wikipedia.org/wiki/Liskov_substitution_principle).

##Classes, interfaces, enums and annotations
Classes, interfaces, enums and annotations start with the upper-case first letter. Except annotations,
the newly created class, interface or enumeration must contain at least one member. (What use is, for example,
an interface with no members?) Be sure to use interfaces to define behaviour contract; in other words,
be sure to only include methods in interfaces.

#####Listing 2. Constant interface
```java
/**
 * Contains useful constants
 */
public interface Constants {
    /**
     * The meaning of life
     */
    static int MOL = 42;
}
```

The ``Constants`` interface is an example of the _constant interface_ anti-pattern. Remember that
the access level of interface members is ``public``, so there is no need to repeat the ``public``
keyword.

#####Listing 3. Interface members
```java
/**
 * Provides access to {@code Foo}s.
 */
interface FooSource {

    /**
     * Provides access to an [infinite] stream of the {@code Foo} instances.
     */
    @Blocking
    Iterator<Foo> getFoos();

}
```

Notice the seemingly missing ``public`` keyword on the ``getFoos()`` method; also notice that we have
annotated it with the ``@Blocking`` annotation. (It is our own annotation, it does not exist in the JDK.)
If we wish, we can implement annotation processor to automatically issue warnings or errors when we
call a method with the ``@Blocking`` annotation from non-blocking context.

Moving on, enumeration members should follow the same naming conventions as the enumeration itself: in other words, have the
members of enumeration start with an upper-case letter.


#####Listing 3. Enums
```java
/**
 * Retry mode for HTTP operations
 */
public enum RetryMode {
    /**
     * Do not retry, throw the exception on the first encountered error
     */
    FailOnFirstError,
    /**
     * Retry fixed number of times (5 by default), then fail
     */
    FixedRetryThenFail,
    /**
     * Keep retrying the operation until the JVM exits
     */
    KeepRetrying
}
```

Annotations are the only elements that may be empty, used only as markers. When defining an annotation,
be sure to annotate its targets (using the ``@Target`` annotation) and set its retention policy (by
annotating with the ``@Retention`` annotation). It is useful to include the ``@Documented`` annotation
to indicate to the Javadoc processor that the annotation should be included in the Javadoc for the
target members.

#####Listing 4. Annotations
```java
/**
 * Expresses that a method call blocks the calling thread
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Blocking {
}
```

