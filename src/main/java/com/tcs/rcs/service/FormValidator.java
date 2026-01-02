package com.tcs.rcs.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Validates submission data against schemaJson defined in FormTemplate. Schema
 * format example: { "required": ["customerName","contactEmail"], "fields": {
 * "customerName": { "type":"string", "minLength":3 }, "industry": {
 * "type":"string", "enum":["Finance","Retail","Telecom"] }, "contactEmail": {
 * "type":"string", "pattern":"^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$" } } }
 */
@Component
public class FormValidator {

	private final ObjectMapper mapper = new ObjectMapper();

	public void validate(String schemaJson, String dataJson) {
		try {
			JsonNode schema = mapper.readTree(schemaJson);
			JsonNode data = mapper.readTree(dataJson);

			List<String> errors = new ArrayList<>();

			// 1. Required fields
			if (schema.has("required")) {
				for (JsonNode req : schema.get("required")) {
					String field = req.asText();
					if (!data.has(field) || data.get(field).isNull()) {
						errors.add("Missing required field: " + field);
					}
				}
			}

			// 2. Field rules
			if (schema.has("fields")) {
				Iterator<Map.Entry<String, JsonNode>> fields = schema.get("fields").fields();
				while (fields.hasNext()) {
					Map.Entry<String, JsonNode> entry = fields.next();
					String fieldName = entry.getKey();
					JsonNode rules = entry.getValue();

					if (!data.has(fieldName))
						continue; // skip if not present
					JsonNode value = data.get(fieldName);

					// Type check
					if (rules.has("type")) {
						String type = rules.get("type").asText();
						if (!isTypeValid(type, value)) {
							errors.add("Field '" + fieldName + "' must be of type " + type);
						}
					}

					// Min length
					if (rules.has("minLength") && value.isTextual()) {
						int min = rules.get("minLength").asInt();
						if (value.asText().length() < min) {
							errors.add("Field '" + fieldName + "' must have at least " + min + " characters");
						}
					}

					// Enum
					if (rules.has("enum") && value.isTextual()) {
						boolean match = false;
						for (JsonNode e : rules.get("enum")) {
							if (e.asText().equals(value.asText())) {
								match = true;
								break;
							}
						}
						if (!match) {
							errors.add("Field '" + fieldName + "' must be one of " + rules.get("enum"));
						}
					}

					// Regex pattern
					if (rules.has("pattern") && value.isTextual()) {
						String regex = rules.get("pattern").asText();
						if (!Pattern.matches(regex, value.asText())) {
							errors.add("Field '" + fieldName + "' does not match pattern " + regex);
						}
					}
				}
			}

			if (!errors.isEmpty()) {
				throw new ValidationException(errors);
			}

		} catch (Exception e) {
			throw new ValidationException(List.of("Invalid JSON or schema: " + e.getMessage()));
		}
	}

	private boolean isTypeValid(String type, JsonNode value) {
		return switch (type) {
		case "string" -> value.isTextual();
		case "number" -> value.isNumber();
		case "boolean" -> value.isBoolean();
		case "object" -> value.isObject();
		case "array" -> value.isArray();
		default -> true;
		};
	}

	// Custom exception
	public static class ValidationException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final List<String> errors;

		public ValidationException(List<String> errors) {
			super("Form validation failed: " + errors);
			this.errors = errors;
		}

		public List<String> getErrors() {
			return errors;
		}
	}
}
