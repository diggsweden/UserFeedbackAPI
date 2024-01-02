// SPDX-FileCopyrightText: 2023 Digg - Agency for Digital Government
//
// SPDX-License-Identifier: MIT

package se.digg.api.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
public class WebUtils {

    public static String decodeURL(String s) {
        return decodeURL(s, "iso-8859-1");
    }

    public static String decodeURL(String s, String encoding) {
        if (s == null || s.trim().length() == 0)
            return "";

        try {
            return URLEncoder.encode(s, encoding);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }

        return s;
    }
}
