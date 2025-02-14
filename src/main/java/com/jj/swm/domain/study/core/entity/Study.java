package com.jj.swm.domain.study.core.entity;

import com.jj.swm.domain.study.core.dto.request.AddStudyRequest;
import com.jj.swm.domain.study.core.dto.request.ModifyStudyRequest;
import com.jj.swm.domain.study.core.dto.request.ModifyStudyStatusRequest;
import com.jj.swm.domain.study.recruitmentposition.entity.StudyRecruitmentPosition;
import com.jj.swm.domain.user.core.entity.User;
import com.jj.swm.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@Table(name = "study")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update study set deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at is null")
public class Study extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "open_chat_url", length = 300, nullable = false)
    private String openChatUrl;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "category", nullable = false)
    private StudyCategory category;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @Column(name = "comment_count", nullable = false)
    private int commentCount;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status", nullable = false)
    private StudyStatus status;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "study")
    private List<StudyTag> studyTagList = new ArrayList<>();

    @OneToMany(mappedBy = "study")
    private List<StudyRecruitmentPosition> studyRecruitmentPositionList = new ArrayList<>();

    public static Study of(User user, AddStudyRequest request) {
        return Study.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .openChatUrl(request.getOpenChatUrl())
                .category(request.getCategory())
                .likeCount(0)
                .commentCount(0)
                .status(StudyStatus.ACTIVE)
                .viewCount(0)
                .user(user)
                .build();
    }

    public void modify(ModifyStudyRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.category = request.getCategory();
        this.openChatUrl = request.getOpenChatUrl();
    }

    public void modifyStatus(ModifyStudyStatusRequest request) {
        this.status = request.getStatus();
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount = Math.max(0, this.likeCount - 1);
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }

    public void decrementCommentCount() {
        this.commentCount = Math.max(0, this.commentCount - 1);
    }

    public void incrementViewCount() {
        this.viewCount++;
    }
}
