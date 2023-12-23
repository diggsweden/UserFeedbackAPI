// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.controller;

import com.google.gson.JsonObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.digg.api.form.RedisForm;
import se.digg.api.logic.RedisCacheLogic;
import se.digg.api.response.CustomResponse;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@Slf4j
@Tag(name = "Redis controller")
@RequestMapping("/redis")
public class RedisController {

    private RedisCacheLogic redisCacheLogic;

    @Autowired
    private void setImpressionLogic(final RedisCacheLogic redisCacheLogic) {
        this.redisCacheLogic = redisCacheLogic;
    }

    @PostMapping(value = "/set", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> set(@Valid @RequestBody RedisForm redisForm) {
        Object response = redisCacheLogic.set(redisForm.getKey(), redisForm.getValue());

        return ResponseEntity.ok(response.toString());
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@RequestParam String key) {
        Object response = redisCacheLogic.get(key);

        return ResponseEntity.ok(response.toString());
    }

    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@RequestParam String key) {
        CustomResponse customResponse = new CustomResponse();
        JsonObject jsonResponse = customResponse.buildResponse(CustomResponse.Status.SUCCESS, "Removed key:" + key + " (and value) from Redis!");

        redisCacheLogic.delete(key);

        return ResponseEntity.ok(jsonResponse.toString());
    }

    @DeleteMapping(value = "/delete/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete() {
        CustomResponse customResponse = new CustomResponse();
        JsonObject jsonResponse = customResponse.buildResponse(CustomResponse.Status.SUCCESS, "Removed all keys from Redis!");

        redisCacheLogic.deleteAll();

        return ResponseEntity.ok(jsonResponse.toString());
    }

    @GetMapping(value = "/keycount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> keyCount() {
        int count = redisCacheLogic.keyCount();

        return ResponseEntity.ok(count);
    }

    @GetMapping(value = "/keys", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> keys() {
        String[] keys = redisCacheLogic.getKeys();

        return ResponseEntity.ok(Arrays.toString(keys));
    }
}
