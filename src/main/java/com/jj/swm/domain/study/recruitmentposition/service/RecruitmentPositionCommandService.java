package com.jj.swm.domain.study.recruitmentposition.service;

import com.jj.swm.domain.study.constants.StudyElementLimit;
import com.jj.swm.domain.study.core.entity.Study;
import com.jj.swm.domain.study.core.repository.StudyRepository;
import com.jj.swm.domain.study.recruitmentposition.dto.request.CreateRecruitmentPositionRequest;
import com.jj.swm.domain.study.recruitmentposition.dto.request.UpdateRecruitmentPositionRequest;
import com.jj.swm.domain.study.recruitmentposition.dto.response.CreateRecruitmentPositionResponse;
import com.jj.swm.domain.study.recruitmentposition.entity.StudyRecruitmentPosition;
import com.jj.swm.domain.study.recruitmentposition.repository.RecruitmentPositionRepository;
import com.jj.swm.global.common.enums.ErrorCode;
import com.jj.swm.global.exception.GlobalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecruitmentPositionCommandService {

    private final StudyRepository studyRepository;
    private final RecruitmentPositionRepository recruitmentPositionRepository;

    @Transactional
    public CreateRecruitmentPositionResponse addRecruitmentPosition(
            UUID userId,
            Long studyId,
            CreateRecruitmentPositionRequest request
    ) {
        verifyRecruitmentPositionSizeLimit(studyId);

        Study study = studyRepository.findByIdAndUserId(studyId, userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "study not found"));

        StudyRecruitmentPosition recruitmentPosition = StudyRecruitmentPosition.of(study, request);
        recruitmentPositionRepository.save(recruitmentPosition);

        return CreateRecruitmentPositionResponse.from(recruitmentPosition);
    }

    private void verifyRecruitmentPositionSizeLimit(Long studyId) {
        int recruitmentPositionSize = recruitmentPositionRepository.countByStudyId(studyId);
        if (recruitmentPositionSize + 1 > StudyElementLimit.RECRUITMENT_POSITION) {
            throw new GlobalException(ErrorCode.NOT_VALID, "Recruitment Position Limit Exceeded");
        }
    }

    @Transactional
    public void modifyRecruitmentPosition(
            UUID userId,
            Long recruitmentPositionId,
            UpdateRecruitmentPositionRequest request
    ) {
        StudyRecruitmentPosition recruitmentPosition =
                recruitmentPositionRepository.findByIdAndStudyUserId(recruitmentPositionId, userId)
                        .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "recruitment position not found"));

        validateAcceptedCount(request);

        recruitmentPosition.modify(request);
    }

    private void validateAcceptedCount(UpdateRecruitmentPositionRequest request) {
        if (request.getHeadcount() < request.getAcceptedCount()) {
            throw new GlobalException(ErrorCode.NOT_VALID, "The number of accepted exceeds the recruitment limit.");
        }
    }

    @Transactional
    public void removeRecruitmentPosition(UUID userId, Long recruitmentPositionId) {
        recruitmentPositionRepository.deleteByIdAndStudyUserId(recruitmentPositionId, userId);
    }

//    @Transactional
//    public RecruitmentPositionApplyResponse apply(UUID userId, Long recruitPositionId) {
//        User user = userRepository.getReferenceById(userId);
//
//        StudyRecruitmentPosition recruitmentPosition = recruitmentPositionRepository.findById(recruitPositionId)
//                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "recruitment position not found"));
//
//        Optional<StudyParticipant> optionalStudyParticipant =
//                participantRepository.findByUserIdAndStudyRecruitmentPositionId(userId, recruitPositionId);
//
//        if (optionalStudyParticipant.isPresent()) {
//            return RecruitmentPositionApplyResponse.from(optionalStudyParticipant.get());
//        }
//
//        Integer acceptedCount = participantRepository.countAcceptedByRecruitmentPositionId(recruitmentPosition);
//        if (acceptedCount >= recruitmentPosition.getHeadcount()) {
//            throw new GlobalException(ErrorCode.NOT_VALID, "It is already full");
//        }
//
//        StudyParticipant participant = StudyParticipant.of(recruitmentPosition, user);
//        participantRepository.save(participant);
//
//        return RecruitmentPositionApplyResponse.from(participant);

//    }

//    @Transactional
//    public void withdraw(UUID userId, Long participantId) {
//        StudyParticipant studyParticipant =
//                participantRepository.findByIdAndStudyUserId(participantId, userId)
//                        .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "recruitment position not found"));
//
//        if (studyParticipant.getStatus() != StudyParticipantStatus.PENDING) {
//            throw new GlobalException(ErrorCode.NOT_VALID, "application status is not pending");
//        }
//
//        participantRepository.delete(studyParticipant);
//    }
}
