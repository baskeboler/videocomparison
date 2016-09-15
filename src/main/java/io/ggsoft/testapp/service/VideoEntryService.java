package io.ggsoft.testapp.service;

import io.ggsoft.testapp.config.JHipsterProperties;
import io.ggsoft.testapp.domain.VideoEntry;
import io.ggsoft.testapp.repository.VideoEntryRepository;
import io.ggsoft.testapp.service.dto.VideoEntryDTO;
import io.ggsoft.testapp.service.mapper.VideoEntryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing VideoEntry.
 */
@Service
@Transactional
public class VideoEntryService {

    private final Logger log = LoggerFactory.getLogger(VideoEntryService.class);

    @Inject
    private VideoEntryRepository videoEntryRepository;

    @Inject
    private VideoEntryMapper videoEntryMapper;

    @Inject
    private JHipsterProperties jHipsterProperties;

    @PostConstruct
    void init() {
        log.debug("Starting {}", this.getClass().getSimpleName());
        log.debug("Video storage folder: {}", jHipsterProperties.getStorage().getVideosPath());
    }

    /**
     * Save a videoEntry.
     *
     * @param videoEntryDTO the entity to save
     * @return the persisted entity
     */
    public VideoEntryDTO save(VideoEntryDTO videoEntryDTO) {
        log.debug("Request to save VideoEntry : {}", videoEntryDTO);
        VideoEntry videoEntry = videoEntryMapper.videoEntryDTOToVideoEntry(videoEntryDTO);
        videoEntry = videoEntryRepository.save(videoEntry);
        VideoEntryDTO result = videoEntryMapper.videoEntryToVideoEntryDTO(videoEntry);
        return result;
    }

    /**
     *  Get all the videoEntries.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VideoEntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VideoEntries");
        Page<VideoEntry> result = videoEntryRepository.findAll(pageable);
        return result.map(videoEntry -> videoEntryMapper.videoEntryToVideoEntryDTO(videoEntry));
    }

    /**
     *  Get one videoEntry by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public VideoEntryDTO findOne(Long id) {
        log.debug("Request to get VideoEntry : {}", id);
        VideoEntry videoEntry = videoEntryRepository.findOne(id);
        VideoEntryDTO videoEntryDTO = videoEntryMapper.videoEntryToVideoEntryDTO(videoEntry);
        return videoEntryDTO;
    }

    /**
     *  Delete the  videoEntry by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete VideoEntry : {}", id);
        videoEntryRepository.delete(id);
    }
}
