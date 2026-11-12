package com.epam.micro.visualizer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedMessageRepository extends JpaRepository<SavedMessage, Long> {
}
