package apps.itemservice.platform.converter.typeconverter;

import lombok.extern.slf4j.Slf4j;
import apps.itemservice.platform.converter.type.IpPort;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {
    @Override
    public String convert(IpPort source) {
        log.info("convert source={}", source);
        return source.getIp() + ":" + source.getPort();
    }
}
