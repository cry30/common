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
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <pre>
 * @author $Author$ 
 * @version $Date$
 * </pre>
 */
public class ReflectUtil {


    /** Internal source control version. */
    public static final String RCS_ID = "$Revision$";


    /**
     * Returns all fields of a bean including inherited.
     * 
     * @param bean - the bean whose properties are to be derived. Must not be
     *            null.
     * 
     * @return - array of bean fields/properties
     */
    public static Field[] getAllFields(final Object bean)
    {
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


    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return super.toString() + " " + RCS_ID;
    }

}