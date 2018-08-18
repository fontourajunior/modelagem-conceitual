package br.com.cursomc.cursomodelagemconceitual.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {

    public static List<Integer> decodeIntList(String s) {
        return Arrays.stream(s.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public static String decodeParam(String param) {
        try {
            return URLDecoder.decode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
