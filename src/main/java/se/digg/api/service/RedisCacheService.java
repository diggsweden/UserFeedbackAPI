// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.service;

public interface RedisCacheService {

    void set(String key, Object value);

    Object get(String key);

    void delete(String key);

    void deleteAll();

    int keyCount();

    String[] getKeys();
}
