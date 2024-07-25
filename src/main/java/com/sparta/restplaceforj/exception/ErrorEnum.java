package com.sparta.restplaceforj.exception;

import com.sparta.restplaceforj.common.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorEnum implements Response {
  // 공통
  BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),

  //user error
  USER_NOT_FOUND("유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  //auth error

  //Post error
  POST_NOT_FOUND("포스트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  //Plan error
  PLAN_NOT_FOUND("플랜을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  // Column errors
  COLUMN_NOT_FOUND("컬럼을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),


  // Card errors
  CARD_NOT_FOUND("카드를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

  private final String message;
  private final HttpStatus httpStatus;

}
