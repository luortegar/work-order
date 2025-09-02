package cl.creando.skappserver.workorder.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum WorkOrderStatus {
    DRAFT("Draft"),
    SENT("Sent");

    private final String displayName;

    WorkOrderStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonCreator
    public static WorkOrderStatus fromString(String value) {
        if (value == null) return null;
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value) ||
                        status.displayName.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid WorkOrderStatus: " + value));
    }
}
