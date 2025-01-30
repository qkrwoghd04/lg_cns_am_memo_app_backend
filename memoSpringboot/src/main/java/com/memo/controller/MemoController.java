package com.memo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.memo.dto.Memo;
import com.memo.service.MemoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/memos")
@RequiredArgsConstructor
public class MemoController {
    private final MemoService memoService;

    private static final String UPLOAD_DIR = "C:\\uploads\\";

    @GetMapping
    public String listMemos(Model model) {
        List<Memo> memos = memoService.getAllMemos();
        model.addAttribute("memos", memos);
        return "memo/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("memo", new Memo());
        return "memo/form";
    }

    @PostMapping("/new")
    public String createMemo(@ModelAttribute Memo memo, @RequestParam("images") List<MultipartFile> images) throws IOException {
        memoService.createMemo(memo);
        List<String> imageUrls = saveImages(images); // 이미지 저장
        memoService.addImagesToMemo(memo.getId(), imageUrls); // 이미지 URL 저장
        return "redirect:/memos";
    }

    @GetMapping("/{id}")
    public String viewMemo(@PathVariable("id") int id, Model model) {
        Memo memo = memoService.getMemoById(id);
        List<String> imageUrls = memoService.getImagesByMemoId(id); // 이미지 URL 조회
        model.addAttribute("memo", memo);
        model.addAttribute("imageUrls", imageUrls); // 이미지 URL 전달
        return "memo/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Memo memo = memoService.getMemoById(id);
        List<String> imageUrls = memoService.getImagesByMemoId(id); // 이미지 URL 조회
        model.addAttribute("memo", memo);
        model.addAttribute("imageUrls", imageUrls); // 이미지 URL 전달
        return "memo/form";
    }

    @PostMapping("/{id}/edit")
    public String updateMemo(@PathVariable("id") int id, @ModelAttribute Memo memo, @RequestParam("images") List<MultipartFile> images) throws IOException {
        memo.setId(id);
        memoService.updateMemo(memo);

        // 기존 이미지 삭제
        memoService.deleteImagesByMemoId(id);

        // 새 이미지 저장
        List<String> imageUrls = saveImages(images);
        memoService.addImagesToMemo(id, imageUrls);

        return "redirect:/memos";
    }

    @PostMapping("/{id}/priority")
    public ResponseEntity<Void> togglePriority(@PathVariable("id") int id) {
        memoService.updateMemoPriority(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/delete")
    public String deleteMemo(@PathVariable("id") int id) {
        memoService.deleteImagesByMemoId(id); // 이미지 삭제
        memoService.deleteMemo(id); // 메모 삭제
        return "redirect:/memos";
    }

    // 이미지 저장 로직
    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        // 디렉토리가 없으면 생성
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName); 
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imageUrls.add("/uploads/" + fileName); // URL 형식으로 저장
            }
        }
        return imageUrls;
    }
}