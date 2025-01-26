package com.memo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.memo.dto.Memo;
import com.memo.mapper.MemoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoMapper memoMapper;

    public void createMemo(Memo memo) {
        memo.setId(memoMapper.getNextId());
        memoMapper.insertMemo(memo);
    }

    public List<Memo> getAllMemos() {
        return memoMapper.selectAllMemos();
    }

    public Memo getMemoById(int id) {
        return memoMapper.selectMemoById(id);
    }

    public void updateMemo(Memo memo) {
        memoMapper.updateMemo(memo);
    }
    
    public void updateMemoPriority(int id) {
        try {
            memoMapper.updateMemoPriority(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update memo priority");
        }
    }
	
    public void deleteMemo(int id) {
        memoMapper.deleteMemo(id);
    }
}