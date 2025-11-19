package server.api;

import commons.Event;
import commons.Participant;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ParticipantRepository;
import server.utils.DatabaseUtils;

@RestController
@RequestMapping("")
@Transactional
public class ParticipantController {

    @Autowired
    private final DatabaseUtils du;

    /**
     * Constructor for a controller.
     * @param du the repo to use
     */
    public ParticipantController(DatabaseUtils du) {
        this.du = du;
    }

    /**
     * Gets all the participants involved in an event
     * @param eid the id of the event
     * @return a list of all the participants in the event
     */
    @GetMapping("/events/{eid}/participants")
    public ResponseEntity<List<Participant>> getAllParticipantsByEventId(@PathVariable Long eid) {
        if (eid == null || !du.eventExistsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        Event event = du.eventFindById(eid).orElse(null);
        List<Participant> participants = new ArrayList<>();

        if (event != null && event.getPrtList() != null) {
            participants.addAll(event.getPrtList());
        }

        return ResponseEntity.ok(participants);

    }

    /**
     * Get a certain participant by their id
     * (event id not needed here, since participant id is unique)
     * @param pid the id of the participant
     * @return the requested participant
     */
    @GetMapping("/participants/{pid}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long pid) {


        if (pid == null || !du.participantExistsById(pid)) {
            return ResponseEntity.notFound().build();
        }

        Participant participant = du.participantFindById(pid).orElse(null);

        return new ResponseEntity<>(participant, HttpStatus.OK);
    }


    /**
     * Creates a new participant
     * @param eid the id of the event
     * @param participantReq the participant object that should be added
     * @return the added participant
     */
    @PostMapping("/events/{eid}/participants")
    public ResponseEntity<String> createParticipant(@PathVariable Long eid,
                                                 @RequestBody Participant participantReq) {
        if (eid == null || !du.eventExistsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        Event event = du.eventFindById(eid).orElse(null);
        participantReq.setPartEvent(event);
        if (event.getPrtList() == null) {
            event.setPrtList(new ArrayList<>());
        }
        List<Participant> participants = event.getPrtList();
        List<String> names = new ArrayList<>();

        for (Participant part : participants) {
            names.add(part.getName());
        }

        if (names.contains(participantReq.getName())) {
            return new ResponseEntity<>("Warning, name already exists!", HttpStatus.BAD_REQUEST);
        }

        participantReq = du.participantCreate(participantReq);
        event.getPrtList().add(participantReq);
        du.eventSave(event);

        return new ResponseEntity<>(
                "Participant with name " + participantReq.getName() + " was added!",
                HttpStatus.CREATED);
    }


    /**
     * Updates an existing participant
     * @param pid the id of the participant
     * @param participantReq the participant object that should be updated
     * @return the updated participant
     */
    @PutMapping("/participants/{pid}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Long pid,
                                                         @RequestBody Participant participantReq) {

        if (pid != null && participantReq != null && du.participantExistsById(pid)) {
            Participant part = du.participantFindById(pid).get();
            part.setName(participantReq.getName());
            part.setBic(participantReq.getBic());
            part.setIban(participantReq.getIban());
            part.setEmail(participantReq.getEmail());
            part.setNetDebt(participantReq.getNetDebt());
            return new ResponseEntity<>(du.participantSave(part), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a participant
     * @param eid the id of the event
     * @param pid the id of the participant
     * @return a String message of confirmation in case of successful deletion
     */
    @DeleteMapping("/events/{eid}/participants/{pid}")
    public ResponseEntity<String> deleteParticipant(@PathVariable Long eid,
                                                    @PathVariable Long pid) {

        if (eid != null && pid != null && du.participantExistsById(pid)) {
            du.participantDeleteById(pid);
            du.eventSave(du.eventFindById(eid).get());
            return ResponseEntity.ok("Participant with id " + pid + " was deleted");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
