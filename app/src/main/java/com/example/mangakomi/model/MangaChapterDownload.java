package com.example.mangakomi.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class MangaChapterDownload {

        @Embedded
        public MangaDownload mangaDownload;
        @Relation(
                parentColumn = "id",
                entityColumn = "manga_id"
        )
        public List<ChapterDownload> chapterDownloadList;
}
