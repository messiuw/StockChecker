package com.messiuw.query;

/**
 * Created by matthias.polkehn on 06.03.2018.
 */
public enum StockData {

    BATCH("/stock/[SYMBOL]/batch"),
    BOOK("/stock/[SYMBOL]/book"),
    CHART("/stock/[SYMBOL]/chart/[TIMEFRAME]"),
    COMPANY("/stock/[SYMBOL]/company"),
    DELAYED_QUOTE("/stock/[SYMBOL]/delayed-quote"),
    DIVIDENDS("/stock/[SYMBOL]/dividends/[TIMEFRAME]"),
    EARNINGS("/stock/[SYMBOL]/earnings"),
    EFFECTIVE_SPREAD("/stock/[SYMBOL]/effective-spread"),
    FINANCIALS("/stock/[SYMBOL]/financials"),
    KEY_STATS("/stock/[SYMBOL]/stats"),
    LIST("/stock/market/list/[SPECIFIER]"),
    OHLC("/stock/[SYMBOL]/ohlc"),
    PRICE("/stock/[SYMBOL]/price"),
    QUOTE("/stock/[SYMBOL]/quote"),
    VOLUME_BY_VENUE("/stock/[SYMBOL]/volume-by-venue");

    private String value;

    StockData(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
