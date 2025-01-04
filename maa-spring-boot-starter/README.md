maa spring boot starter
===

1. add dependency

```xml

<dependency>
    <groupId>io.github.hanhuoer</groupId>
    <artifactId>maa-spring-boot-starter</artifactId>
    <version>2.1.3</version>
</dependency>
```

2. enable maa

```java
package io.github.hanhuoer;

import io.github.hanhuoer.spring.boot.EnableMaaFramework;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMaaFramework
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(MaaApplication.class, args);
    }

}
```



