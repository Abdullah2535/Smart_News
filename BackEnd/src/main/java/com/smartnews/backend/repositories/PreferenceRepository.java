package com.smartnews.backend.repositories;

import com.smartnews.backend.entities.Preference;
import org.springframework.data.repository.CrudRepository;

public interface PreferenceRepository extends CrudRepository<Preference, Integer> {
}