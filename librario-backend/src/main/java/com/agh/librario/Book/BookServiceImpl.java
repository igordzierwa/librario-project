package com.agh.librario.Book;

import com.agh.librario.Picture.Picture;
import com.agh.librario.Picture.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Override
    public void save(Book book, Picture picture) {
        pictureRepository.save(picture);
        book.setcover_photo(picture);
        bookRepository.save(book);
    }

}
