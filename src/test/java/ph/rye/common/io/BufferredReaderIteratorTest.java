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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author royce
 *
 */
public class BufferredReaderIteratorTest {

    /**
     * Test method for {@link ph.rye.common.io.ReaderIterator#eachLine()}.
     */
    @Test
    public void testEachLine() {
        new BufferedReaderIterator(
            new BufferedReader(
                new InputStreamReader(
                    new ByteArrayInputStream("test".getBytes()))),
            (index, nextElement) -> {
                Assert.assertEquals("test", nextElement);

            }).eachLine();
    }

    /**
     * Test method for {@link ph.rye.common.io.ReaderIterator#eachLine()}.
     *
     * @throws IOException
     */
    @Test(expected = ReaderException.class)
    @SuppressWarnings("PMD.MethodNamingConventions")
    public void testEachLine_IOEx() throws IOException {

        final BufferedReader mockBf = Mockito.mock(BufferedReader.class);
        Mockito.doThrow(new IOException()).when(mockBf).readLine();

        new BufferedReaderIterator(mockBf, (index, nextElement) -> {})
            .eachLine();
    }


}
