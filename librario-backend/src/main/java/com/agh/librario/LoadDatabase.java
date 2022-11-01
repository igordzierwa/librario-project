package com.agh.librario;


import com.agh.librario.Book.BookRepository;
import com.agh.librario.Role.RoleService;
import com.agh.librario.Admin.AdminFunc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminFunc adminFunc;
    @Bean
    CommandLineRunner initDatabase(BookRepository repo){
        if(roleService.checkRoles()) {
            roleService.makeRoles();
        }
        adminFunc.addAdminAccIfNotExist();
        return args -> {
            log.info("Database loaded successfully");
        };

    }
}
