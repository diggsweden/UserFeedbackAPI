package se.digg.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Getter
@Setter
public class RedisClusterConfig {

    /*private List<String> nodes;
    private int maxRedirects;

    @Bean
    LettuceConnectionFactory redisConnectionFactory(RedisClusterConfiguration redisConfiguration) {

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED).build();

        return new LettuceConnectionFactory(redisConfiguration, clientConfig);
    }

    @Bean
    RedisClusterConfiguration redisConfiguration() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(this.getNodes());
        redisClusterConfiguration.setMaxRedirects(this.getMaxRedirects());

        return redisClusterConfiguration;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    @Primary
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        // other settings...
        return template;
    }*/
}
