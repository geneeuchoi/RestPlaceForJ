package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.CardRequestDto;
import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.dto.CardUpdateRequestDto;
import com.sparta.restplaceforj.dto.PlanResponseDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.service.CardService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/columns/{column-id}/cards")
public class CardController {

  private final CardService cardService;

  /**
   * 카드 생성 controller
   *
   * @param columnId
   * @param cardRequestDto
   * @return CommonResponse
   */
  @PostMapping
  public ResponseEntity<CommonResponse<CardResponseDto>> createCard(

      @PathVariable("column-id") Long columnId,
      @RequestBody @Valid CardRequestDto cardRequestDto) {

    CardResponseDto cardResponseDto = cardService.createCard(
        columnId,
        cardRequestDto
    );

    return ResponseEntity.ok(
        CommonResponse.<CardResponseDto>builder()
            .response(ResponseEnum.CREATE_CARD)
            .data(cardResponseDto)
            .build()
    );
  }

  /**
   * 카드 수정 controller
   *
   * @param cardId
   * @param cardUpdateRequestDto
   * @return CommonResponse
   */
  @PatchMapping("/{card-id}")
  public ResponseEntity<CommonResponse<CardResponseDto>> updateCard(
      @RequestParam("card-id") Long cardId,
      @RequestBody @Valid CardUpdateRequestDto cardUpdateRequestDto) {

    CardResponseDto cardResponseDto = cardService.updateCard(cardId, cardUpdateRequestDto);

    return ResponseEntity.ok(
        CommonResponse.<CardResponseDto>builder()
            .response(ResponseEnum.UPDATE_CARD)
            .data(cardResponseDto)
            .build()
    );
  }

  /**
   * 카드 전체 조희 controller
   *
   * @param columnId
   * @return ResponseEntity
   */
  @GetMapping
  public ResponseEntity<CommonResponse<List<CardResponseDto>>> getCardList(
      @RequestParam(value = "column-id") Long columnId) {

    return ResponseEntity.ok(
        CommonResponse.<List<CardResponseDto>>builder()
            .response(ResponseEnum.FIND_CARD)
            .data(cardService.getCardList(columnId))
            .build());

  }

  /**
   * 카드 단건 조회 controller
   *
   * @param cardId
   * @return ResponseEntity
   */
  @GetMapping("/{card-id}")
  public ResponseEntity<CommonResponse<CardResponseDto>> getCard(
      @PathVariable("card-id") Long cardId) {

    return ResponseEntity.ok(
        CommonResponse.<CardResponseDto>builder()
            .response(ResponseEnum.FIND_CARD)
            .data(cardService.getCard(cardId))
            .build());
  }

  /**
   * 카드 삭제  controller
   *
   * @param cardId
   * @return ResponseEntity
   */
  @DeleteMapping("/{card-id}")
  public ResponseEntity<CommonResponse<CardResponseDto>> deleteCard(
      @PathVariable("card-id") Long cardId) {

    cardService.deleteCard(cardId);

    return ResponseEntity.ok(
        CommonResponse.<CardResponseDto>builder()
            .response(ResponseEnum.DELETE_CARD)
            .data(null)
            .build());
  }

  /**
   * 카드 게시물 추가  controller
   *
   * @param cardId 카드 아이디
   * @param postId 게시물 아이디
   * @return PostResponseDto : id, userId, title, content, address, likesCount, viewsCount,
   * themeEnum
   */
  @PostMapping("/{card-id}/posts/{post-id}")
  public ResponseEntity<CommonResponse<PostResponseDto>> cardAddPost(
      @PathVariable("card-id") Long cardId,
      @PathVariable("post-id") Long postId) {

    PostResponseDto postResponseDto = cardService.cardAddPost(cardId, postId);

    return ResponseEntity.ok(
        CommonResponse.<PostResponseDto>builder()
            .response(ResponseEnum.ADD_POST)
            .data(postResponseDto)
            .build());
  }

  /**
   * 카드 연관 게시물 다건 조회 controller
   *
   * @param cardId 유저 아이디
   * @return PlanResponseDto : id, title
   */
  @GetMapping("/posts")
  public ResponseEntity<CommonResponse<List<PostResponseDto>>> getPostList(
      @PathVariable("column-id") Long columnId,
      @RequestParam Long cardId) {
    List<PostResponseDto> postResponseDtoList = cardService
        .getPostList(cardId, columnId);

    return ResponseEntity.ok(
        CommonResponse.<List<PostResponseDto>>builder()
            .response(ResponseEnum.GET_PLAN_LIST)
            .data(postResponseDtoList)
            .build()
    );
  }

}

