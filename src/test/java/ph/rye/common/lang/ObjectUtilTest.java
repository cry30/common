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

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author royce
 *
 */
public class ObjectUtilTest {

    /**
     * Test method for
     * {@link ph.rye.common.lang.ObjectUtil#mapGetInit(java.util.Map, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testMapGetInit() {

        Assert.assertEquals(
            "world",
            ObjectUtil
                .mapGetInit(new HashMap<String, String>(), "hello", "world"));

    }

    /**
     * Test method for
     * {@link ph.rye.common.lang.ObjectUtil#isEqual(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testIsEqual() {
        Assert.assertTrue(ObjectUtil.isEqual(null, null));
    }

    /**
     * Test method for
     * {@link ph.rye.common.lang.ObjectUtil#nvl(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testNvl() {
        Assert.assertSame(true, ObjectUtil.nvl(true, 1));
        Assert.assertSame(1, ObjectUtil.nvl(null, 1));
    }

    /**
     * Test method for
     * {@link ph.rye.common.lang.ObjectUtil#decode(java.lang.Object, java.lang.Object, T[])}
     * .
     */
    @Test
    public void testDecode() {

        Assert.assertSame(1, ObjectUtil.decode(true, true, 1));
        Assert.assertSame(1, ObjectUtil.decode(false, false, 1));

        Assert.assertSame(1, ObjectUtil.decode(true, true, 1, false, 2));

        Assert.assertSame(2, ObjectUtil.decode(false, true, 1, false, 2));

        Assert.assertSame('E', ObjectUtil.decode('z', 'a', 'A', 'b', 'B', 'E'));

        //nulls
        Assert.assertEquals(
            null,
            ObjectUtil.decode(null, null, null, "not null"));

        Assert.assertNull(ObjectUtil.decode('a', 'a', (Object[]) null));

        Assert.assertNull(ObjectUtil.decode('a', 'a'));


    }

}

