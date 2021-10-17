package com.techleads.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.techleads.app.model.Library;
import com.techleads.app.model.Response;
import com.techleads.app.repository.LibraryRepository;
import com.techleads.app.service.LibraryService;

@RestController
public class LibraryController {

	private LibraryRepository libraryRepository;
	private  LibraryService libraryService;

	@Autowired
	public LibraryController(LibraryRepository libraryRepository, LibraryService libraryService) {
		this.libraryRepository = libraryRepository;
		this.libraryService = libraryService;
	}

	@PostMapping(value = { "/book" })
	public ResponseEntity<Response> addBook(@RequestBody Library library) {

		Response resp = new Response();
		HttpHeaders headers=new HttpHeaders();
		
		String id=libraryService.buildId(library.getIsbn(), library.getAisle());;
		
		library.setId(id);
		boolean checkIfBookAlreayExists = libraryService.checkIfBookAlreayExists(library);
		
		
		if(checkIfBookAlreayExists) {
			resp.setMessage("Warning book is already present");
			resp.setId(id);
			headers.add("myId", id);
			return new ResponseEntity<Response>(resp, headers, HttpStatus.BAD_REQUEST);
		}
		
		
		library.setId(id);
		libraryRepository.save(library);
		resp.setMessage("Sucess book is added");
		resp.setId(id);
		headers.add("myId", id);
		return new ResponseEntity<Response>(resp, headers, HttpStatus.CREATED);
	}

	

	@GetMapping(value = { "/book/{id}" })
	public ResponseEntity<Library> findBookById(@PathVariable String id) {

		Optional<Library> findById = libraryRepository.findById(id);

		if (findById.isPresent()) {
			return new ResponseEntity<>(findById.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(new Library(), HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(value = { "/book/author/{author}" })
	public ResponseEntity<Library> findBookByAuthor(@PathVariable String author) {

		Optional<Library> findByAuthor = libraryRepository.findByAuthor(author);

		if (findByAuthor.isPresent()) {
			return new ResponseEntity<>(findByAuthor.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(new Library(), HttpStatus.NO_CONTENT);
	}

}
