package com.inspirecamp7.myspringboot4lab.repository;

import com.inspirecamp7.myspringboot4lab.entity.Book;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("lab")
@Transactional
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    private Book createBook() {
        return new Book(
                "스프링 부트 입문",
                "홍길동",
                "9788956746425",
                LocalDate.of(2025, 5, 7),
                30000
        );
    }

    // 저장 내용을 DB에 반영하고, 다음 조회는 DB에서 읽도록 합니다.
    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCreateBook() {
        Book savedBook = bookRepository.save(createBook());
        Long id = savedBook.getId();

        flushAndClear();

        Book foundBook = bookRepository.findById(id).orElseThrow();

        assertThat(id).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("스프링 부트 입문");
        assertThat(foundBook.getAuthor()).isEqualTo("홍길동");
        assertThat(foundBook.getIsbn()).isEqualTo("9788956746425");
        assertThat(foundBook.getPublishDate())
                .isEqualTo(LocalDate.of(2025, 5, 7));
        assertThat(foundBook.getPrice()).isEqualTo(30000);
    }

    @Test
    void testFindByIsbn() {
        bookRepository.save(createBook());
        flushAndClear();

        Book foundBook = bookRepository
                .findByIsbn("9788956746425")
                .orElseThrow();

        assertThat(foundBook.getTitle()).isEqualTo("스프링 부트 입문");
        assertThat(foundBook.getAuthor()).isEqualTo("홍길동");
    }

    @Test
    void testFindByAuthor() {
        Book firstBook = createBook();

        Book secondBook = new Book(
                "JPA 프로그래밍",
                "박둘리",
                "9788956746432",
                LocalDate.of(2025, 4, 30),
                35000
        );

        bookRepository.saveAll(List.of(firstBook, secondBook));
        flushAndClear();

        List<Book> books = bookRepository.findByAuthor("홍길동");

        assertThat(books)
                .extracting(Book::getIsbn)
                .contains("9788956746425")
                .doesNotContain("9788956746432");

        assertThat(books)
                .allMatch(book -> "홍길동".equals(book.getAuthor()));
    }

    @Test
    void testUpdateBook() {
        Book savedBook = bookRepository.save(createBook());
        Long id = savedBook.getId();
        flushAndClear();

        Book book = bookRepository.findById(id).orElseThrow();
        book.setTitle("스프링 부트 입문 개정판");
        book.setPrice(32000);

        bookRepository.save(book);
        flushAndClear();

        Book updatedBook = bookRepository.findById(id).orElseThrow();

        assertThat(updatedBook.getTitle())
                .isEqualTo("스프링 부트 입문 개정판");
        assertThat(updatedBook.getPrice()).isEqualTo(32000);
    }

    @Test
    void testDeleteBook() {
        Book savedBook = bookRepository.save(createBook());
        Long id = savedBook.getId();
        flushAndClear();

        bookRepository.deleteById(id);
        flushAndClear();

        assertThat(bookRepository.findById(id)).isEmpty();
    }
}