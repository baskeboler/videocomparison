package io.ggsoft.testapp.web.rest;

import io.ggsoft.testapp.TestappApp;

import io.ggsoft.testapp.domain.VideoEntry;
import io.ggsoft.testapp.repository.VideoEntryRepository;
import io.ggsoft.testapp.service.VideoEntryService;
import io.ggsoft.testapp.service.dto.VideoEntryDTO;
import io.ggsoft.testapp.service.mapper.VideoEntryMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VideoEntryResource REST controller.
 *
 * @see VideoEntryResource
 */
@RunWith(SpringRunner.class)

@SpringBootTest(classes = TestappApp.class)

public class VideoEntryResourceIntTest {
    private static final String DEFAULT_VIDEO_1 = "AAAAA";
    private static final String UPDATED_VIDEO_1 = "BBBBB";
    private static final String DEFAULT_VIDEO_2 = "AAAAA";
    private static final String UPDATED_VIDEO_2 = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    @Inject
    private VideoEntryRepository videoEntryRepository;

    @Inject
    private VideoEntryMapper videoEntryMapper;

    @Inject
    private VideoEntryService videoEntryService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restVideoEntryMockMvc;

    private VideoEntry videoEntry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VideoEntryResource videoEntryResource = new VideoEntryResource();
        ReflectionTestUtils.setField(videoEntryResource, "videoEntryService", videoEntryService);
        this.restVideoEntryMockMvc = MockMvcBuilders.standaloneSetup(videoEntryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoEntry createEntity(EntityManager em) {
        VideoEntry videoEntry = new VideoEntry()
                .video1(DEFAULT_VIDEO_1)
                .video2(DEFAULT_VIDEO_2)
                .description(DEFAULT_DESCRIPTION)
                .title(DEFAULT_TITLE);
        return videoEntry;
    }

    @Before
    public void initTest() {
        videoEntry = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideoEntry() throws Exception {
        int databaseSizeBeforeCreate = videoEntryRepository.findAll().size();

        // Create the VideoEntry
        VideoEntryDTO videoEntryDTO = videoEntryMapper.videoEntryToVideoEntryDTO(videoEntry);

        restVideoEntryMockMvc.perform(post("/api/video-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(videoEntryDTO)))
                .andExpect(status().isCreated());

        // Validate the VideoEntry in the database
        List<VideoEntry> videoEntries = videoEntryRepository.findAll();
        assertThat(videoEntries).hasSize(databaseSizeBeforeCreate + 1);
        VideoEntry testVideoEntry = videoEntries.get(videoEntries.size() - 1);
        assertThat(testVideoEntry.getVideo1()).isEqualTo(DEFAULT_VIDEO_1);
        assertThat(testVideoEntry.getVideo2()).isEqualTo(DEFAULT_VIDEO_2);
        assertThat(testVideoEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVideoEntry.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void getAllVideoEntries() throws Exception {
        // Initialize the database
        videoEntryRepository.saveAndFlush(videoEntry);

        // Get all the videoEntries
        restVideoEntryMockMvc.perform(get("/api/video-entries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(videoEntry.getId().intValue())))
                .andExpect(jsonPath("$.[*].video1").value(hasItem(DEFAULT_VIDEO_1.toString())))
                .andExpect(jsonPath("$.[*].video2").value(hasItem(DEFAULT_VIDEO_2.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getVideoEntry() throws Exception {
        // Initialize the database
        videoEntryRepository.saveAndFlush(videoEntry);

        // Get the videoEntry
        restVideoEntryMockMvc.perform(get("/api/video-entries/{id}", videoEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(videoEntry.getId().intValue()))
            .andExpect(jsonPath("$.video1").value(DEFAULT_VIDEO_1.toString()))
            .andExpect(jsonPath("$.video2").value(DEFAULT_VIDEO_2.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVideoEntry() throws Exception {
        // Get the videoEntry
        restVideoEntryMockMvc.perform(get("/api/video-entries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideoEntry() throws Exception {
        // Initialize the database
        videoEntryRepository.saveAndFlush(videoEntry);
        int databaseSizeBeforeUpdate = videoEntryRepository.findAll().size();

        // Update the videoEntry
        VideoEntry updatedVideoEntry = videoEntryRepository.findOne(videoEntry.getId());
        updatedVideoEntry
                .video1(UPDATED_VIDEO_1)
                .video2(UPDATED_VIDEO_2)
                .description(UPDATED_DESCRIPTION)
                .title(UPDATED_TITLE);
        VideoEntryDTO videoEntryDTO = videoEntryMapper.videoEntryToVideoEntryDTO(updatedVideoEntry);

        restVideoEntryMockMvc.perform(put("/api/video-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(videoEntryDTO)))
                .andExpect(status().isOk());

        // Validate the VideoEntry in the database
        List<VideoEntry> videoEntries = videoEntryRepository.findAll();
        assertThat(videoEntries).hasSize(databaseSizeBeforeUpdate);
        VideoEntry testVideoEntry = videoEntries.get(videoEntries.size() - 1);
        assertThat(testVideoEntry.getVideo1()).isEqualTo(UPDATED_VIDEO_1);
        assertThat(testVideoEntry.getVideo2()).isEqualTo(UPDATED_VIDEO_2);
        assertThat(testVideoEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVideoEntry.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void deleteVideoEntry() throws Exception {
        // Initialize the database
        videoEntryRepository.saveAndFlush(videoEntry);
        int databaseSizeBeforeDelete = videoEntryRepository.findAll().size();

        // Get the videoEntry
        restVideoEntryMockMvc.perform(delete("/api/video-entries/{id}", videoEntry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<VideoEntry> videoEntries = videoEntryRepository.findAll();
        assertThat(videoEntries).hasSize(databaseSizeBeforeDelete - 1);
    }
}
