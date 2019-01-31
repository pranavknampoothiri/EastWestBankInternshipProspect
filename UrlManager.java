package com.example.pranav.sharkfeed;

import android.net.Uri;

/*＊
 Flickr API

 You can make this call to the Flickr API to return a JSON object with a list of photos.
 *
 */
public class UrlManager {
    private static final String TAG = UrlManager.class.getSimpleName();

    public static final String API_KEY = "949e98778755d1982f537d56236bbb42";
    //public static final String API_KEY = "51885ef41dff54033e1de374307f110c"; old one
    public static final String PREF_SEARCH_QUERY ="searchQuery";

    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String METHOD_GETRECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";

    private static volatile UrlManager instance = null;
    private UrlManager() {

    }

    public static UrlManager getInstance() {
        if (instance == null) {
            synchronized (UrlManager.class) {
                if (instance == null) {
                    instance = new UrlManager();
                }
            }
        }
        return instance;
    }

    public static String getItemUrl(int page) {
        String url;

        url = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_SEARCH)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("text", "shark")
                .appendQueryParameter("page", String.valueOf(page))
                .build().toString();

        return url;
    }

    private static final String FLICKR_URL = "http://flickr.com/photo.gne?id=%s";
    private static final String METHOD_GETINFO = "flickr.photos.getInfo";

    public static String getFlickrUrl(String id) {
        return String.format(FLICKR_URL, id);
    }

    public static String getPhotoInfoUrl(String id) {
        return Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_GETINFO)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("photo_id", id)
                .build().toString();
    }
}


