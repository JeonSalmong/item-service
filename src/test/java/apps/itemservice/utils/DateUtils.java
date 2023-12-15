package apps.itemservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtils {
    @Test
    void yearDiff() {

        // 오늘날짜 가져오기
        LocalDate today = LocalDate.now();

        // 입력받은 숫자
        int inputNumber = 10;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyMMdd");
        // 입력받은 숫자만큼 날짜를 빼기
        LocalDate subtractDate = today.minusYears(inputNumber);

        log.info(today.format(dateTimeFormatter));
        log.info(today.toString());
        log.info(subtractDate.toString());

    }
}
