package com.linkage.ibsource.dao;

import com.linkage.ibsource.entity.FileInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface FileInfoDao {
    @Insert("insert into fileInfo(filename, identifier, totalSize, type, path) " +
            "values (#{filename}, #{identifier}, #{totalSize}, #{type}, #{path})")
    @Options(useGeneratedKeys = true)
    int add(FileInfo fileInfo);
}
