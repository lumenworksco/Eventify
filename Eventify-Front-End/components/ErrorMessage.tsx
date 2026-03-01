'use client';
import { useLanguage } from '../context/LanguageContext';

interface ErrorMessageProps {
  title?: string;
  message: string;
  showBackendHint?: boolean;
  onRetry?: () => void;
}

/**
 * Reusable ErrorMessage component
 * Displays error information with optional retry button
 */
export default function ErrorMessage({ 
  title,
  message, 
  showBackendHint = true,
  onRetry 
}: ErrorMessageProps) {
  const { t } = useLanguage();
  
  return (
    <div className="card" style={{ borderLeft: '4px solid var(--danger)' }}>
      <div style={{ color: 'var(--danger)', fontWeight: 600, marginBottom: '0.5rem' }}>
        {title || t('common.error')}
      </div>
      <p style={{ color: 'var(--danger)', marginBottom: '0.5rem' }}>{message}</p>
      {showBackendHint && (
        <p className="small muted">
          {t('common.backendError')}
        </p>
      )}
      {onRetry && (
        <button 
          onClick={onRetry} 
          className="btn btn-ghost"
          style={{ marginTop: '0.75rem' }}
        >
          {t('common.retry')}
        </button>
      )}
    </div>
  );
}
