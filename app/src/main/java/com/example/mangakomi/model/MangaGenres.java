package com.example.mangakomi.model;

import java.util.List;
import java.util.Objects;

public class MangaGenres {
    private List<Object> cate_manga;
    private Page page;

    public MangaGenres() {
    }

    public MangaGenres(List<Object> cate_manga, Page page) {
        this.cate_manga = cate_manga;
        this.page = page;
    }

    public List<Object> getCate_manga() {
        return cate_manga;
    }

    public void setCate_manga(List<Object> cate_manga) {
        this.cate_manga = cate_manga;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public class Page {

        private  int page_last;
        private List<Page_info> list_page;

        public Page() {
        }

        public Page(int page_last, List<Page_info> list_page) {
            this.page_last = page_last;
            this.list_page = list_page;
        }

        public int getPage_last() {
            return page_last;
        }

        public void setPage_last(int page_last) {
            this.page_last = page_last;
        }

        public List<Page_info> getList_page() {
            return list_page;
        }

        public void setList_page(List<Page_info> list_page) {
            this.list_page = list_page;
        }


        public class Page_info {
            private String link_page;
            private String page_number;


            public Page_info() {
            }

            public Page_info(String link_page, String page_number) {
                this.link_page = link_page;
                this.page_number = page_number;
            }

            public String getLink_page() {
                return link_page;
            }

            public void setLink_page(String link_page) {
                this.link_page = link_page;
            }

            public String getPage_number() {
                return page_number;
            }

            public void setPage_number(String page_number) {
                this.page_number = page_number;
            }
        }
    }
}
