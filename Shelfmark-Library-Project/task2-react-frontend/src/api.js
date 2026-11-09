// Thin wrapper around fetch() for talking to the Spring Boot REST backend.
// Default backend URL: http://localhost:8080/api (Task 3 Monolith or Task 4 API Gateway)
const BASE_URL = "http://localhost:8080/api";

// Current member session. Default is member id 1 ("Manoj") pre-seeded in the database.
export let CURRENT_MEMBER_ID = 1;

export function setSessionMemberId(id) {
  CURRENT_MEMBER_ID = Number(id);
}

export function getSessionMemberId() {
  return CURRENT_MEMBER_ID;
}

async function handle(res) {
  if (!res.ok) {
    let message = `Request failed (${res.status})`;
    try {
      const body = await res.json();
      message = body.message || message;
    } catch {
      // response had no JSON body; keep default message
    }
    throw new Error(message);
  }
  if (res.status === 204) return null;
  return res.json();
}

export function fetchBooks(query = "", category = "") {
  const params = new URLSearchParams();
  if (query) params.set("q", query);
  if (category && category !== "All categories") params.set("category", category);
  return fetch(`${BASE_URL}/books?${params.toString()}`).then(handle);
}

export function fetchMembers() {
  return fetch(`${BASE_URL}/members`).then(handle);
}

export function fetchActiveLoans(memberId = CURRENT_MEMBER_ID) {
  return fetch(`${BASE_URL}/loans/member/${memberId}`).then(handle);
}

export function borrowBook(bookId, memberId = CURRENT_MEMBER_ID) {
  return fetch(`${BASE_URL}/loans`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ bookId, memberId }),
  }).then(handle);
}

export function returnBook(loanId, bookId) {
  return fetch(`${BASE_URL}/loans/${loanId}/return`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ bookId }),
  }).then(handle);
}

export function registerMember(member) {
  return fetch(`${BASE_URL}/members`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(member),
  }).then(handle);
}
