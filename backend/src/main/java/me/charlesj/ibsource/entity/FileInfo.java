package com.linkage.ibsource.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name="fileInfo")
public class FileInfo implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String filename;

    private String identifier;

    private Long totalSize;

    private String type;

    private String path;
}
