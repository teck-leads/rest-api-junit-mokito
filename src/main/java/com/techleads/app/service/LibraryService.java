package com.techleads.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techleads.app.model.Library;
import com.techleads.app.repository.LibraryRepository;

@Service
public class LibraryService {

	private LibraryRepository libraryRepository;

	@Autowired
	public LibraryService(LibraryRepository libraryRepository) {
		this.libraryRepository = libraryRepository;
	}

	public boolean checkIfBookAlreayExists(Library library) {

		Optional<Library> findById = libraryRepository.findById(library.getId());
		if (!findById.isPresent()) {
			return false;
		}
		return true;
	}
	
	
	public String buildId(String isbn, Integer sisle) {
		return (isbn + sisle);
	}
	

}
