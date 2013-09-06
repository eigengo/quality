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

##Fields
All immutable fields should be declared as ``final`` (this is to indicate immutability, as well as to ensure that we don't accidentally forget to set the field at the construction time). All access to the fields must be prefixed with ``this.``.

```java
/**
 * Intention for the class
 */
public class Foo {
	private final String name;
	private final int meaningOfLife;

 	/**
 	 * Instantiates Foo, assigning the {@code name} and {@code meaningOfLife} fields.
 	 *
 	 * @param name the name, never {@code null} or empty.
 	 * @param meaningOfLife the meaning of life, typically {@code 42}, never negative.
 	 */
 	public Foo(String name, int meaningOfLife) {
 		if (name == null) throw new IllegalArgumentException("The argument 'name' must not be null.");
 		if (meaningOfLife < 0) throw new IllegalArgumentException("The argument 'meaningOfLife' must not be positive.");

 		this.name = name;
 		this.meaningOfLife = meaningOfLife;
 	}

 	/**
 	 * Performs some work
 	 */
 	void work() {
 		for (int i = 0; i < this.meaningOfLife; i++) {
 			// interesting code here
 		}
 	}

}
```

Notice in particular the fail-fast checks in the constructor, the ``final`` fields and the usage of ``this.`` in the ``work()`` method.

##Naming rules

* Use meaningful names
* Package names are all lowercase
* Constants are in UPPER CASE. Use ``_`` to separate words
* Class starts with an uppercase and each starting word is upper cased. No ``_``.
* The class name of an exception class must end in ``Exception`` or ``Error`` depending on which it descends from.
* Methods, fields, and variables must start with lower case and then follow the same rule as classes.
* Abbreviations are not acronyms, so the above rule does not apply to them. For example, ``getId()`` is fine for "get the identifier".
* Never name a method parameter the same name as a Class field (i.e., if you ever have to use this you're doing something wrong)

#Structure
Interfaces are used when defining a generic set of methods that needs to be implemented. Interfaces are not to be used to define behavior; the implementations of those methods should be allowed to vary as widely as is necessary for the particular concrete implementation. If your Javadoc for a method actually defines a contract for the method (e.g., under condition 1 you must do A, under condition 2 you must do B" etc.) you should be using an abstract class (see below). You should always try to keep the interfaces as succinct as possible, ideally containing exactly one method.

An Abstract class is one which implements some, or all, of the methods of an interface(s) and thus defines a set of behavior that will be common to the concrete implementations of the interface(s) which extend this abstract class. As a corollary, concrete classes which do not extend this abstract class may have different behavioral implementations for those interface methods. The names of these types of classes must start with ``Abstract``.

An base class is an abstract class that does not implement an interface (with the exception of low-level language interfaces like ``java.io.Serializable`` or ``java.lang.Comparable`` and others) but instead directly defines a set of behaviors that apply to every concrete implementation of the class. Such behavioral methods are often, but not always, final methods. The names of these types of should end with ``Support``.

##Scoping
All class fields which are not constants must be declared as private. If you want to allow subclasses access to it but don't have public getter/setter methods then provide protected ones.

If a class is part of the public API for the system you should avoid making methods private scoped unless you're absolutely certain no one should ever extend the class and override the method's behavior. Otherwise, mark the method as protected.

##Use of 'final'
All fields should be marked as final. All method variables which are not explicitly meant to be reassigned must be marked as final. Utility classes (those containing only static methods) must be marked as final. Other classes and methods may be marked as final as appropriate.

We mark method arguments and variables final as a means of documentation. It allows the developer to say "this variable should never be reassigned". If a variable is not marked as such it indicates that the developer is explicitly treating that variable differently.

##Null References
We've had a lot of bugs that result from an unintentional use of a ``null`` reference. This document explains the problems with ``null`` and its meaning within the language. In order to avoid this, avoid returning ``null`` unless ``null`` is a semantically meaningful value. For example, in ``StringSupport.trimOrNull(String)``, ``null`` means that the reference contains no useful content, for a particular definition of useful. In contrast, methods that return collections should not--in vast majority of cases, return ``null``s, but instead return an empty collection.

When accepting arguments in a method, the method should fail-fast when a parameter that is required to be not null actually is ``null``. The typical pattern for this is 

```java
public void work(Foo foo) {
    if (foo == null) throw new IllegalArgumentException("The argument 'foo' must not be null.");

    this.field = foo.getField();
} 
```

Notice the empty line after the ``null`` check, you may wish to add an IntelliJ IDEA template to do this:

```


```

Do not use ``null`` to express the difference between _valid value_ and _not there_.

##Overridable methods
Be very careful not to call overridable methods from the constructor; the overriden method may not see fully constructed object if that is the case. The only methods the can be called from the constructor are ``private`` and ``final`` non-private methods; and all methods in the ``final`` class.

```java
public class Foo {
	
	public Foo() {

		doSetup();		// OK: private
		setup();		// OK: non-overridable

		doInitialize();	// Not OK: overridable
		initialize();	// Not OK: overridable
	}

	private void doSetup() {

	}

	protected final void setup() {

	}

	protected void doInitialize() {

	}

	public void initialize() {

	}

}
```

##Generics
Always use full generic signatures when they are available; when defining your own types, always favour generic signatures over using some common type, in the worst-case scenario ``Object``. Use upper-case letters typically ``A``, ``B`` for generic parameters ``U`` for return types, for example. Keep in mind that Java does not reify types, the generic types are only names or placeholders for the types. If you need to reify the type, you must include the parameter of type ``Class<>``.

```java
<A> A getService(Class<A> clazz) {
	// clazz is the "reified" view of type A
}
```

Be sure to limit the scope of type parameters to only the memeber they apply to; for example, a ``ServiceRegistry`` class that contains entries of varying types should not include generic parameter on the type, but on its methods.

```java
public interface ServiceRegistry {
	
	<A> A getService(Class<A> clazz);

}
```

It is now possible to get the compiler to "infer" composed interface types without the need to define a common interface.

```java
interface Alpha {
	void alpha();
}

interface Beta {
	void beta();
}

interface Gamma {
	void gamma();
}

class Foo {
	<A extends Alpha & Beta & Gamma> void foo(A a) {
		a.alpha();	// OK
		a.beta();	// OK
		a.gamma();	// OK
	}
}

class AllThree implements Alpha, Beta, Gamma {
	// implementations omitted
}

new Foo().foo(new AllThree());	// OK

```

In some cases, this may be preferrable option to defining a common interface that extends the interfaces ``Alpha``, ``Beta`` and ``Gamma``; in the example above, the instance of ``AllThree`` is acceptable value for the parameter ``A``, because it conforms to the type ``A``.

##Utility classes
There is sometimes need to have utility classes that contain only static methods. These utility classes must _never_ hold any state, and it must be impossible to instantiate them. Therefore, make the class ``final`` and give it a ``private`` constructor.

```java
public class Utils {
	private Utils() { }

	public static String dateToString(Date date) { ... }

	public static Date stringToDate(String date) { ... }
}
```