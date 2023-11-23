package apps.itemservice.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    NOT_FOUND_USER(1, "등록된 사용자가 없습니다."),
    NOT_VALID_TOKEN(2, "토큰이 없습니다.");

    private final int code;
    private final String message;
}
