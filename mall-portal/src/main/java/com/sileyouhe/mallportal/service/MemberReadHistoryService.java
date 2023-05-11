package com.sileyouhe.mallportal.service;

import com.sileyouhe.mallportal.nosql.mongodb.document.MemberReadHistory;

import java.util.List;

public interface MemberReadHistoryService {

    int create(MemberReadHistory memberReadHistory);

    int delete(List<String> ids);

    List<MemberReadHistory> list(Long memberId);
}
