package org.example.newsfeed.follow.dto;

import lombok.Getter; // Lombok에서 제공하는 Getter 어노테이션. 모든 필드에 대한 getter 메서드를 자동 생성합니다.

/**
 * 사용자가 특정 대상(예: 다른 사용자 등)을 팔로우하고 있는지 여부를 응답으로 전달하기 위한 DTO 클래스입니다.
 */
@Getter // isFollow 필드의 getter 메서드가 자동으로 생성됩니다.
public class IsFollowResponseDto {

    /**
     * 팔로우 여부를 나타내는 불리언 값입니다.
     * true이면 팔로우 중, false이면 팔로우하지 않은 상태를 의미합니다.
     */
    private final boolean isFollow;

    /**
     * 생성자. 팔로우 여부를 파라미터로 받아 해당 값을 필드에 할당합니다.
     *
     * @param isFollow 사용자가 팔로우 중인지 여부
     */
    public IsFollowResponseDto(boolean isFollow){
        this.isFollow = isFollow;
    }
}
