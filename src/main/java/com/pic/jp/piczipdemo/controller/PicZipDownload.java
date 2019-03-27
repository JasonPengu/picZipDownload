package com.pic.jp.piczipdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/picZipDownload")
public class PicZipDownload {

    @RequestMapping("/download")
    public void picZipDownload(HttpServletResponse response,
                               @RequestParam("urls") String urls
    ) throws IOException {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=" + UUID.randomUUID().toString() + ".zip");
        String urlList[] = urls.split(",");
        URL url = null;
        try(OutputStream out = response.getOutputStream(); ZipOutputStream zos = new ZipOutputStream(out)){
            byte [] bufs = new byte[1024 * 10];
            for(String oneurl : urlList){
                try{
                    url = new URL(oneurl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestMethod("GET");
                    InputStream inputStream = connection.getInputStream();
                    ZipEntry zipEntry = new ZipEntry(UUID.randomUUID().toString()+".jpg");
                    zos.putNextEntry(zipEntry);
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    int read = 0;
                    while ((read=bis.read(bufs))!= -1){
                        zos.write(bufs,0,read);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
