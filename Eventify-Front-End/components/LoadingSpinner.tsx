'use client';
import { useState, useEffect } from 'react';

interface LoadingSpinnerProps {
  message?: string;
  size?: 'small' | 'medium' | 'large';
  showColdStartHint?: boolean;
  coldStartDelayMs?: number;
}

/**
 * Reusable LoadingSpinner component
 * Shows loading state with optional message
 * Can show cold start hint after a delay for free tier hosting services
 */
export default function LoadingSpinner({ 
  message = 'Loading...', 
  size = 'medium',
  showColdStartHint = true,
  coldStartDelayMs = 5000,
}: LoadingSpinnerProps) {
  const [showHint, setShowHint] = useState(false);

  useEffect(() => {
    if (!showColdStartHint) return;
    
    const timer = setTimeout(() => {
      setShowHint(true);
    }, coldStartDelayMs);

    return () => clearTimeout(timer);
  }, [showColdStartHint, coldStartDelayMs]);

  const sizeClasses = {
    small: { spinner: 16, text: '0.875rem' },
    medium: { spinner: 24, text: '1rem' },
    large: { spinner: 32, text: '1.125rem' },
  };

  const { spinner: spinnerSize, text: textSize } = sizeClasses[size];

  return (
    <div className="card" style={{ textAlign: 'center', padding: '2rem' }}>
      <div 
        style={{ 
          display: 'inline-block',
          width: spinnerSize,
          height: spinnerSize,
          border: '3px solid #e2e8f0',
          borderTopColor: '#6366f1',
          borderRadius: '50%',
          animation: 'spin 0.8s linear infinite',
        }} 
      />
      <p style={{ marginTop: '0.75rem', fontSize: textSize }} className="muted">
        {message}
      </p>
      {showHint && (
        <p style={{ marginTop: '0.5rem', fontSize: '0.8rem', color: '#6b7280' }}>
          ⏳ The server is waking up from sleep mode. This may take up to 1-2 minutes...
        </p>
      )}
      <style jsx>{`
        @keyframes spin {
          to { transform: rotate(360deg); }
        }
      `}</style>
    </div>
  );
}
