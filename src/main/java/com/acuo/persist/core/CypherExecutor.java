package com.acuo.persist.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface CypherExecutor {

    /**
     * Execute the supplied statement and return the results
     *
     * @param query      Parameterised cypher query to execute
     * @param parameters Parameters to inject in the query
     * @return Collection of {@link ICypherResult} results
     */
    Collection<ICypherResult> executeParameterisedStatement(String query, Map<String, Object> parameters);

    /**
     * Execute the supplied statement and return the results
     *
     * @param query Cypher query to execute
     * @return Collection of {@link ICypherResult} results
     */
    Collection<ICypherResult> executeStatement(String query);

    /**
     * Execute a collection of statements atomically
     *
     * @param queries Collection of cypher queries to execute
     * @return Collection of {@link ICypherResult} results
     */
    Collection<ICypherResult> executeStatements(Collection<String> queries);

    @SuppressWarnings("unchecked")
    default String substituteQueryParameterValues(String parameterizedQuery, Map<String, Object> queryParameter) {

        String transformedQuery = parameterizedQuery;
        // replace all parameter placeholder string with values in parameter map
        // obtained from <mytemplate>.sub file
        for (String paramKey : queryParameter.keySet()) {
            String rgx = String.format("\\{\\s*%s\\s*\\}", paramKey);
            Object paramValue = queryParameter.get(paramKey);
            String val = null;
            if (paramValue == null) {
                rgx = String.format("=\\s*\\{\\s*%s\\s*\\}", paramKey);
                val = "IS NULL";
            } else if (paramValue instanceof String) {
                val = String.format("'%s'", (String) paramValue);
            } else if (paramValue instanceof Double) {
                val = Double.toString((Double) paramValue);
            } else if (paramValue instanceof ArrayList<?>) {
                ArrayList<String> arrVals = (ArrayList<String>) paramValue;
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                for (String element : arrVals) {
                    sb.append('\'').append(element).append('\'').append(',').append(' ');
                }
                sb.setLength(sb.length() - 2);
                sb.append(']');
                val = sb.toString();
            } else {
                throw new PersistenceException(
                        String.format("Unsupported parameter value type [%s] found for parameter %s",
                                paramValue.getClass().getName(), paramKey));
            }
            transformedQuery = transformedQuery.replaceAll(rgx, val);
        }
        // finally remove any remaining AND clauses with parameter values that
        // are not specified the <mytemplate>.sub file. Note the WHERE clause
        // parameter is mandatory.
        Pattern p = Pattern.compile("([Oo][Rr]|[Aa][Nn][Dd])\\s+\\w+\\.\\w+\\s*([Ii][nN]|=)\\s*\\{\\w+\\}");
        Matcher m = p.matcher(transformedQuery);
        String result = m.replaceAll("");
        return result;
    }

}