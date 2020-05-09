package com.linkage.ibsource.controller;


import com.alibaba.fastjson.JSONObject;
import com.linkage.ibsource.entity.FileInfo;
import com.linkage.ibsource.service.FileInfoService;
import com.linkage.ibsource.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/filemanager")
public class FileManageController {
    @Resource
    private FileInfoService fileInfoService;

    @Value("${prop.upload-folder}")
    private String uploadFolder;

    @PostMapping("/list")
    public Object list(@RequestBody FileInfo fileInfo) {
        try{
            List<JSONObject> fileItems = FileUtils.findAllFiles(fileInfo, uploadFolder);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", fileItems);
            jsonObject.put("code", 20000);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail to get file list";
        }
    }

    @PostMapping("/delete")
    public Object deleteFile(@RequestBody FileInfo fileInfo){
        JSONObject jsonObject = new JSONObject();
        try {
            String parentPath = uploadFolder + '/' + fileInfo.getPath();
            String filename = fileInfo.getFilename();
            Path path = Paths.get(parentPath,"/"+filename);
            jsonObject.put("message", FileUtils.deleteFile(path));
        } catch (Exception e){
            e.printStackTrace();
            jsonObject.put("message", "Delete Failed");
        }
        jsonObject.put("code",20000);
        return jsonObject;
    }
    @PostMapping("createFolder")
    public Object createFolder(@RequestBody FileInfo fileInfo) {
        JSONObject jsonObject = new JSONObject();
        try {
            String newPath = fileInfo.getPath();
            File newDir = new File(uploadFolder + "/"+newPath);
            if (!newDir.mkdir()) {
                throw new Exception("cannot create path" + newPath);
            }
            jsonObject.put("message", "Create Success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("message", "Folder creating failed");
        }
        jsonObject.put("code",20000);
        return jsonObject;
    }

    @PostMapping("/download")
    public Object downloadFile(@RequestBody FileInfo fileInfo,
                               HttpServletResponse response) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        String filename = fileInfo.getFilename();
        String filePath = fileInfo.getPath();
        File file = new File(filePath + "/" + filename);
        if (!file.exists()) {
            jsonObject.put("message","Server File Error");
            return jsonObject;
        }
        else {
            // Download Config
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                } // Download Success
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

}
