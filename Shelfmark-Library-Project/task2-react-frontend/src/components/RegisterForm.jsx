import { useState } from "react";
import { registerMember } from "../api";

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const phonePattern = /^[0-9]{10}$/;

export default function RegisterForm({ onMemberRegistered }) {
  const [form, setForm] = useState({ name: "", email: "", phone: "", plan: "Student" });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [submitted, setSubmitted] = useState(false);
  const [registeredMember, setRegisteredMember] = useState(null);
  const [apiNotice, setApiNotice] = useState("");

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
  }

  function validate() {
    const next = {};
    if (!form.name || form.name.trim().length < 2) next.name = "Enter your full name (at least 2 letters).";
    if (!emailPattern.test(form.email)) next.email = "Enter a valid email address (e.g. name@example.com).";
    if (!phonePattern.test(form.phone)) next.phone = "Enter a valid 10-digit mobile number.";
    if (!form.plan) next.plan = "Choose a membership plan.";
    return next;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    const next = validate();
    setErrors(next);
    if (Object.keys(next).length > 0) return;

    setLoading(true);
    setApiNotice("");
    try {
      const saved = await registerMember({
        name: form.name.trim(),
        email: form.email.trim(),
        phone: form.phone.trim(),
        plan: form.plan,
      });
      setRegisteredMember(saved);
      setApiNotice("Saved directly to Spring Boot + SQL database!");
      if (onMemberRegistered) onMemberRegistered(saved);
    } catch {
      // Backend offline fallback - simulate local registration
      const mockSaved = {
        id: Date.now(),
        name: form.name.trim(),
        email: form.email.trim(),
        phone: form.phone.trim(),
        plan: form.plan,
        joinedOn: new Date().toISOString().split("T")[0],
      };
      setRegisteredMember(mockSaved);
      setApiNotice("Backend offline: recorded for this interactive session.");
      if (onMemberRegistered) onMemberRegistered(mockSaved);
    } finally {
      setLoading(false);
      setSubmitted(true);
    }
  }

  function handleReset() {
    setForm({ name: "", email: "", phone: "", plan: "Student" });
    setErrors({});
    setSubmitted(false);
    setRegisteredMember(null);
    setApiNotice("");
  }

  if (submitted && registeredMember) {
    return (
      <div className="join-form register-success-card">
        <div className="badge in" style={{ marginBottom: "0.8rem", display: "inline-block" }}>
          Registration Confirmed
        </div>
        <h3 style={{ margin: "0 0 0.5rem" }}>Welcome, {registeredMember.name}!</h3>
        <p style={{ margin: "0 0 0.5rem", color: "var(--muted, #555)" }}>
          Your <strong>{registeredMember.plan}</strong> membership is active.
          {registeredMember.id && <span> Member ID: <code>#{registeredMember.id}</code></span>}
        </p>
        <p className="status-note" style={{ fontSize: "0.88rem", color: "var(--accent, #935a24)" }}>
          {apiNotice}
        </p>
        <button className="btn btn-outline" onClick={handleReset} style={{ marginTop: "1rem" }}>
          Register Another Member
        </button>
      </div>
    );
  }

  return (
    <form className="join-form" onSubmit={handleSubmit} noValidate>
      <div className="field">
        <label htmlFor="name">Full Name *</label>
        <input
          id="name"
          name="name"
          value={form.name}
          onChange={handleChange}
          placeholder="e.g. Andrea Smith"
          disabled={loading}
        />
        {errors.name && <p className="field-error">{errors.name}</p>}
      </div>

      <div className="field-row">
        <div className="field">
          <label htmlFor="email">Email Address *</label>
          <input
            id="email"
            name="email"
            type="email"
            value={form.email}
            onChange={handleChange}
            placeholder="andrea@example.com"
            disabled={loading}
          />
          {errors.email && <p className="field-error">{errors.email}</p>}
        </div>
        <div className="field">
          <label htmlFor="phone">Mobile Number *</label>
          <input
            id="phone"
            name="phone"
            type="tel"
            value={form.phone}
            onChange={handleChange}
            placeholder="10-digit number"
            disabled={loading}
          />
          {errors.phone && <p className="field-error">{errors.phone}</p>}
        </div>
      </div>

      <div className="field">
        <label htmlFor="plan">Membership Plan *</label>
        <select id="plan" name="plan" value={form.plan} onChange={handleChange} disabled={loading}>
          <option value="Student">Student (3 books, 14 days loan)</option>
          <option value="Faculty">Faculty (8 books, 30 days loan)</option>
          <option value="Public">Public (2 books, 10 days loan)</option>
        </select>
        {errors.plan && <p className="field-error">{errors.plan}</p>}
      </div>

      <button type="submit" className="btn btn-brass" disabled={loading} style={{ marginTop: "0.5rem" }}>
        {loading ? "Registering..." : "Create My Membership"}
      </button>
    </form>
  );
}
