package com.donphds.ma.common.utils;

import com.donphds.ma.common.exception.FormatException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import static com.donphds.ma.common.constants.CommonConstants.PATTERN_DATE;
import static com.donphds.ma.common.constants.CommonConstants.PATTERN_DATETIME;
import static com.donphds.ma.common.constants.CommonConstants.PATTERN_TIME;

@UtilityClass
public class JsonUtil {


  public static JSON getInstance() {
    return ObjectMapperHolder.INSTANCE;
  }


  public static String stringify(Object obj) {
    try {
      return getInstance().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new FormatException(e);
    }
  }

  public static <T> T parse(String val, Class<T> clazz) {
    try {
      return getInstance().readValue(val, clazz);
    } catch (JsonProcessingException e) {
      throw new FormatException(e);
    }
  }

  public static <T> T parse(String val, TypeReference<T> type) {
    try {
      return getInstance().readValue(val, type);
    } catch (JsonProcessingException e) {
      throw new FormatException(e);
    }
  }

  public static JsonNode toJsonNode(Object val) {
    try {
      return getInstance().readTree(val instanceof String ? (String)val : stringify(val));
    } catch (JsonProcessingException e) {
      throw new FormatException(e);
    }
  }


  private static class ObjectMapperHolder {
    private static final JSON INSTANCE = new JSON();
  }

  private static class JSON extends ObjectMapper {
    JSON() {
      super();
      super.setLocale(Locale.CHINA);
      super.enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature());
      super.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
      super.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
      super.findAndRegisterModules();
      // 配置 objectMapper 时间处理
      JavaTimeModule javaTimeModule = new JavaTimeModule();
      // LocalDateTime
      javaTimeModule.addDeserializer(
        LocalDateTime.class,
        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(PATTERN_DATETIME)));
      javaTimeModule.addSerializer(
        LocalDateTime.class,
        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(PATTERN_DATETIME)));
      // LocalDate
      javaTimeModule.addDeserializer(
        LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(PATTERN_DATE)));
      javaTimeModule.addSerializer(
        LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(PATTERN_DATE)));
      // LocalTime
      javaTimeModule.addDeserializer(
        LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(PATTERN_TIME)));
      javaTimeModule.addSerializer(
        LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(PATTERN_TIME)));
      super.registerModule(javaTimeModule);
    }
  }
}



