package com.linkage.ibsource.service.implementation;

import com.linkage.ibsource.dao.FileInfoDao;
import com.linkage.ibsource.entity.FileInfo;
import com.linkage.ibsource.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.nio.file.*;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

@Service
public class FileInfoServiceImpl implements FileInfoService {
    @Autowired
    private FileInfoDao fileInfoDao;

    @Override
    public int addFileInfo(FileInfo fileInfo) {
        return fileInfoDao.add(fileInfo);
    }

}
