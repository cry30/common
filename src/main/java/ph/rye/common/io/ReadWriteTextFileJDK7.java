package ph.rye.common.io;

/**
 *  Copyright 2013 Royce Remulla
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
 *
 */
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * File reader/writer.
 *
 * @author Royce Remulla
 */
public class ReadWriteTextFileJDK7 {


    /** File encoding. */
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    /** Platform independent line separator. */
    public static final String LINE_SEP = System.getProperty("line.separator");

    /**
     * Unbuffered reader of small text files. For files less than 8K.
     *
     * @param pFileName filename.
     * @throws IOException if error occurs while parsing the file.
     */
    public List<String> readSmallTextFile(final String pFileName)
            throws IOException {
        final Path path = Paths.get(pFileName);
        return Files.readAllLines(path, ENCODING);
    }

    /**
     * Write small text file. Below 8k in size.
     *
     * @param aLines list of String lines to write to file.
     * @param aFileName filename.
     * @throws IOException if error occurs while parsing the file.
     */
    public void writeSmallTextFile(final List<String> aLines,
                                   final String aFileName) throws IOException {
        final Path path = Paths.get(aFileName);
        Files.write(path, aLines, ENCODING);
    }

    /**
     * Buffered reading of large text file. 8k and above.
     *
     * @param aFileName file name of the file to read.
     * @throws IOException if error occurs while parsing the file.
     */
    public List<String> readLargeTextFile(final String aFileName)
            throws IOException {

        final List<String> retval = new ArrayList<String>();

        try (Scanner scanner =
                new Scanner(Paths.get(aFileName), ENCODING.name())) {
            while (scanner.hasNextLine()) {
                retval.add(scanner.nextLine());
            }
        }
        return retval;
    }

    /**
     * Buffered reading of large text file. 8k and above.
     *
     * @param aFileName file name of the file to read.
     * @throws IOException if error occurs while parsing the file.
     */
    public String readLargeAsString(final String aFileName) throws IOException {
        final StringBuilder retval = new StringBuilder();

        try (Scanner scanner =
                new Scanner(Paths.get(aFileName), ENCODING.name())) {

            while (scanner.hasNextLine()) {
                retval.append(scanner.nextLine());
                retval.append(LINE_SEP);
            }
        }
        return retval.toString();
    }


    /**
     * Write large text file. Above 8k in size.
     *
     * @param aFileName file name
     * @param aLines lines of String to write.
     * @throws IOException if error occurs while parsing the file.
     */
    public void writeLargeTextFile(final String aFileName,
                                   final List<String> aLines)
                                           throws IOException {
        try (BufferedWriter writer =
                Files.newBufferedWriter(Paths.get(aFileName), ENCODING)) {

            for (final String line : aLines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

}