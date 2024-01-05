package BooksVille.services.implementation;

import BooksVille.entities.model.BookEntity;
import BooksVille.infrastructure.exceptions.ApplicationException;
import BooksVille.payload.request.BookEntityRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import BooksVille.repositories.BookRepository;
import BooksVille.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ModelMapper modelMapper;
    private final BookRepository bookRepository;

    @Override
    public ResponseEntity<ApiResponse<BookEntityResponse>> addBook(BookEntityRequest bookEntityRequest) {
        Optional<BookEntity> bookEntityOptional = bookRepository
                .findByBookTitleAndAuthor(bookEntityRequest.getBookTitle(), bookEntityRequest.getAuthor());

        if (bookEntityOptional.isPresent()) {
            throw new ApplicationException("Book Already Exist");
        }

        BookEntity newBook = bookRepository.save(modelMapper.map(bookEntityRequest, BookEntity.class));

        BookEntityResponse bookResponse = modelMapper.map(newBook, BookEntityResponse.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                "Book created successfully",
                                bookResponse
                        )
                );
    }



}
