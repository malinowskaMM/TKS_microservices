package p.lodz.tks.rent.service.soap.controller.adapters;

import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String marshal(LocalDateTime localDateTime) throws Exception {
        return dateTimeFormatter.format(localDateTime);
    }

    @Override
    public LocalDateTime unmarshal(String dateTime) throws Exception {
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }
}