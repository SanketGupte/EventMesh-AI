package com.eventmesh.common.constants;

public class KafkaTopics {
    //Incoming event from external systems
    public static final String RAW_EVENTS = "event.raw.ingest";

    //Events after initial processing
    public static final String PROCESSED_EVENTS = "event.processed";

    //Routed events (dynamic topics)
    public static final String ROUTE_PAYMENT = "event.route.payment";
    public static final String ROUTE_DEFAULT = "event.route.default";

    //LOGS (for future use)
    public static final String LOGS_RAW = "logs.raw";

    //AI Insights (for future use)
    public static final  String AI_INSIGHTS = "ai.insights";
}
