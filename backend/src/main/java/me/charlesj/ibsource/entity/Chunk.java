package com.linkage.ibsource.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
@Entity
public class Chunk implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private Integer chunkNumber;

    private Long chunkSize;

    private Long currentChunkSize;

    private Long totalSize;

    private String identifier;

    private String filename;

    private String relativePath;

    private Integer totalChunks;

    private String type;
    @Transient
    private MultipartFile file;
}
