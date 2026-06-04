import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

// POST /api/login
export const loginUser = (name, rollNumber) =>
  api.post('/login', { name, rollNumber });

// GET /api/questions
export const fetchQuestions = () =>
  api.get('/questions');

// POST /api/submit
export const submitTest = (userId, answers, submissionType, tabSwitchCount) =>
  api.post('/submit', { userId, answers, submissionType, tabSwitchCount });

// GET /api/result?userId=X
export const fetchResult = (userId) =>
  api.get(`/result?userId=${userId}`);

export default api;
