export default function MyLoans({ borrowedBooks, onReturn, activeMember }) {
  return (
    <section className="section section-tint" id="my-loans">
      <div className="wrap">
        <header className="section-head">
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "baseline", flexWrap: "wrap", gap: "0.5rem" }}>
            <h2>My loans</h2>
            {activeMember && (
              <span className="member-indicator">
                Member: <strong>{activeMember.name}</strong> ({activeMember.plan || "Student"})
              </span>
            )}
          </div>
          <p>Books currently checked out in this account ({borrowedBooks.length} active).</p>
        </header>

        {borrowedBooks.length === 0 ? (
          <div className="empty-state">
            <p>You haven't borrowed any books yet under this account.</p>
            <p style={{ fontSize: "0.9rem", color: "var(--muted, #666)", marginTop: "0.5rem" }}>
              Click <strong>Borrow this book</strong> on any available title in the catalogue above.
            </p>
          </div>
        ) : (
          <ul className="loan-list">
            {borrowedBooks.map((book) => (
              <li key={book.id}>
                <div>
                  <strong>{book.title}</strong>
                  <span>{book.author ? `By ${book.author}` : "Shelfmark Library Collection"}</span>
                  {book.dueDate && (
                    <span className="due-tag" style={{ display: "block", fontSize: "0.8rem", color: "var(--navy, #1c2e42)", marginTop: "0.25rem" }}>
                      📅 Due: {book.dueDate}
                    </span>
                  )}
                </div>
                <button
                  className="btn btn-small btn-outline"
                  onClick={() => onReturn(book.id)}
                  aria-label={`Return ${book.title}`}
                >
                  Return Book
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
    </section>
  );
}
