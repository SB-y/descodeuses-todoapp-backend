package com.descodeuses.planit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.descodeuses.planit.service.LogDocumentService;

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

	    @MockBean
    private LogDocumentService logDocumentService;

    @Test
    void contextLoads() {
    }


}
