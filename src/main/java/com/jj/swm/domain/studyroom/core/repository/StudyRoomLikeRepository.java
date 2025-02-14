package com.jj.swm.domain.studyroom.core.repository;

import com.jj.swm.domain.studyroom.core.entity.StudyRoom;
import com.jj.swm.domain.studyroom.core.entity.StudyRoomLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudyRoomLikeRepository extends JpaRepository<StudyRoomLike, Long> {

    boolean existsByStudyRoomIdAndUserId(Long studyRoomId, UUID userId);

    Optional<StudyRoomLike> findByStudyRoomIdAndUserId(Long studyRoomId, UUID userId);

    @Query("select s.id from StudyRoomLike s where s.studyRoom.id = ?1 and s.user.id = ?2")
    Long findIdByStudyRoomIdAndUserId(Long studyRoomId, UUID userId);

    int countStudyRoomLikeByStudyRoom(StudyRoom studyRoom);

    @Modifying
    @Query("delete from StudyRoomLike s where s.studyRoom.id = ?1")
    void deleteAllByStudyRoomId(Long studyRoomId);

    @Query("select s.studyRoom from StudyRoomLike s where s.user.id = ?1")
    Page<StudyRoom> findPagedStudyRoomByUserId(UUID userId, Pageable pageable);
}
