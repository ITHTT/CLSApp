package com.tysci.cls.networks.cookie;


import com.tysci.cls.networks.cookie.store.CookieStore;
import com.tysci.cls.networks.cookie.store.HasCookieStore;
import com.tysci.cls.networks.cookie.store.PersistentCookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by zhy on 16/3/10.
 */
public class CookieJarImpl implements CookieJar, HasCookieStore
{
    private CookieStore cookieStore;

    public CookieJarImpl(PersistentCookieStore cookieStore)
    {
        if (cookieStore == null) throw new RuntimeException("cookieStore can not be null.");
        this.cookieStore = cookieStore;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        cookieStore.add(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url)
    {
        return cookieStore.get(url);
    }

    @Override
    public CookieStore getCookieStore()
    {
        return cookieStore;
    }
}
