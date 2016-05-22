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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ph.rye.logging.OneLogger;


/**
 */
public final class ReflectUtil {


    private static final OneLogger LOG1 = OneLogger.getInstance();


    private ReflectUtil() {}


    /**
     * Returns all fields of a bean including inherited.
     *
     * @param bean - the bean whose properties are to be derived. Must not be
     *            null.
     *
     * @return - array of bean fields/properties
     */
    public static Field[] getAllFields(final Object bean) {
        assert bean != null;

        final List<Field> retval = new ArrayList<Field>();
        Class<?> current = bean.getClass();
        while (!current.equals(Object.class)) {
            for (final Field field : current.getDeclaredFields()) {
                retval.add(field);
            }
            current = current.getSuperclass();
        }
        return retval.toArray(new Field[retval.size()]);
    }

    /**
     * Helper method for calling method on a bean. Exception during target
     * invocation is thrown back to the client code. For primitive typed return,
     * the corresponding wrapper method should be called on return Object.
     *
     * @param object object instance to invoke the method from. Not null.
     * @param methodName method name. Not null. Must be existing.
     * @param types method parameter types.
     * @param args argument passed to the bean via reflection
     * @return null if any exception is encountered other than the
     *         InvocationTargetException
     * @throws InvocationTargetException - when an exception is thrown inside
     *             the target object.
     * @throws NoSuchMethodException
     */
    public static Object invokeMethod(final Object object,
                                      final String methodName,
                                      final Class<?>[] types,
                                      final Object[] args)
            throws InvocationTargetException, NoSuchMethodException {
        assert object != null;
        assert ObjectUtil.hasValue(methodName);

        Object retval = null; //NOPMD: null default, conditionally redefine.
        try {
            final Method method =
                    getDeclaredOrInheritedMethod(object, methodName, types);
            if (method != null) {

                if (!method.isAccessible()) {
                    final Method caller = getDeclaredOrInheritedMethod(
                        method,
                        "setAccessible",
                        new Class<?>[] {
                                Boolean.TYPE });
                    caller.invoke(method, new Object[] {
                            Boolean.TRUE });
                }
                retval = method.invoke(object, args);
            }
        } catch (final IllegalArgumentException e) {
            LOG1.debug(e.getMessage(), e);
        } catch (final SecurityException e) {
            LOG1.debug(e.getMessage(), e);
        } catch (final IllegalAccessException e) {
            LOG1.debug(e.getMessage(), e);
        }
        return retval;
    }

    /**
     * Helper method for calling method on a bean. Exception during target
     * invocation is thrown back to the client code. For primitive typed return,
     * the corresponding wrapper method should be called on return Object.
     *
     * @param object object instance to invoke the method from. Not null.
     * @param methodName method name. Not null. Must be existing.
     * @param types method parameter types.
     * @param args argument passed to the bean via reflection
     * @return null if any exception is encountered other than the
     *         InvocationTargetException
     * @throws InvocationTargetException - when an exception is thrown inside
     *             the target object.
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("unchecked")
    public static <T> T staticInvokeMethod(final Class<?> klass,
                                           final String methodName,
                                           final Class<?>[] types,
                                           final Object[] args)
            throws InvocationTargetException, NoSuchMethodException {
        assert klass != null;
        assert ObjectUtil.hasValue(methodName);

        Object retval = null; //NOPMD: null default, conditionally redefine.
        try {
            final Method method = klass.getDeclaredMethod(methodName, types);
            if (method != null) {

                if (!method.isAccessible()) {
                    final Method caller = getDeclaredOrInheritedMethod(
                        method,
                        "setAccessible",
                        new Class<?>[] {
                                Boolean.TYPE });
                    caller.invoke(method, new Object[] {
                            Boolean.TRUE });
                }
                retval = method.invoke(null, args);
            }
        } catch (final IllegalArgumentException e) {
            LOG1.debug(e.getMessage(), e);
        } catch (final SecurityException e) {
            LOG1.debug(e.getMessage(), e);
        } catch (final IllegalAccessException e) {
            LOG1.debug(e.getMessage(), e);
        }
        return (T) retval;
    }

    /**
     * Will get the accessible method from the class either declared or
     * inherited. Will attempt to retrieve generic method as well.
     *
     * @param object - object instance containing the method.
     * @param methName - method name.
     * @param types - argument type for the method.
     * @return the accessible method. null if no accessible method with the
     *         correct signature is found.
     *
     * @throws SecurityException When the object exist but the calling class do
     *             not have access to the method.
     *
     * @throws NoSuchMethodException when the method was not found in the passed
     *             object instance.
     */
    static Method getDeclaredOrInheritedMethod(final Object object,
                                               final String methName,
                                               final Class<?>[] types)
            throws NoSuchMethodException {

        Class<?> current = object.getClass();
        final Ano<Method> retval = new Ano<>();

        do {
            try {
                retval.set(current.getDeclaredMethod(methName, types));
            } catch (final NoSuchMethodException nsme) {
                current = retry(methName, types, current, retval);
            }
        } while (current != null && retval == null);
        return retval.get();
    }


    /**
     * @param methName
     * @param types
     * @param current
     * @param retval
     * @return
     */
    private static Class<?> retry(final String methName, final Class<?>[] types,
                                  final Class<?> current,
                                  final Ano<Method> retval) {
        @SuppressWarnings("unchecked")
        final Class<Object>[] objTypes = new Class[types.length];
        Arrays.fill(objTypes, Object.class);
        try {
            retval.set(current.getDeclaredMethod(methName, objTypes));
        } catch (final NoSuchMethodException nsmeIn) {
            return current.getSuperclass();
        }
        return current;
    }

    /**
     * @param bean - the bean whose field is to be derived. Must not be null.
     *
     * @param fieldName field name. Must not be null.
     */
    static Field findField(final Object bean, final String fieldName)
            throws NoSuchFieldException {

        assert bean != null;
        assert fieldName != null;

        final Ano<Field> retval = new Ano<>();
        Class<?> current = bean.getClass();
        while (!current.equals(Object.class)) {
            for (final Field field : current.getDeclaredFields()) {
                if (field.getName().equals(fieldName)) {
                    retval.set(field);
                    break;
                }
            }
            current = current.getSuperclass();
        }

        if (retval.get() == null) {
            throw new NoSuchFieldException(fieldName);
        }

        return retval.get();
    }

    /**
     * Returns field of a bean including inherited.
     *
     * @param bean - the bean whose properties are to be derived. Must not be
     *            null.
     *
     * @return - array of bean fields/properties
     */
    public static Field getField(final Object bean, final String name) {
        assert bean != null;

        final Ano<Field> retval = new Ano<>();
        Class<?> current = bean.getClass();
        while (!current.equals(Object.class)) {
            try {
                retval.set(current.getDeclaredField(name));
            } catch (NoSuchFieldException | SecurityException e) {
                current = current.getSuperclass();
            }
        }
        return retval.get();
    }


}