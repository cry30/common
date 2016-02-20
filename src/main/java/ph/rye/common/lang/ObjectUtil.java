/**
 *   Copyright 2014 Royce Remulla
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
package ph.rye.common.lang;

import java.util.Map;


/**
 * Common object utility.
 *
 * <pre>
 * $Author: $
 * $Date: $
 * $HeadURL: $
 * </pre>
 *
 * @author Royce.
 */
public final class ObjectUtil {


    private ObjectUtil() {}


    /**
     * Get value from map and initialize when empty.
     *
     * @param map target map.
     * @param key key.
     * @param valForNull value to initialize map key with in case it is null.
     *
     * @param <K> map key generic type.
     * @param <V> map value generic type.
     */
    public static <K, V> V mapGetInit(final Map<K, V> map, final K key,
                                      final V valForNull) {
        if (map.get(key) == null) {
            map.put(key, valForNull);
        }
        return map.get(key);
    }

    /**
     * Compares two objects for equality.
     *
     * @param object1 first object.
     * @param object2 second object.
     */
    public static boolean isEqual(final Object object1, final Object object2) {

        boolean retval;
        if (object1 == null && object2 == null) {
            retval = true;
        } else if (object2 == null) {
            retval = object1 != null;
        } else if (object1 == null) {
            retval = object2 != null;
        } else {
            retval = object1.equals(object2);
        }
        return retval;
    }

    /**
     * Return first parameter if non-null, otherwise return the second
     * parameter.
     *
     * @param ifObj return if non-null;
     * @param elseObj return if null.
     * @param <T> generic method, any type of object.
     */
    public static <T> T nvl(final T ifObj, final T elseObj) {
        return ifObj == null ? elseObj : ifObj;
    }

    /**
     * PLSQL decode function. NOTE: Use traditional if-then-elseif or switch
     * statement for performance consideration.
     *
     * @param expression is the value to compare. S
     * @param search is the value that is compared against expression. S
     * @param result is the value returned, if expression is equal to search.
     *            default is optional.
     *
     * @see Unit Test: PrsUtil_decodeTest.
     */
    public static Object decode(final Object expression, final Object search,
                                final Object... result) {

        final java.util.List<Object> ifList = new java.util.ArrayList<>();
        ifList.add(search);

        final java.util.List<Object> thenList = new java.util.ArrayList<>();

        final Ano<Object> defaultResult =
                new Ano<>(buildIfThenList(ifList, thenList, result));
        final Ano<Object> retval = new Ano<>(defaultResult.get());
        for (int i = 0; i < ifList.size(); i++) {
            final Object nextIf = ifList.get(i);
            if (isEqual(expression, nextIf)) {
                retval.set(thenList.get(i));
                break;
            }
        }

        return retval.get();
    }

    private static Object buildIfThenList(final java.util.List<Object> ifList,
                                          final java.util.List<Object> thenList,
                                          final Object... result) {

        final Ano<Object> defaultResult = new Ano<>();
        if (result == null || result.length == 0) {
            thenList.add(null);

        } else {
            if (result.length % 2 == 0) {
                defaultResult.set(result[result.length - 1]);
            }

            new Range<Object>(0, result.length - 1).each((i, nextElement) -> {
                if (i % 2 == 1 && i != result.length - 1) {
                    ifList.add(result[i]);
                } else if (i % 2 == 0) {
                    thenList.add(result[i]);
                }
            });
        }

        return defaultResult.get();
    }


}
