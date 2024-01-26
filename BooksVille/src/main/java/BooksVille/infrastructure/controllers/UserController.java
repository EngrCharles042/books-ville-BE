package BooksVille.infrastructure.controllers;

import BooksVille.payload.request.BookFilterRequest;
import BooksVille.payload.request.UserEntityRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.BookEntityResponse;
import BooksVille.payload.response.BookResponsePage;
import BooksVille.services.UserService;
import BooksVille.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/search/title-or-author-or-price-or-genre")
    public ResponseEntity<ApiResponse<List<BookEntityResponse>>> bookSearchWithKeyword (@RequestParam("query")String query) {
        return userService.searchBooks(query);
    }

    @GetMapping("/filter-books")
    public ResponseEntity<ApiResponse<BookResponsePage>> bookSearchWithFilter(
            @Valid @RequestBody BookFilterRequest bookFilterRequest,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        return userService.filterBooks(pageNo, pageSize, sortBy, sortDir, bookFilterRequest);
    }

    @PutMapping("/account-info")
    public ResponseEntity<ApiResponse<String>> userInfoUpdate(@RequestBody UserEntityRequest userEntityRequest) {

        return userService.userInfoUpdate(userEntityRequest);
    }

    @PatchMapping("/profile-pic")
    public ResponseEntity<ApiResponse<String>> profilePicUpdate(@RequestParam MultipartFile profilePic) throws IOException {

        return userService.profilePicUpdate(profilePic);
    }
}
