package com.shelfmark.library;

import com.shelfmark.library.model.Book;
import com.shelfmark.library.model.Loan;
import com.shelfmark.library.model.Member;
import com.shelfmark.library.repository.BookRepository;
import com.shelfmark.library.repository.LoanRepository;
import com.shelfmark.library.repository.MemberRepository;
import com.shelfmark.library.controller.LoanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LibraryBackendApplicationTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanService loanService;

    @Test
    @DisplayName("Context Loads & Database Seed Data is Initialized")
    void contextLoads() {
        List<Book> books = bookRepository.findAll();
        assertNotNull(books, "Books list should not be null");
        assertTrue(books.size() >= 8, "Expected at least 8 seeded books from data.sql");

        List<Member> members = memberRepository.findAll();
        assertNotNull(members, "Members list should not be null");
        assertTrue(members.size() >= 2, "Expected at least 2 seeded members from data.sql");
    }

    @Test
    @DisplayName("Search books by title query and category filter")
    void testBookSearch() {
        List<Book> searchResults = bookRepository.search("Clean Code", "Computer Science");
        assertFalse(searchResults.isEmpty(), "Search for Clean Code in Computer Science should return results");
        assertEquals("Clean Code", searchResults.get(0).getTitle());
    }

    @Test
    @DisplayName("Member registration and lookup")
    void testMemberRepository() {
        String testEmail = "test_" + System.currentTimeMillis() + "@example.com";
        Member newMember = new Member(null, "Test Student", testEmail, "9998887776", "Student");
        Member saved = memberRepository.save(newMember);

        assertNotNull(saved.getId(), "Saved member must have generated ID");
        Optional<Member> found = memberRepository.findById(saved.getId());
        assertTrue(found.isPresent(), "Member should be found by ID");
        assertEquals("Test Student", found.get().getName());
    }

    @Test
    @DisplayName("Borrow and return transaction workflow via LoanService")
    void testBorrowAndReturn() {
        // Book 3 (Database System Concepts) has 5 copies
        int initialCopies = bookRepository.findById(3L).orElseThrow().getCopies();
        assertTrue(initialCopies > 0, "Book must have copies to borrow");

        // Borrow as member 2 (Divya)
        Loan loan = loanService.borrow(3L, 2L);
        assertNotNull(loan.getId(), "Loan record must be generated");
        assertEquals(3L, loan.getBookId());
        assertEquals(2L, loan.getMemberId());

        // Verify copy count decremented
        int afterBorrowCopies = bookRepository.findById(3L).orElseThrow().getCopies();
        assertEquals(initialCopies - 1, afterBorrowCopies, "Book copies should decrement by 1");

        // Return the book
        loanService.returnBook(loan.getId(), 3L);

        // Verify copy count restored
        int afterReturnCopies = bookRepository.findById(3L).orElseThrow().getCopies();
        assertEquals(initialCopies, afterReturnCopies, "Book copies should be restored to initial count");
    }
}
