'use client';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useVenues, useCities, useEvents } from '../../hooks/useApi';
import { useLanguage } from '../../context/LanguageContext';
import { Venue } from '../../services/api';
import VenueCard from '../../components/VenueCard';
import LoadingSpinner from '../../components/LoadingSpinner';
import ErrorMessage from '../../components/ErrorMessage';

export default function VenuesPage() {
  const [cityFilter, setCityFilter] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const router = useRouter();
  const { t } = useLanguage();

  const { data: venues, error: venuesError, isLoading: venuesLoading, mutate: mutateVenues } = useVenues();
  const { data: cities } = useCities();
  const { data: events } = useEvents();

  if (venuesLoading) {
    return (
      <div>
        <h2>{t('venues.title')}</h2>
        <LoadingSpinner message={t('common.loading')} />
      </div>
    );
  }

  if (venuesError) {
    return (
      <div>
        <h2>{t('venues.title')}</h2>
        <ErrorMessage message={venuesError.message} onRetry={() => mutateVenues()} />
      </div>
    );
  }

  // Filter venues
  const filtered = (venues || [])
    .filter((v) => {
      if (!searchQuery) return true;
      const q = searchQuery.toLowerCase();
      return (
        v.name.toLowerCase().includes(q) ||
        v.address?.toLowerCase().includes(q) ||
        v.city?.name?.toLowerCase().includes(q)
      );
    })
    .filter((v) => {
      if (!cityFilter) return true;
      return v.city?.cityId === parseInt(cityFilter);
    })
    .sort((a, b) => a.name.localeCompare(b.name));

  // Get events per venue
  function getVenueEvents(venueId: number) {
    return (events || [])
      .filter(e => e.venues?.some((v: Venue) => v.venueId === venueId))
      .filter(e => new Date(e.eventDate) >= new Date())
      .sort((a, b) => new Date(a.eventDate).getTime() - new Date(b.eventDate).getTime());
  }

  function handleViewSchedule(venueId: number) {
    router.push(`/venue/${venueId}`);
  }

  return (
    <div>
      <div style={{ marginBottom: 24 }}>
        <h2 style={{ marginBottom: 4 }}>{t('venues.title')}</h2>
        <p className="muted">{t('venues.subtitle')}</p>
      </div>

      <div className="card" style={{ marginBottom: 20 }}>
        <div style={{ marginBottom: 12 }}>
          <div style={{ position: 'relative' }}>
            <svg
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="var(--muted)"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
              style={{ position: 'absolute', left: 12, top: '50%', transform: 'translateY(-50%)', pointerEvents: 'none' }}
            >
              <circle cx="11" cy="11" r="8" />
              <line x1="21" y1="21" x2="16.65" y2="16.65" />
            </svg>
            <input
              type="text"
              placeholder="Search venues, addresses..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              style={{ paddingLeft: 40 }}
            />
          </div>
        </div>
        <div className="controls">
          <div>
            <label className="small">{t('venues.filterCity')}</label>
            <select value={cityFilter} onChange={(e) => setCityFilter(e.target.value)}>
              <option value="">{t('venues.allCities')}</option>
              {(cities || []).map((c) => (
                <option key={c.cityId} value={c.cityId}>{c.name}</option>
              ))}
            </select>
          </div>
        </div>
      </div>

      <div style={{ marginBottom: 12, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <p className="small muted">
          {filtered.length} {filtered.length === 1 ? 'venue' : 'venues'} found
          {(searchQuery || cityFilter) && (
            <button
              onClick={() => { setSearchQuery(''); setCityFilter(''); }}
              style={{
                background: 'none',
                border: 'none',
                color: 'var(--accent)',
                cursor: 'pointer',
                fontSize: 13,
                fontWeight: 600,
                marginLeft: 8,
              }}
            >
              Clear filters
            </button>
          )}
        </p>
      </div>

      {filtered.length === 0 ? (
        <div className="empty-state">
          <p>{t('venues.noVenues')}</p>
          <p className="small muted" style={{ marginTop: 8 }}>Try adjusting your filters or search terms</p>
        </div>
      ) : (
        <div className="venues-grid">
          {filtered.map((venue) => (
            <VenueCard
              key={venue.venueId}
              venue={venue}
              events={getVenueEvents(venue.venueId)}
              onViewSchedule={handleViewSchedule}
            />
          ))}
        </div>
      )}
    </div>
  );
}
