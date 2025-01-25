package com.memo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.memo.dto.Memo;


@Mapper
public interface MemoMapper {
	void insertMemo(Memo memo);
	List<Memo> selectAllMemos();
	Memo selectMemoById(int id);
	void updateMemo(Memo memo);
	void deleteMemo(int id);
	int getNextId();
}
