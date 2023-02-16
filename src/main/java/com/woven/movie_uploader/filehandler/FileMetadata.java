package com.woven.movie_uploader.filehandler;


import org.immutables.value.Value.Immutable;

@Immutable
public interface FileMetadata {
    public  String fileId();
    public  String name();
    public int filesize();
    public String createdAt();
}
