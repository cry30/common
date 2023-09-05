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
package ph.rye.common.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author royce
 *
 */
public class ConsoleInputReaderTest {

    /**
     * Test method for {@link ph.rye.common.io.ConsoleInputReader#readInput()}.
     *
     * @throws IOException
     */
    @Test
    public void testReadInput() throws IOException {
        final InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream("dummy".getBytes()));

        final ConsoleInputReader sut = Mockito.spy(new ConsoleInputReader());
        Assert.assertEquals("dummy", sut.readInput());
        System.setIn(stdin);
    }
}
