package ph.rye.common.builder;

/**
 * Use this locally inside method to avoid the risk of memory leak due to
 * StringBuffer instance.
 *
 */
public class SQLBuilder {

    /** */
    @SuppressWarnings("PMD.AvoidStringBufferField" /* Warning on type javadoc. */)
    protected transient StringBuilder strBuilder = new StringBuilder();


    public SQLBuilder select(final String firstCol,
                             final String... restOfCols) {
        strBuilder.append("SELECT ").append(firstCol);

        for (final String string : restOfCols) {
            strBuilder.append(", ").append(string);
        }
        strBuilder.append('\n');
        return this;
    }

    public SQLBuilder from(final String firstTable,
                           final String... restOfTables) {
        strBuilder.append(pad(4)).append("FROM ").append(firstTable);

        for (final String string : restOfTables) {
            strBuilder.append('\n').append(pad(7)).append(", ").append(string);
        }
        strBuilder.append('\n');
        return this;
    }

    public SQLBuilder where(final String where) {
        strBuilder.append(pad(4)).append("WHERE ").append(pad(2)).append(where);
        return this;
    }

    public SQLBuilder where(final SQLClauseBuilder sqlClauseBld) {
        strBuilder.append(pad(4)).append("WHERE ").append(pad(2)).append(
            sqlClauseBld.getClause());
        return this;
    }

    public SQLBuilder and(final String and, final String... restOfAnd) {
        strBuilder.append('\n').append(pad(8)).append("AND ").append(and);

        for (final String string : restOfAnd) {
            strBuilder
                .append('\n')
                .append(pad(8))
                .append("AND ")
                .append(string);
        }
        strBuilder.append('\n');
        return this;
    }

    public String getScript() {
        return strBuilder.toString();
    }

    String pad(final int spaces) {
        return spaces == 0 ? "" : String.format("%-" + spaces + "s", " ");
    }
}
