package ph.rye.common.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * @author royce
 */
public class BufferedReaderIterator {

	/** Reference to the internal buffered reader. */
    private final transient BufferedReader reader;
    
    
    private final transient BiConsumer<Integer, String> body;

    private transient int index;


    public BufferedReaderIterator(final BufferedReader reader,
            final BiConsumer<Integer, String> body) {

        this.reader = reader;
        this.body = body;
        index = 0;
    }

    public void eachLine() {
        try {
            String line = reader.readLine();
            while (line != null) {
                body.accept(index, line);
                line = reader.readLine();
                index++;
            }

        } catch (final IOException e) {
            throw new ReaderException(e);
        }

    }

}

class ReaderException extends RuntimeException {
    /** */
    private static final long serialVersionUID = -257710506179020957L;

    public ReaderException(final Throwable throwable) {
        super(throwable);
    }
}
