package com.example.mangakomi.service.api;

public interface Server {
    String  DOMAIN = "http://14.225.7.179:19883/";
    String  URI_HOME = "ver";
    String URI_DETAIL = "detailmanga";
    String URI_CHAPTER = "chapter";
    String  URI_CATEGORY  = "category";
    String  URI_SEARCH  = "searchmanga";

    String KEY_HEADER = "Link-Full";
    String HEADER_POPULAR =  "Link-Full:https://mangakomi.io";

    String HEADER_LATEST_START =  "https://mangakomi.io/manga/page/";
    String HEADER_LATEST_END =  "/?m_orderby=latest";

    String HEADER_MANGA_HOT_START =   "https://mangakomi.io/manga/page/";
    String HEADER_MANGA_HOT_END = "/?m_orderby=views";

    String HEADER_MANGA_NEW_START =  "https://mangakomi.io/manga/page/";
    String HEADER_MANGA_NEW_END = "/?m_orderby=new-manga";
    String HEADER_MANGA_GENRES = "https://mangakomi.io/manga-genre/";

    String HEADER_MANGA_SEARCH_START = "https://mangakomi.io/?s=";
    String HEADER_MANGA_SEARCH_END = "&post_type=wp-manga";

}
