package cl.creando.skappserver.workorder.entity;

import lombok.Getter;

@Getter
public enum TypeOfPurchase {
    DEL_GIRO("Del Giro", "Significa que el documento respalda compras relacionadas con la actividad que declaraste ante el Servicio de Impuestos Internos."),
    SUPERMERCADO("Supermercado", "Corresponde a compras efectuadas en supermercados o negocios similares, necesarias para producir la renta del contribuyente."),
    BIENES_RAISES("Bienes Raíces", "Identifica las compras que si bien no son parte de la actividad que desarrollas, son necesarias para poder realizar dichas actividades y corresponden principalmente a infraestructura física."),
    ACTIVO_FIJO("Activo Fijo", "Identifica las compras que si bien no son parte de la actividad que desarrollas, son necesarias para poder realizar dichas actividades y corresponden principalmente a infraestructura física."),
    IVA_USO_COMUN("IVA Uso Común", "Identifica las compras que no es posible identificar si se destinarán a ventas afectas o exentas de IVA. En este caso, debes aplicar un factor de proporcionalidad, que se calcula de acuerdo a la relación de las ventas afectas sobre el total. Al informar este tipo de compra, puedes también informar si para el mismo documento corresponde la categoría de Activo Fijo, Bienes Raíces o Supermercados."),
    IVA_NO_RECUPERABLE("IVA No Recuperable", "Son aquellas compras en las que no tienes derecho a utilizar el crédito, por ejemplo, por encontrarse fuera de plazo."),
    NO_INCLUIR("No Incluir", "Corresponde a los documentos que respaldan compras que no son necesarias para el desarrollo de tu actividad económica, y que, por lo tanto, no se consideran como crédito de IVA, ni como gasto para el Impuesto a la Renta.");

    private final String name;
    private final String description;

    TypeOfPurchase(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }

}
