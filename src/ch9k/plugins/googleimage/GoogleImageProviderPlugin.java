package ch9k.plugins.googleimage;

import ch9k.plugins.ImageProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Class that gives us the ability to look up images on Flickr.
 * @author Jasper Van der Jeugt
 */
public class GoogleImageProviderPlugin extends ImageProvider {
    /**
     * Logger, well, to log.
     */
    private static final Logger LOGGER =
            Logger.getLogger(GoogleImageProviderPlugin.class.getName());


    /**
     * The google API needs a referer.
     */
    private final String HTTP_REFERER = "http://zeus.ugent.be/";

    /**
     * Our Google API key.
     */
    private final String API_KEY =
            "ABQIAAAAs8RaJYu0ZebpBO6jB93ADhTsxjHa7uN5E720o7nIY50" +
            "-3t3KCxQI3dDybveQylpIWU1JS9e16BZIiQ";

    /**
     * Constructor.
     */
    GoogleImageProviderPlugin() {
    }

    @Override
    public String[] getImageUrls(String text, int maxResults) {
        try {
            /* Create an URL and open the connection. */
            URL queryURL = makeURLFromText(text);
            URLConnection connection = queryURL.openConnection();
            connection.addRequestProperty("Referer", HTTP_REFERER);

            /* Get the results list from the object. */
            JSONObject json = getJSONResponse(connection);
            JSONArray results =
                    json.getJSONObject("responseData").getJSONArray("results");

            /* Put the results in a list of URL's, and log it as well. */
            int numberOfResults = results.length() < maxResults ?
                    results.length() : maxResults;
            String[] urls = new String[numberOfResults];
            for(int i = 0; i < numberOfResults; i++) {
                JSONObject object = results.getJSONObject(i);
                String url = object.getString("url");
                urls[i] = url;

                /* Log some information as well. */
                LOGGER.info("Photo result: " +
                        object.getString("titleNoFormatting") + " - " + url);
            }

            return urls;
        } catch (IOException exception) {
            // TODO: Send relevant warning.
            return null;
        } catch (JSONException exception) {
            // TODO: Send relevant warning.
            return null;
        }
    }

    /**
     * Auxiliary function to create a query URL from a given piece of text.
     * @param text Search query.
     * @return A query URL.
     */
    protected URL makeURLFromText(String text) {
        /* Convert spaces to '+' etc. */
        String encoded;
        try {
            encoded = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException exception) {
            encoded = text;
        }

        /* Construct the url. */
        URL url;
        try {
            url = new URL("http://ajax.googleapis.com/ajax/services/search/" +
                "images?start=0&rsz=large&v=1.0&q=" + text);
        } catch (MalformedURLException exception) {
            // TODO: Send warning.
            url = null;
        }

        return url;
    }

    /**
     * Auxiliary function to read a JSONObject from a connection.
     * @param connection Connection to read from.
     * @return A JSONObject.
     */
    protected JSONObject getJSONResponse(URLConnection connection)
            throws IOException, JSONException {
        /* Use a StringBuilder for slightly more efficient appending. */
        String line;
        StringBuilder builder = new StringBuilder();

        /* Read everything from the stream. */
        BufferedReader reader = new BufferedReader(
        new InputStreamReader(connection.getInputStream()));
        while((line = reader.readLine()) != null) {
            builder.append(line);
        }

        /* Close the stream and return the read object. */
        reader.close();
        String response = builder.toString();
        return new JSONObject(response);
    }
}