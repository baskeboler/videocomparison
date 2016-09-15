package io.ggsoft.testapp.service;

import io.ggsoft.testapp.config.JHipsterProperties;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by victor on 14/09/16.
 */
@Service
public class FileStorageService {
    @Inject
    private JHipsterProperties jHipsterProperties;

    public boolean existsDirectoryForId(Long id) {
        return new File(jHipsterProperties.getStorage().getVideosPath() + File.pathSeparator + id).exists();
    }

    public String saveVideo(Long id, String fileExtension, InputStream stream) throws IOException {
        String dirPath = jHipsterProperties.getStorage().getVideosPath() + File.pathSeparator + id;
        if (!existsDirectoryForId(id)) {
            File dir = new File(dirPath);
            org.apache.commons.io.FileUtils.forceMkdir(dir);
        }
        File file = new File(dirPath + File.pathSeparator+ generateFileName(fileExtension));
//        file.createNewFile()
        FileUtils.copyInputStreamToFile(stream, file);
        return file.getAbsolutePath();
    }

    public Resource getVideoResource(Long id, String name) {
        String dirPath = jHipsterProperties.getStorage().getVideosPath() + File.pathSeparator + id;
        File file = new File(dirPath + File.pathSeparator + name);
        return new FileSystemResource(file);
    }

    private String generateFileName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }
}
