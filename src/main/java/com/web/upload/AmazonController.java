package com.web.upload;

import com.web.upload.AmazonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class AmazonController {

    private final AmazonService s3Service;

    public AmazonController(AmazonService s3Service) {
        this.s3Service = s3Service;
    }

    // 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3Service.uploadFile(file);
            return ResponseEntity.ok("파일 업로드 성공: " + fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }

    // 전체 목록 조회
    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        List<String> fileUrls = s3Service.listFiles();
        return ResponseEntity.ok(fileUrls);
    }

    // 특정 폴더에 있는 파일들 조회
    @GetMapping("/files/{folder}")
    public ResponseEntity<List<String>> listFilesByFolder(@PathVariable("folder") String folder) {
        List<String> fileUrls = s3Service.listFilesByPrefix(folder + "/"); // 전달 받은 경로를 통해 해당 폴더에 접근
        return ResponseEntity.ok(fileUrls);
    }

    // 특정 폴더로 파일 업로드
    @PostMapping("/uploadFolder/{folder}")
    public ResponseEntity<String> uploadFileToFolder(
            @RequestParam("file") MultipartFile file,
            @PathVariable("folder") String folder) {
        try {
            String fileUrl = s3Service.uploadFileToFolder(file, folder);
            return ResponseEntity.ok("파일 업로드 성공: " + fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }
}
