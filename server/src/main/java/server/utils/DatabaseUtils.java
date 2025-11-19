package server.utils;

import commons.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.api.EventController;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;
import server.database.TagRepository;

@Service
public class DatabaseUtils {
    private final EventRepository eventRepository;
    private final ExpenseRepository expenseRepository;
    private final ParticipantRepository participantRepository;
    private final TagRepository tagRepository;
    private EventController ec;

    /**
     * Constructor
     * @param eventRepository repository for events
     * @param expenseRepository repository for expenses
     * @param participantRepository repository for participants
     * @param tagRepository repository for tags
     */
    @Autowired
    public DatabaseUtils(EventRepository eventRepository,
                       ExpenseRepository expenseRepository,
                       ParticipantRepository participantRepository,
                       TagRepository tagRepository) {
        this.eventRepository = eventRepository;
        this.expenseRepository = expenseRepository;
        this.participantRepository = participantRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * Get all events
     * @return all saved events
     */
    public List<Event> eventGetAll() {
        return eventRepository.findAll();
    }

    /**
     * Checks if event exists by id
     * @param id id of the event
     * @return True if it exists
     */
    public boolean eventExistsById(Long id) {
        if (id == null) {
            return false;
        }
        return eventRepository.existsById(id);
    }

    /**
     * Find event by id
     * @param id Id to search for
     * @return The event with that id(optional)
     */
    public Optional<Event> eventFindById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return eventRepository.findById(id);
    }

    /**
     * Save event; Id should not be null
     * @param ev Event to save
     * @return The saved event
     */
    public Event eventSave(Event ev) {
        if (ev == null) {
            return null;
        }
        ev.setLastUpdate(new Date(System.currentTimeMillis()));
        if (ev.getPrtList() != null) {
            for (int i = 0; i < ev.getPrtList().size(); i++) {
                if (ev.getPrtList().get(i) != null
                        && !participantExistsById(ev.getPrtList().get(i).getId())) {
                    ev.getPrtList().set(i, participantCreate(ev.getPrtList().get(i)));
                } else if (ev.getPrtList().get(i) != null) {
                    ev.getPrtList().set(i,
                            participantFindById(ev.getPrtList().get(i).getId()).get());
                }
            }
        }
        if (ev.getExpList() != null) {
            for (int i = 0; i < ev.getExpList().size(); i++) {
                if (ev.getExpList().get(i) != null
                        && !expenseExistsById(ev.getExpList().get(i).getId())) {
                    ev.getExpList().set(i, expenseCreate(ev.getExpList().get(i)));
                } else if (ev.getExpList().get(i) != null) {
                    ev.getExpList().set(i,
                            expenseFindById(ev.getExpList().get(i).getId()).get());
                }
            }
        }
        if (ev.getTagList() != null) {
            for (int i = 0; i < ev.getTagList().size(); i++) {
                if (!tagExistsById(ev.getTagList().get(i).getId())) {
                    ev.getTagList().set(i, tagCreate(ev.getTagList().get(i)));
                } else {
                    ev.getTagList().set(i,
                            tagFindById(ev.getTagList().get(i).getId()).get());
                }
            }
        }
        Event event = eventRepository.save(ev);
        if (ec != null) {
            ec.handleUpdate(event);
        }
        if (EventController.listeners.containsKey(event.getId())) {
            EventController.listeners.get(event.getId())
                    .forEach((k, l) -> l.accept(event));
        }
        return event;
    }

    /**
     * Deletes event by id
     * @param id id to delete
     */
    public void eventDeleteById(Long id) {
        if (id == null) {
            return;
        }
        eventRepository.deleteById(id);
        eventRepository.flush();
    }

    /**
     * Creates a COPY of the event; Makes a new id, with everything empty except title
     * @param ev Event to copy
     * @return The new event
     */
    public Event eventCreate(Event ev) {
        if (ev == null) {
            return null;
        }
        ev.setPrtList(new ArrayList<>());
        ev.setExpList(new ArrayList<>());
        ev.setTagList(new ArrayList<>());
        ev.setLastUpdate(new Date(System.currentTimeMillis()));
        ev.setDateOfCreation(new Date(System.currentTimeMillis()));
        ev.setId(null);
        ev = eventRepository.save(ev);
        ev.generateInviteCode();
        return eventRepository.save(ev);
    }











    /**
     * Checks if tag exists by id
     * @param id id to check
     * @return True if it exists
     */
    public boolean tagExistsById(Long id) {
        if (id == null) {
            return false;
        }
        return tagRepository.existsById(id);
    }

    /**
     * Find tag by id
     * @param id id to search for
     * @return Optional for the tag
     */
    public Optional<Tag> tagFindById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return tagRepository.findById(id);
    }

    /**
     * Save tag to database; to be used when id is not null
     * @param t Tag to save
     * @return The saved tag
     */
    public Tag tagSave(Tag t) {
        if (t == null) {
            return null;
        }
        return tagRepository.save(t);
    }

    /**
     * Delete tag by id
     * @param id id for which tag to delete
     */
    public void tagDeleteById(Long id) {
        if (id == null) {
            return;
        }
        tagRepository.deleteById(id);
        tagRepository.flush();
    }

    /**
     * Creates a COPY of the tag; This means a NEW id is created
     * @param t The tag to copy
     * @return The created tag
     */
    public Tag tagCreate(Tag t) {
        if (t == null) {
            return null;
        }
        t.setId(null);
        return tagRepository.save(t);
    }










    /**
     * Checks if expense exists by id
     * @param id id to check for
     * @return True if it exists
     */
    public boolean expenseExistsById(Long id) {
        if (id == null) {
            return false;
        }
        return expenseRepository.existsById(id);
    }

    /**
     * Find expense by id
     * @param id id to search for
     * @return The optional for the expense
     */
    public Optional<Expense> expenseFindById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return expenseRepository.findById(id);
    }

    /**
     * Save expense method; Not to be used with id null
     * @param exp expense to be saved
     * @return The saved expense
     */
    public Expense expenseSave(Expense exp) {
        if (exp == null) {
            return null;
        }
        if (exp.getTag() != null && !tagExistsById(exp.getTag().getId())) {
            exp.setTag(this.tagCreate(exp.getTag()));
        } else if (exp.getTag() != null) {
            exp.setTag(tagFindById(exp.getTag().getId()).get());
        }
        if (exp.getWhoPaid() != null && !participantExistsById(exp.getWhoPaid().getId())) {
            exp.setWhoPaid(this.participantCreate(exp.getWhoPaid()));
        } else if (exp.getWhoPaid() != null) {
            exp.setWhoPaid(this.participantFindById(exp.getWhoPaid().getId()).get());
        }
        if (exp.getParticipants() != null) {
            for (int i = 0; i < exp.getParticipants().size(); i++) {
                if (exp.getParticipants().get(i) != null
                        && !participantExistsById(exp.getParticipants().get(i).getId())) {
                    exp.getParticipants()
                            .set(i, this.participantCreate(exp.getParticipants().get(i)));
                } else if (exp.getParticipants().get(i) != null) {
                    exp.getParticipants()
                            .set(i, participantFindById(exp.getParticipants()
                                    .get(i).getId()).get());
                }

            }
        }
        return expenseRepository.save(exp);
    }

    /**
     * Deletes expense by id
     * @param id Id for the deleted expense
     */
    public void expenseDeleteById(Long id) {
        if (id == null) {
            return;
        }
        Expense expense = expenseRepository.findById(id).orElse(null);
        if (expense == null) {
            return;
        }
        if (expense.getParticipants() != null) {
            expense.getParticipants().clear();
        }
        expense.setTag(null);
        expense.setWhoPaid(null);
        expenseRepository.save(expense);
        expenseRepository.deleteById(id);
        expenseRepository.flush();
    }

    /**
     * Saves a COPY of the expense to the database(new id)
     * @param exp Expense to create
     * @return The created expense
     */
    public Expense expenseCreate(Expense exp) {
        if (exp == null) {
            return null;
        }
        if (exp.getTag() != null) {
            if (!tagExistsById(exp.getTag().getId())) {
                exp.setTag(this.tagCreate(exp.getTag()));
            } else {
                exp.setTag(tagFindById(exp.getTag().getId()).orElse(null));
            }
        }
        if (exp.getWhoPaid() != null) {
            if (!participantExistsById(exp.getWhoPaid().getId())) {
                exp.setWhoPaid(this.participantCreate(exp.getWhoPaid()));
            } else {
                exp.setWhoPaid(this.participantFindById(exp.getWhoPaid().getId()).orElse(null));
            }
        }
        if (exp.getParticipants() != null) {
            for (int i = 0; i < exp.getParticipants().size(); i++) {
                if (!participantExistsById(exp.getParticipants().get(i).getId())) {
                    exp.getParticipants()
                            .set(i, this.participantCreate(exp.getParticipants().get(i)));
                } else {
                    exp.getParticipants()
                            .set(i, participantFindById(exp.getParticipants()
                                    .get(i).getId()).orElse(null));
                }

            }
        }
        exp.setId(null);
        return expenseRepository.save(exp);
    }











    /**
     * Checks if participant exists by id in the database
     * @param id Id which is searched for
     * @return True if participant exists in the database
     */
    public boolean participantExistsById(Long id) {
        if (id == null) {
            return false;
        }
        return participantRepository.existsById(id);
    }

    /**
     * Finds participant from database by id
     * @param id Id which is searched in the database
     * @return the optional for participant
     */
    public Optional<Participant> participantFindById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return participantRepository.findById(id);
    }

    /**
     * Save participant to database
     * @param participant participant, which should not have null as id
     * @return the participant from the database
     */
    public Participant participantSave(Participant participant) {
        if (participant == null) {
            return null;
        }
        return participantRepository.save(participant);
    }

    /**
     * Deletes participant by id from the database
     * @param id id to delete
     */
    public void participantDeleteById(Long id) {
        if (id == null) {
            return;
        }
        participantRepository.deleteById(id);
        participantRepository.flush();
    }

    /**
     * Saves a COPY of p to the database (new id)
     * @param p The participant we create from
     * @return The newly created participant
     */
    public Participant participantCreate(Participant p) {
        if (p == null) {
            return null;
        }
        p.setId(null);
        return participantRepository.save(p);
    }

    /**
     * Setter
     * @param ec event controller
     */
    public void setEc(EventController ec) {
        this.ec = ec;
    }

    /**
     * Event clone method
     * @param event event to clone
     */
    public void eventClone(Event event) {
        Event dc = new Event(event.getDateOfCreation(), event.getLastUpdate(),
                event.getName(), new ArrayList<>(event.getPrtList()),
                new ArrayList<>(event.getExpList()),
                new ArrayList<>(event.getTagList()));
        Event copy = this.eventCreate(dc);
        HashMap<Long, Participant> partmap = new HashMap<>();
        HashMap<Long, Tag> tagmap = new HashMap<>();
        for (Participant part : event.getPrtList()) {
            final long oldid = part.getId();
            part.setPartEvent(copy);
            part.setId(null);
            part = this.participantSave(part);
            copy.getPrtList().add(part);
            copy = this.eventSave(copy);
            partmap.put(oldid, part);
        }
        for (Tag part : event.getTagList()) {
            final long oldid = part.getId();
            part.setId(null);
            part.setTagEvent(copy);
            part = this.tagSave(part);
            copy.getTagList().add(part);
            copy = this.eventSave(copy);
            tagmap.put(oldid, part);
        }
        for (Expense exp : event.getExpList()) {
            exp.setWhoPaid(partmap.get(exp.getWhoPaid().getId()));
            exp.setTag(tagmap.get(exp.getTag().getId()));
            exp.getParticipants().replaceAll(participant -> partmap.get(
                    participant.getId()));
            copy.getExpList().add(this.expenseSave(exp));
            copy = this.eventSave(copy);
        }

        if (ec != null) {
            ec.handleUpdate(copy);
        }
    }
}