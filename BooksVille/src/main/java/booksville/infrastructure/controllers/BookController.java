package booksville.infrastructure.controllers;

import booksville.payload.request.BookEntityRequest;
import booksville.payload.response.ApiResponse;
import booksville.payload.response.BookEntityResponse;
import booksville.payload.response.BookResponsePage;
import booksville.services.BookService;
import booksville.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @GetMapping("/get-book")
    public ResponseEntity<ApiResponse<BookEntityResponse>> findById (@RequestParam Long id) {
        return bookService.findById(id);
    }

    @PostMapping("/add-book")
    public ResponseEntity<ApiResponse<BookEntityResponse>> addBook(@Valid BookEntityRequest bookEntityRequest) throws IOException {
        return bookService.addBook(bookEntityRequest);
    }

    @GetMapping("/books")
    public ResponseEntity<ApiResponse<BookResponsePage>> getAllBooks(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        return bookService.getAllBooks(pageNo, pageSize, sortBy, sortDir);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<BookEntityResponse>> editBook(@RequestBody BookEntityRequest bookEntityRequest,
                                                                    @PathVariable Long id){
        return bookService.editBook(bookEntityRequest,id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id){
        return bookService.deleteBook(id);
    }

    @PatchMapping("/hide/{bookId}")
    public ResponseEntity<ApiResponse<String>> hideBook(@PathVariable("bookId")Long bookId){
        return bookService.hideBook(bookId);
    }

    @GetMapping("/saved-books")
    public ResponseEntity<ApiResponse<BookResponsePage>> getSavedBooks(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        return bookService.getSavedBooks(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/save/{id}")
    public ResponseEntity<ApiResponse<String>> saveBook(@PathVariable("id") Long id) {
        return bookService.saveBook(id);
    }

    @DeleteMapping("/save/remove/{id}")
    public ResponseEntity<ApiResponse<String>> removeSavedBook(@PathVariable("id") Long id) {
        return bookService.removeSavedBook(id);
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadBooks(@RequestParam("book_id") Long bookId) {
        byte[] bookData = bookService.downloadBook(bookId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bookData);
    }

    @GetMapping("purchased")
    public ResponseEntity<ApiResponse<BookResponsePage>> getAllPurchasedBooks(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        return bookService.getPurchasedBooks(pageNo, pageSize, sortBy, sortDir);
    }
}
