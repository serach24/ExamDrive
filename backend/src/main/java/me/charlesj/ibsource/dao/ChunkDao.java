package com.linkage.ibsource.dao;

import com.linkage.ibsource.entity.Chunk;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface ChunkDao {
//    @Insert("insert into file(filename, identifier, totalSize, type, path) " +
//            "values (#{filename},#{identifier}, #{totalSize}, #{type}, #{path})")
    @Options(useGeneratedKeys = true)
    int add(Chunk chunk);
}
