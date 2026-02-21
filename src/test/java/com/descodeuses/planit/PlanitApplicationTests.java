package com.descodeuses.planit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.descodeuses.planit.service.LogDocumentService;

@SpringBootTest

@ActiveProfiles("test")
class PlanitApplicationTests {

	 @org.springframework.test.context.bean.override.mockito.MockitoBean
    private LogDocumentService logDocumentService;

    @Test
    void contextLoads() {
    }

}