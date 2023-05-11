package com.sileyouhe.mallportal.nosql.mongodb.repository;

import com.sileyouhe.mallportal.nosql.mongodb.document.MemberReadHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MemberReadHistoryRepository extends MongoRepository<MemberReadHistory, String> {

    List<MemberReadHistory> findByMemberIdOrderByCreateTimeDesc(Long memberId);

}
