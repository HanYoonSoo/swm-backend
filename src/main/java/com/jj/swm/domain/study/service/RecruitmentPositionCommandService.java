package com.jj.swm.domain.study.service;

import com.jj.swm.domain.study.dto.request.RecruitPositionUpsertRequest;
import com.jj.swm.domain.study.dto.response.RecruitmentPositionCreateResponse;
import com.jj.swm.domain.study.entity.Study;
import com.jj.swm.domain.study.entity.StudyRecruitmentPosition;
import com.jj.swm.domain.study.repository.RecruitmentPositionRepository;
import com.jj.swm.domain.study.repository.StudyRepository;
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
    public RecruitmentPositionCreateResponse create(
            UUID userId,
            Long studyId,
            RecruitPositionUpsertRequest createRequest
    ) {
        Study study = studyRepository.findByIdAndUserId(studyId, userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "study not found"));

        StudyRecruitmentPosition recruitmentPosition = StudyRecruitmentPosition.of(study, createRequest);
        recruitmentPositionRepository.save(recruitmentPosition);

        return RecruitmentPositionCreateResponse.from(recruitmentPosition);
    }

    @Transactional
    public void update(
            UUID userId,
            Long recruitPositionId,
            RecruitPositionUpsertRequest updateRequest
    ) {
        StudyRecruitmentPosition recruitmentPosition =
                recruitmentPositionRepository.findByIdAndStudyUserId(recruitPositionId, userId)
                        .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "recruitment position not found"));

        if (updateRequest.getHeadcount() < updateRequest.getAcceptedCount()) {
            throw new GlobalException(ErrorCode.NOT_VALID, "The number of accepted exceeds the recruitment limit.");
        }

        recruitmentPosition.modify(updateRequest);
    }

    @Transactional
    public void delete(UUID userId, Long recruitPositionId) {
        recruitmentPositionRepository.deleteByIdAndStudyUserId(recruitPositionId, userId);
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
