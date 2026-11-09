import BookCard from "./BookCard";

export default function BookGrid({ books, borrowedIds, onBorrow, onReturn }) {
  if (books.length === 0) {
    return <p className="empty-state">No books match your search. Try a different title or category.</p>;
  }

  return (
    <div className="book-grid">
      {books.map((book) => (
        <BookCard
          key={book.id}
          book={book}
          isBorrowed={borrowedIds.includes(book.id)}
          onBorrow={onBorrow}
          onReturn={onReturn}
        />
      ))}
    </div>
  );
}
