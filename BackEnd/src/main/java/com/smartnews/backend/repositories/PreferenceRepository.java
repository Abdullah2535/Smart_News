package com.smartnews.backend.repositories;

import com.smartnews.backend.entities.Preference;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PreferenceRepository extends CrudRepository<Preference, Integer> {


    @Query(value = "SELECT preference_content AS preferenceContent FROM preferences p where user_id = 1 order by p.chosen_at desc limit 1 ", nativeQuery = true)
    String findTopPreferenceContentByChosenAtDEC (@Param("userId")Integer userId);
}