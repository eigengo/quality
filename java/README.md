This module hosts the Java code quality tool configuration files used across Eigengo projects. Tools currently
in use include checkstyle, findbugs and pmd.

This module can be made available on the classpath of maven plugins as a maven build extension as well as directly as a plugin dependency. See the examples below:


## Example configuration as a maven build extension
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.eigengo.monitor</groupId>
    <artifactId>parent</artifactId>
    <packaging>pom</packaging>
    <version>0.1-SNAPSHOT</version>
    <name>Monitor</name>

    ...

    <build>
        <extensions>
            <extension>
                <groupId>org.eigengo.quality</groupId>
                <artifactId>java</artifactId>
                <version>0.1-SNAPSHOT</version>
            </extension>
        </extensions>
    </build>

    ...

</project>
```


## Example configuration as a maven plugin dependency
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.eigengo.monitor</groupId>
    <artifactId>parent</artifactId>
    <packaging>pom</packaging>
    <version>0.1-SNAPSHOT</version>
    <name>Monitor</name>

    ...

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-checkstyle-plugin</artifactId>
                <version>${maven.checkstyle.plugin}</version>
                <dependencies>
                    <dependency>
                        <groupId>org.eigengo.quality</groupId>
                        <artifactId>java</artifactId>
                        <version>0.1-SNAPSHOT</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <id>default-checkstyle-checks</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>check</goal>
                        </goals>
                        <configuration>
                            <configLocation>checkstyle.xml</configLocation>
                            <consoleOutput>true</consoleOutput>
                            <logViolationsToConsole>true</logViolationsToConsole>
                        </configuration>
                    </execution>
                </executions>
                <!-- Use -Dcheckstyle.skipExec=true to bypass -->
            </plugin>
        </plugins>
    </build>

    ...

</project>
```