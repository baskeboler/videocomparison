package io.ggsoft.testapp.repository;

import io.ggsoft.testapp.domain.VideoEntry;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the VideoEntry entity.
 */
@SuppressWarnings("unused")
public interface VideoEntryRepository extends JpaRepository<VideoEntry,Long> {

}
