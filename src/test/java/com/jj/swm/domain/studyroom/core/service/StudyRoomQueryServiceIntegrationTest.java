package com.jj.swm.domain.studyroom.core.service;

import com.jj.swm.IntegrationContainerSupporter;
import com.jj.swm.domain.studyroom.core.entity.*;
import com.jj.swm.domain.studyroom.core.fixture.StudyRoomBookmarkFixture;
import com.jj.swm.domain.studyroom.core.fixture.StudyRoomFixture;
import com.jj.swm.domain.studyroom.core.fixture.StudyRoomLikeFixture;
import com.jj.swm.domain.studyroom.core.fixture.StudyRoomOptionInfoFixture;
import com.jj.swm.domain.studyroom.core.repository.StudyRoomBookmarkRepository;
import com.jj.swm.domain.studyroom.core.repository.StudyRoomLikeRepository;
import com.jj.swm.domain.studyroom.core.repository.StudyRoomOptionInfoRepository;
import com.jj.swm.domain.studyroom.core.repository.StudyRoomRepository;
import com.jj.swm.domain.studyroom.core.dto.GetStudyRoomCondition;
import com.jj.swm.domain.studyroom.core.dto.SortCriteria;
import com.jj.swm.domain.studyroom.core.dto.response.GetStudyRoomDetailResponse;
import com.jj.swm.domain.studyroom.core.dto.response.GetStudyRoomResponse;
import com.jj.swm.domain.studyroom.review.entity.StudyRoomReview;
import com.jj.swm.domain.studyroom.review.fixture.StudyRoomReviewFixture;
import com.jj.swm.domain.studyroom.review.repository.StudyRoomReviewRepository;
import com.jj.swm.domain.user.core.fixture.UserFixture;
import com.jj.swm.domain.user.core.entity.User;
import com.jj.swm.domain.user.core.repository.UserRepository;
import com.jj.swm.global.common.dto.PageResponse;
import com.jj.swm.global.exception.GlobalException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StudyRoomQueryServiceIntegrationTest extends IntegrationContainerSupporter {

    // Service Bean
    @Autowired private StudyRoomQueryService queryService;

    // Repository Bean
    @Autowired private StudyRoomRepository studyRoomRepository;
    @Autowired private StudyRoomBookmarkRepository bookmarkRepository;
    @Autowired private StudyRoomLikeRepository likeRepository;
    @Autowired private StudyRoomOptionInfoRepository optionInfoRepository;

    // Repository Bean For Test
    @Autowired private UserRepository userRepository;
    @Autowired private StudyRoomReviewRepository reviewRepository;

    private List<User> users;
    private List<StudyRoom> studyRooms;

    /**
     * 스터디 룸 5개 생성
     * 스터디 룸 옵션 => ELECTRICAL
     * 스터디 룸 이용후기 => 5, 4, 3, 2, 1 -> 5, 4, 3, 2 -> 5, 4, 3....
     * 스터디 룸 좋아요 개수 => 5개, 4개, 3개, 2개, 1개
     */
    @BeforeEach
    void setUp(){
        User roomAdmin = userRepository.saveAndFlush(UserFixture.createRoomAdmin());

        users = createTestUsers();
        studyRooms = new ArrayList<>();

        for(int i = 5; i >= 1; i--){
            StudyRoom studyRoom = StudyRoomFixture.createStudyRoom(roomAdmin);
            studyRoom = studyRoomRepository.save(studyRoom);

            StudyRoomOptionInfo optionInfo = StudyRoomOptionInfoFixture.createOptionInfo(studyRoom);
            optionInfoRepository.save(optionInfo);

            for(int j = i; j >= 1; j--){
                StudyRoomReview review = StudyRoomReviewFixture.createReview(studyRoom, i, users.get(j - 1));
                studyRoom.addReviewStudyRoom(i);

                reviewRepository.save(review);
            }

            for(int j = 1; j <= i; j++){
                StudyRoomLike like = StudyRoomLikeFixture.createLike(studyRoom, users.get(j - 1));
                studyRoom.likeStudyRoom();

                likeRepository.save(like);
            }

            studyRooms.add(studyRoom);
        }
    }

    @Test
    @DisplayName("스터디 룸 페이지네이션 평점 순 조회에 성공한다.")
    @Transactional
    void studyRoom_getStudyRooms_orderByStars_Success(){
        //given
        GetStudyRoomCondition condition = new GetStudyRoomCondition();
        condition.setOptions(List.of(StudyRoomOption.ELECTRICAL));

        //when
        PageResponse<GetStudyRoomResponse> response = queryService.getStudyRooms(condition, null);

        //then
        for(int i = 0; i < 4; i++){
            GetStudyRoomResponse currStudyRoom = response.getData().get(i);
            GetStudyRoomResponse nextStudyRoom = response.getData().get(i + 1);

            assertThat(currStudyRoom.getStarAvg()).isGreaterThanOrEqualTo(nextStudyRoom.getStarAvg());
        }
    }

    @Test
    @DisplayName("스터디 룸 페이지네이션 좋아요 순 조회에 성공한다.")
    @Transactional
    void studyRoom_getStudyRooms_orderByLikes_Success() {
        //given
        GetStudyRoomCondition condition = new GetStudyRoomCondition();
        condition.setOptions(List.of(StudyRoomOption.ELECTRICAL));
        condition.setSortCriteria(SortCriteria.LIKE);

        //when
        PageResponse<GetStudyRoomResponse> response = queryService.getStudyRooms(condition, null);

        //then
        for(int i = 0; i < 4; i++){
            GetStudyRoomResponse currStudyRoom = response.getData().get(i);
            GetStudyRoomResponse nextStudyRoom = response.getData().get(i + 1);

            assertThat(currStudyRoom.getLikeCount()).isGreaterThanOrEqualTo(nextStudyRoom.getLikeCount());
        }
    }

    @Test
    @DisplayName("스터디 룸 페이지네이션 리뷰 순 조회에 성공한다.")
    @Transactional
    void studyRoom_getStudyRooms_orderByReviews_Success() {
        //given
        GetStudyRoomCondition condition = new GetStudyRoomCondition();
        condition.setOptions(List.of(StudyRoomOption.ELECTRICAL));
        condition.setSortCriteria(SortCriteria.REVIEW);

        //when
        PageResponse<GetStudyRoomResponse> response = queryService.getStudyRooms(condition, null);

        //then
        for(int i = 0; i < 4; i++){
            GetStudyRoomResponse currStudyRoom = response.getData().get(i);
            GetStudyRoomResponse nextStudyRoom = response.getData().get(i + 1);

            assertThat(currStudyRoom.getReviewCount()).isGreaterThanOrEqualTo(nextStudyRoom.getReviewCount());
        }
    }

    @Test
    @DisplayName("스터디 룸 페이지네이션 가격 오름차순 정렬에 성공한다.")
    @Transactional
    void studyRoom_getStudyRooms_orderByPriceAsc_Success() {
        //given
        GetStudyRoomCondition condition = new GetStudyRoomCondition();
        condition.setOptions(List.of(StudyRoomOption.ELECTRICAL));
        condition.setSortCriteria(SortCriteria.PRICE_ASC);

        //when
        PageResponse<GetStudyRoomResponse> response = queryService.getStudyRooms(condition, null);

        //then
        for(int i = 0; i < 4; i++){
            GetStudyRoomResponse currStudyRoom = response.getData().get(i);
            GetStudyRoomResponse nextStudyRoom = response.getData().get(i + 1);

            assertThat(currStudyRoom.getEntireMinPricePerHour()).isLessThanOrEqualTo(nextStudyRoom.getEntireMinPricePerHour());
        }
    }

    @Test
    @DisplayName("스터디 룸 페이지네이션 가격 내림차순 정렬에 성공한다.")
    @Transactional
    void studyRoom_getStudyRooms_orderByPriceDesc_Success() {
        //given
        GetStudyRoomCondition condition = new GetStudyRoomCondition();
        condition.setOptions(List.of(StudyRoomOption.ELECTRICAL));
        condition.setSortCriteria(SortCriteria.PRICE_DESC);

        //when
        PageResponse<GetStudyRoomResponse> response = queryService.getStudyRooms(condition, null);

        //then
        for(int i = 0; i < 4; i++){
            GetStudyRoomResponse currStudyRoom = response.getData().get(i);
            GetStudyRoomResponse nextStudyRoom = response.getData().get(i + 1);

            assertThat(currStudyRoom.getEntireMaxPricePerHour())
                    .isGreaterThanOrEqualTo(nextStudyRoom.getEntireMaxPricePerHour());
        }
    }

    @Test
    @DisplayName("스터디 룸 페이지네이션 거리 순 정렬에 성공한다.")
    @Transactional
    void studyRoom_getStudyRooms_orderByDistance_Success(){
        //given
        GetStudyRoomCondition condition = new GetStudyRoomCondition();
        condition.setOptions(List.of(StudyRoomOption.ELECTRICAL));
        condition.setSortCriteria(SortCriteria.DISTANCE);
        condition.setUserLatitude(StudyRoomFixture.distanceCount.doubleValue());
        condition.setUserLongitude(StudyRoomFixture.distanceCount.doubleValue());

        //when
        PageResponse<GetStudyRoomResponse> response = queryService.getStudyRooms(condition, null);

        //then
        for(int i = 0; i < 4; i++){
            GetStudyRoomResponse currStudyRoom = response.getData().get(i);
            GetStudyRoomResponse nextStudyRoom = response.getData().get(i + 1);

            assertThat(currStudyRoom.getCoordinates().getLatitude())
                    .isGreaterThanOrEqualTo(nextStudyRoom.getCoordinates().getLatitude());

            assertThat(currStudyRoom.getCoordinates().getLongitude())
                    .isGreaterThanOrEqualTo(nextStudyRoom.getCoordinates().getLongitude());
        }
    }


    @Test
    @DisplayName("스터디 룸 조회시 관련 locality가 없다면 빈값 조회에 성공한다.")
    @Transactional
    void studyRoom_getStudyRooms_whenWithoutLocality_Success() {
        //given
        GetStudyRoomCondition condition = new GetStudyRoomCondition();
        condition.setOptions(List.of(StudyRoomOption.ELECTRICAL));
        condition.setLocality("잘못된 locality");

        //when
        PageResponse<GetStudyRoomResponse> response = queryService.getStudyRooms(condition, null);

        //then
        assertThat(response.getData()).isEmpty();
    }

    @Test
    @DisplayName("유저 ID와 함께 조회 요청 시 북마크 정보도 반환한다.")
    @Transactional
    void studyRoom_getStudyRoomsWithUserId_thenReturnBookmarkInfo_Success() {
        //given
        User user = users.getFirst();

        StudyRoomBookmark bookmark
                = StudyRoomBookmarkFixture.createBookmark(studyRooms.getFirst(), user);

        bookmarkRepository.save(bookmark);

        GetStudyRoomCondition condition = new GetStudyRoomCondition();
        condition.setOptions(List.of(StudyRoomOption.ELECTRICAL));
        condition.setSortCriteria(SortCriteria.REVIEW);

        //when
        PageResponse<GetStudyRoomResponse> response = queryService.getStudyRooms(condition, user.getId());

        //then
        GetStudyRoomResponse studyRoom = response.getData().getFirst();

        assertThat(studyRoom.getStudyBookmarkId()).isNotNull();
    }

    @Test
    @DisplayName("스터디 룸이 조회시 관련 옵션이 없을 때 빈값 반환에 성공한다.")
    @Transactional
    void studyRoom_getStudyRoom_whenNotCorrect_emptyReturn_Success(){
        //given
        GetStudyRoomCondition condition = new GetStudyRoomCondition();
        condition.setOptions(List.of(StudyRoomOption.ALCOHOL));

        //when
        PageResponse<GetStudyRoomResponse> response = queryService.getStudyRooms(condition, null);

        //then
        assertThat(response.getData()).isEmpty();
    }

    @Test
    @DisplayName("스터디 룸 상세조회에 성공한다.")
    @Transactional
    void studyRoom_getStudyRoomDetail_Success() {
        //when
        GetStudyRoomDetailResponse response
                = queryService.getStudyRoomDetail(studyRooms.getFirst().getId(), null);

        //then
        assertThat(response.getStudyRoomId()).isEqualTo(studyRooms.getFirst().getId());
    }

    @Test
    @DisplayName("잘못된 스터디 룸 ID 접근시 상세조회에 실패한다.")
    void studyRoom_getStudyRoomDetail_thenReturnNotFound() {
        //when
        Assertions.assertThrows(GlobalException.class,
                () -> queryService.getStudyRoomDetail(-1L, null));
    }

    @Test
    @DisplayName("유저 ID와 함께 조회 요청 시 좋아요 정보도 반환한다.")
    @Transactional
    void studyRoom_getStudyRoomDetailWithUserId_thenReturnLikeId_Success() {
        //given
        User user = users.getFirst();

        //when
        GetStudyRoomDetailResponse response
                = queryService.getStudyRoomDetail(studyRooms.getFirst().getId(), user.getId());

        //then
        assertThat(response.getLikeId()).isNotNull();
    }

    @Test
    @DisplayName("유저 ID와 함께 조회 요청 시 북마크 정보도 반환한다.")
    @Transactional
    void studyRoom_getStudyRoomDetailWithUserId_thenReturnBookmarkId_Success() {
        //given
        User user = users.getFirst();
        bookmarkRepository.save(StudyRoomBookmark.of(studyRooms.getFirst(), user));

        //when
        GetStudyRoomDetailResponse response
                = queryService.getStudyRoomDetail(studyRooms.getFirst().getId(), user.getId());

        //then
        assertThat(response.getBookmarkId()).isNotNull();
    }

    @Test
    @DisplayName("특정 유저가 좋아요를 누른 스터디 룸 목록을 반환한다.")
    @Transactional
    void studyRoom_getLikedStudyRooms_Success() {
        //given
        User user = users.getFirst();

        //when
        PageResponse<GetStudyRoomResponse> response
                = queryService.getLikedStudyRooms(0, user.getId());

        //then
        assertThat(response.getData().size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("특정 유저가 북마크를 누른 스터디 룸 목록을 반환한다.")
    @Transactional
    void studyRoom_getBookmarkedStudyRooms_Success() {
        //given
        User user = users.getFirst();

        StudyRoomBookmark studyRoomBookmark
                = StudyRoomBookmarkFixture.createBookmark(studyRooms.getFirst(), user);

        bookmarkRepository.save(studyRoomBookmark);

        //when
        PageResponse<GetStudyRoomResponse> response
                = queryService.getBookmarkedStudyRooms(0, user.getId());

        //then
        assertThat(response.getData().size()).isGreaterThan(0);
        assertThat(response.getData().getFirst().getStudyBookmarkId()).isNotNull();
    }

    private List<User> createTestUsers() {
        List<User> users = UserFixture.multiUser(5);

        userRepository.saveAll(users);

        return users;
    }
}

