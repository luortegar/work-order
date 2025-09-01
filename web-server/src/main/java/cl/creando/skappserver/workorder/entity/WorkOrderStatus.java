package cl.creando.skappserver.workorder.entity;

import lombok.Getter;

@Getter
public enum WorkOrderStatus {
    DRAFT("Draft"),
    SENT("Sent");

    private final String displayName;

    WorkOrderStatus(String displayName) {
        this.displayName = displayName;
    }

}
