package com.messiuw.apiconnector;

import com.messiuw.exceptions.QueryBuilderException;
import com.messiuw.query.QueryBuilder;
import com.messiuw.query.StockData;
import com.messiuw.query.TimeFrame;
import com.messiuw.request.APIConnector;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ApiConnectorTest {

    private QueryBuilder queryBuilder;

    private final String scheme = "https";
    private final String host = "api.iextrading.com/1.0";

    @Before
    public void testQueryBuilder() {
        try {
            this.queryBuilder = new QueryBuilder(StockData.CHART, TimeFrame.ONE_DAY, "AAPL");
        } catch (QueryBuilderException qbe) {
            qbe.printStackTrace();
        }
    }

    @Test(expected = QueryBuilderException.class)
    public void testQueryBuilderException() throws QueryBuilderException {
        QueryBuilder queryBuilder = new QueryBuilder(StockData.COMPANY,null,null);
    }

    @Test
    public void apiConnectorTest() throws Exception {
        QueryBuilder queryBuilder = new QueryBuilder(StockData.CHART, TimeFrame.ONE_DAY, "AAPL");
        APIConnector apiConnector = new APIConnector(scheme,host);
        apiConnector.executeRequest(queryBuilder);
    }

    @Test
    public void testBasicHttpConnection() {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(scheme).setHost(host).setPath(queryBuilder.build());

        HttpGet httpGet;
        try {
            httpGet = new HttpGet(uriBuilder.build());
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        try(CloseableHttpClient closeableHttpClient = HttpClients.createDefault())  {
            RequestConfig configuration = createRequestConfig();
            httpGet.setConfig(configuration);
            try(CloseableHttpResponse response = closeableHttpClient.execute(httpGet)) {

                testResponse(response);
                testResponseIsJsonObject(response);

            } catch (Exception ex) {
                System.out.println("Error during request execution");
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.println("Error with HTTP Client");
            ex.printStackTrace();
        }
    }

    private RequestConfig createRequestConfig() {
        RequestConfig.Builder builder = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD);
        return builder.build();
    }

    private void testResponse(CloseableHttpResponse response) {
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(response.getHeaders("content-type")[0].toString().contains("json"));
    }

    private void testResponseIsJsonObject(CloseableHttpResponse response) throws Exception {
        String responseString = EntityUtils.toString(response.getEntity());
        Object object = new JSONTokener(responseString).nextValue();
        assertTrue(object instanceof JSONArray);
    }

}
