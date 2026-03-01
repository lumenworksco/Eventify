/**
 * Custom hook for API requests with SWR
 * Provides caching, revalidation, and error handling
 */
import useSWR, { SWRConfiguration, SWRResponse } from 'swr';
import { useAuth } from '../context/AuthContext';
import type { Event, Venue, City } from '../services/api';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'https://eventify-back-end.onrender.com/api';

// Timeout for API requests (2 minutes for cold starts on Render free tier)
const API_TIMEOUT_MS = 120000;

interface ApiError {
  message: string;
  status: number;
  isTimeout?: boolean;
}

// Fetcher function for SWR with timeout support
async function fetcher<T>(url: string, token?: string): Promise<T> {
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), API_TIMEOUT_MS);

  const headers: HeadersInit = {
    'Content-Type': 'application/json',
  };
  
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  try {
    const response = await fetch(url, {
      headers,
      signal: controller.signal,
    });

    clearTimeout(timeoutId);

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      const error: ApiError = {
        message: errorData.message || `HTTP error! status: ${response.status}`,
        status: response.status,
      };
      throw error;
    }

    const text = await response.text();
    return text ? JSON.parse(text) : ({} as T);
  } catch (err) {
    clearTimeout(timeoutId);
    
    if (err instanceof Error && err.name === 'AbortError') {
      const error: ApiError = {
        message: 'Request timed out. The server may be starting up (cold start). Please try again.',
        status: 408,
        isTimeout: true,
      };
      throw error;
    }
    
    // Handle network errors (Failed to fetch)
    if (err instanceof TypeError && err.message === 'Failed to fetch') {
      const error: ApiError = {
        message: 'Failed to fetch',
        status: 0,
      };
      throw error;
    }
    
    throw err;
  }
}

/**
 * Custom hook for fetching data with SWR
 * Includes authentication token automatically when user is logged in
 * Configured with retry logic for handling cold starts on Render free tier
 */
export function useApi<T>(
  endpoint: string | null,
  config?: SWRConfiguration
): SWRResponse<T, ApiError> {
  const { user } = useAuth();
  
  const url = endpoint ? `${API_BASE_URL}${endpoint}` : null;
  
  return useSWR<T, ApiError>(
    url,
    (url: string) => fetcher<T>(url, user?.token),
    {
      revalidateOnFocus: false,
      // Retry up to 3 times with exponential backoff for cold starts
      errorRetryCount: 3,
      errorRetryInterval: 10000,
      // Keep showing stale data while revalidating
      revalidateOnReconnect: true,
      // Dedupe requests within 10s to prevent request storms during cold starts
      dedupingInterval: 10000,
      ...config,
    }
  );
}

/**
 * Hook for fetching events
 */
export function useEvents() {
  return useApi<Event[]>('/events');
}

/**
 * Hook for fetching a single event
 */
export function useEvent(eventId: number | null) {
  return useApi<Event>(eventId ? `/events/${eventId}` : null);
}

/**
 * Hook for fetching venues
 */
export function useVenues() {
  return useApi<Venue[]>('/venues');
}

/**
 * Hook for fetching a single venue
 */
export function useVenue(venueId: number | null) {
  return useApi<Venue>(venueId ? `/venues/${venueId}` : null);
}

/**
 * Hook for fetching cities
 */
export function useCities() {
  return useApi<City[]>('/cities');
}

/**
 * Hook for fetching a single city
 */
export function useCity(cityId: number | null) {
  return useApi<City>(cityId ? `/cities/${cityId}` : null);
}

/**
 * Hook for fetching event types
 */
export function useEventTypes() {
  return useApi<string[]>('/events/types');
}
