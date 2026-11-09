# Task 2 — Shelfmark Library (React App)

Interactive JavaScript and ReactJS Application Development. This app fetches
its data from the Task 3 Spring Boot backend over a REST API - start that
first (see `../task3-springboot-backend/README.md`), then run this.

## Run it

```
npm install
npm run dev
```

Then open the local URL shown in the terminal (usually http://localhost:5173).
If the backend isn't running yet, the page shows a banner and falls back to
sample data so the UI still works for demos.

## What it demonstrates

- React function components, props, and the `useState` / `useMemo` hooks
- Controlled form inputs for search and registration
- List rendering with `.map()` and conditional rendering
- Event handling (borrow / return / submit)
- Client-side form validation with regular expressions
- Lifting state up to a shared parent (`App.jsx`) so components stay in sync

## Files

- `src/App.jsx` — top-level state (books, borrowed list, search/filter) and page layout
- `src/components/` — Navbar, SearchBar, BookGrid, BookCard, MyLoans, RegisterForm
- `src/data/books.js` — sample catalogue data (replace with a live API call in Task 3)
- `src/App.css` — styling, carried over from Task 1
