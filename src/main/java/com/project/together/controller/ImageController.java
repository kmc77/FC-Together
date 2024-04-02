package com.project.together.controller;

import com.project.together.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {

        this.imageService = imageService;
    }

    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> imageUpload(MultipartRequest request) throws Exception {

        Map<String, Object> responseData = new HashMap<>();

        try {

            String s3Url = imageService.imageUpload(request);

            responseData.put("uploaded", true);
            responseData.put("url", s3Url);

            return responseData;

        } catch (IOException e) {

            responseData.put("uploaded", false);

            return responseData;
        }
    }

    @PostMapping("/ruleUpload")
    @ResponseBody
    public Map<String, Object> ruleUpload(MultipartHttpServletRequest request) {
        Map<String, Object> responseData = new HashMap<>();

        try {
            MultipartFile file = request.getFile("upload");

            // 이미지 업로드를 처리하는 서비스 메서드를 호출합니다.
            String s3Url = imageService.ruleImageUpload(file); // 서비스 메서드 수정

            responseData.put("uploaded", true);
            responseData.put("url", s3Url);

            return responseData;
        } catch (IOException e) {
            responseData.put("uploaded", false);
            responseData.put("error", e.getMessage()); // 에러 메시지도 함께 반환
            return responseData;
        }
    }



}
