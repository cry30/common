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
public class ObjectUtil {


    /**
     * Get value from map and initialize when empty.
     * 
     * @param map target map.
     * @param key key.
     * @param nullVal value to initialize map key with in case it is null.
     * 
     * @param <K> map key generic type.
     * @param <V> map value generic type.
     */
    public <K, V> V mapGetInit(final Map<K, V> map, final K key, final V nullVal)
    {
        if (map.get(key) == null) {
            map.put(key, nullVal);
        }
        return map.get(key);
    }

    /**
     * Compares two objects for equality.
     * 
     * @param object1 first object.
     * @param object2 second object.
     */
    public boolean isEqual(final Object object1, final Object object2)
    {

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

}
