import { categories } from "../data/books";

// Controlled form: query and category live in App's state (lifted up),
// and every keystroke/selection updates them via the setter props.
export default function SearchBar({ query, setQuery, category, setCategory }) {
  return (
    <form className="search" onSubmit={(e) => e.preventDefault()} role="search">
      <div className="field">
        <label htmlFor="q">Title or author</label>
        <input
          type="search"
          id="q"
          placeholder="For example, Clean Code"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>
      <div className="field">
        <label htmlFor="category">Category</label>
        <select id="category" value={category} onChange={(e) => setCategory(e.target.value)}>
          {categories.map((c) => (
            <option key={c} value={c}>
              {c}
            </option>
          ))}
        </select>
      </div>
    </form>
  );
}
