package org.example.newsfeed.user.repository;

import org.example.newsfeed.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * User 엔티티에 대한 데이터 접근 레이어입니다.
 * Spring Data JPA를 통해 사용자 관련 CRUD 및 검색 기능을 제공합니다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 주어진 사용자 이름과 일치하는 사용자 수를 반환합니다.
     * 주로 중복 검사 용도로 사용됩니다.
     *
     * @param username 검사할 사용자 이름
     * @return 일치하는 사용자 수
     */
    int countByUsername(String username);

    /**
     * 주어진 이메일과 일치하는 사용자 수를 반환합니다.
     * 이메일 중복 여부 확인용입니다.
     *
     * @param email 검사할 이메일
     * @return 일치하는 사용자 수 (double 반환은 의도된 것이 아니라면 int로 바꾸는 것이 바람직)
     */
    double countByEmail(String email); // ⚠ double은 부적절, int 또는 long이 일반적입니다.

    /**
     * 주어진 이메일과 일치하는 사용자를 조회합니다.
     *
     * @param email 사용자 이메일
     * @return Optional<User> 객체로 반환 (존재하지 않을 경우 empty)
     */
    Optional<User> findByEmail(String email);

    /**
     * 사용자 이름에 특정 문자열이 포함된 모든 사용자 목록을 조회합니다.
     * 부분 검색에 사용됩니다.
     *
     * @param username 포함된 문자열
     * @return 조건에 부합하는 사용자 리스트
     */
    List<User> findByUsernameContaining(String username);

    /**
     * 사용자 이름에 특정 문자열이 포함된 사용자 목록을 페이징 처리하여 조회합니다.
     *
     * @param username 포함된 문자열
     * @param pageable 페이지 정보
     * @return 조건에 부합하는 사용자 페이지 객체
     */
    Page<User> findByUsernameContaining(String username, Pageable pageable);

    /**
     * 사용자 이름에 특정 문자열이 포함되며, 특정 ID를 제외한 사용자 목록을 페이징하여 조회합니다.
     * 자기 자신을 검색 결과에서 제외할 때 주로 사용됩니다.
     *
     * @param username 포함된 문자열
     * @param id 제외할 사용자 ID
     * @param pageable 페이지 정보
     * @return 조건에 부합하는 사용자 페이지 객체
     */
    Page<User> findByUsernameContainingAndIdIsNot(String username, Long id, Pageable pageable);
}
