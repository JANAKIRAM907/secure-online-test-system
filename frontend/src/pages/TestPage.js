import React, { useState, useEffect, useRef, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchQuestions, submitTest } from '../services/api';

const TEST_DURATION_SECONDS = 600; // 10 minutes
const MAX_TAB_SWITCHES = 3;

function TestPage() {
  const [questions, setQuestions] = useState([]);
  const [answers, setAnswers] = useState({});
  const [tabSwitchCount, setTabSwitchCount] = useState(0);
  const [warning, setWarning] = useState('');
  const [timeLeft, setTimeLeft] = useState(TEST_DURATION_SECONDS);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();
  const timerRef = useRef(null);
  const submittedRef = useRef(false);

  const userId = sessionStorage.getItem('userId');
  const userName = sessionStorage.getItem('userName');

  // Redirect if not logged in
  useEffect(() => {
    if (!userId) navigate('/');
  }, [userId, navigate]);

  // Fetch questions on mount
  useEffect(() => {
    fetchQuestions()
      .then((res) => {
        setQuestions(res.data);
        setLoading(false);
      })
      .catch(() => {
        alert('Failed to load questions. Please refresh.');
        setLoading(false);
      });
  }, []);

  // Auto-submit function
  const autoSubmit = useCallback(
    async (reason) => {
      if (submittedRef.current) return;
      submittedRef.current = true;
      clearInterval(timerRef.current);
      setSubmitting(true);

      try {
        await submitTest(Number(userId), answers, reason, tabSwitchCount);
        navigate('/result');
      } catch (err) {
        alert('Auto-submission failed. Please contact admin.');
      }
    },
    [userId, answers, tabSwitchCount, navigate]
  );

  // ====================================================
  //  TAB SWITCH DETECTION (core security feature)
  // ====================================================
  useEffect(() => {
    const handleVisibilityChange = () => {
      if (document.hidden && !submittedRef.current) {
        setTabSwitchCount((prev) => {
          const newCount = prev + 1;
          if (newCount >= MAX_TAB_SWITCHES) {
            setWarning(`⚠️ WARNING: You switched tabs ${newCount} times. Test is being auto-submitted!`);
            setTimeout(() => autoSubmit('AUTO'), 2000);
          } else {
            setWarning(
              `⚠️ WARNING ${newCount}/${MAX_TAB_SWITCHES}: Tab switch detected! Test will auto-submit after ${MAX_TAB_SWITCHES} switches.`
            );
          }
          return newCount;
        });
      }
    };

    const handleBlur = () => {
      if (!submittedRef.current) {
        setWarning('⚠️ Focus lost. Please keep this tab active during the test.');
      }
    };

    const handleFocus = () => {
      if (tabSwitchCount < MAX_TAB_SWITCHES) {
        setWarning('');
      }
    };

    document.addEventListener('visibilitychange', handleVisibilityChange);
    window.addEventListener('blur', handleBlur);
    window.addEventListener('focus', handleFocus);

    return () => {
      document.removeEventListener('visibilitychange', handleVisibilityChange);
      window.removeEventListener('blur', handleBlur);
      window.removeEventListener('focus', handleFocus);
    };
  }, [autoSubmit, tabSwitchCount]);

  // ====================================================
  //  COUNTDOWN TIMER
  // ====================================================
  useEffect(() => {
    timerRef.current = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(timerRef.current);
          autoSubmit('AUTO');
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(timerRef.current);
  }, [autoSubmit]);

  const formatTime = (secs) => {
    const m = Math.floor(secs / 60).toString().padStart(2, '0');
    const s = (secs % 60).toString().padStart(2, '0');
    return `${m}:${s}`;
  };

  const handleAnswer = (questionId, option) => {
    setAnswers((prev) => ({ ...prev, [questionId]: option }));
  };

  const handleManualSubmit = async () => {
    const unanswered = questions.length - Object.keys(answers).length;
    if (unanswered > 0) {
      const confirm = window.confirm(
        `You have ${unanswered} unanswered question(s). Submit anyway?`
      );
      if (!confirm) return;
    }
    await autoSubmit('MANUAL');
  };

  if (loading) return <div style={styles.center}>Loading questions...</div>;
  if (submitting) return <div style={styles.center}>Submitting your test... Please wait.</div>;

  return (
    <div style={styles.page}>
      {/* Header */}
      <div style={styles.header}>
        <div>
          <strong>👤 {userName}</strong>
        </div>
        <div style={{ ...styles.timer, color: timeLeft < 60 ? '#cc0000' : '#0f3460' }}>
          ⏱ {formatTime(timeLeft)}
        </div>
        <div>
          Answered: {Object.keys(answers).length}/{questions.length}
        </div>
      </div>

      {/* Warning Banner */}
      {warning && <div style={styles.warningBanner}>{warning}</div>}

      {/* Questions */}
      <div style={styles.content}>
        {questions.map((q, index) => (
          <div key={q.id} style={styles.questionCard}>
            <p style={styles.questionText}>
              <strong>Q{index + 1}.</strong> {q.question}
            </p>
            <div style={styles.options}>
              {['A', 'B', 'C', 'D'].map((opt) => {
                const optionText = q[`option${opt}`];
                const selected = answers[q.id] === opt;
                return (
                  <label key={opt} style={{ ...styles.optionLabel, background: selected ? '#dbeafe' : '#f9fafb', borderColor: selected ? '#3b82f6' : '#e5e7eb' }}>
                    <input
                      type="radio"
                      name={`q_${q.id}`}
                      value={opt}
                      checked={selected}
                      onChange={() => handleAnswer(q.id, opt)}
                      style={{ marginRight: '8px' }}
                    />
                    <strong>{opt}.</strong>&nbsp;{optionText}
                  </label>
                );
              })}
            </div>
          </div>
        ))}

        <button style={styles.submitBtn} onClick={handleManualSubmit}>
          ✅ Submit Test
        </button>
      </div>
    </div>
  );
}

const styles = {
  page: { minHeight: '100vh', background: '#f3f4f6', fontFamily: 'sans-serif' },
  header: {
    position: 'sticky',
    top: 0,
    background: '#fff',
    padding: '16px 32px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
    zIndex: 100,
  },
  timer: { fontSize: '22px', fontWeight: 'bold' },
  warningBanner: {
    background: '#fef3cd',
    borderLeft: '6px solid #f59e0b',
    padding: '14px 24px',
    color: '#92400e',
    fontWeight: '600',
  },
  content: { maxWidth: '800px', margin: '0 auto', padding: '24px' },
  questionCard: {
    background: '#fff',
    borderRadius: '10px',
    padding: '24px',
    marginBottom: '20px',
    boxShadow: '0 2px 8px rgba(0,0,0,0.07)',
  },
  questionText: { fontSize: '16px', marginBottom: '16px', lineHeight: '1.6' },
  options: { display: 'flex', flexDirection: 'column', gap: '10px' },
  optionLabel: {
    display: 'flex',
    alignItems: 'center',
    padding: '12px 16px',
    border: '2px solid',
    borderRadius: '8px',
    cursor: 'pointer',
    fontSize: '15px',
    transition: 'all 0.15s',
  },
  submitBtn: {
    width: '100%',
    padding: '16px',
    background: '#0f3460',
    color: '#fff',
    border: 'none',
    borderRadius: '10px',
    fontSize: '18px',
    fontWeight: 'bold',
    cursor: 'pointer',
    marginTop: '10px',
  },
  center: { minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '18px' },
};

export default TestPage;
