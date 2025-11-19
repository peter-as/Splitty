package commons;

public class EventDTO {
    private boolean isDeleted;
    private Event event;

    /**
     * Constroctor
     */
    public EventDTO() {
    }

    /**
     * Constructor
     * @param isDeleted whether the event is to be deleted
     * @param event the event
     */
    public EventDTO(boolean isDeleted, Event event) {
        this.isDeleted = isDeleted;
        this.event = event;
    }

    /**
     * Getter
     * @return is deleted
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Setter
     * @param deleted deleted
     */
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    /**
     * Getter
     * @return event
     */

    public Event getEvent() {
        return event;
    }

    /**
     * Setter
     * @param event event
     */

    public void setEvent(Event event) {
        this.event = event;
    }
}
