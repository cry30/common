package ph.rye.common.builder;

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

@SuppressWarnings("PMD.ShortMethodName")
public class SQLClauseBuilder {

    /** */
    @SuppressWarnings("PMD.AvoidStringBufferField" /* Warning on type javadoc. */)
    protected transient StringBuilder strBuilder = new StringBuilder();

    /** */
    protected transient int indentCounter = 1;


    /**  */
    public SQLClauseBuilder nil(final String column)
    {
        breakClause();
        this.strBuilder.append(column).append(" IS NULL");
        return this;
    }

    /**  */
    public SQLClauseBuilder and(final String expr)
    {
        breakClause();
        this.strBuilder.append(" AND ").append(expr);
        return this;
    }

    /**  */
    public SQLClauseBuilder nnil(final String column)
    {
        breakClause();
        this.strBuilder.append(column).append(" IS NOT NULL");
        return this;
    }

    /**  */
    public SQLClauseBuilder in(final String pExpr, final String... restOfExpr)
    {
        this.strBuilder.append(" IN (").append(pExpr);
        for (final String string : restOfExpr) {
            this.strBuilder.append(", ").append(string);
        }
        this.strBuilder.append(')');

        return this;
    }

    /**  */
    public SQLClauseBuilder ins(final String pExpr, final String... restOfExpr)
    {
        this.strBuilder.append(" IN ('").append(pExpr).append('\'');
        for (final String string : restOfExpr) {
            this.strBuilder.append(", '").append(string).append('\'');
        }
        this.strBuilder.append(')');
        return this;
    }

    /**  */
    public SQLClauseBuilder expr(final String pExpr)
    {
        breakClause();
        this.strBuilder.append(pExpr);
        return this;
    }

    /**  */
    public SQLClauseBuilder eq(final String pExpr)
    {
        this.strBuilder.append(" = ").append(pExpr);
        return this;
    }

    /**
     * @param pExpr SQL expression.
     * @return SQLClause Builder instance.
     */
    public SQLClauseBuilder like(final String pExpr)
    {
        this.strBuilder.append(" LIKE ").append(pExpr);
        return this;
    }

    /**
     * @param pExpr SQL expression.
     * @return SQLClause Builder instance.
     */
    public SQLClauseBuilder reLike(final String pExpr, final String rePattern)
    {
        this.strBuilder
            .append(" REGEXP_LIKE(")
            .append(pExpr)
            .append(", ")
            .append(rePattern)
            .append(")");
        return this;
    }

    /**  */
    public SQLClauseBuilder eqs(final String pExpr)
    {
        this.strBuilder.append(" = '").append(pExpr).append('\'');
        return this;
    }

    void breakClause()
    {
        if (this.strBuilder.length() > 0) {
            this.strBuilder.append('\n').append(
                String.format("%-" + 8 + "s", " "));
        }
    }

    public String getClause()
    {
        return this.strBuilder.toString();
    }


}
