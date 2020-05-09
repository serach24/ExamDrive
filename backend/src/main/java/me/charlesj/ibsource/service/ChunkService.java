package com.linkage.ibsource.service;

import com.linkage.ibsource.entity.Chunk;

public interface ChunkService {
    void saveChunk(Chunk chunk);

    boolean checkChunk(String identifier, Integer chunkNumber);
}
