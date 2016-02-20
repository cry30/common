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

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author royce
 *
 */
public class ResourceIterTest {

    /**
     * Test method for
     * {@link ph.rye.common.lang.ResourceIter#each(ph.rye.common.lang.MultiIndexLoopBody)}
     * .
     */
    @Test
    @SuppressWarnings("PMD.MethodNamingConventions")
    public void testEachMultiIndexLoopBody_withKeySingle() {
        final ResourceIter sut = new ResourceIter("resource");
        sut.each((nextElement, index) -> {
            switch (index[0]) {
                case 0:
                    Assert.assertEquals("A", nextElement);
                    break;
                case 1:
                    Assert.assertEquals("B", nextElement);
                    break;
                case 2:
                    Assert.assertEquals("C", nextElement);
                    break;
                default:
                    Assert.assertEquals("D", nextElement);
                    break;
            }

        } , "single");
    }

    /**
     * Test method for
     * {@link ph.rye.common.lang.ResourceIter#each(ph.rye.common.lang.MultiIndexLoopBody, java.lang.String)}
     * .
     */
    @Test
    @SuppressWarnings("PMD.MethodNamingConventions")
    public void testEachMultiIndexLoopBody_noKeySingle() {
        final List<String> valueList = new ArrayList<>();
        final ResourceIter sut = new ResourceIter("resource");
        sut.each((nextElement, index) -> {
            valueList.add(nextElement);
        });

        Assert.assertThat(valueList, is(Arrays.asList(new String[] {
                "good bye",
                "A",
                "B",
                "C",
                "D" })));
    }


}
