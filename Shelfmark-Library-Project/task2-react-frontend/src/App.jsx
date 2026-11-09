import { useEffect, useState, useMemo } from "react";
import fallbackBooks from "./data/books";
import {
  fetchBooks,
  fetchActiveLoans,
  borrowBook,
  returnBook,
  CURRENT_MEMBER_ID,
  setSessionMemberId,
} from "./api";
import Navbar from "./components/Navbar";
import SearchBar from "./components/SearchBar";
import BookGrid from "./components/BookGrid";
import MyLoans from "./components/MyLoans";
import RegisterForm from "./components/RegisterForm";
import "./App.css";

const DEFAULT_MEMBERS = [
  { id: 1, name: "Manoj", email: "manoj@example.com", plan: "Student" },
  { id: 2, name: "Divya", email: "divya@example.com", plan: "Student" },
];

export default function App() {
  const [books, setBooks] = useState([]);
  const [loans, setLoans] = useState([]);
  const [query, setQuery] = useState("");
  const [category, setCategory] = useState("All categories");
  const [status, setStatus] = useState("loading"); // "loading" | "ready" | "offline"
  const [actionError, setActionError] = useState("");
  const [toast, setToast] = useState(null);
  const [activeMemberId, setActiveMemberId] = useState(CURRENT_MEMBER_ID);
  const [membersList, setMembersList] = useState(DEFAULT_MEMBERS);

  // Helper for quick toast alert
  function showToast(message, type = "success") {
    setToast({ message, type });
    setTimeout(() => {
      setToast(null);
    }, 3500);
  }

  // Handle active member change
  function handleMemberChange(newId) {
    const idNum = Number(newId);
    setActiveMemberId(idNum);
    setSessionMemberId(idNum);
  }

  // Reload catalogue on query/category change
  useEffect(() => {
    let cancelled = false;
    setStatus((s) => (s === "offline" ? "offline" : "loading"));

    fetchBooks(query, category)
      .then((data) => {
        if (!cancelled) {
          setBooks(data);
          setStatus("ready");
        }
      })
      .catch(() => {
        if (!cancelled) {
          setStatus("offline");
          // Use fallback data filtered
          setBooks(offlineBooks(query, category));
        }
      });

    return () => {
      cancelled = true;
    };
  }, [query, category]);

  // Load active loans whenever active member changes
  useEffect(() => {
    let cancelled = false;
    fetchActiveLoans(activeMemberId)
      .then((data) => {
        if (!cancelled) setLoans(data || []);
      })
      .catch(() => {
        if (!cancelled && status === "offline") {
          // If offline, maintain local loans for demo
          setLoans((prev) => prev.filter((l) => l.memberId === activeMemberId));
        }
      });

    return () => {
      cancelled = true;
    };
  }, [activeMemberId, status]);

  async function handleBorrow(bookId) {
    setActionError("");
    try {
      const loan = await borrowBook(bookId, activeMemberId);
      setLoans((prev) => [...prev, loan]);
      setBooks((prev) =>
        prev.map((b) => (b.id === bookId ? { ...b, copies: Math.max(0, b.copies - 1) } : b))
      );
      const targetBook = books.find((b) => b.id === bookId);
      showToast(`Borrowed "${targetBook?.title || 'Book'}" successfully!`);
    } catch (err) {
      if (status === "offline") {
        // Offline simulation mode
        const targetBook = books.find((b) => b.id === bookId);
        if (targetBook && targetBook.copies > 0) {
          const mockLoan = {
            id: Date.now(),
            bookId,
            memberId: activeMemberId,
            bookTitle: targetBook.title,
            borrowedOn: new Date().toISOString().split("T")[0],
            dueDate: new Date(Date.now() + 14 * 86400000).toISOString().split("T")[0],
          };
          setLoans((prev) => [...prev, mockLoan]);
          setBooks((prev) =>
            prev.map((b) => (b.id === bookId ? { ...b, copies: b.copies - 1 } : b))
          );
          showToast(`[Demo Mode] Borrowed "${targetBook.title}"`);
        } else {
          setActionError("No copies available to borrow.");
        }
      } else {
        setActionError(err.message || "Failed to borrow book.");
      }
    }
  }

  async function handleReturn(bookId) {
    setActionError("");
    const loan = loans.find((l) => l.bookId === bookId && (!l.memberId || l.memberId === activeMemberId));
    if (!loan) return;

    try {
      await returnBook(loan.id, bookId);
      setLoans((prev) => prev.filter((l) => l.id !== loan.id));
      setBooks((prev) =>
        prev.map((b) => (b.id === bookId ? { ...b, copies: b.copies + 1 } : b))
      );
      showToast("Book returned successfully!");
    } catch (err) {
      if (status === "offline") {
        // Offline simulation mode
        setLoans((prev) => prev.filter((l) => l.id !== loan.id));
        setBooks((prev) =>
          prev.map((b) => (b.id === bookId ? { ...b, copies: b.copies + 1 } : b))
        );
        showToast(`[Demo Mode] Returned "${loan.bookTitle || 'Book'}"`);
      } else {
        setActionError(err.message || "Failed to return book.");
      }
    }
  }

  function handleNewMemberAdded(newMember) {
    setMembersList((prev) => [...prev, newMember]);
    setActiveMemberId(newMember.id);
    setSessionMemberId(newMember.id);
    showToast(`Switched active session to new member: ${newMember.name}`);
  }

  const borrowedIds = useMemo(() => loans.map((l) => l.bookId), [loans]);
  const borrowedBooks = useMemo(
    () =>
      loans.map((l) => {
        const bookDetails = books.find((b) => b.id === l.bookId);
        return {
          id: l.bookId,
          loanId: l.id,
          title: l.bookTitle || bookDetails?.title || `Book #${l.bookId}`,
          author: bookDetails?.author || "",
          borrowedOn: l.borrowedOn,
          dueDate: l.dueDate,
        };
      }),
    [loans, books]
  );

  const activeMember = membersList.find((m) => m.id === activeMemberId) || {
    id: activeMemberId,
    name: `Member #${activeMemberId}`,
    plan: "Standard",
  };

  return (
    <>
      <Navbar loanCount={loans.length} activeMember={activeMember} />

      {toast && (
        <div className={`toast-notification ${toast.type}`}>
          <span>{toast.message}</span>
        </div>
      )}

      <main>
        <section className="hero" id="top">
          <div className="wrap">
            <div className="member-bar">
              <span className="member-label">Active Session:</span>
              <select
                className="member-select"
                value={activeMemberId}
                onChange={(e) => handleMemberChange(e.target.value)}
                aria-label="Select active member"
              >
                {membersList.map((m) => (
                  <option key={m.id} value={m.id}>
                    {m.name} ({m.plan} #{m.id})
                  </option>
                ))}
              </select>
            </div>

            <h1>Search the shelves, right from this page</h1>
            <p className="lead">
              Interactive React application talking to Spring Boot + JDBC + SQL backend (Task 3 & 4). Search,
              borrow, and return books with real-time state synchronization.
            </p>
            <SearchBar query={query} setQuery={setQuery} category={category} setCategory={setCategory} />
          </div>
        </section>

        {status === "offline" && (
          <div className="wrap">
            <div className="offline-banner">
              <strong>Running in Interactive Standalone Mode:</strong>
              Backend at <code>localhost:8080</code> is not connected. All borrow, return, and registration features remain fully interactive with client-side reactive state. To connect live: run <code>mvn spring-boot:run</code> in <code>task3-springboot-backend</code>.
            </div>
          </div>
        )}

        <section className="section" id="catalogue">
          <div className="wrap">
            <header className="section-head">
              <h2>Catalogue</h2>
              <p>
                {status === "loading"
                  ? "Loading books..."
                  : `Showing ${books.length} book${books.length === 1 ? "" : "s"} for "${category}".`}
              </p>
            </header>

            {actionError && (
              <div className="field-error-banner" role="alert">
                {actionError}
              </div>
            )}

            <BookGrid
              books={books}
              borrowedIds={borrowedIds}
              onBorrow={handleBorrow}
              onReturn={handleReturn}
            />
          </div>
        </section>

        <MyLoans borrowedBooks={borrowedBooks} onReturn={handleReturn} activeMember={activeMember} />

        <section className="section join" id="join">
          <div className="wrap join-grid">
            <div className="join-intro">
              <h2>Join Shelfmark Library</h2>
              <p>
                Instant membership registration. In live mode, saves a row to the SQL members table. In offline mode, immediately activates your account in this session!
              </p>
            </div>
            <RegisterForm onMemberRegistered={handleNewMemberAdded} />
          </div>
        </section>
      </main>

      <footer className="site-footer">
        <p className="wrap">
          Shelfmark Library &mdash; React Frontend (Task 2) connected to Spring Boot & Microservices (Task 3 & 4)
        </p>
      </footer>
    </>
  );
}

function offlineBooks(query, category) {
  const q = (query || "").toLowerCase();
  return fallbackBooks.filter((b) => {
    const matchesQuery =
      !q ||
      b.title.toLowerCase().includes(q) ||
      b.author.toLowerCase().includes(q) ||
      b.callNo.toLowerCase().includes(q);
    const matchesCategory = category === "All categories" || b.category === category;
    return matchesQuery && matchesCategory;
  });
}
