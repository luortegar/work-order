package cl.creando.skappserver.common.errorhandler;

import java.util.Date;

public record ErrorMessage(int statusCode, Date timestamp, String message, String description) {
}