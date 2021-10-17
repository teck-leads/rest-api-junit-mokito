package com.techleads.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.techleads.app.model.Library;
import com.techleads.app.model.Response;
import com.techleads.app.repository.LibraryRepository;
import com.techleads.app.service.LibraryService;

@RestController
public class LibraryController {

	private LibraryRepository libraryRepository;
	private LibraryService libraryService;

	@Autowired
	public LibraryController(LibraryRepository libraryRepository, LibraryService libraryService) {
		this.libraryRepository = libraryRepository;
		this.libraryService = libraryService;
	}

	@PostMapping(value = { "/book" })
	public ResponseEntity<Response> addBook(@RequestBody Library library) {

		Response resp = new Response();
		HttpHeaders headers = new HttpHeaders();

		String id = libraryService.buildId(library.getIsbn(), library.getAisle());
		;

		library.setId(id);
		boolean checkIfBookAlreayExists = libraryService.checkIfBookAlreayExists(id);

		if (checkIfBookAlreayExists) {
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
		return new ResponseEntity<>(resp, headers, HttpStatus.CREATED);
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
	public ResponseEntity<List<Library>> findBookByAuthor(@PathVariable String author) {
		List<Library> findByAuthor = new ArrayList<>();
		findByAuthor = libraryRepository.findByAuthor(author);
		if (findByAuthor.size() > 0) {
			return new ResponseEntity<>(findByAuthor, HttpStatus.OK);
		}
		return new ResponseEntity<>(findByAuthor, HttpStatus.NO_CONTENT);
	}

	@PutMapping(value = { "/book/{id}" })
	public ResponseEntity<Library> updateBookById(@RequestBody Library library, @PathVariable("id") String id) {
		HttpHeaders headers = new HttpHeaders();
		Optional<Library> findById = libraryRepository.findById(id);
		if (!findById.isPresent()) {
			headers.add("status", "Not updated");
			return new ResponseEntity<>(new Library(), headers, HttpStatus.CREATED);
		}

		headers.add("status", "Updated");
		Library oldLibrary = findById.get();
		oldLibrary.setName(library.getName());
		oldLibrary.setAuthor(library.getAuthor());

		libraryRepository.save(oldLibrary);
		Library updaed = libraryRepository.findById(id).get();
		return new ResponseEntity<>(updaed, headers, HttpStatus.OK);

	}

	@DeleteMapping(value = { "/book/{id}" })
	public ResponseEntity<Response> deleteBookById(@PathVariable("id") String id) {

		Response resp = new Response();
		HttpHeaders headers = new HttpHeaders();

		boolean checkIfBookAlreayExists = libraryService.checkIfBookAlreayExists(id);

		if (checkIfBookAlreayExists) {
			libraryRepository.deleteById(id);
			resp.setMessage("book is deleted with Id " + id);
			resp.setId(id);
			headers.add("Status", "Deleted");
			return new ResponseEntity<Response>(resp, headers, HttpStatus.OK);
		}

		resp.setMessage("Note:  book does not exit with Id: " + id);
		resp.setId(id);
		headers.add("Status", "Not deleted");
		return new ResponseEntity<>(resp, headers, HttpStatus.BAD_REQUEST);
	}

}
