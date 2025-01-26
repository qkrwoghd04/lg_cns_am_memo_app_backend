package com.memo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.memo.dto.Memo;
import com.memo.service.MemoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/memos")
@RequiredArgsConstructor
public class MemoController {
	private final MemoService memoService;
	
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
	public String createMemo(@ModelAttribute Memo memo) {
		memoService.createMemo(memo);
		return "redirect:/memos";
	}
	
	@GetMapping("/{id}")
	public String viewMemo(@PathVariable("id") int id, Model model) {
		Memo memo = memoService.getMemoById(id);
		model.addAttribute("memo", memo);
		return "memo/view";
	}
	
	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable("id") int id, Model model) {
		Memo memo = memoService.getMemoById(id);
		model.addAttribute("memo", memo);
		return "memo/form";
	}
	
	@PostMapping("/{id}/edit")
	public String updateMemo(@PathVariable("id") int id, @ModelAttribute Memo memo) {
		memo.setId(id);
		memoService.updateMemo(memo);
		return "redirect:/memos";
	}
	
	@PostMapping("/{id}/priority")
	public ResponseEntity<Void> togglePriority(@PathVariable("id") int id) {
	    memoService.updateMemoPriority(id);
	    return ResponseEntity.ok().build();
	}
	
	@GetMapping("{id}/delete")
	public String deleteMemo(@PathVariable("id") int id) {
		memoService.deleteMemo(id);
		return "redirect:/memos";
	}
	
}
