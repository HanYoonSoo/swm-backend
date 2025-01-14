package com.jj.swm.domain.study.repository;

import com.jj.swm.domain.study.entity.StudyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface StudyBookmarkRepository extends JpaRepository<StudyBookmark, Long>, CustomStudyBookmarkRepository {

    @Modifying
    @Query("delete from StudyBookmark b where b.id = ?1 and b.user.id = ?2")
    void deleteByIdAndUserId(Long bookmarkId, UUID userId);

    Optional<StudyBookmark> findByUserIdAndStudyId(UUID userId, Long studyId);

    @Modifying
    @Query("delete from StudyBookmark b where b.study.id = ?1")
    void deleteAllByStudyId(Long studyId);
}
