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
package ph.rye.common.lang;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ph.rye.common.CommonException;

/**
 * @author royce
 *
 */
public final class BeanUtil {


    private BeanUtil() {}

    /**
     * Retrieves a property value of the bean. Note: This will work only when a
     * public getter method is available for the property.
     *
     * @param bean instance where we want to get the property value from. Not
     *            null.
     * @param property property name of the bean we want to get the property
     *            value from.
     * @return null when the property could not be retrieved.
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException when the bean is null or the property is
     *             not valid;
     */
    public static Object getProperty(final Object bean, final String property)
            throws NoSuchMethodException {

        assert bean != null;

        final String getterMethod = getGetterMethodName(property);

        try {
            return ReflectUtil
                .invokeMethod(bean, getterMethod, new Class[0], new Object[0]);
        } catch (final InvocationTargetException e) {
            throw CommonException.wrapperException(e);
        } catch (final IllegalArgumentException e) {
            throw CommonException.wrapperException(e);
        }
    }

    /**
     * Returns the get accessor method name of the given property name.
     *
     * @param property property name to derive getter method from. Must not be
     *            null nor empty.
     * @return accessor method name. null if the property parameter is null or
     *         and empty String.
     */
    public static String getGetterMethodName(final String property) {
        assert property != null && property.length() > 0;

        final StringBuilder retval = new StringBuilder(
            "get" + property.substring(0, 1).toUpperCase());
        if (property.length() > 1) {
            retval.append(property.substring(1));
        }
        return retval.toString();
    }


    /**
     * Returns all properties of a bean available via accessor methods.
     *
     * @param bean - the bean whose properties are to be derived.
     *
     * @return - array of bean fields/properties
     */
    public static String[] getProperties(final Object bean) {
        return getProperties(bean, new String[0]);
    }

    /**
     * TODO: Update Unit Test cases for exclusion of static fields. Returns all
     * properties of a bean available via accessor methods. Will disregard
     * static properties.
     *
     * @param bean - the bean whose properties are to be derived.
     * @param exemptParam - array of properties not will be excluded from list.
     *
     * @return - array of bean fields/properties
     */
    public static String[] getProperties(final Object bean,
                                         final String[] exemptParam) {
        assert bean != null;

        final String[] exemption = ObjectUtil.nvl(exemptParam, new String[0]);

        final List<String> exemptList =
                new ArrayList<String>(Arrays.asList(exemption));
        exemptList.add("class");

        final List<String> fields = new ArrayList<String>();
        for (final Field nextField : ReflectUtil.getAllFields(bean)) {
            final String property = nextField.getName();
            if (exemptList.contains(property)
                    || Modifier.isStatic(nextField.getModifiers())) {
                continue;
            }
            fields.add(property);
        }
        return fields.toArray(new String[fields.size()]);
    }

    /**
     * Retrieves the type of the bean property.
     *
     * @param bean bean instance whose property type we are getting. Must not be
     *            null.
     * @param property bean property whose type we are getting. Must be an
     *            existing property.
     * @return null when the property cannot be retrieved.
     */
    public static Class<?> getPropertyType(final Object bean,
                                           final String property) {
        assert bean != null;
        assert ObjectUtil.hasValue(property);

        try {
            return ReflectUtil.findField(bean, property).getType();
        } catch (final NoSuchFieldException e) {
            throw CommonException.wrapperException(
                new IllegalArgumentException(
                    "Invalid Property: " + property,
                    e));
        }
    }


}
