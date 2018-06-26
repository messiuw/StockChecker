package com.messiuw.request;

import com.messiuw.query.QueryBuilder;
import com.messiuw.stocks.StockResult;
import com.messiuw.exceptions.IexTradingException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;


/**
 * Created by matthias.polkehn on 06.03.2018.
 */
public class APIConnector {

    private static final String BASE_SCHEME = "https";
    private static final String BASE_HOST = "api.iextrading.com/1.0";

    private static final boolean DEBUG = true;

    private URIBuilder uriBuilder = new URIBuilder();

    public APIConnector() {
        uriBuilder.setScheme(BASE_SCHEME).setHost(BASE_HOST);
    }

    public APIConnector(String scheme, String host) {
        uriBuilder.setScheme(scheme).setHost(host);
    }

    public StockResult executeRequest(QueryBuilder queryBuilder) throws IexTradingException {
        URI uri = null;
        String path = queryBuilder.build();
        try {
            uri = this.uriBuilder.setPath(path).build();
        } catch (Exception ex) {
            throw new IexTradingException("Error while building query. Going to terminate all open connections.");
        }

        if (uri != null) {
            if(DEBUG) System.out.println(uri.toString());
            HttpGet get = new HttpGet(uri);
            try {
                Object httpResponse = HttpConnectionManager.getInstance().executeHttpGet(get);
                String finalQuery = queryBuilder.getFinalQuery();
                return new StockResult(httpResponse,finalQuery);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

}





