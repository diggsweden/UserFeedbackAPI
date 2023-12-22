package se.digg.api.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheServiceImpl implements RedisCacheService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, gson.toJson(value));
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void deleteAll() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }

    public int keyCount() {
        return redisTemplate.keys("*").size();
    }

    public String[] getKeys() {
        return redisTemplate.keys("*").toArray(new String[0]);
    }
}
