package com.woven.movie_uploader.components;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

public class StorageUtilTest {

    @TempDir
    private File tmpDir;

    private MemoryUtil storageUtil;

    @BeforeEach
    public void tearUp() throws Exception {
        storageUtil = new MemoryUtil();
    }

    @Test
    public void testListFiles() throws IOException {
    }

    @Test
    public void test2() {

    }
}
