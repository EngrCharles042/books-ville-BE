package BooksVille.infrastructure.controllers;

import BooksVille.payload.request.BookEntityRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import BooksVille.services.BookService;
import BooksVille.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @GetMapping("/get-book")
    public ResponseEntity<ApiResponse<BookEntityResponse>> findById (@RequestParam Long id) {
        return bookService.findById(id);

    }

    @GetMapping("/books")
    public ResponseEntity<ApiResponse<BookResponsePage>> getAllBooks(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        return bookService.getAllBooks(pageNo, pageSize, sortBy, sortDir);
    }

    @PutMapping("/edit/{bookEntityId}")
    public ResponseEntity<ApiResponse<BookEntityResponse>> editBook(@RequestBody BookEntityRequest bookEntityRequest,
                                                                    @PathVariable("bookEntityId")Long bookEntityId){
        return bookService.editBook(bookEntityRequest,bookEntityId);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable("bookId")Long bookId){
        return bookService.deleteBook(bookId);
    }

    @PatchMapping("/hide/{bookId}")
    public ResponseEntity<ApiResponse<String>> hideBook(@PathVariable("bookId")Long bookId){
        return bookService.hideBook(bookId);
    }
}
