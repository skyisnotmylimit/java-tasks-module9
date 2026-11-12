package com.epam.micro.collector;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedMessageRepository extends JpaRepository<SavedMessage, Long> {
}
