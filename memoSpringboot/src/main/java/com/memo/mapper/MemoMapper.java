package com.memo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.memo.dto.Memo;


@Mapper
public interface MemoMapper {
	void insertMemo(Memo memo);
	List<Memo> selectAllMemos();
	Memo selectMemoById(int id);
	void updateMemo(Memo memo);
	void updateMemoPriority(int id);
	void deleteMemo(int id);
	int getNextId();
	void insertImage(@Param("memoId") int memoId, @Param("imageUrl") String imageUrl);
	List<String> selectImagesByMemoId(int memoId);
	void deleteImagesByMemoId(int memoId);
}
