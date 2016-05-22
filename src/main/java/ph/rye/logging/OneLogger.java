package ph.rye.logging;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import ph.rye.common.lang.Ano;
import ph.rye.common.lang.ObjectUtil;

/**
 * Simple logger. Configuration via property file or via runtime. Will look for
 * log4one.properties in root classes directory.
 *
 * Example log4one.properties. Copy this to class folder (e.g. build\classes)
 *
 * <pre>
 * #log4one.properties
 *
 * #defaults to INFO
 * log4one.defaultLevel=INFO
 *
 * #defaults to yes
 * log4one.showMethod=yes
 *
 * #defaults to yes
 * #log4one.showPackage=yes
 *
 * #required
 * #log4one.basePackage=ph.rye
 *
 * #defaults to no
 * log4one.printToConsole=yes
 *
 * #defaults to yes
 * log4one.isDeployed=no
 *
 * # log categories
 * log4one.logger.ph.rye.logging=DEBUG
 * </pre>
 *
 */
@SuppressWarnings({
        "PMD.GodClass" /* God of logs. */,
        "PMD.TooManyMethods" })
public class OneLogger {


    /** Allow System.out.println flag. */
    private static final boolean ENABLE_SYSOUT = true;

    /** Maximum length of RCS_ID for display. */
    static final int MAX_RCSLEN = 20;

    /** Transient session logs captured during processFormRequest. */
    static final String DEFERRED_LOGS = OneLogger.class.getName();

    /** Singleton instance. */
    private static final OneLogger INSTANCE = new OneLogger();

    /**
     * This will be the subject of the logger. Set this using getInstance(Class)
     * otherwise the calling class will be the active Class.
     */
    private static String activeClass;


    /** Original stream used to toggle blocking of System.out.println. */
    private static final PrintStream ORIG_STREAM = System.out;

    /** Original stream used to toggle blocking of System.out.println. */
    private static final PrintStream ORIG_ERR_STREAM = System.err;


    /** Empty stream used to block System.out.println. */
    private static final PrintStream EMPTY_STREAM =
            new PrintStream(new OutputStream() {
                @Override
                public void write(final int unused) {
                    //NO-OP
                }
            });

    /** Logging properties config file. */
    private static final String RESOURCE_NAME = "log4one";

    /** */
    private static final String[] LOG_PREFIX = {
            "IGNO",
            "DEBUG",
            "INFO",
            "WARN",
            "ERROR" };


    private final transient Set<String> ignoreSet = new HashSet<String>();

    /** Flag to enable/disable printing to standard output. */
    private transient boolean printToConsole;
    /** Flag to enable/disable class name or simple name. */
    private transient boolean showPackage = true;
    /**
     * Flag to enable/disable shortened (remove 'adb.oracle.apps' prefix)
     * package.
     */
    private transient boolean shortPackage = true;
    /** Base package of project. */
    private transient String basePackage = "";
    /** Flag to show or hide the calling method. */
    private boolean showMethod = true;

    private transient int defaultLevel = Level.INFO;
    private final transient Map<String, Integer> classLevel =
            new LinkedHashMap<String, Integer>();

    /** Flag for initialize from properties file. */
    private static boolean initialized;


    /** Level - Message separator. */
    static final String SEP_MSG = " - ";


    /** Log levels. */
    public class Level {

        /** Will never show. */
        public static final int OFF = 0;
        /** Most detailed. */
        public static final int IGNORE = 1;
        /** Verbose. */
        public static final int DEBUG = 2;
        /** */
        public static final int INFO = 3;
        /** Important, appears in red. */
        public static final int WARN = 4;
        /** Critical, appears in red. */
        public static final int ERROR = 5;

        /** Utility method. */
        private Level() {}
    }


    /** This is a utility logging class. */
    private OneLogger() {}

    @SuppressWarnings({
            "PMD.UncommentedEmptyConstructor",
            "PMD.UnusedFormalParameter" })
    OneLogger(final Object nop) {}


    /**
     * Factory method.
     *
     * @return Singleton instance.
     */
    public static OneLogger getInstance() {
        if (!initialized) {
            try {
                final ResourceBundle resBundle =
                        ResourceBundle.getBundle(RESOURCE_NAME);

                final String cfgDefaultLevel =
                        resBundle.getString("log4one.defaultLevel");

                final Map<String, Integer> levelToStr =
                        new HashMap<String, Integer>();

                levelToStr.put("INFO", Level.INFO);
                levelToStr.put("DEBUG", Level.DEBUG);
                levelToStr.put("WARN", Level.WARN);
                levelToStr.put("ERROR", Level.ERROR);
                levelToStr.put("OFF", Level.OFF);

                INSTANCE.defaultLevel = ObjectUtil
                    .nvl(levelToStr.get(cfgDefaultLevel), Level.INFO);

                INSTANCE.showMethod = getResourceValue(
                    resBundle,
                    "log4one.showMethod",
                    INSTANCE.showMethod);

                INSTANCE.showPackage = getResourceValue(
                    resBundle,
                    "log4one.showPackage",
                    INSTANCE.showPackage);

                INSTANCE.shortPackage = getResourceValue(
                    resBundle,
                    "log4one.shortPackage",
                    INSTANCE.shortPackage);

                INSTANCE.basePackage =
                        resBundle.getString("log4one.basePackage");

                INSTANCE.printToConsole = getResourceValue(
                    resBundle,
                    "log4one.printToConsole",
                    INSTANCE.printToConsole);


                for (final Enumeration<String> enu = resBundle.getKeys(); enu
                    .hasMoreElements();) {

                    final String logger = enu.nextElement();

                    if (logger.startsWith("log4one.logger.")
                            && !"log4one.logger.".equals(logger.trim())) {

                        final String classPrefix = logger.substring(15);

                        INSTANCE.setLevel(
                            classPrefix,
                            levelToStr.get(resBundle.getString(logger).trim()));
                    }
                }

                if (!ENABLE_SYSOUT) {
                    System.setOut(EMPTY_STREAM);
                    System.setErr(EMPTY_STREAM);
                }

                INSTANCE.print(
                    "Completed configuring from " + RESOURCE_NAME
                            + ".properties",
                    OneLogger.Level.INFO);

            } catch (final MissingResourceException mre) {

                INSTANCE.print(
                    "INFO Resource " + RESOURCE_NAME
                            + " was not found. Configure from client calls.",
                    OneLogger.Level.WARN);

            }
            initialized = true;
        }

        activeClass = "";
        return INSTANCE;
    }

    /**
     * Retrieve key values from resource.
     *
     * @param resBundle resource bundle.
     * @param resourceKey resource key.
     * @param defaultValue default value to use if resource key do not exist.
     */
    private static boolean getResourceValue(final ResourceBundle resBundle,
                                            final String resourceKey,
                                            final boolean defaultValue) {
        final Ano<Boolean> retval = new Ano<>(false);
        try {
            String resValue;
            if (resBundle.getString(resourceKey) == null) {
                resValue = "false";
            } else {
                resValue = resBundle.getString(resourceKey).trim();
            }

            retval.set(Arrays
                .asList(new String[] {
                        "yes",
                        "true" })
                .contains(resValue.toLowerCase(Locale.getDefault())));
        } catch (final MissingResourceException mre) { //NOPMD Reviewed.
            retval.set(defaultValue);
        }
        return retval.get();
    }


    public void setShortPackage(final boolean shortPackage) {
        this.shortPackage = shortPackage;
    }

    /**
     * @param source source class.
     * @param level log level.
     *
     * @param <T> source class type.
     */
    public <T> void setLevel(final Class<T> source, final int level) {
        if (source != null && level <= Level.ERROR) {
            setLevel(source.getName(), level);
        }
    }


    /**
     * @param source source class name.
     * @param level log level.
     */
    public void setLevel(final String source, final int level) {
        if (source != null && level <= Level.ERROR) {
            classLevel.put(source, level);
        }
    }

    private void log(final Object pMessage, final StackTraceElement ste,
                     final int level) {

        if (printToConsole && isPrinted(getClassName(ste), level)) {

            final String classNameDisp = getClassNameDisp(ste);
            final String methName = getMethodDisp(ste);
            final int lineNo = ste.getLineNumber();

            print(
                classNameDisp + methName + ":" + lineNo + SEP_MSG + pMessage,
                level);
        }

    }

    public void log(final Object message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, defaultLevel);
    }

    public void info(final Object message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, Level.INFO);
    }

    public void infof(final String message, final Object... args) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(String.format(message, args), ste, Level.INFO);
    }

    public void ignore(final Object message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, Level.IGNORE);
    }

    /**
     * Note changes are for testing purpose on eclipse only in case I
     * accidentally commit.
     *
     * @param message debug message.
     */
    public void debug(final Object message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, Level.DEBUG);
    }

    /**
     * Note changes are for testing purpose on eclipse only in case I
     * accidentally commit.
     *
     * @param message debug message.
     */
    public void debugf(final String message, final Object... args) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(String.format(message, args), ste, Level.DEBUG);
    }

    public void warn(final Object message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, Level.WARN);
    }

    public void error(final Object message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, Level.ERROR);
    }

    public void log(final Object message, final Throwable exception) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

        final String dispMessage = getDispMessage(message, exception);
        log(dispMessage, ste, defaultLevel);
    }

    public void info(final Object message, final Throwable exception) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

        final String dispMessage = getDispMessage(message, exception);
        log(dispMessage, ste, Level.INFO);
    }

    public void debug(final Object message, final Throwable exception) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

        final String dispMessage = getDispMessage(message, exception);
        log(dispMessage, ste, Level.DEBUG);
    }

    public void warn(final Object message, final Throwable exception) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

        final String dispMessage = getDispMessage(message, exception);
        log(dispMessage, ste, Level.WARN);
    }

    public void error(final Object message, final Throwable exception) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];

        final String dispMessage = getDispMessage(message, exception);
        log(dispMessage, ste, Level.ERROR);
    }

    public void log(final Throwable message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, defaultLevel);
    }

    public void info(final Throwable message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, Level.INFO);
    }

    public void debug(final Throwable message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, Level.DEBUG);
    }

    public void warn(final Throwable message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, Level.WARN);
    }

    public void error(final Throwable message) {
        final StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log(message, ste, Level.ERROR);
    }

    private void log(final Throwable message, final StackTraceElement ste,
                     final int level) {
        if (printToConsole && isPrinted(getClassName(ste), level)) {

            final String classNameDisp = getClassNameDisp(ste);
            final String methName = getMethodDisp(ste);
            final int lineNo = ste.getLineNumber();

            print(
                classNameDisp + methName + ":" + lineNo + SEP_MSG + "\n"
                        + stackTraceToString(message),
                level);
        }
    }

    /**
     * Converts the stack trace to a string object.
     *
     * @param exception - the throwable instance of which to translate.
     * @return String representation of the stack trace.
     * @exception IllegalArgumentException when the e parameter is null.
     */
    String stackTraceToString(final Throwable exception) {
        String retval = null; //NOPMD: null default, conditionally redefine.
        if (exception != null) {
            final StringBuilder strBuilder = new StringBuilder();
            final StackTraceElement[] steArr = exception.getStackTrace();
            for (final StackTraceElement stackTraceElement : steArr) {
                strBuilder.append(stackTraceElement.toString());
                strBuilder.append('\n');
            }
            retval = strBuilder.toString();
        }
        return retval;
    }

    /** Checks the className against the ignore Set; */
    private boolean isPrinted(final String className, final int level) {
        final Ano<Boolean> retval = new Ano<>(true);
        if (className != null) {
            retval.set(!isInIgnoreList(className));
            if (retval.get()) {
                retval.set(isUnIgnoredPrinted(retval.get(), className, level));
            }
        }
        return retval.get();
    }

    private boolean isUnIgnoredPrinted(final boolean pCurrVal,
                                       final String className,
                                       final int level) {
        final Ano<Boolean> retval = new Ano<>(pCurrVal);
        final Ano<Boolean> isIdentified = new Ano<>(false);

        for (final String nextClsLvl : classLevel.keySet()) {
            if (className.startsWith(nextClsLvl)) {
                retval.set(
                    level >= classLevel.get(nextClsLvl)
                            && classLevel.get(nextClsLvl) != Level.OFF);

                isIdentified.set(true);
            }
        }
        if (!isIdentified.get()) {
            retval.set(level >= defaultLevel);
        }
        return retval.get();
    }

    boolean isInIgnoreList(final String className) {
        boolean retval = false; //NOPMD: false default, conditionally redefine.
        for (final String nextIgnore : ignoreSet) {
            if (className.startsWith(nextIgnore)) {
                retval = true;
                break;
            }
        }
        return retval;
    }

    String getDispMessage(final Object message, final Throwable exception) {
        return message == null ? "null\n"
                : message.toString() + '\n' + stackTraceToString(exception);
    }

    String getMethodDisp(final StackTraceElement ste) {
        return isShowMethod() ? '.' + ste.getMethodName() : "";
    }

    String getClassNameDisp(final StackTraceElement ste) {

        final String className =
                "".equals(activeClass) ? ste.getClassName() : activeClass;

        final Ano<String> retval = new Ano<>();
        if (showPackage) {
            retval.set(className);
            if (shortPackage) {
                retval.set(
                    className.substring(INSTANCE.basePackage.length() + 1));
            }
        } else {
            retval.set(className.substring(className.lastIndexOf('.') + 1));
        }
        return retval.get();
    }

    String getClassName(final StackTraceElement ste) {
        return "".equals(activeClass) ? ste.getClassName() : activeClass;
    }

    @SuppressWarnings("PMD.SystemPrintln")
    private void print(final String message, final int level) {
        final Date now = new Date();

        final DateFormat format =
                new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());

        if (Level.ERROR == level || Level.WARN == level) {
            System.setErr(ORIG_ERR_STREAM);

            System.err.printf(
                "%s %5s %s\n",
                format.format(now),
                LOG_PREFIX[level - 1],
                message);

            if (!ENABLE_SYSOUT) {
                System.setErr(EMPTY_STREAM);
            }

        } else {
            System.setOut(ORIG_STREAM);

            System.out.printf(
                "%s %5s %s\n",
                format.format(now),
                LOG_PREFIX[level - 1],
                message);


            if (!ENABLE_SYSOUT) {
                System.setOut(EMPTY_STREAM);
            }
        }
    }

    public void setPrintToConsole(final boolean pPrintToConsole) {
        printToConsole = pPrintToConsole;
    }

    public void setShowPackage(final boolean pShowPackage) {
        showPackage = pShowPackage;
    }

    public void setShowMethod(final boolean pShowMethod) {
        showMethod = pShowMethod;
    }

    private boolean isShowMethod() {
        return showMethod;
    }

    /** Override. */
    @Override
    public String toString() {
        return getClass().getName();
    }

    public void setDefaultLevel(final int pDefaultLevel) {
        defaultLevel = pDefaultLevel;
    }

    public int getDefaultLevel() {
        return defaultLevel;
    }

}
