package com.closememo.autotag.config.messaging.kafka;

import com.closememo.autotag.infra.messaging.DomainEvent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class KafkaConfig {

  @Bean
  public ObjectMapper kafkaObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(LocalDateTime.class,
        new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
    javaTimeModule.addDeserializer(LocalDateTime.class,
        new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
    javaTimeModule.addSerializer(ZonedDateTime.class,
        new ZonedDateTimeSerializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    javaTimeModule.addDeserializer(ZonedDateTime.class,
        InstantDeserializer.ZONED_DATE_TIME);

    mapper.registerModule(javaTimeModule);
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    return mapper;
  }

  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory(
      ConsumerFactory<String, Object> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setMessageConverter(kafkaMessageConverter());
    factory.setConcurrency(3);
    return factory;
  }

  @Bean
  public KafkaMessageConverter kafkaMessageConverter() {
    return new KafkaMessageConverter(kafkaObjectMapper(), getTopicClassMap());
  }

  private Map<String, Class<?>> getTopicClassMap() {
    Reflections reflections = new Reflections("com.closememo.autotag.infra.messaging");
    return reflections.getSubTypesOf(DomainEvent.class)
        .stream()
        .map(clazz -> Pair.of(clazz.getSimpleName(), clazz))
        .collect(Collectors.toUnmodifiableMap(Pair::getLeft, Pair::getRight));
  }
}