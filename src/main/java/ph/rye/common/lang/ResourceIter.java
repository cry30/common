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

import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resource reader. Iterates with keys that have the format key_n_n.
 *
 * <pre>
 * $Author$
 * $Date$
 * </pre>
 *
 * @author royce
 */
public class ResourceIter {


    private static final String PATTERN_INDEX =
            "+_(\\d{1,3})(_\\d{1,3})?(_\\d{1,3})?";


    private final transient String resourceName;


    /**
     * Keys are expected to follow the format: key_0 or key_0_0_0. <br />
     * 0 <= index <= 999.
     *
     * @param resourceName
     */
    public ResourceIter(final String resourceName) {
        this.resourceName = resourceName;
    }

    public void each(final MultiIndexLoopBody loopBody, final String key) {

        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle(resourceName);

        final Map<String, String> sortToKeyTreeMap = new TreeMap<>();
        for (final Enumeration<String> en = resourceBundle.getKeys(); en
            .hasMoreElements();) {

            final String next = en.nextElement();
            String patternStr;
            if (key == null) {
                patternStr = "[A-Za-z_]" + PATTERN_INDEX;
            } else {
                patternStr = key + PATTERN_INDEX;
            }

            final Pattern pattern = Pattern.compile(patternStr);
            final Matcher matcher = pattern.matcher(next);
            if (matcher.find()) {
                final int indexPos = next.indexOf('_');
                final String keyName = next.substring(0, indexPos);
                final String sortableKeyName = keyName + '_'
                        + toSortable(next.substring(indexPos + 1));
                sortToKeyTreeMap.put(sortableKeyName, next);
            }

        }

        for (final String sortableKey : sortToKeyTreeMap.keySet()) {

            final String actualKey = sortToKeyTreeMap.get(sortableKey);

            final String indexStr =
                    actualKey.substring(actualKey.indexOf('_') + 1);

            final int[] indexArr = toIntArray(indexStr.split("_"));
            loopBody.next(resourceBundle.getString(actualKey).trim(), indexArr);
        }

    }

    public void each(final MultiIndexLoopBody loopBody) {
        each(loopBody, null);
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private int[] toIntArray(final String[] strArray) {
        final int[] retval = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            retval[i] = Integer.valueOf(strArray[i]);
        }
        return retval;
    }

    private static String toSortable(final String indexStr) {
        return regexpReplace(
            regexpReplace(
                regexpReplace(indexStr, "(^|_)(\\d{3})", "$1$2"),
                "(^|_)(\\d{2})",
                "$10$2"),
            "(^|_)(\\d)",
            "$100$2");

    }

    private static String regexpReplace(final String string,
                                        final String pattern,
                                        final String replaceWith) {
        return string.replaceAll(pattern, replaceWith);
    }

}