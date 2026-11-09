export default function BookCard({ book, isBorrowed, onBorrow, onReturn }) {
  const available = book.copies > 0;

  return (
    <article className="book">
      <div className="cover" aria-hidden="true">
        <span>{book.title}</span>
      </div>
      <div className="info">
        <h3>{book.title}</h3>
        <p className="author">{book.author}</p>
        <p className="callno">Call no. {book.callNo}</p>

        {isBorrowed ? (
          <p className="badge borrowed">You have this book</p>
        ) : available ? (
          <p className="badge in">{book.copies} copies available</p>
        ) : (
          <p className="badge out">All copies issued</p>
        )}

        {isBorrowed ? (
          <button className="btn btn-small btn-outline" onClick={() => onReturn(book.id)}>
            Return book
          </button>
        ) : (
          <button
            className="btn btn-small"
            disabled={!available}
            onClick={() => onBorrow(book.id)}
          >
            {available ? "Borrow this book" : "Join waitlist"}
          </button>
        )}
      </div>
    </article>
  );
}
