package org.zayl.jireh.tool.datahandle.util;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Jireh
 */
public class MapUtil {

    public static <K, V extends Comparable<? super V>> HashMap<K, V> sortByValue(HashMap<K, V> map) {
        HashMap<K, V> result = new LinkedHashMap<>();

        map.entrySet().stream()
                .sorted(HashMap.Entry.<K, V>comparingByValue()
                        .reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

}
