//package com.eventmesh.integration;
//
//import com.eventmesh.common.dto.EventDTO;
//import com.eventmesh.routing.controller.RoutingController;
//import com.eventmesh.routing.entity.RoutingRuleEntity;
//import com.eventmesh.routing.repository.EventLogRepository;
//import com.eventmesh.routing.repository.RoutingRuleRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Integration Tests for REST API Endpoints.
// * Tests complete REST API functionality through MockMvc.
// */
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class RestApiIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private RoutingRuleRepository routingRuleRepository;
//
//    @Autowired
//    private EventLogRepository eventLogRepository;
//
//    @BeforeEach
//    void setUp() {
//        eventLogRepository.deleteAll();
//        routingRuleRepository.deleteAll();
//    }
//
//    @Test
//    void testCreateRoutingRuleEndpoint() throws Exception {
//        // Arrange
//        String routingRuleJson = "{\n" +
//            "  \"eventType\": \"ORDER_CREATED\",\n" +
//            "  \"destination\": \"warehouse-service\",\n" +
//            "  \"active\": true,\n" +
//            "  \"priority\": 1\n" +
//            "}";
//
//        // Act & Assert
//        mockMvc.perform(post("/api/v1/routing-rules")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(routingRuleJson))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"));
//    }
//
//    @Test
//    void testGetAllRoutingRulesEndpoint() throws Exception {
//        // Arrange: Create test routing rule
//        RoutingRuleEntity rule = RoutingRuleEntity.builder()
//            .eventType("ORDER_CREATED")
//            .destination("warehouse-service")
//            .active(true)
//            .priority(1)
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//        routingRuleRepository.save(rule);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/routing-rules"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"))
//            .andExpect(jsonPath("$.data", hasSize(1)));
//    }
//
//    @Test
//    void testDeleteRoutingRuleEndpoint() throws Exception {
//        // Arrange: Create test routing rule
//        RoutingRuleEntity rule = RoutingRuleEntity.builder()
//            .eventType("ORDER_CREATED")
//            .destination("warehouse-service")
//            .active(true)
//            .priority(1)
//            .createdAt(LocalDateTime.now())
//            .updatedAt(LocalDateTime.now())
//            .build();
//        RoutingRuleEntity savedRule = routingRuleRepository.save(rule);
//
//        // Act & Assert
//        mockMvc.perform(delete("/api/v1/routing-rules/" + savedRule.getId()))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"));
//    }
//
//    @Test
//    void testGetEventLogsEndpoint() throws Exception {
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/event-logs"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"));
//    }
//
//    @Test
//    void testGetEventLogsByStatusEndpoint() throws Exception {
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/event-logs/status/PENDING"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"));
//    }
//
//    @Test
//    void testGetEventLogsByEventTypeEndpoint() throws Exception {
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/event-logs/event/ORDER_CREATED"))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.status").value("SUCCESS"));
//    }
//}
//
