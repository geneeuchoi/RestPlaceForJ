package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.CardDetailResponseDto;
import com.sparta.restplaceforj.dto.CardRequestDto;
import com.sparta.restplaceforj.dto.CardResponseDto;
import com.sparta.restplaceforj.dto.CardUpdateRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.Card;
import com.sparta.restplaceforj.entity.RelatedPost;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.CardPostRepository;
import com.sparta.restplaceforj.repository.CardRepository;
import com.sparta.restplaceforj.repository.ColumnRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CardService {

  private final ColumnRepository columnRepository;
  private final CardRepository cardRepository;
  private final PostRepository postRepository;
  private final CardPostRepository cardPostRepository;

  /**
   * 카드 생성 로직
   *
   * @param
   * @param columId
   * @param cardRequestDto
   * @return CardResponseDto
   */
  @Transactional
  public CardResponseDto createCard(Long columId, CardRequestDto cardRequestDto) {
    Column column = columnRepository.findByIdOrThrow(columId);
    Card card = Card.builder()
        .column(column)
        .title(cardRequestDto.getTitle())
        .address(cardRequestDto.getAddress())
        .placeName(cardRequestDto.getPlaceName())
        .startedAt(cardRequestDto.getStartedAt())
        .endedAt(cardRequestDto.getEndedAt())
        .memo(cardRequestDto.getMemo())
        .build();
    cardRepository.save(card);
    return CardResponseDto.builder()
        .id(card.getId())
        .title(card.getTitle())
        .address(card.getAddress())
        .placeName(card.getPlaceName())
        .startedAt(card.getStartedAt())
        .endedAt(card.getEndedAt())
        .memo(card.getMemo())
        .build();
  }

  /**
   * 카드 수정 로직
   *
   * @param cardId
   * @param cardUpdateRequestDto
   * @return CardResponseDto
   */
  @Transactional
  public CardResponseDto updateCard(Long cardId, CardUpdateRequestDto cardUpdateRequestDto) {

    Card card = cardRepository.findCardById(cardId);

    String title = card.getTitle();
    if (cardUpdateRequestDto.getTitle() != null) {
      title = cardUpdateRequestDto.getTitle();
    }

    String address = card.getAddress();
    if (cardUpdateRequestDto.getAddress() != null) {
      address = cardUpdateRequestDto.getAddress();
    }

    String placeName = card.getPlaceName();
    if (cardUpdateRequestDto.getPlaceName() != null) {
      placeName = cardUpdateRequestDto.getPlaceName();
    }
    LocalTime startedAt = card.getStartedAt();
    if (cardUpdateRequestDto.getStartedAt() != null) {
      startedAt = cardUpdateRequestDto.getStartedAt();
    }
    LocalTime endedAt = card.getEndedAt();
    if (cardUpdateRequestDto.getEndedAt() != null) {
      endedAt = cardUpdateRequestDto.getEndedAt();
    }
    String memo = card.getMemo();
    if (cardUpdateRequestDto.getMemo() != null) {
      memo = cardUpdateRequestDto.getMemo();
    }

    card.builder()
        .title(title)
        .address(address)
        .placeName(placeName)
        .startedAt(startedAt)
        .endedAt(endedAt)
        .memo(memo)
        .build();

    cardRepository.save(card);

    return CardResponseDto.builder().id(card.getId()).build();
  }

  /**
   * 카드 전체 조회
   *
   * @param columId
   * @return List<CardResponseDto>
   */
  public List<CardResponseDto> getCardList(Long columId) {
    Column column = columnRepository.findByIdOrThrow(columId);

    return cardRepository.findAllByColumn(column);
  }

  /**
   * 카드 단건 조희
   *
   * @param cardId
   * @return CardResponseDto
   */
  public CardDetailResponseDto getCard(Long cardId) {
    Card card = cardRepository.findCardById(cardId);
    List<PostResponseDto> postResponseDtoList = cardPostRepository.findPostsByCardId(cardId);
    return CardDetailResponseDto.builder()
        .id(cardId)
        .title(card.getTitle())
        .address(card.getAddress())
        .placeName(card.getPlaceName())
        .memo(card.getMemo())
        .postList(postResponseDtoList)
        .build();
  }

  public void deleteCard(Long cardId) {
    Card card = cardRepository.findCardById(cardId);
    cardRepository.delete(card);
  }

  /**
   * 카드 연관 게시물 추가
   *
   * @param
   * @param cardId 카드 아이디
   * @param postId 포스트 아이디
   * @return CardResponseDto List<PlanResponseDto> : planId, title
   */
  @Transactional
  public PostResponseDto cardAddPost(Long cardId, Long postId) {
    Card card = cardRepository.findCardById(cardId);
    Post post = postRepository.findByIdOrThrow(postId);
    if (cardPostRepository.findPostsByCardId(cardId).equals(post)) {
      throw new CommonException(ErrorEnum.BAD_REQUEST);
    }
    RelatedPost cardPost = RelatedPost.builder().card(card).post(post).build();
    cardPostRepository.save(cardPost);
    return PostResponseDto.builder().post(post).build();
  }
}

