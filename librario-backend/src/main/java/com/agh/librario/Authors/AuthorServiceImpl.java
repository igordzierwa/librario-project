package com.agh.librario.Authors;

import com.agh.librario.Picture.Picture;
import com.agh.librario.Picture.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Override
    public void save(Author author, Picture picture) {
        pictureRepository.save(picture);
        author.setProfile_photo(picture);
        authorRepository.save(author);
    }

}
