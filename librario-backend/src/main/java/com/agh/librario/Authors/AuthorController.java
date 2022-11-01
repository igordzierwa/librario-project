package com.agh.librario.Authors;

import com.agh.librario.NotFoundException;
import com.agh.librario.Picture.Picture;
import com.agh.librario.Picture.PictureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@Slf4j
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private PictureRepository pictureRepository;

    private final AuthorRepository repo;

    AuthorController(AuthorRepository repo){
        this.repo = repo;
    }

    @GetMapping("/rest/author")
    List<Author> all(){
        return repo.findAll();
    }

    @GetMapping("/rest/author/{id}")
    Author one(@PathVariable Long id){
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException(id, "Book"));
    }

    @GetMapping("/rest/author/name/{name}")
    Author oneByName(@PathVariable String name){
        return repo.findByName(name);
    }


    @DeleteMapping("/rest/author/{id}")
    @Transactional
    HashMap<String, String> deleteAuthor(@PathVariable Long id){
        repo.deleteById(id);

        HashMap<String, String> map = new HashMap<>();
        map.put("code", "200");
        map.put("message", "Deleted successfully");
        return map;
    }


    @PutMapping("/rest/author/{id}")
    @ResponseBody
    public ResponseEntity<String> editAuthor(Author authorData, @RequestParam("filename")   String filename, @RequestParam("picture_name") String picture_name, @PathVariable Long id) {
        Optional<Author> authorFromDb = repo.findById(id);
        if(authorFromDb.isPresent()){
            Author authorDb = authorFromDb.get();

            if(!filename.equalsIgnoreCase("")) {
                Picture p = new Picture(picture_name, filename);
                pictureRepository.save(p);
                authorDb.setProfile_photo(p);
            }
            authorDb.setName(authorData.getName());
            authorDb.setDescription(authorData.getDescription());
            authorDb.setYear(authorData.getYear());
            repo.save(authorDb);

            return new ResponseEntity<>("Successful updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error updating entity", HttpStatus.BAD_REQUEST);
        }


    }



    @PostMapping("/rest/author")
    public HashMap<String, String> addAuthor(Author authorData, @RequestParam("filename") String filename, @RequestParam("picture_name") String picture_name, HttpServletRequest request) {

        log.info("Adding author with data: ", authorData);
        Picture p = new Picture(picture_name, filename);

        authorService.save(authorData, p);

        HashMap<String, String> map = new HashMap<>();
        map.put("code", "200");
        map.put("message", "Added author successfully!");
        return map;
    }
}
