package cl.creando.skappserver.common.response;

import cl.creando.skappserver.common.entity.common.File;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FileResponse {
    private UUID fileId;
    private String fileName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String link;


    public FileResponse(File file) {
        this.fileId = file.getFileId();
        this.fileName = file.getFileName();
    }

    public FileResponse(File file, String host) {
        this.fileId = file.getFileId();
        this.fileName = file.getFileName();
        this.link = host + "/public/v1/files/" + file.getFileId().toString() + "/download";
    }
}
