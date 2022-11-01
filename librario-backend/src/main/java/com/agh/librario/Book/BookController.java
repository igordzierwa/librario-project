package com.agh.librario.Book;

import com.agh.librario.Picture.Picture;
import com.agh.librario.Picture.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@RestController
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository repo;

    @Autowired
    private PictureRepository pictureRepository;

    @GetMapping("/rest/books")
    List<Book> allBooks(){
        return repo.findAll();
    }


    @PostMapping("/rest/books")
    public HashMap<String, String> addBook(Book bookData, @RequestParam("filename") String filename, @RequestParam("picture_name") String picture_name, HttpServletRequest request) {

        log.info("Adding book with data: ", bookData);
        Picture p = new Picture(picture_name, filename);

        bookService.save(bookData, p);

        HashMap<String, String> map = new HashMap<>();
        map.put("code", "200");
        map.put("message", "Added author successfully!");
        return map;
    }


    @GetMapping("/rest/categories")
    List<CategorySummary> allCategories() {
        List<CategorySummary> summary = new ArrayList<>();
        for (Category category : Category.values()) {
            summary.add(new CategorySummary(category, repo.findByCategory(category).size()));
        }
        return summary;
    }

    @GetMapping("/rest/books/{id}")
    Book one(@PathVariable Long id){
        return repo.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @GetMapping("/rest/books/author/{name}")
    List<Book> allByAuthor(@PathVariable String name) {
        return repo.findByAuthor(name);
    }

    @GetMapping("/rest/books/count/author/{name}")
    int numberOfBooksByAuthor(@PathVariable String name) {
        return repo.findByAuthor(name).size();
    }

    @GetMapping("/rest/books/list/{category}")
    List<Book> allByCategory(@PathVariable Category category) {
        return repo.findByCategory(category);
    }

    @GetMapping("/rest/books/unreserved")
    List<Book> allUnreserved() {
        return repo.findByStockCountGreaterThan(0);
    }

    @DeleteMapping("/rest/books/{id}")
    @Transactional
    HashMap<String, String> deleteBook(@PathVariable Long id){
        repo.deleteById(id);

        HashMap<String, String> map = new HashMap<>();
        map.put("code", "200");
        map.put("message", "Deleted successfully");
        return map;
    }

    @PutMapping("/rest/books/{id}")
    @ResponseBody
    public ResponseEntity<String> editBook(Book bookData, @RequestParam("filename")   String filename, @RequestParam("picture_name") String picture_name, @PathVariable Long id) {
        Optional<Book> bookFromDb = repo.findById(id);
        if (bookFromDb.isPresent()) {
            Book bookDb = bookFromDb.get();

            bookDb.setName(bookData.getName());
            bookDb.setAuthor(bookData.getAuthor());
            bookDb.setLength(bookData.getLength());
            bookDb.setStockCount(bookData.getStockCount());
            bookDb.setDescription(bookData.getDescription());
            if(!filename.equalsIgnoreCase("")) {
                Picture p = new Picture(picture_name, filename);
                pictureRepository.save(p);
                bookDb.setcover_photo(p);
            }
            repo.save(bookDb);

            return new ResponseEntity<>("Succesful updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error updating", HttpStatus.BAD_REQUEST);
        }
    }
}
