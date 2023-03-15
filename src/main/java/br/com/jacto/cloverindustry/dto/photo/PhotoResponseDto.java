package br.com.jacto.cloverindustry.dto.photo;

import java.time.LocalDateTime;
import java.util.UUID;

public class PhotoResponseDto {

    private UUID id;
    private  String filename;
    private String type;
    private LocalDateTime created_at;

    public PhotoResponseDto(UUID id, String filename, String type, LocalDateTime created_at) {
        this.id = id;
        this.filename = filename;
        this.type = type;
        this.created_at = created_at;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
