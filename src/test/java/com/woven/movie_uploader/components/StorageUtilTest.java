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

    private StorageUtil storageUtil;

    @BeforeEach
    public void tearUp() {
        storageUtil = new StorageUtil(tmpDir.getAbsolutePath());
    }

    @Test
    public void testListFiles() throws IOException {
        Assertions.assertIterableEquals(Lists.newArrayList(), storageUtil.allfiles());
        final String file = "file";
        final String file2 = "file2";
        storageUtil.uploadFile(file);
        storageUtil.uploadFile(file2);
        Assertions.assertTrue(storageUtil.exists(file));
        Assertions.assertTrue(storageUtil.exists(file2));
        Assertions.assertIterableEquals(Lists.newArrayList(file, file2), storageUtil.allfiles());
        Assertions.assertTrue(storageUtil.deleteFile(file));
        Assertions.assertTrue(storageUtil.deleteFile(file2));
        Assertions.assertFalse(storageUtil.deleteFile(file));
    }

    @Test
    public void test2() {

    }
}
