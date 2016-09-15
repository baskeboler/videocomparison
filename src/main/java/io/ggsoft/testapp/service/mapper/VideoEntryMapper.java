package io.ggsoft.testapp.service.mapper;

import io.ggsoft.testapp.domain.*;
import io.ggsoft.testapp.service.dto.VideoEntryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity VideoEntry and its DTO VideoEntryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VideoEntryMapper {

    VideoEntryDTO videoEntryToVideoEntryDTO(VideoEntry videoEntry);

    List<VideoEntryDTO> videoEntriesToVideoEntryDTOs(List<VideoEntry> videoEntries);

    VideoEntry videoEntryDTOToVideoEntry(VideoEntryDTO videoEntryDTO);

    List<VideoEntry> videoEntryDTOsToVideoEntries(List<VideoEntryDTO> videoEntryDTOs);
}
