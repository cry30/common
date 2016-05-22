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
package ph.rye.common.loop;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author royce
 */
@SuppressWarnings("PMD.ShortMethodName")
public final class IterM<K, V> {


    private final Map<K, V> map;


    private IterM(final Map<K, V> map) {
        this.map = map;
    }

    public static <K, V> IterM<K, V> of(final Map<K, V> map) {
        return new IterM<>(map);
    }

    public void each(final BiConsumer<K, V> body) {
        for (final K key : map.keySet()) {
            body.accept(key, map.get(key));
        }
    }
}
