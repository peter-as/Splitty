package server.api;

import commons.Debt;
import commons.Event;
import commons.Participant;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.utils.DatabaseUtils;

@Controller
@RequestMapping("/debts")
public class DebtController {
    @Autowired
    private final DatabaseUtils du;

    /**
     * Constructor
     */
    public DebtController(DatabaseUtils du) {
        this.du = du;
    }

    /**
     * Debts get method
     * @param id The id of the event whose debts we want to get
     * @return A list of all debts in an event
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<Debt>> debts(@PathVariable("id") long id) {
        if (id < 0 || !du.eventExistsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Participant> participants = new ArrayList<>(du.eventFindById(id).get().getPrtList());
        participants.sort(Comparator.comparing(Participant::getNetDebt));
        List<Debt> ans = new ArrayList<>();
        for (int i = 0; i < participants.size() - 1; i++) {
            if (participants.get(i).getNetDebt() > 0) {
                ans.add(new Debt(participants.get(i),
                        participants.get(participants.size() - 1),
                        participants.get(i).getNetDebt(),
                        -participants.get(i).getNetDebt(),
                        null));
            } else if (participants.get(i).getNetDebt() != 0) {
                ans.add(new Debt(participants.get(participants.size() - 1),
                        participants.get(i),
                        -participants.get(i).getNetDebt(),
                        participants.get(i).getNetDebt(),
                        null));
            }
        }
        return ResponseEntity.ok(ans);
    }

    /**
     * remove debt method, which resolves debt between 2 people
     * @param id Id of the event in which there is debt resolving
     * @param debt The debt to be resolved
     * @return An empty debt object if the method worked, otherwise error
     */
    @PutMapping("/{id}")
    public ResponseEntity<Debt> removeDebt(@PathVariable("id") long id, @RequestBody Debt debt) {
        if (id < 0 || !du.eventExistsById(id) || debt == null
                || debt.getPerson1() == null || debt.getPerson2() == null) {
            return ResponseEntity.notFound().build();
        } else {
            Event ev = du.eventFindById(id).orElseThrow();
            List<Participant> p = ev.getPrtList();

            Long id1 = debt.getPerson1().getId();
            Long id2 = debt.getPerson2().getId();

            Participant firstt = null;
            Participant secondd = null;

            for (Participant part : p) {
                if (Objects.equals(part.getId(), id1)) {
                    firstt = part;
                }
                if (Objects.equals(part.getId(), id2)) {
                    secondd = part;
                }
            }
            final Participant first = firstt;
            final Participant second = secondd;
            if (first == null || second == null) {
                return ResponseEntity.notFound().build();
            }
            int firstDebt = du.participantFindById(first.getId()).orElseThrow().getNetDebt();
            int secondDebt = du.participantFindById(second.getId()).orElseThrow().getNetDebt();
            firstDebt -= debt.getAmt1Owes();
            secondDebt += debt.getAmt1Owes();
            ev.getPrtList()
                    .stream()
                    .filter(x -> x.getId().equals(first.getId()))
                    .findAny().orElse(new Participant(null, null, null, null))
                    .setNetDebt(firstDebt);
            ev.getPrtList()
                    .stream()
                    .filter(x -> x.getId().equals(second.getId()))
                    .findAny()
                    .orElse(new Participant(null, null, null, null))
                    .setNetDebt(secondDebt);
            du.eventSave(ev);
            debt.setAmt1Owes(0);
            debt.setAmt2Owes(0);
            return ResponseEntity.ok(debt);
        }
    }
}
