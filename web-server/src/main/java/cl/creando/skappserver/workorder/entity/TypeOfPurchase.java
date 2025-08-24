package cl.creando.skappserver.workorder.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum TypeOfPurchase {
    NONE("", ""), // Para manejar el caso de string vacío
    DEL_GIRO("Del Giro", "..."),
    SUPERMERCADO("Supermercado", "..."),
    BIENES_RAISES("Bienes Raíces", "..."),
    ACTIVO_FIJO("Activo Fijo", "..."),
    IVA_USO_COMUN("IVA Uso Común", "..."),
    IVA_NO_RECUPERABLE("IVA No Recuperable", "..."),
    NO_INCLUIR("No Incluir", "...");

    private final String name;
    private final String description;

    TypeOfPurchase(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @JsonCreator
    public static TypeOfPurchase fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return NONE;
        }
        for (TypeOfPurchase type : values()) {
            if (type.name().equalsIgnoreCase(value) || type.name.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    @Override
    public String toString() {
        return name;
    }
}

