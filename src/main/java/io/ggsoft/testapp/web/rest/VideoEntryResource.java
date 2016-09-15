package io.ggsoft.testapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.ggsoft.testapp.service.FileStorageService;
import io.ggsoft.testapp.service.VideoEntryService;
import io.ggsoft.testapp.web.rest.util.HeaderUtil;
import io.ggsoft.testapp.web.rest.util.PaginationUtil;
import io.ggsoft.testapp.service.dto.VideoEntryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.StreamUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing VideoEntry.
 */
@RestController
@RequestMapping("/api")
public class VideoEntryResource {

    private final Logger log = LoggerFactory.getLogger(VideoEntryResource.class);

    @Inject
    private VideoEntryService videoEntryService;

    @Inject
    private FileStorageService fileStorageService;

    /**
     * POST  /video-entries : Create a new videoEntry.
     *
     * @param videoEntryDTO the videoEntryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new videoEntryDTO, or with status 400 (Bad Request) if the videoEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/video-entries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VideoEntryDTO> createVideoEntry(@RequestBody VideoEntryDTO videoEntryDTO) throws URISyntaxException {
        log.debug("REST request to save VideoEntry : {}", videoEntryDTO);
        if (videoEntryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("videoEntry", "idexists", "A new videoEntry cannot already have an ID")).body(null);
        }
        VideoEntryDTO result = videoEntryService.save(videoEntryDTO);
        return ResponseEntity.created(new URI("/api/video-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("videoEntry", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /video-entries : Updates an existing videoEntry.
     *
     * @param videoEntryDTO the videoEntryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated videoEntryDTO,
     * or with status 400 (Bad Request) if the videoEntryDTO is not valid,
     * or with status 500 (Internal Server Error) if the videoEntryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/video-entries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VideoEntryDTO> updateVideoEntry(@RequestBody VideoEntryDTO videoEntryDTO) throws URISyntaxException {
        log.debug("REST request to update VideoEntry : {}", videoEntryDTO);
        if (videoEntryDTO.getId() == null) {
            return createVideoEntry(videoEntryDTO);
        }
        VideoEntryDTO result = videoEntryService.save(videoEntryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("videoEntry", videoEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /video-entries : get all the videoEntries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of videoEntries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/video-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<VideoEntryDTO>> getAllVideoEntries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of VideoEntries");
        Page<VideoEntryDTO> page = videoEntryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/video-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /video-entries/:id : get the "id" videoEntry.
     *
     * @param id the id of the videoEntryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the videoEntryDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/video-entries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VideoEntryDTO> getVideoEntry(@PathVariable Long id) {
        log.debug("REST request to get VideoEntry : {}", id);
        VideoEntryDTO videoEntryDTO = videoEntryService.findOne(id);
        return Optional.ofNullable(videoEntryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /video-entries/:id : delete the "id" videoEntry.
     *
     * @param id the id of the videoEntryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/video-entries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVideoEntry(@PathVariable Long id) {
        log.debug("REST request to delete VideoEntry : {}", id);
        videoEntryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("videoEntry", id.toString())).build();
    }

    @RequestMapping(value = "/video-entries/{id}/first",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> uploadVideo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            InputStream fileInputStream = file.getInputStream();
            int i = file.getOriginalFilename().lastIndexOf(".");
            String extension = "";
            if (i > 0) {
                extension = file.getOriginalFilename().substring(i + 1);
            }
            String videoFileName = fileStorageService.saveVideo(id, extension, fileInputStream);
            VideoEntryDTO dto = videoEntryService.findOne(id);
            dto.setVideo1(videoFileName);
            videoEntryService.save(dto);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("videoEntry", id.toString())).build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @RequestMapping(value = "/video-entries/{id}/first",
    method = RequestMethod.GET)
    public  void getVideo(@PathVariable Long id, HttpServletResponse servletResponse) throws IOException {
        VideoEntryDTO one = videoEntryService.findOne(id);
        Resource videoResource = new FileSystemResource(one.getVideo1());
        InputStream inputStream = videoResource.getInputStream();
        ServletOutputStream outputStream = servletResponse.getOutputStream();
        byte[] buffer = new byte[512];
        int bytesRead = -1;

        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();

    }

}
