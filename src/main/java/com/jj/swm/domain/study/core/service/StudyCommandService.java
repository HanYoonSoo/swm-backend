package com.jj.swm.domain.study.core.service;

import com.jj.swm.domain.study.comment.repository.CommentRepository;
import com.jj.swm.domain.study.constants.StudyElementLimit;
import com.jj.swm.domain.study.core.dto.request.*;
import com.jj.swm.domain.study.core.dto.response.CreateStudyBookmarkResponse;
import com.jj.swm.domain.study.core.entity.Study;
import com.jj.swm.domain.study.core.entity.StudyBookmark;
import com.jj.swm.domain.study.core.entity.StudyLike;
import com.jj.swm.domain.study.core.repository.*;
import com.jj.swm.domain.study.recruitmentposition.repository.RecruitmentPositionRepository;
import com.jj.swm.domain.user.core.entity.User;
import com.jj.swm.domain.user.core.repository.UserRepository;
import com.jj.swm.global.common.enums.ErrorCode;
import com.jj.swm.global.exception.GlobalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudyCommandService {

    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final CommentRepository commentRepository;
    private final StudyTagRepository studyTagRepository;
    private final StudyLikeRepository studyLikeRepository;
    private final StudyImageRepository studyImageRepository;
    private final StudyBookmarkRepository studyBookmarkRepository;
    private final RecruitmentPositionRepository recruitmentPositionRepository;

    @Transactional
    public void addStudy(UUID userId, CreateStudyRequest request) {
        User user = userRepository.getReferenceById(userId);

        Study study = Study.of(user, request);
        studyRepository.save(study);

        storeTagListIfPresent(study, request.getTagList());

        storeImageListIfPresent(study, request.getImageUrlList());

        recruitmentPositionRepository.batchInsert(study, request.getCreateRecruitmentPositionRequestList());
    }

    @Transactional
    public void modifyStudy(
            UUID userId,
            Long studyId,
            UpdateStudyRequest request
    ) {
        Study study = loadStudyOrException(userId, studyId);

        saveTagList(study, request.getSaveTagRequest());

        saveImageList(study, request.getSaveImageRequest());

        study.modify(request);
    }

    private void saveTagList(Study study, SaveStudyTagRequest request) {
        if (request != null) {
            int oldTagSize = studyTagRepository.countByStudyId(study.getId());
            int newTagSize =
                    oldTagSize + request.getTagListToAdd().size() - request.getTagIdListToRemove().size();

            if (newTagSize > StudyElementLimit.TAG) {
                throw new GlobalException(ErrorCode.NOT_VALID, "Tag Limit Exceeded");
            }

            List<String> tagListToAdd = request.getTagListToAdd();
            storeTagListIfPresent(study, tagListToAdd);

            List<Long> tagIdListToRemove = request.getTagIdListToRemove();
            if (tagIdListToRemove != null && !tagIdListToRemove.isEmpty()) {
                studyTagRepository.deleteAllByIdListAndStudyId(tagIdListToRemove, study.getId());
            }
        }
    }

    private void saveImageList(Study study, SaveStudyImageRequest request) {
        if (request != null) {
            int oldImageSize = studyImageRepository.countByStudyId(study.getId());
            int newImageSize =
                    oldImageSize + request.getImageUrlListToAdd().size() - request.getImageIdListToRemove().size();

            if (newImageSize > StudyElementLimit.IMAGE) {
                throw new GlobalException(ErrorCode.NOT_VALID, "Image Limit Exceeded");
            }

            List<String> imageUrlListToAdd = request.getImageUrlListToAdd();
            storeImageListIfPresent(study, imageUrlListToAdd);

            List<Long> imageIdListToRemove = request.getImageIdListToRemove();
            if (imageIdListToRemove != null && !imageIdListToRemove.isEmpty()) {
                studyImageRepository.deleteAllByIdListAndStudyId(imageIdListToRemove, study.getId());
            }
        }
    }

    @Transactional
    public void modifyStudyStatus(
            UUID userId,
            Long studyId,
            UpdateStudyStatusRequest request
    ) {
        Study study = loadStudyOrException(userId, studyId);
        study.modifyStatus(request);
    }

    @Transactional
    public void removeStudy(UUID userId, Long studyId) {
        Study study = loadStudyOrException(userId, studyId);

        deleteStudyAndAssociations(studyId, study);
    }

    private void deleteStudyAndAssociations(Long studyId, Study study) {
        studyTagRepository.deleteAllByStudyId(studyId);
        studyImageRepository.deleteAllByStudyId(studyId);
        recruitmentPositionRepository.deleteAllByStudyId(studyId);
        studyLikeRepository.deleteAllByStudyId(studyId);
        commentRepository.deleteAllByStudyId(studyId);
        studyBookmarkRepository.deleteAllByStudyId(studyId);
        studyRepository.delete(study);
    }

    @Transactional
    public CreateStudyBookmarkResponse addStudyBookmark(UUID userId, Long studyId) {
        Optional<StudyBookmark> optionalStudyBookmark = studyBookmarkRepository.findByUserIdAndStudyId(userId, studyId);
        if (optionalStudyBookmark.isPresent()) {
            return CreateStudyBookmarkResponse.from(optionalStudyBookmark.get());
        }

        User user = userRepository.getReferenceById(userId);

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "study not found"));

        StudyBookmark studyBookmark = StudyBookmark.of(study, user);
        studyBookmarkRepository.save(studyBookmark);

        return CreateStudyBookmarkResponse.from(studyBookmark);
    }

    @Transactional
    public void removeStudyBookmark(UUID userId, Long bookmarkId) {
        studyBookmarkRepository.deleteByIdAndUserId(bookmarkId, userId);
    }

    @Transactional
    public void addStudyLike(UUID userId, Long studyId) {
        Optional<StudyLike> optionalStudyLike = studyLikeRepository.findByUserIdAndStudyId(userId, studyId);
        if (optionalStudyLike.isPresent()) {
            return;
        }

        User user = userRepository.getReferenceById(userId);

        Study study = loadStudyUsingPessimisticLock(studyId);

        StudyLike studyLike = StudyLike.of(user, study);
        studyLikeRepository.save(studyLike);

        study.incrementLikeCount();
    }

    @Transactional
    public void removeStudyLike(UUID userId, Long studyId) {
        Optional<StudyLike> optionalStudyLike = studyLikeRepository.findByUserIdAndStudyId(userId, studyId);
        if (optionalStudyLike.isEmpty()) {
            return;
        }

        Study study = loadStudyUsingPessimisticLock(studyId);

        studyLikeRepository.delete(optionalStudyLike.get());

        study.decrementLikeCount();
    }

    private void storeTagListIfPresent(Study study, List<String> tagList) {
        if (tagList != null && !tagList.isEmpty()) {
            studyTagRepository.batchInsert(study, tagList);
        }
    }

    private void storeImageListIfPresent(Study study, List<String> imageUrlList) {
        if (imageUrlList != null && !imageUrlList.isEmpty()) {
            studyImageRepository.batchInsert(study, imageUrlList);
        }
    }

    private Study loadStudyOrException(UUID userId, Long studyId) {
        return studyRepository.findByIdAndUserId(studyId, userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "study not found"));
    }

    private Study loadStudyUsingPessimisticLock(Long studyId) {
        return studyRepository.findByIdUsingPessimisticLock(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "study not found"));
    }
}
