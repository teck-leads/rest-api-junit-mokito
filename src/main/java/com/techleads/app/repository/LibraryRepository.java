package com.techleads.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techleads.app.model.Library;

public interface LibraryRepository extends JpaRepository<Library, String> {
	
	public Optional<Library> findByAuthor(String author);

}
