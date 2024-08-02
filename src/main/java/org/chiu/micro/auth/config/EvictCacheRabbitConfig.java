package org.chiu.micro.auth.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mingchiuli
 * @create 2022-12-25 4:13 pm
 */
@Configuration
public class EvictCacheRabbitConfig {

    public static final String CACHE_USER_EVICT_QUEUE = "cache.user.evict.queue";

    public static final String CACHE_EVICT_EXCHANGE = "cache.user.direct.exchange";

    public static final String CACHE_USER_EVICT_BINDING_KEY = "cache.user.evict.binding";

    @Bean("cacheUserEvictQueue")
    Queue queue() {
        return new Queue(CACHE_USER_EVICT_QUEUE, true, false, false);
    }

    @Bean("cacheUserEvictExchange")
    DirectExchange exchange() {
        return new DirectExchange(CACHE_EVICT_EXCHANGE, true, false);
    }

    @Bean("cacheUserEvictBinding")
    Binding binding(@Qualifier("cacheUserEvictQueue") Queue userQueue,
                    @Qualifier("cacheUserEvictExchange") DirectExchange userExchange) {
        return BindingBuilder
                .bind(userQueue)
                .to(userExchange)
                .with(CACHE_USER_EVICT_BINDING_KEY);
    }
}
