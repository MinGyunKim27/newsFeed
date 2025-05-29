package org.example.newsfeed.follow.repository;

import org.example.newsfeed.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * 특정 사용자가 다른 사용자를 팔로우하고 있는지 확인
     * 중복 팔로우 방지
     *
     * @param followerId 팔로우 하는 사용자 ID
     * @param followingId 팔로우 당하는 사용자 ID
     * @return 팔로우 관계 존재 여부
     */
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * 팔로우 관계 조회
     * 언팔 시 삭제할 관계를 찾기 위해 사용
     *
     * @param followerId 팔로우 하는 사용자 ID
     * @param followingId 팔로우 당하는 사용자 ID
     * @return 팔로우 관계 엔티티
     */
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    /**
     * 특정 사용자가 팔로우하는 사람들 목록 조회 (팔로잉 목록)
     *
     * @param followerId 팔로우하는 사용자 ID
     * @return  팔로우 관계 목록
     */
    @Query("SELECT f FROM Follow f JOIN FETCH f.following WHERE f.follower.id = :followerId")
    List<Follow> findFollowingByFollowerId(@Param("followerId") Long followerId);

    /**
     * 특정 사용자를 팔로우하는 사람들 목록 조회 (팔로워 목록)
     *
     * @param followingId 팔로우 당하는 사용자 ID
     * @return 팔로우 관계 목록
     */
    @Query("SELECT f FROM Follow f JOIN FETCH f.follower WHERE f.following.id = :followingId")
    List<Follow> findFollowersByFollowingId(@Param("followingId") Long followingId);

    long countByFollowerId(Long followerId);

    long countByFollowingId(Long followingId);

    /**
     * 사용자 탈퇴 시 모든 팔로우 관계 삭제
     *
     * @param userId 탈퇴하는 사용자 Id
     */
    void deleteByFollowerIdOrFollowingId(Long userId, Long userId2);
}
