package cl.creando.skappserver.common.entity.common;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public enum EntityStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;

    EntityStatus(final String value) {
        this.value = value;
    }

    public static Optional<EntityStatus> getByKeyOrValue(String str) {
        return Arrays.stream(values()).filter(e -> (e.name().equalsIgnoreCase(str) || e.getValue().equalsIgnoreCase(str))).findFirst();
    }

    public static List<EntityStatus> getAll() {
        return Arrays.asList(values());
    }

}
