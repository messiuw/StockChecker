package com.messiuw.request;

import org.apache.http.HttpConnection;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONTokener;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by matthias.polkehn on 06.03.2018.
 */
public class HttpConnectionManager {
    private static HttpConnectionManager ourInstance = new HttpConnectionManager();
    private Set<CloseableHttpClient> connections = new HashSet<>();

    public static HttpConnectionManager getInstance() {
        return ourInstance;
    }

    private HttpConnectionManager() {
    }

    private boolean registerConnection(CloseableHttpClient p_connection) {
        return this.connections.add(p_connection);
    }

    public void disconnectConnection(HttpConnection p_connection) {
        try {
            for (CloseableHttpClient connection : connections) {
                if (connection.equals(p_connection)) {
                    connection.close();
                    connections.remove(connection);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error while disconnecting session " + p_connection.toString());
            ex.printStackTrace();
        }
    }

    public void disconnectAllConnections() {
        try {
            for (CloseableHttpClient connection : connections) {
                connection.close();
            }
        } catch (Exception ex) {
            System.out.println("IO Exception while disconnecting all connections...");
            ex.printStackTrace();
        }
    }

    CloseableHttpClient getCloseableHttpClient() {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        if (registerConnection(closeableHttpClient)) {
            return closeableHttpClient;
        } else {
            return null;
        }
    }

    Object executeHttpGet(HttpGet p_httpGet) {
        try(CloseableHttpClient closeableHttpClient = HttpClients.createDefault())  {
            RequestConfig configuration = createRequestConfig();
            p_httpGet.setConfig(configuration);
            try(CloseableHttpResponse response = closeableHttpClient.execute(p_httpGet)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    return null;
                }
                if (!response.getHeaders("content-type")[0].toString().contains("json")) {
                    return null;
                }
                String responseString = EntityUtils.toString(response.getEntity());
                return new JSONTokener(responseString).nextValue();
            } catch (Exception ex) {
                System.out.println("Error during request execution");
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.println("Error with HTTP Client");
            ex.printStackTrace();
        }
        return null;
    }

    private RequestConfig createRequestConfig() {
        RequestConfig.Builder builder = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD);
        return builder.build();
    }
}
