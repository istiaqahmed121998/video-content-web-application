package com.example.video_sharing_web_application.video;


import com.example.video_sharing_web_application.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoContentRepository extends JpaRepository<VideoContent,Long> {
    Optional<VideoContent> findVideoContentById(Long id);


    @Query(value = "select case when (count(v)>0)  then true else false end from VideoContent v inner join v.likes likedbyuser where likedbyuser=:user and v=:videoContent")
    Boolean findUserIsLikedVideo(@Param("user") User user,@Param("videoContent") VideoContent videoContent);







}
