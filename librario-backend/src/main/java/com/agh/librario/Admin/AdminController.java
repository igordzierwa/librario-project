package com.agh.librario.Admin;

import com.agh.librario.Book.Book;
import com.agh.librario.Book.BookRepository;
import com.agh.librario.Role.Role;
import com.agh.librario.Role.RoleRepository;
import com.agh.librario.SecurityService;
import com.agh.librario.User.User;
import com.agh.librario.User.UserRepository;
import com.agh.librario.User.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AdminFunc adminFunc;

  @GetMapping(value = "/admin/users/", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Map<String,String>>> getUsers(){
        return ResponseEntity.ok(adminFunc.getListOfUsers(true));
    }


    @GetMapping(value = "/admin/employees/", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Map<String, String>>> getEmployee() {
        return ResponseEntity.ok(adminFunc.getListOfUsers(false));
    }

    @GetMapping(value = "/admin/employees/{username}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getEmployeeDetails(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        Map<String, String> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("name", user.getName());
        map.put("surname", user.getSurname());
        map.put("email", user.getEmail());
        return ResponseEntity.ok(map);
    }

    @PostMapping(value = "/admin/employees/")
    @ResponseBody
    public ResponseEntity<String> registerUserAccount(@RequestBody User userData) {

        userService.save(userData, false);
        return ResponseEntity.ok("\"employeeStatus\": \"added\"");
    }

    @PutMapping(value = "/admin/employees")
    @ResponseBody
    public ResponseEntity<String> editEmployee(@RequestBody User userData) {
        User userToEdit = userRepository.findByUsername(userData.getUsername());

        if (userToEdit!= null) {
            userToEdit.setEmail(userData.getEmail());
            userToEdit.setName(userData.getName());
            userToEdit.setSurname(userData.getSurname());
            userService.save(userToEdit, false);

            return ResponseEntity.ok("\"editStatus\": \"done\"");
        }
        return ResponseEntity.ok("\"editStatus\": \"error\"");
    }

    @DeleteMapping(value = "/admin/employees/{username}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> deleteEmployee(@PathVariable String username) {

        userRepository.deleteUserByUsername(username);

        if (userRepository.findByUsername(username) == null) {
            log.info("Employee deleted: " + username);
            return ResponseEntity.ok("{\"Status\": \"Success\"");
        }
        return ResponseEntity.ok("\"Status\": \"Error\"");
    }

    @PostMapping("/admin/import")
    @ResponseBody
    public  ResponseEntity<HashMap<String,String>>  handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        File booksDB = new File("./src/main/webapp/booksDB.txt");

        Files.deleteIfExists(booksDB.toPath());

        //Create the file
        booksDB.createNewFile();

        file.transferTo(Paths.get(booksDB.getPath()));
        Type listType = new TypeToken<List<Book>>(){}.getType();
        String json = new String(Files.readAllBytes(Paths.get(booksDB.getPath())));

        List<Book> result = new Gson().fromJson(json, listType);

        log.info(result.toString());
        for(Book b : result){
            if(bookRepository.findByName(b.getName())==null){
                bookRepository.save(b);
            }
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("name", "booksDB");
        map.put("message", "Added successfully");
        return ResponseEntity.ok(map);
    }

    @GetMapping(value = "/admin/export")
    @ResponseBody
    public ResponseEntity<Resource> exportBooksDB() throws IOException {

        File booksDB = new File("./src/main/webapp/booksDB.txt");

        Files.deleteIfExists(booksDB.toPath());

        //Create the file
        booksDB.createNewFile();

        List<Book> list = bookRepository.findAll();

        String jsonList = new Gson().toJson(list);

        //Write Content
        FileWriter writer = new FileWriter(booksDB);
        if(!list.isEmpty()) {
            writer.write(jsonList);
        }
        writer.close();
        Resource file = new UrlResource(booksDB.toURI());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"booksDB.txt" + "\"").body(file);

    }

}