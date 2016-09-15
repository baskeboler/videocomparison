package io.ggsoft.testapp.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the VideoEntry entity.
 */
public class VideoEntryDTO implements Serializable {

    private Long id;

    private String video1;

    private String video2;

    private String description;

    private String title;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getVideo1() {
        return video1;
    }

    public void setVideo1(String video1) {
        this.video1 = video1;
    }
    public String getVideo2() {
        return video2;
    }

    public void setVideo2(String video2) {
        this.video2 = video2;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VideoEntryDTO videoEntryDTO = (VideoEntryDTO) o;

        if ( ! Objects.equals(id, videoEntryDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VideoEntryDTO{" +
            "id=" + id +
            ", video1='" + video1 + "'" +
            ", video2='" + video2 + "'" +
            ", description='" + description + "'" +
            ", title='" + title + "'" +
            '}';
    }
}
