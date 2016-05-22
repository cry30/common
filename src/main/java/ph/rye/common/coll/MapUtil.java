/**
 *   Copyright 2016 Royce Remulla
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ph.rye.common.coll;

import java.util.Map;

/**
 * @author royce
 *
 */
public final class MapUtil {


    private MapUtil() {}


    /**
     * Copy map contents.
     *
     * @param dest destination map.
     * @param source source map.
     *
     * @param <K> the type of keys maintained by this map
     * @param <V> the type of mapped values
     */
    public static <K, V> void copyMap(final Map<K, V> dest,
                                      final Map<K, V> source) {
        for (final K key : source.keySet()) {
            dest.put(key, source.get(key));
        }
    }

}
