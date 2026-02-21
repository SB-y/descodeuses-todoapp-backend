package com.descodeuses.planit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    properties = {
        "spring.data.mongodb.uri=",
        "spring.autoconfigure.exclude=" +
        "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration," +
        "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration"
    }
)

@ActiveProfiles("test")
class PlanitApplicationTests {

    @Test
    void contextLoads() {
    }


}
