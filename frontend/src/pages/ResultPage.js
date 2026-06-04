import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { fetchResult } from '../services/api';

function ResultPage() {
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const userId = sessionStorage.getItem('userId');

  useEffect(() => {
    if (!userId) { navigate('/'); return; }
    fetchResult(userId)
      .then((res) => { setResult(res.data); setLoading(false); })
      .catch(() => { alert('Failed to load result.'); setLoading(false); });
  }, [userId, navigate]);

  const handleLogout = () => {
    sessionStorage.clear();
    navigate('/');
  };

  if (loading) return <div style={styles.center}>Loading result...</div>;

  const percentage = result ? Math.round((result.score / result.totalQuestions) * 100) : 0;
  const passed = percentage >= 40;

  return (
    <div style={styles.page}>
      <div style={styles.card}>
        <div style={{ ...styles.badge, background: passed ? '#dcfce7' : '#fee2e2', color: passed ? '#166534' : '#991b1b' }}>
          {passed ? '🎉 PASSED' : '❌ FAILED'}
        </div>

        <h1 style={styles.title}>Test Result</h1>
        <p style={styles.name}>{result.name} — {result.rollNumber}</p>

        <div style={styles.scoreCircle}>
          <span style={styles.scoreNum}>{result.score}/{result.totalQuestions}</span>
          <span style={styles.scoreLabel}>{percentage}%</span>
        </div>

        <div style={styles.details}>
          <div style={styles.detailRow}>
            <span>Submission Type</span>
            <span style={{ color: result.submissionType === 'AUTO' ? '#cc0000' : '#166534', fontWeight: '600' }}>
              {result.submissionType === 'AUTO' ? '⚡ Auto (Tab Switch)' : '✅ Manual'}
            </span>
          </div>
          <div style={styles.detailRow}>
            <span>Tab Switches</span>
            <span style={{ color: result.tabSwitchCount > 0 ? '#cc0000' : '#166534', fontWeight: '600' }}>
              {result.tabSwitchCount}
            </span>
          </div>
          <div style={styles.detailRow}>
            <span>Submitted At</span>
            <span>{new Date(result.submittedTime).toLocaleString()}</span>
          </div>
        </div>

        <button style={styles.btn} onClick={handleLogout}>
          🔄 Take Another Test
        </button>
      </div>
    </div>
  );
}

const styles = {
  page: { minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'linear-gradient(135deg, #1a1a2e, #16213e, #0f3460)', fontFamily: 'sans-serif' },
  card: { background: '#fff', padding: '40px', borderRadius: '16px', textAlign: 'center', maxWidth: '460px', width: '100%', boxShadow: '0 20px 60px rgba(0,0,0,0.3)' },
  badge: { display: 'inline-block', padding: '8px 24px', borderRadius: '20px', fontWeight: '700', fontSize: '14px', marginBottom: '20px' },
  title: { fontSize: '28px', color: '#1a1a2e', marginBottom: '8px' },
  name: { color: '#666', marginBottom: '24px' },
  scoreCircle: { background: '#f0f4ff', borderRadius: '50%', width: '140px', height: '140px', margin: '0 auto 28px', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' },
  scoreNum: { fontSize: '28px', fontWeight: 'bold', color: '#0f3460' },
  scoreLabel: { fontSize: '16px', color: '#666' },
  details: { textAlign: 'left', background: '#f9fafb', borderRadius: '10px', padding: '16px', marginBottom: '24px' },
  detailRow: { display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #e5e7eb', fontSize: '14px' },
  btn: { width: '100%', padding: '14px', background: '#0f3460', color: '#fff', border: 'none', borderRadius: '8px', fontSize: '16px', fontWeight: '600', cursor: 'pointer' },
  center: { minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '18px' },
};

export default ResultPage;
