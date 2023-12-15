package apps.itemservice.web.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ActuatorTags {

    public static final String ORDER_TIMED = "my.order";
    public static final String ORDER_COUNTED = "my.order";

    public static final String ITEM_TIMED = "my.item";
    public static final String ITEM_COUNTED = "my.item";

    public static final String LOGIN_TIMED = "my.login";
    public static final String LOGIN_COUNTED = "my.login";

}
