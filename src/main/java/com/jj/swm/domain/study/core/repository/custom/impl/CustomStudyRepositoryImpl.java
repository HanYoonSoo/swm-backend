package com.jj.swm.domain.study.core.repository.custom.impl;

import com.jj.swm.domain.study.core.dto.GetStudyCondition;
import com.jj.swm.domain.study.core.dto.SortCriteria;
import com.jj.swm.domain.study.core.entity.Study;
import com.jj.swm.domain.study.core.entity.StudyCategory;
import com.jj.swm.domain.study.core.entity.StudyStatus;
import com.jj.swm.domain.study.core.repository.custom.CustomStudyRepository;
import com.jj.swm.domain.study.recruitmentposition.entity.RecruitmentPositionTitle;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Supplier;

import static com.jj.swm.domain.study.core.entity.QStudy.study;


@RequiredArgsConstructor
public class CustomStudyRepositoryImpl implements CustomStudyRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Study> findPagedStudyListByCondition(int pageSize, GetStudyCondition condition) {
        return jpaQueryFactory.selectFrom(study)
                .where(
                        studyTitleContains(condition.getTitle()),
                        studyCategoryEq(condition.getCategory()),
                        studyStatusEq(condition.getStatus()),
                        recruitmentPositionTitleExists(condition.getRecruitmentPositionTitleList()),
                        createSortPredicate(condition)
                )
                .orderBy(createOrderSpecifier(condition.getSortCriteria()))
                .limit(pageSize)
                .fetch();
    }

    private BooleanBuilder studyTitleContains(String title) {
        return this.nullSafeBuilder(() -> study.title.contains(title));
    }

    private BooleanBuilder studyCategoryEq(StudyCategory category) {
        return this.nullSafeBuilder(() -> study.category.eq(category));
    }

    private BooleanBuilder studyStatusEq(StudyStatus status) {
        return this.nullSafeBuilder(() -> study.status.eq(status));
    }

    private BooleanBuilder recruitmentPositionTitleExists(List<RecruitmentPositionTitle> titleList) {
        return titleList == null || titleList.isEmpty()
                ? null
                : this.nullSafeBuilder(() -> study.studyRecruitmentPositionList.any().title.in(titleList));
    }

    private BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    private OrderSpecifier<?>[] createOrderSpecifier(SortCriteria sortCriteria) {
        return switch (sortCriteria) {
            case SortCriteria.LIKE -> new OrderSpecifier<?>[]{
                    new OrderSpecifier<>(Order.DESC, study.likeCount),
                    new OrderSpecifier<>(Order.DESC, study.id),
            };
            case SortCriteria.NEWEST -> new OrderSpecifier<?>[]{
                    new OrderSpecifier<>(Order.DESC, study.id)
            };
        };
    }

    private BooleanBuilder createSortPredicate(GetStudyCondition condition) {
        Integer lastSortValue = condition.getLastSortValue();
        SortCriteria sortCriteria = condition.getSortCriteria();
        Long lastStudyId = condition.getLastStudyId();

        return switch (sortCriteria) {
            case LIKE -> this.nullSafeBuilder(() -> study.likeCount.lt(lastSortValue)
                    .or(study.likeCount.eq(lastSortValue).and(study.id.lt(lastStudyId))));
            default -> this.nullSafeBuilder(() -> study.id.lt(lastStudyId));
        };
    }
}
