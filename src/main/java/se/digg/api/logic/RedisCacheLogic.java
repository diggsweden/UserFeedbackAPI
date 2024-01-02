// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import se.digg.api.service.RedisCacheService;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;

@Component
public class RedisCacheLogic {
    private final RedisCacheService redisCacheService;

    public Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RedisCacheLogic(final RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }

    public Object set(String key, Object value) {
        redisCacheService.set(key, value);
        return redisCacheService.get(key);
    }

    public Object get(String key) {
        return redisCacheService.get(key);
    }

    public void delete(String key) {
        redisCacheService.delete(key);
    }

    public void deleteAll() {
        redisCacheService.deleteAll();
    }

    public int keyCount() {
        return redisCacheService.keyCount();
    }

    public String[] getKeys() {
        return redisCacheService.getKeys();
    }

    public String getSerializedString(Object object) {
        List<String> excludedFields = List.of("id", "createdAt", "updatedAt");
        String serializedObjectString = "";

        for (Field f : object.getClass().getDeclaredFields()) {

            if (excludedFields.contains(f.getName())) {
                continue;
            }

            try {
                f.setAccessible(true);
                Object value = f.get(object);

                if (value != null) {
                    serializedObjectString += f.getName() + ":" + value.toString() + ";";
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return serializedObjectString;
    }

    public String getGsonSerializedHash(Object object) {
        String content = gson.toJson(object);
        return Base64.getEncoder().encodeToString(content.getBytes());
    }

    public String getBase64dHash(String content) {
        return Base64.getEncoder().encodeToString(content.getBytes());
    }
}
