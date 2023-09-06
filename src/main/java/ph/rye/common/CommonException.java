package ph.rye.common;

import java.lang.reflect.InvocationTargetException;

/**
 * @author royce
 *
 */
public class CommonException extends RuntimeException {


    /** */
    private static final long serialVersionUID = 1L;


    private static Class<? extends RuntimeException> appException =
            RuntimeException.class;


    CommonException(final Throwable throwable) {
        super(throwable);
    }


    public static <E extends RuntimeException> void setAppException(final Class<E> exception) {
        appException = exception;
    }

    public static RuntimeException wrapperException(final Throwable throwable) {
        try {
            return appException
                .getDeclaredConstructor(new Class[] {
                        Throwable.class })
                .newInstance(new Object[] {
                        throwable });
        } catch (InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new CommonException(e);
        }
    }


}
