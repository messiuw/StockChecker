package com.messiuw.query;

import com.messiuw.exceptions.QueryBuilderException;

import static com.messiuw.query.StockData.*;

/**
 * Created by matthias.polkehn on 06.03.2018.
 */
public class QueryBuilder {

    private String requestedData;
    private String symbol;
    private String finalQuery;
    private String timeFrame;
    private String listspecifier;

    public QueryBuilder(StockData stockData, SpecifierIF specifier, String symbol) throws QueryBuilderException {
        if (stockData == null) {
            throw new QueryBuilderException("No Stock data Request has been entered!");
        }
        this.requestedData = stockData.getValue();

        if ((requestedData.contains("[TIMEFRAME]") || requestedData.contains("[SPECIFIER]")) && specifier == null) {
            throw new QueryBuilderException("Requested StockData requires " + (requestedData.contains("[TIMEFRAME]") ? "[TIMEFRAME]" : "[SPECIFIER]") + " specifier!");
        }
        if (requestedData.contains("[SYMBOL]") && symbol == null) {
            throw new QueryBuilderException("Requested StockData requires symbol to be specified!");
        }

        if (stockData.equals(DIVIDENDS) || stockData.equals(CHART)) {
            this.timeFrame = specifier.getValue();
        }

        if (stockData.equals(LIST)) {
            this.listspecifier = specifier.getValue();
        }

        if (symbol != null) {
            this.symbol = symbol;
        }
    }

    public String build() {
        finalQuery = this.requestedData;
        if (this.requestedData.contains("[TIMEFRAME]")) {
            finalQuery = finalQuery.replace("[TIMEFRAME]",this.timeFrame);
        }
        if (this.requestedData.contains("[SYMBOL]")) {
            finalQuery = finalQuery.replace("[SYMBOL]",this.symbol);
        }
        if (this.requestedData.contains("[SPECIFIER]")) {
            finalQuery = finalQuery.replace("[SPECIFIER]",this.listspecifier);
        }
        return finalQuery;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setTimeFrame(TimeFrame timeFrame) {
        this.timeFrame = timeFrame.getValue();
    }

    public void setlistspecifier(ListSpecifier specifier) {
        this.listspecifier = specifier.getValue();
    }

    public String getFinalQuery() {
        return this.finalQuery;
    }
}