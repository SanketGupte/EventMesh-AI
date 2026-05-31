package com.eventmesh.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Map;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.eventmesh")
public class IngestionServiceApplication {

	public static void main(String[] args) {
		// Ensure the JVM/postgres timezone is valid and normalized regardless of environment
		// Some environments may set deprecated zone names (e.g. "Asia/Calcutta") which PostgreSQL
		// rejects. Normalize common legacy zones to their canonical names before any DB or
		// driver initialization.
		String configured = System.getProperty("user.timezone");
		String normalized = normalizeTimeZone(configured);
		if (normalized == null || normalized.isBlank()) {
			normalized = "UTC"; // safe default
		}
		// Apply to JVM and system properties before Spring (and drivers) initialize
		System.setProperty("user.timezone", normalized);
		TimeZone.setDefault(TimeZone.getTimeZone(normalized));
		System.out.println("Timezone normalized: from='" + configured + "' to='" + normalized + "'");

		SpringApplication.run(IngestionServiceApplication.class, args);
	}

	private static String normalizeTimeZone(String tz) {
		if (tz == null) return null;
		String t = tz.trim();
		// Map known deprecated or non-standard names to canonical IANA names accepted by Postgres
		Map<String, String> legacy = Map.of(
				"Asia/Calcutta", "Asia/Kolkata",
				"India/Calcutta", "Asia/Kolkata",
				"IST", "Asia/Kolkata"
		);
		String mapped = legacy.get(t);
		if (mapped != null) return mapped;
		// If the ID is already valid for Java, return it. Otherwise fall back to null so caller can decide.
		try {
			// Use TimeZone to validate: if it returns GMT or unknown, treat as invalid
			TimeZone tzObj = TimeZone.getTimeZone(t);
			String id = tzObj.getID();
			// If getTimeZone returned "GMT" for an unknown id (and original wasn't GMT), treat as invalid
			if ("GMT".equals(id) && !t.equalsIgnoreCase("GMT") && !t.startsWith("GMT")) {
				return null;
			}
			return id;
		} catch (Exception e) {
			return null;
		}
	}

}
