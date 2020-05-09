package com.linkage.ibsource.utils;

import com.alibaba.fastjson.JSONObject;
import com.linkage.ibsource.entity.Chunk;
import com.linkage.ibsource.entity.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Slf4j
public class FileUtils {

    public static String generatePath(String uploadFolder, Chunk chunk) {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFolder).append("/").append(chunk.getIdentifier());
        if (!Files.isWritable(Paths.get(sb.toString()))) {
            log.info("path not exist,create path: {}", sb.toString());
            try {
                Files.createDirectories(Paths.get(sb.toString()));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return sb.append("/")
                .append(chunk.getFilename())
                .append("-")
                .append(chunk.getChunkNumber()).toString();
    }
    public static List<JSONObject> findAllFiles(FileInfo fileInfo, String rootPath){
        String path = fileInfo.getPath();
        List<JSONObject> fileItems = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(rootPath, path))) {
            for (Path pathObj : directoryStream) {
                // Get file attrs
                File file = new File(pathObj.toString());
                if (file.canRead()) {
                    BasicFileAttributes attrs = Files.readAttributes(pathObj, BasicFileAttributes.class);
                    // Put into JSON and return
                    JSONObject fileItem = new JSONObject();
                    fileItem.put("name", pathObj.getFileName().toString());
                    fileItem.put("size", attrs.size());
                    fileItem.put("path", path);
                    if (attrs.isDirectory()) {
                        fileItem.put("type", "dir");
                        FileInfo newPath = new FileInfo();
                        newPath.setPath(fileInfo.getPath() + "/" + fileItem.getString("name"));
                        fileItem.put("children", findAllFiles(newPath, rootPath));
                    } else {
                        fileItem.put("type", "file");
                    }
                    fileItems.add(fileItem);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileItems;
    }
    public static String deleteFile(Path path) {
        if (!Files.exists(path,
                new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) return "File not Found";
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("delete file: " + file.toString());
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    System.out.println("delete dir: " + dir.toString());
                    return FileVisitResult.CONTINUE;
                }
            });
            return "Delete Success";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Access Denied";
    }


    public static void merge(String targetFile, String folder) {
        try {
            Files.createFile(Paths.get(targetFile));
            Files.list(Paths.get(folder))
                    .filter(path -> path.getFileName().toString().contains("-"))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
//        deleteFile(Paths.get(folder));
    }
}
