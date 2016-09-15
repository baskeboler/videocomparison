package io.ggsoft.testapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A VideoEntry.
 */
@Entity
@Table(name = "video_entry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VideoEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "video_1")
    private String video1;

    @Column(name = "video_2")
    private String video2;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
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

    public VideoEntry video1(String video1) {
        this.video1 = video1;
        return this;
    }

    public void setVideo1(String video1) {
        this.video1 = video1;
    }

    public String getVideo2() {
        return video2;
    }

    public VideoEntry video2(String video2) {
        this.video2 = video2;
        return this;
    }

    public void setVideo2(String video2) {
        this.video2 = video2;
    }

    public String getDescription() {
        return description;
    }

    public VideoEntry description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public VideoEntry title(String title) {
        this.title = title;
        return this;
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
        VideoEntry videoEntry = (VideoEntry) o;
        if(videoEntry.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, videoEntry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VideoEntry{" +
            "id=" + id +
            ", video1='" + video1 + "'" +
            ", video2='" + video2 + "'" +
            ", description='" + description + "'" +
            ", title='" + title + "'" +
            '}';
    }
}
