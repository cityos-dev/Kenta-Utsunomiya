package com.woven.movie_uploader.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FileRepository extends MongoRepository<FileMetadataModel, String> {

    @Query("{id:'?0'}")
    FileMetadataModel findFileByName(String id);

    @Query("{}")
    List<FileMetadataModel> findAll();

    @Override
    long count();

}
