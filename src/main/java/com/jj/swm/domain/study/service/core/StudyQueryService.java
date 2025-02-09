package com.jj.swm.domain.study.service.core;

import com.jj.swm.domain.study.dto.core.StudyBookmarkInfo;
import com.jj.swm.domain.study.dto.core.StudyInquiryCondition;
import com.jj.swm.domain.study.dto.comment.response.ParentCommentInquiryResponse;
import com.jj.swm.domain.study.dto.core.response.StudyDetailsResponse;
import com.jj.swm.domain.study.dto.core.response.StudyImageInquiryResponse;
import com.jj.swm.domain.study.dto.core.response.StudyInquiryResponse;
import com.jj.swm.domain.study.dto.recruitmentposition.response.RecruitPositionInquiryResponse;
import com.jj.swm.domain.study.entity.core.Study;
import com.jj.swm.domain.study.entity.core.StudyImage;
import com.jj.swm.domain.study.entity.recruitmentposition.StudyRecruitmentPosition;
import com.jj.swm.domain.study.repository.core.StudyBookmarkRepository;
import com.jj.swm.domain.study.repository.core.StudyImageRepository;
import com.jj.swm.domain.study.repository.core.StudyLikeRepository;
import com.jj.swm.domain.study.repository.core.StudyRepository;
import com.jj.swm.domain.study.repository.recruitmentposition.RecruitmentPositionRepository;
import com.jj.swm.domain.study.service.comment.CommentQueryService;
import com.jj.swm.global.common.dto.PageResponse;
import com.jj.swm.global.common.enums.ErrorCode;
import com.jj.swm.global.common.enums.PageSize;
import com.jj.swm.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyQueryService {

    private final StudyRepository studyRepository;
    private final CommentQueryService commentQueryService;
    private final StudyLikeRepository studyLikeRepository;
    private final StudyImageRepository studyImageRepository;
    private final StudyBookmarkRepository studyBookmarkRepository;
    private final RecruitmentPositionRepository recruitmentPositionRepository;

    @Transactional(readOnly = true)
    public PageResponse<StudyInquiryResponse> getList(
            UUID userId,
            StudyInquiryCondition inquiryCondition
    ) {
        List<Study> studies = studyRepository.findPagedWithTags(PageSize.Study + 1, inquiryCondition);

        if (studies.isEmpty()) {
            return PageResponse.of(List.of(), false);
        }

        boolean hasNext = studies.size() > PageSize.Study;

        List<Study> pagedStudies = hasNext ? studies.subList(0, PageSize.Study) : studies;

        Map<Long, Long> bookmarkIdByStudyId = getBookmarkMapping(userId, pagedStudies);

        List<StudyInquiryResponse> inquiryResponses = pagedStudies.stream()
                .map(study -> StudyInquiryResponse.of(
                        study, bookmarkIdByStudyId.getOrDefault(study.getId(), null)
                ))
                .toList();

        return PageResponse.of(inquiryResponses, hasNext);
    }

    public Map<Long, Long> getBookmarkMapping(UUID userId, List<Study> studies) {
        List<Long> studyIds = studies.stream()
                .map(Study::getId)
                .toList();

        return userId != null
                ? studyBookmarkRepository.findAllByUserIdAndStudyIds(userId, studyIds)
                .stream()
                .collect(Collectors.toMap(StudyBookmarkInfo::getStudyId, StudyBookmarkInfo::getId))
                : Collections.emptyMap();
    }

    @Transactional
    public StudyDetailsResponse get(UUID userId, Long studyId) {
        Study study = studyRepository.findByIdUsingPessimisticLock(studyId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "study not found"));

        boolean likeStatus = false;
        if (userId != null) {
            likeStatus = studyLikeRepository.existsByUserIdAndStudyId(userId, studyId);
        }

        study.incrementViewCount();

        List<StudyImage> images = studyImageRepository.findAllByStudyId(studyId);

        List<StudyImageInquiryResponse> imageInquiryResponses = images.stream()
                .map(StudyImageInquiryResponse::from)
                .toList();

        List<StudyRecruitmentPosition> recruitmentPositions =
                recruitmentPositionRepository.findAllByStudyId(studyId);

        List<RecruitPositionInquiryResponse> recruitPositionInquiryResponses = recruitmentPositions.stream()
                .map(RecruitPositionInquiryResponse::from)
                .toList();

        Pageable pageable = PageRequest.of(
                0,
                PageSize.StudyComment,
                Sort.by("id").descending()
        );
        PageResponse<ParentCommentInquiryResponse> commentPageResponse =
                commentQueryService.loadCommentPageResponse(studyId, pageable);

        return StudyDetailsResponse.of(
                likeStatus,
                study,
                imageInquiryResponses,
                recruitPositionInquiryResponses,
                commentPageResponse
        );
    }
}
