// Sample catalogue data used as fallback when the backend is offline
// and as the initial catalog specification.

const fallbackBooks = [
  {
    id: 1,
    title: "Clean Code",
    author: "Robert C. Martin",
    category: "Computer Science",
    callNo: "005.1 MAR",
    copies: 3,
  },
  {
    id: 2,
    title: "Introduction to Algorithms",
    author: "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein",
    category: "Computer Science",
    callNo: "005.1 COR",
    copies: 0,
  },
  {
    id: 3,
    title: "Database System Concepts",
    author: "Abraham Silberschatz, Henry F. Korth, S. Sudarshan",
    category: "Computer Science",
    callNo: "005.74 SIL",
    copies: 5,
  },
  {
    id: 4,
    title: "Wings of Fire",
    author: "A. P. J. Abdul Kalam, Arun Tiwari",
    category: "History and Biography",
    callNo: "920 KAL",
    copies: 2,
  },
  {
    id: 5,
    title: "The Discovery of India",
    author: "Jawaharlal Nehru",
    category: "History and Biography",
    callNo: "954 NEH",
    copies: 0,
  },
  {
    id: 6,
    title: "Head First Java",
    author: "Kathy Sierra, Bert Bates",
    category: "Computer Science",
    callNo: "005.13 SIE",
    copies: 4,
  },
  {
    id: 7,
    title: "Artificial Intelligence: A Modern Approach",
    author: "Stuart Russell, Peter Norvig",
    category: "Science and Technology",
    callNo: "006.3 RUS",
    copies: 1,
  },
  {
    id: 8,
    title: "Malgudi Days",
    author: "R. K. Narayan",
    category: "Literature",
    callNo: "823 NAR",
    copies: 6,
  },
];

export default fallbackBooks;
