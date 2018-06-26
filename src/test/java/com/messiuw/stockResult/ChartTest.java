package com.messiuw.stockResult;

import com.messiuw.query.StockData;
import com.messiuw.stocks.StockResult;
import com.messiuw.request.APIConnector;
import com.messiuw.query.QueryBuilder;
import com.messiuw.query.TimeFrame;
import org.junit.Before;
import org.junit.Test;

public class ChartTest {

    private QueryBuilder queryBuilder;

    @Before
    public void setUp() {
        try {
            queryBuilder = new QueryBuilder(StockData.CHART,TimeFrame.ONE_DAY,"AAPL");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void executeQuery() {
        APIConnector apiConnector = new APIConnector();
        try {
            StockResult stockResult = apiConnector.executeRequest(queryBuilder);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
