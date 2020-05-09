package com.linkage.ibsource.controller;

import com.linkage.ibsource.entity.Chunk;
import com.linkage.ibsource.entity.FileInfo;
import com.linkage.ibsource.service.ChunkService;
import com.linkage.ibsource.service.FileInfoService;
import com.linkage.ibsource.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {
    @Value("${prop.upload-folder}")
    private String uploadFolder;
    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private ChunkService chunkService;

    @GetMapping("/test")
    public String test(){
        return "success";
    }

    @PostMapping("/chunk")
    public String uploadChunk(Chunk chunk) {
        MultipartFile file = chunk.getFile();
        log.debug("file originName: {}, chunkNumber: {}", file.getOriginalFilename(), chunk.getChunkNumber());
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(FileUtils.generatePath(uploadFolder, chunk));
            Files.write(path, bytes);
            log.debug("File {} Write Success, uuid:{}", chunk.getFilename(), chunk.getIdentifier());
//            chunkService.saveChunk(chunk);
            return "Upload Success";
        } catch (IOException e) {
            e.printStackTrace();
            return "Server Error...";
        }
    }

    @GetMapping("/chunk")
    public Object checkChunk(Chunk chunk, HttpServletResponse response) {
        if (chunkService.checkChunk(chunk.getIdentifier(), chunk.getChunkNumber())) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }

        return chunk;
    }


    @PostMapping("/mergeFile")
    public String mergeFile(FileInfo fileInfo) {
        String path = uploadFolder + "/" +  fileInfo.getPath() + "/" +  fileInfo.getFilename();
        String folder = uploadFolder + "/" + fileInfo.getIdentifier();
        String result;
        if (fileInfo.getPath() != null) {
            FileUtils.merge(path, folder);
            result = "Merge Success";
        } else {
            FileUtils.deleteFile(Paths.get(path));
            result = "Empty Path";
        }
        FileUtils.deleteFile(Paths.get(folder));
        fileInfoService.addFileInfo(fileInfo);
        return result;
    }

}
