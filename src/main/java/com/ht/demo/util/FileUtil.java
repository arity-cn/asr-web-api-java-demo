package com.ht.demo.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class FileUtil {

    public static ByteBuf readFileToByteBuf(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(path);
        return Unpooled.wrappedBuffer(fileBytes);
    }

    public static List<ByteBuf> readFileToByteBuf(String filePath, int chunkSize) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(path);

        int fileSize = fileBytes.length;
        int numOfChunks = (fileSize + chunkSize - 1) / chunkSize;

        List<ByteBuf> byteBufList = new ArrayList<>();

        for (int i = 0; i < numOfChunks; i++) {
            int startIndex = i * chunkSize;
            int endIndex = Math.min(startIndex + chunkSize, fileSize);
            byte[] chunk = new byte[endIndex - startIndex];
            System.arraycopy(fileBytes, startIndex, chunk, 0, endIndex - startIndex);
            ByteBuf byteBuf = Unpooled.wrappedBuffer(chunk);
            byteBufList.add(byteBuf);
        }

        return byteBufList;
    }

    public static String readFileToBase64(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] fileBytes = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(fileBytes);
    }

    public static byte[] readFileToBytes(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }
}
