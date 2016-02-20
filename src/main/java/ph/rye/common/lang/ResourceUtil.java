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

import java.util.ArrayList;
import java.util.List;


/**
 * Resource bundle utility.
 *
 * <pre>
 * $Author$
 * $Date$
 * </pre>
 */
public class ResourceUtil {


    /**
     * Reads a String array using the indexed key.
     *
     * @param resource resource bundle name. Not null.
     * @param key resource key. Not null.
     */
    public String[] getArray(final String resource, final String key) {
        assert resource != null;
        assert key != null;

        final List<String> retval = new ArrayList<String>();
        new ResourceIter(resource)
            .each((nextElement, index) -> retval.add(nextElement), key);

        //        new AbstractResource() {
        //            @Override
        //            protected void process(final String string, final int... index) {
        //                retval.add(string);
        //            }
        //        }.execute(resource, key);

        return retval.toArray(new String[retval.size()]);
    }

}
