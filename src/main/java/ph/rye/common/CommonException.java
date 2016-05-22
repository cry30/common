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
package ph.rye.common;

import java.lang.reflect.InvocationTargetException;

/**
 * @author royce
 *
 */
public class CommonException extends RuntimeException {


    /** */
    private static final long serialVersionUID = 1L;


    private static Class<? extends RuntimeException> appException =
            RuntimeException.class;


    CommonException(final Throwable throwable) {
        super(throwable);
    }


    public static <E extends RuntimeException> void setAppException(final Class<E> exception) {
        appException = exception;
    }

    public static RuntimeException wrapperException(final Throwable throwable) {
        try {
            return appException
                .getDeclaredConstructor(new Class[] {
                        Throwable.class })
                .newInstance(new Object[] {
                        throwable });
        } catch (InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new CommonException(e);
        }
    }


}
