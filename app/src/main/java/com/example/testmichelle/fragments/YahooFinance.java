package com.example.testmichelle.fragments;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.testmichelle.activities.BasicActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class YahooFinance {
    public static RequestQueue requestQueue;

    public static void initRequestQueue(Context context){
         requestQueue = Volley.newRequestQueue(context);
    }

    public static final double[][] arrERROR = {{0}};
    public static final double[] arrERROR1D = {0};


    /*
     * @param ticker: string of tickers, max 10, seperated by comma. EX: "MSFT,TSLA,AMZN"
     * @param range: 1d 5d 1mo 3mo 6mo 1y 5y max
     * @param interval: 1m 5m 15m 1d 1wk 1mo
     */
    public static void requestSpark(String[] tickers, String range, String interval, Context act, RequestQueue requestQueue){
        String strTickers = "";
        for (String ticker : tickers){
            strTickers += (ticker + ',');
        }
        strTickers = strTickers.substring(0, strTickers.length() - 1);

        // SECRET KEY
        String key = "3Z8LSHmB1l8lfS6qpRoba35QRos3zDZ69s2JS8IJ";
        // Build URL
        String url = "https://yfapi.net/v8/finance/spark?";
        url = url + "interval=" + interval;     //add sampling interval
        url = url + "&range=" + range;          //add range
        url = url + "&symbols=" + strTickers;   //add ticker

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    /* Handles a JSON object response. Returns data in the form:
                     * TODO: figure out format to return spark data
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        double[][] arrData;
                        // parse response
                        JSONObject stockData;
                        JSONArray closePrices;
                        try {
                            int numDatapoints = response.getJSONObject(tickers[0]).getJSONArray("close").length();
                            arrData = new double[response.length()][numDatapoints];
                            for (int i=0; i<tickers.length; i++) {
                                closePrices = response.getJSONObject(tickers[i]).getJSONArray("close");
                                for(int c=0; c < closePrices.length(); c++) {
                                    double price = closePrices.getDouble(c);
                                    arrData[i][c] = price;
                                }
                            }
                        } catch (Exception e) {
                            e.getStackTrace();
                            arrData = arrERROR;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSONObject VolleyError", "Error: " + error.getMessage());

                        if (error instanceof TimeoutError) {
                            Toast.makeText(act,
                                    "Bad Network, Try again",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(act,
                                    "Bad Network, Try again",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(act,
                                    "Auth failed",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(act,
                                    "Server Not Responding",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(act,
                                    "Network Error",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(act,
                                    "try again"+error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("accept", "application/json");
                params.put("X-API-KEY", key);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    /*
     * @param ticker: string of tickers, max 10, seperated by comma. EX: "MSFT,TSLA,AMZN"
     * @param range: 1d 5d 1mo 3mo 6mo 1y 5y 10y ytd max
     * @param interval: 1m 5m 15m 1d 1wk 1mo
     */
    public static void requestChart(String ticker, String range, String interval, Context act, RequestQueue requestQueue, backTestingFragment btfragment){
        // SECRET KEY
        String key = "3Z8LSHmB1l8lfS6qpRoba35QRos3zDZ69s2JS8IJ";
        // Build URL
        String url = "https://yfapi.net/v8/finance/chart/";
        url = url + ticker + "?";               //add ticker
        url = url + "range=" + range;           //add range
        url = url + "&region=US";               //add region
        url = url + "&interval=" + interval;    //add sampling interval
        url = url + "&lang=en";                 //add language

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    /* Handles a JSON object response. Returns data in the form:
                     * [[open1, high1, low1, close1],
                     *  [open2, high2, low2, close2],
                     *  ...
                     * ]
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        double[][] finalData;
                        JSONArray JSONopen;
                        JSONArray JSONhigh;
                        JSONArray JSONclose;
                        JSONArray JSONlow;
                        String error = null;
                        Log.d("response", response.toString());
                        try {
                            JSONArray result = response.getJSONObject("chart").getJSONArray("result");
                            JSONObject indicators = result.getJSONObject(0).getJSONObject("indicators");
                            JSONObject quote = indicators.getJSONArray("quote").getJSONObject(0);
                            JSONopen = quote.getJSONArray("open");
                            JSONhigh = quote.getJSONArray("high");
                            JSONclose = quote.getJSONArray("close");
                            JSONlow = quote.getJSONArray("low");
                            finalData = new double[JSONopen.length()][4];
                            for (int i=0; i < JSONopen.length(); i++){
                                double[] row = {JSONopen.getDouble(i), JSONhigh.getDouble(i), JSONlow.getDouble(i), JSONclose.getDouble(i)};
                                finalData[i] = row;
                            }
                            /** MAKE DELIS CALL USING FINALDATA */
                            btfragment.createRules(finalData);
                        } catch (Exception e) {
                            e.getStackTrace();
                            finalData = arrERROR;
                            try {
                                error = response.getJSONObject("chart").getJSONObject("error").getString("description");
                            }catch(Exception e2){}
                            if (error != null) {
                                Toast.makeText(act,
                                    error,
                                    Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(act,
                                        "Unknown Error",
                                        Toast.LENGTH_LONG).show();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSONObject VolleyError", "Error: " + error.getMessage());

                        if (error instanceof TimeoutError) {
                            Toast.makeText(act,
                                    "Bad Network, Try again",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(act,
                                    "Bad Network, Try again",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(act,
                                    "Auth failed",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(act,
                                    "Server Not Responding",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(act,
                                    "Network Error",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(act,
                                    "try again"+error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("accept", "application/json");
                params.put("X-API-KEY", key);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public static void ta4jRequestChart(String ticker, String range, Context act, backTestingFragment btfragmnent){
        requestChart(ticker, range, "1d", act, requestQueue, btfragmnent);
    }

    /*
     * @param ticker: string of tickers, max 10, seperated by comma. EX: "MSFT,TSLA,AMZN"
     * @param range: 1d 5d 1mo 3mo 6mo 1y 5y 10y ytd max
     * @param interval: 1m 5m 15m 1d 1wk 1mo
     */
    public static void basicActivityRequestChart(String ticker, Context act, BasicActivity callbackObject, Map.Entry<String, ArrayList<Object>> entry){
        // SECRET KEY
        String key = "3Z8LSHmB1l8lfS6qpRoba35QRos3zDZ69s2JS8IJ";
        // Build URL
        String url = "https://yfapi.net/v8/finance/chart/";
        url = url + ticker + "?";               //add ticker
        url = url + "range=" + "3mo";           //add range
        url = url + "&region=US";               //add region
        url = url + "&interval=" + "1d";    //add sampling interval
        url = url + "&lang=en";                 //add language

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    /* Handles a JSON object response. Returns data in the form:
                     * [[open1, high1, low1, close1],
                     *  [open2, high2, low2, close2],
                     *  ...
                     * ]
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        double[][] finalData;
                        JSONArray JSONopen;
                        JSONArray JSONhigh;
                        JSONArray JSONclose;
                        JSONArray JSONlow;
                        try {
                            JSONArray result = response.getJSONObject("chart").getJSONArray("result");
                            JSONObject indicators = result.getJSONObject(0).getJSONObject("indicators");
                            JSONObject quote = indicators.getJSONArray("quote").getJSONObject(0);
                            JSONopen = quote.getJSONArray("open");
                            JSONhigh = quote.getJSONArray("high");
                            JSONclose = quote.getJSONArray("close");
                            JSONlow = quote.getJSONArray("low");
                            finalData = new double[JSONopen.length()][4];
                            for (int i=0; i < JSONopen.length(); i++){
                                double[] row = {JSONopen.getDouble(i), JSONhigh.getDouble(i), JSONlow.getDouble(i), JSONclose.getDouble(i)};
                                finalData[i] = row;
                            }
                            /** MAKE DELIS CALL USING FINALDATA */
                            Log.d("getprices", Arrays.deepToString(finalData));
                            callbackObject.updateAlgorithms(entry, finalData);
                        } catch (Exception e) {
                            e.getStackTrace();
                            finalData = arrERROR;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSONObject VolleyError", "Error: " + error.getMessage());

                        if (error instanceof TimeoutError) {
                            Toast.makeText(act,
                                    "Bad Network, Try again",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(act,
                                    "Bad Network, Try again",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(act,
                                    "Auth failed",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(act,
                                    "Server Not Responding",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(act,
                                    "Network Error",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(act,
                                    "try again"+error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("accept", "application/json");
                params.put("X-API-KEY", key);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public static void requestSummary(String ticker, Context act, TransactionFragment fragmentObj){
        // SECRET KEY
        String key = "3Z8LSHmB1l8lfS6qpRoba35QRos3zDZ69s2JS8IJ";
        // Build URL
        String url = "https://yfapi.net/v11/finance/quoteSummary/" + ticker + "?lang=en&region=US&modules=defaultKeyStatistics%2CfinancialData%2CesgScores";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("gotResponse", response.toString());
                        Hashtable<String, String> myDict = new Hashtable<String, String>();
                        JSONObject esgScores;
                        JSONObject defaultKeyStatistics;
                        JSONObject financialData;
                        String error = null;
                        try {
                            Log.d("start of try/catch", myDict.toString());
                            JSONArray result = response.getJSONObject("quoteSummary").getJSONArray("result");
                            esgScores = result.getJSONObject(0).getJSONObject("esgScores");
                            defaultKeyStatistics = result.getJSONObject(0).getJSONObject("defaultKeyStatistics");
                            financialData = result.getJSONObject(0).getJSONObject("financialData");

                            myDict.put("esgRaw", esgScores.getJSONObject("totalEsg").getString("fmt"));
                            myDict.put("environmentScore", esgScores.getJSONObject("environmentScore").getString("fmt"));
                            myDict.put("socialScore", esgScores.getJSONObject("socialScore").getString("fmt"));
                            myDict.put("governanceScore", esgScores.getJSONObject("governanceScore").getString("fmt"));
                            myDict.put("esgPerformance", esgScores.getString("esgPerformance"));
                            myDict.put("esgPercentile", esgScores.getJSONObject("percentile").getString("fmt"));
                            Log.d("parsedResponse1", myDict.toString());

                            myDict.put("marketCap", defaultKeyStatistics.getJSONObject("enterpriseValue").getString("fmt"));
                            myDict.put("forwardPE", defaultKeyStatistics.getJSONObject("forwardPE").getString("fmt"));
                            myDict.put("profitMargins", defaultKeyStatistics.getJSONObject("profitMargins").getString("fmt"));
                            myDict.put("priceToBook", defaultKeyStatistics.getJSONObject("priceToBook").getString("fmt"));
                            myDict.put("trailingEPS", defaultKeyStatistics.getJSONObject("trailingEps").getString("fmt"));
                            myDict.put("pegRatio", defaultKeyStatistics.getJSONObject("pegRatio").getString("fmt"));
                            Log.d("parsedResponse2", myDict.toString());

                            myDict.put("currentPrice", financialData.getJSONObject("currentPrice").getString("fmt"));
                            myDict.put("recommendationKey", financialData.getString("recommendationKey"));
                            myDict.put("numberOfAnalystOpinions", financialData.getJSONObject("numberOfAnalystOpinions").getString("fmt"));
                            myDict.put("ebitda", financialData.getJSONObject("ebitda").getString("fmt"));
                            myDict.put("earningsGrowth", financialData.getJSONObject("earningsGrowth").getString("fmt"));
                            myDict.put("revenueGrowth", financialData.getJSONObject("revenueGrowth").getString("fmt"));
                            Log.d("parsedResponse3", myDict.toString());
                        } catch (Exception e) {
                            Log.d("Exception Found!", myDict.toString());
                            try {
                                error = response.getJSONObject("quoteSummary").getJSONObject("error").getString("description");
                            }catch(Exception e2){}
                        }

                        if (error != null) {
                            Log.d("error description", error);
                            Toast.makeText(act,
                                    error,
                                    Toast.LENGTH_LONG).show();
                        }

                        /** MAKE SAMS CALL USING FINALDATA */
                        Log.d("myDictionary", myDict.toString());
                        Log.d("parsedResponseFinal", myDict.toString());
                        fragmentObj.displayData(myDict);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSONObject VolleyError", "Error: " + error.getMessage());

                        if (error instanceof TimeoutError) {
                            Toast.makeText(act,
                                    "Bad Network, Try again",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(act,
                                    "Bad Network, Try again",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(act,
                                    "Auth failed",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(act,
                                    "Server Not Responding",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(act,
                                    "Network Error",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(act,
                                    "try again"+error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("accept", "application/json");
                params.put("X-API-KEY", key);
                return params;
            }
        };
        Log.d("sentRequest", jsonObjectRequest.toString());
        requestQueue.add(jsonObjectRequest);
    }
    public static void requestSearchFragmentSpark(String ticker, String range, String interval, Context act, TransactionFragment fragmentObj){

        // SECRET KEY
        String key = "3Z8LSHmB1l8lfS6qpRoba35QRos3zDZ69s2JS8IJ";
        // Build URL
        String url = "https://yfapi.net/v8/finance/spark?";
        url = url + "interval=" + interval;     //add sampling interval
        url = url + "&range=" + range;          //add range
        url = url + "&symbols=" + ticker;   //add ticker

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    /* Handles a JSON object response. Returns data in the form:
                     * TODO: figure out format to return spark data
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        double[] arrData;
                        // parse response
                        JSONObject stockData;
                        JSONArray closePrices;
                        try {
                            int numDatapoints = response.getJSONObject(ticker).getJSONArray("close").length();
                            arrData = new double[numDatapoints];
                            closePrices = response.getJSONObject(ticker).getJSONArray("close");
                            for(int c=0; c < closePrices.length(); c++) {
                                double price = closePrices.getDouble(c);
                                arrData[c] = price;
                            }
                            fragmentObj.graph(ticker, arrData, range);

                        } catch (Exception e) {
                            e.getStackTrace();
                            arrData = arrERROR1D;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSONObject VolleyError", "Error: " + error.getMessage());

                        if (error instanceof TimeoutError) {
                            Toast.makeText(act,
                                    "Bad Network, Try again",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(act,
                                    "Bad Network, Try again",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(act,
                                    "Auth failed",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(act,
                                    "Server Not Responding",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(act,
                                    "Network Error",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(act,
                                    "try again"+error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("accept", "application/json");
                params.put("X-API-KEY", key);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}


