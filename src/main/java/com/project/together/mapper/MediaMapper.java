package com.project.together.mapper;

import com.project.together.domain.ClubPhoto;
import com.project.together.domain.ClubVideo;
import com.project.together.domain.News;
import com.project.together.domain.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface MediaMapper {
    List<Notice> findAll(Map<String, Integer> params);

    Notice findNoticeByNoticeNum(int noticeNum);

    Notice findPrevNoticeByCurrentNoticeDate(LocalDate currentNoticeDate);

    List<News> getNewsList(Map<String, Integer> params);

    List<ClubPhoto> getClubPhotoList(Map<String, Integer> params);

    News findNewsByNewsNumber(int newsNum);

    ClubPhoto findPhotosByPhotoNumber(int cpIdx);

    List<ClubVideo> getVideoList(Map<String, Integer> params);

    ClubVideo findVideosByVideoNumber(int cvIdx);

    void updateNoticeHits(@Param("noticeNum") int noticeNum, @Param("hits") int hits);
    void updateNewsHits(@Param("newsIdx") int newsIdx, @Param("hits") int hits);
    void updatePhotoHits(@Param("photoNum") int photoNum, @Param("hits") int hits);
    void updateVideoHits(@Param("cvIdx") int cvIdx, @Param("hits") int hits);
}
