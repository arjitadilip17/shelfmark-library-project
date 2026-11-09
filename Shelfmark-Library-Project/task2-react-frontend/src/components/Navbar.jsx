import { useState } from "react";

export default function Navbar({ loanCount, activeMember }) {
  const [open, setOpen] = useState(false);

  return (
    <header className="site-header">
      <div className="bar">
        <a className="brand" href="#top">
          <span className="brand-mark" aria-hidden="true"></span>
          Shelfmark Library
        </a>

        {activeMember && (
          <div className="nav-member-chip hide-sm">
            <span className="chip-avatar">{activeMember.name.charAt(0)}</span>
            <span className="chip-name">{activeMember.name}</span>
          </div>
        )}

        <button
          className="nav-toggle"
          aria-label="Toggle navigation menu"
          aria-expanded={open}
          onClick={() => setOpen((v) => !v)}
        >
          <span></span><span></span><span></span>
        </button>

        <nav className={`site-nav ${open ? "open" : ""}`} aria-label="Main navigation">
          <ul>
            <li><a href="#top" onClick={() => setOpen(false)}>Home</a></li>
            <li><a href="#catalogue" onClick={() => setOpen(false)}>Catalogue</a></li>
            <li>
              <a href="#my-loans" onClick={() => setOpen(false)}>
                My loans <span className="nav-counter">{loanCount}</span>
              </a>
            </li>
            <li><a className="nav-cta" href="#join" onClick={() => setOpen(false)}>Join the library</a></li>
          </ul>
        </nav>
      </div>
    </header>
  );
}
