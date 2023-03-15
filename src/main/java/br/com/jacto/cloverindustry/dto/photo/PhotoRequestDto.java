package br.com.jacto.cloverindustry.dto.photo;

import br.com.jacto.cloverindustry.model.photo.Photo;

import java.util.UUID;

public class PhotoRequestDto {

    private UUID id;
    private  String filename;
    private String type;
    private byte[] photo;

    public PhotoRequestDto(Photo photo) {
        this.id = photo.getId();
        this.filename = photo.getFilename();
        this.type = photo.getType();
        this.photo = photo.getPhoto();
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
