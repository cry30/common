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

import java.lang.reflect.Array;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import ph.rye.common.lang.ObjectUtil;

/**
 * @author royce
 */
@SuppressWarnings("PMD.ShortMethodName")
public class Iter<T> {


    private final transient Iterable<T> iterable;
    private final transient T[] array;
    private final transient int[] iarray;

    private transient int index;

    public static <T> Iter<T> of(final Iterable<T> iterable) {
        return new Iter<>(iterable);
    }

    public static <T> Iter<T> of(final T[] array) {
        return new Iter<>(array);
    }

    public static <T> Iter<T> of(final int[] array) {
        return new Iter<>(array);
    }


    public void each(final Consumer<T> consumer) {
        for (final T token : iterable) {
            consumer.accept(token);
        }
    }

    @SuppressWarnings("unchecked")
    public void eachWithIndex(final BiConsumer<Integer, T> body) {
        for (final int token : this.iarray) {
            body.accept(index, (T) Integer.valueOf(token));
            index++;
        }
    }

    public static <T> void each(final Iterable<T> iterable,
                                final Consumer<T> consumer) {
        for (final T token : iterable) {
            consumer.accept(token);
        }
    }

    public static Iter<String> string(final String[] stringArray) {
        return new Iter<>(stringArray);
    }

    public Iter(final Iterable<T> iter) {
        assert iter != null;

        this.iterable = iter;
        this.array = null;
        this.iarray = null;
        index = 0;
    }

    public Iter(final T[] array) {
        assert array != null;

        this.iterable = null;
        this.iarray = null;
        this.array = array.clone();
        index = 0;
    }

    public Iter(final int[] array) {
        assert array != null;

        this.iterable = null;
        this.array = null;
        this.iarray = (int[]) Array.newInstance(Integer.TYPE, array.length);
        System.arraycopy(array, 0, this.iarray, 0, array.length);
        index = 0;
    }


    @SuppressWarnings("unchecked")
    public void each(final BiConsumer<Integer, T> iterBody) {
        index = 0;
        if (ObjectUtil.hasValue(this.array)) {
            for (final T next : this.array) {
                iterBody.accept(index, next);
                index++;
            }
        } else if (ObjectUtil.hasValue(iterable)) {
            for (final T next : iterable) {
                iterBody.accept(index, next);
                index++;
            }
        } else {
            for (final int next : iarray) {
                iterBody.accept(index, (T) Integer.valueOf(next));
                index++;
            }

        }

    }

    public int getIndex() {
        return index;
    }

}
