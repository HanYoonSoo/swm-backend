package com.jj.swm.domain.study.core.repository;

import com.jj.swm.domain.study.core.entity.StudyBookmark;
import com.jj.swm.domain.study.core.repository.custom.CustomStudyBookmarkRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("select b from StudyBookmark b join fetch b.study where b.user.id = ?1")
    Page<StudyBookmark> findPagedBookmarkByUserIdWithStudy(UUID userId, Pageable pageable);

    @Query("select b.id from StudyBookmark b where b.user.id = ?1 and b.study.id = ?2")
    Long findIdByUserIdAndStudyId(UUID userId, Long studyId);
}
