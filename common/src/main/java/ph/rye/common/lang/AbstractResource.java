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
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Resource reader.
 * 
 * <pre>
 * $Author$ 
 * $Date$
 * </pre>
 * 
 * @author r39
 */

public abstract class AbstractResource {


    /**
     * Apply custom processing to every indexed resource.
     * 
     * @param string
     */
    protected abstract void process(String string, int... index);

    /**
     * 
     * @param resource resource bundle.
     * @param key prefix for indexed keys. (e.g. key0, key1);
     */
    public void execute(final String resource, final String key)
    {
        final ResourceBundle resBundle = ResourceBundle.getBundle(resource);
        final List<String> idxList = new ArrayList<String>();
        for (final Enumeration<String> en = resBundle.getKeys(); en
            .hasMoreElements();) {
            final String next = en.nextElement();
            if (next.startsWith(key)) {
                idxList.add(next.substring(key.length()));
            }
        }
        Collections.sort(idxList, new Comparator<String>() {

            @Override
            public int compare(final String o1, final String o2)
            {

                return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
            }
        });
        if (!idxList.isEmpty()) {
            int counter = 0;
            for (final String idxStr : idxList) {
                final String[] idxArr = idxStr.split("_");

                if (idxArr.length == 1
                        && Integer.valueOf(idxArr[0]) == counter++
                        || idxArr.length > 1) {

                    final int[] idxIntArr = new int[idxArr.length];
                    for (int j = 0; j < idxIntArr.length; j++) {
                        idxIntArr[j] = Integer.parseInt(idxArr[j]);
                    }
                    process(resBundle.getString(key + idxStr), idxIntArr);
                }
            }
        }

    }

}