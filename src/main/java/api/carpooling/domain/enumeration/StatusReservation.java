package api.carpooling.domain.enumeration;

/**
 * Represents the possible states of a reservation.
 */
public enum StatusReservation {

    /** Reservation has been made but not yet confirmed */
    PENDING,

    /** Reservation has been confirmed */
    CONFIRMED,

    /** Reservation has been canceled */
    CANCELED
}
