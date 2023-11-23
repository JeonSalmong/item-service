package apps.itemservice.web.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderForm {

    private Long memberId;
    private Long itemId;
    private int count;
}
