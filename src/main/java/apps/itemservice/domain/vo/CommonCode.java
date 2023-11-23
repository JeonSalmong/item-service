package apps.itemservice.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonCode {

    ATTACH_FILE_TYPE("A", "첨부파일타입"),
    IMAGES_FILE_TYPE("I", "이미지파일타입");

    private final String code;
    private final String message;
}
