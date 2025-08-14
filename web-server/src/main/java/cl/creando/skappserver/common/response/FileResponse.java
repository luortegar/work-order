package cl.creando.skappserver.common.response;

import cl.creando.skappserver.common.entity.common.File;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FileResponse {
    private UUID fileId;
    private String fileName;

    public FileResponse(File file) {
        this.fileId = file.getFileId();
        this.fileName = file.getFileName();
    }
}
