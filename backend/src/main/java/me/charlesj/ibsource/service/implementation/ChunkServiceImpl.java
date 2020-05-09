package com.linkage.ibsource.service.implementation;

import com.linkage.ibsource.dao.ChunkDao;
import com.linkage.ibsource.entity.Chunk;
import com.linkage.ibsource.service.ChunkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;



@Service
public class ChunkServiceImpl implements ChunkService {
    @Resource
    private ChunkDao chunkDao;

    @Override
    public void saveChunk(Chunk chunk) {
        chunkDao.add(chunk);
    }

    @Override
    public boolean checkChunk(String identifier, Integer chunkNumber){
        return true;
    }
    /*
    public boolean checkChunk(String identifier, Integer chunkNumber) {
        Specification<Chunk> specification = (Specification<Chunk>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("identifier"), identifier));
            predicates.add(criteriaBuilder.equal(root.get("chunkNumber"), chunkNumber));

            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };

        return chunkRepository.findOne(specification).orElse(null) == null;
    }
    */
}
