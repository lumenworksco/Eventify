'use client';
import { useParams } from 'next/navigation';
import Link from 'next/link';
import { useVenue, useEvents } from '../../../hooks/useApi';
import { useLanguage } from '../../../context/LanguageContext';
import { Venue } from '../../../services/api';
import LoadingSpinner from '../../../components/LoadingSpinner';
import ErrorMessage from '../../../components/ErrorMessage';

export default function VenuePage() {
  const params = useParams();
  const venueId = params?.id ? parseInt(params.id as string) : null;
  const { t } = useLanguage();

  const { data: venue, error: venueError, isLoading: venueLoading, mutate: mutateVenue } = useVenue(venueId);
  const { data: events, error: eventsError, isLoading: eventsLoading } = useEvents();

  if (!venueId) {
    return (
      <div className="card">
        {t('venue.idMissing')}
        <a href="/events" className="btn btn-ghost" style={{ marginLeft: 8 }}>
            {t('nav.events')}
          </a>
      </div>
    );
  }

  const isLoading = venueLoading || eventsLoading;
  const error = venueError || eventsError;

  if (isLoading) {
    return <LoadingSpinner message={t('common.loading')} />;
  }

  if (error) {
    return (
      <ErrorMessage 
        message={error.message} 
        onRetry={() => mutateVenue()}
      />
    );
  }

  if (!venue) {
    return <div className="card">{t('venue.notFound')}</div>;
  }

  // Filter events for this venue
  const venueEvents = (events || [])
    .filter(event => event.venues?.some((v: Venue) => v.venueId === venueId))
    .sort((a, b) => {
      const dateCompare = new Date(a.eventDate).getTime() - new Date(b.eventDate).getTime();
      if (dateCompare !== 0) return dateCompare;
      return a.startTime.localeCompare(b.startTime);
    });

  function formatDateTime(date: string, startTime: string): string {
    const eventDate = new Date(date);
    const timeStr = startTime.substring(0, 5);
    return `${eventDate.toLocaleDateString()} ${timeStr}`;
  }

  return (
    <div>
      <div className="card">
        <h2>{venue.name}</h2>
        {venue.address ? (
          <p className="muted">
            <a
              href={`https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(`${venue.address}, ${venue.city.name}, ${venue.city.country}`)}`}
              target="_blank"
              rel="noopener noreferrer"
              style={{ color: 'var(--accent)', textDecoration: 'none', display: 'inline-flex', alignItems: 'center', gap: 4 }}
            >
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
                <circle cx="12" cy="10" r="3" />
              </svg>
              {venue.address}
            </a>
          </p>
        ) : (
          <p className="muted">{t('venue.noAddress')}</p>
        )}
        <div className="small muted">
          <strong>{t('venue.city')}:</strong> {venue.city.name}
          {venue.city.region && `, ${venue.city.region}`}, {venue.city.country}
        </div>
        {venue.capacity && (
          <div className="small muted">
            <strong>{t('venue.capacity')}:</strong> {venue.capacity.toLocaleString()}
          </div>
        )}
      </div>

      <h3 style={{ marginTop: 12 }}>{t('venue.schedule')}</h3>
      {venueEvents.length === 0 ? (
        <div className="card small">{t('venue.noEvents')}</div>
      ) : (
        <div className="card">
          <ul style={{ margin: 0, padding: 0, listStyle: 'none' }}>
            {venueEvents.map((event) => (
              <li key={event.eventId} style={{ padding: '10px 0', borderBottom: '1px solid var(--border-light)' }}>
                <Link href={`/events/${event.eventId}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                  <div className="event-row" style={{ cursor: 'pointer' }}>
                    <div className="event-date">{formatDateTime(event.eventDate, event.startTime)}</div>
                    <div className="event-info">
                      <div className="event-title">{event.title}</div>
                      <div className="event-meta">
                        {event.eventDescription?.eventType}
                        {event.availableTickets !== null && event.availableTickets > 0 &&
                          ` • ${t('events.ticketsAvailable', { count: event.availableTickets })}`}
                      </div>
                      {event.eventDescription?.featuredArtists && (
                        <div className="small muted">
                          {t('venue.featuring', { artists: event.eventDescription.featuredArtists })}
                        </div>
                      )}
                    </div>
                  </div>
                </Link>
              </li>
            ))}
          </ul>
        </div>
      )}

      <div style={{ marginTop: 12 }}>
        <Link href="/events" className="btn btn-ghost">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <polyline points="15 18 9 12 15 6" />
          </svg>
          {t('venue.backToEvents')}
        </Link>
      </div>
    </div>
  );
}
