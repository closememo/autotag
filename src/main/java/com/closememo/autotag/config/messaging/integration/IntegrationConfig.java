package com.closememo.autotag.config.messaging.integration;

import static org.reflections.scanners.Scanners.SubTypes;

import com.closememo.autotag.config.messaging.kafka.KafkaMessageConverter;
import com.closememo.autotag.infra.messaging.DomainEvent;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.support.GenericWebApplicationContext;

@Configuration
public class IntegrationConfig {

  private final Set<Class<? extends DomainEvent>> domainEvents;

  public IntegrationConfig() {
    Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forClass(DomainEvent.class))
        .setScanners(SubTypes));

    domainEvents = reflections.getSubTypesOf(DomainEvent.class);
  }

  @Bean
  public KafkaMessageDrivenChannelAdapter<?, ?> kafkaMessageDrivenAdapter(
      @Qualifier("kafkaListenerContainerFactory") KafkaListenerContainerFactory<?> kafkaListenerContainerFactory,
      @Qualifier("kafkaMessageConverter") KafkaMessageConverter kafkaMessageConverter) {

    String[] topics = domainEvents.stream()
        .map(Class::getSimpleName)
        .toArray(String[]::new);
    ConcurrentMessageListenerContainer<?, ?> container
        = (ConcurrentMessageListenerContainer<?, ?>) kafkaListenerContainerFactory.createContainer(topics);
    KafkaMessageDrivenChannelAdapter<?, ?> kafkaMessageDrivenChannelAdapter
        = new KafkaMessageDrivenChannelAdapter<>(container);
    kafkaMessageDrivenChannelAdapter.setMessageConverter(kafkaMessageConverter);
    kafkaMessageDrivenChannelAdapter.setOutputChannel(inboundKafkaMessageChannel());

    return kafkaMessageDrivenChannelAdapter;
  }

  @Bean
  public MessageChannel inboundKafkaMessageChannel() {
    return new DirectChannel();
  }

  @Bean
  public IntegrationFlow routeKafkaInboundMessage() {
    return IntegrationFlows.from(inboundKafkaMessageChannel())
        .route("headers.kafka_receivedTopic")
        .get();
  }

  @Autowired
  public void registerDomainEventChannels(GenericWebApplicationContext context,
      @Qualifier("messageTaskExecutor") ThreadPoolTaskExecutor messageTaskExecutor) {
    domainEvents.forEach(aClass ->
        context.registerBean(
            aClass.getSimpleName(),
            PublishSubscribeChannel.class,
            () -> new PublishSubscribeChannel(messageTaskExecutor)));
  }
}
