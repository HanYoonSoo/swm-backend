package com.jj.swm.domain.studyroom.core.dto.response;

import com.jj.swm.domain.studyroom.core.entity.StudyRoom;
import com.jj.swm.domain.studyroom.core.entity.embeddable.Address;
import com.jj.swm.domain.studyroom.core.entity.embeddable.Coordinates;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class GetStudyRoomDetailResponse {

    private Long studyRoomId;
    private Long likeId;
    private Long bookmarkId;
    private String title;
    private String subtitle;
    private String introduce;
    private String notice;
    private String guideline;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Address address;
    private Coordinates coordinates;
    private String referenceUrl;
    private String phoneNumber;
    private Integer minReserveTime;
    private Integer entireMinHeadcount;
    private Integer entireMaxHeadcount;
    private String thumbnail;
    private int likeCount;
    private int reviewCount;
    private double starAvg;
    private List<GetStudyRoomTagResponse> tags;
    private List<GetStudyRoomImageResponse> images;
    private List<GetStudyRoomReserveTypeResponse> reserveTypes;
    private List<GetStudyRoomDayOffResponse> dayOffs;
    private List<GetStudyRoomOptionInfoResponse> optionInfos;
    private List<GetStudyRoomTypeInfoResponse> typeInfos;

    public static GetStudyRoomDetailResponse of(
            StudyRoom studyRoom,
            Long likeId,
            Long bookmarkId,
            List<GetStudyRoomImageResponse> imageResponses,
            List<GetStudyRoomDayOffResponse> dayOffResponses,
            List<GetStudyRoomReserveTypeResponse> reserveTypeResponses,
            List<GetStudyRoomOptionInfoResponse> optionInfoResponses,
            List<GetStudyRoomTypeInfoResponse> typeInfoResponses
    ) {
        return GetStudyRoomDetailResponse.builder()
                .studyRoomId(studyRoom.getId())
                .likeId(likeId)
                .bookmarkId(bookmarkId)
                .title(studyRoom.getTitle())
                .subtitle(studyRoom.getSubtitle())
                .introduce(studyRoom.getIntroduce())
                .notice(studyRoom.getNotice())
                .guideline(studyRoom.getGuideline())
                .openingTime(studyRoom.getOpeningTime())
                .closingTime(studyRoom.getClosingTime())
                .address(studyRoom.getAddress())
                .coordinates(studyRoom.getCoordinates())
                .referenceUrl(studyRoom.getReferenceUrl())
                .phoneNumber(studyRoom.getPhoneNumber())
                .minReserveTime(studyRoom.getMinReserveTime())
                .entireMinHeadcount(studyRoom.getEntireMinHeadcount())
                .entireMaxHeadcount(studyRoom.getEntireMaxHeadcount())
                .thumbnail(studyRoom.getThumbnail())
                .likeCount(studyRoom.getLikeCount())
                .reviewCount(studyRoom.getReviewCount())
                .starAvg(studyRoom.getAverageRating())
                .tags(studyRoom.getTags() != null ?
                        studyRoom.getTags().stream().map(GetStudyRoomTagResponse::from).toList()
                        : null
                )
                .images(imageResponses)
                .dayOffs(dayOffResponses)
                .optionInfos(optionInfoResponses)
                .typeInfos(typeInfoResponses)
                .reserveTypes(reserveTypeResponses)
                .build();
    }
}
