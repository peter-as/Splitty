package server.api;

import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;
import server.utils.DatabaseUtils;

@RestController
@RequestMapping("")
public class ExpenseController {

    @Autowired
    DatabaseUtils du;

    /**
     * Constructor for the expense repo
     *
     * @param du The database access class
     */
    public ExpenseController(DatabaseUtils du) {
        this.du = du;
    }

    /**
     * Gets all the expenses in one event
     *
     * @param eid event id the id of the event
     * @return a list of all expenses in the event
     */
    @GetMapping("/events/{eid}/expenses")
    public ResponseEntity<List<Expense>> getAllExpenses(@PathVariable Long eid) {

        if (eid == null || !du.eventExistsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        Event event = du.eventFindById(eid).orElse(null);
        List<Expense> expenses = new ArrayList<>();

        if (event != null && event.getExpList() != null) {
            expenses.addAll(event.getExpList());
        }

        return ResponseEntity.ok(expenses);
    }

    /**
     * Get a certain expense by its id
     * (event id not needed here, since expense id is unique)
     *
     * @param id expense id
     * @return the requested expense
     */
    @GetMapping("/events/{eid}/expenses/{id}")
    public ResponseEntity<Expense> getExpense(@PathVariable Long id) {

        if (id == null || !du.expenseExistsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Expense expense = du.expenseFindById(id).orElse(null);

        return new ResponseEntity<>(expense, HttpStatus.OK);

    }

    /**
     * Creates a new expense
     *
     * @param eid     event id
     * @param expense expense to be created/added to database
     * @return the added expense
     */
    @PostMapping("/events/{eid}/expenses")
    public ResponseEntity<Expense> createExpense(@PathVariable Long eid,
                                                 @RequestBody Expense expense) {


        if (eid == null || !du.eventExistsById(eid)) {
            return ResponseEntity.notFound().build();
        }

        if (expense == null) {
            return ResponseEntity.badRequest().build();
        }

        Event event = du.eventFindById(eid).orElse(null);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        expense = du.expenseCreate(expense);
        //get info for tag list from drop down menu selections
        // and add them to the list of tags in the expense
        if (event.getExpList() == null) {
            event.setExpList(new ArrayList<>());
        }
        List<Expense> expenses = event.getExpList();


        if (expense.getParticipants() != null && !expense.getParticipants().isEmpty()) {
            int amt = expense.getAmountPaid();
            int individualAmt = (int) ((float) amt / expense.getParticipants().size());
            //this individual amount is rounded down, the person who compensates pays
            Participant payer = du.participantFindById(expense.getWhoPaid().getId()).orElse(null);
            if (payer == null) {
                return ResponseEntity.badRequest().build();
            }
            for (Participant newP : expense.getParticipants()) {
                Participant p = du.participantFindById(newP.getId()).orElse(null);
                if (p != null && !Objects.equals(p.getId(), payer.getId())) {
                    p.setNetDebt(p.getNetDebt() + individualAmt);
                    payer.setNetDebt(payer.getNetDebt() - individualAmt);
                    du.participantSave(p);
                    payer = du.participantSave(payer);
                }
            }
        }
        expense = du.expenseFindById(expense.getId()).orElse(null);

        event.getExpList().add(expense);
        du.eventSave(event);

        return ResponseEntity.ok(expense);
    }

    /**
     * Updates an existing expense
     *
     * @param id         expense id
     * @param expenseReq new updated expense to replace the old one
     * @return updated expense
     */
    @PutMapping("/events/{eid}/expenses/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id,
                                                 @PathVariable Long eid,
                                                 @RequestBody Expense expenseReq) {
        Expense expense = du.expenseFindById(id).orElse(null);
        if (expense != null) {
            this.deleteExpense(eid, id);
            this.createExpense(eid, expenseReq);
            return new ResponseEntity<>(du.expenseSave(expenseReq), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an expense
     *
     * @param eid Event id
     * @param id  expense id
     * @return the value of the expense
     */
    @DeleteMapping("/events/{eid}/expenses/{id}")
    public ResponseEntity<Expense> deleteExpense(@PathVariable Long eid,
                                                 @PathVariable Long id) {

        Event event = du.eventFindById(eid).orElse(null);
        Expense expense = du.expenseFindById(id).orElse(null);

        if (event != null
                && expense != null
                && expense.getParticipants() != null
                && !expense.getParticipants().isEmpty()) {
            int amt = expense.getAmountPaid();
            int individualAmt = (int) ((float) amt / expense.getParticipants().size());
            Participant payer = du.participantFindById(expense.getWhoPaid().getId()).orElse(null);
            if (payer == null) {
                return ResponseEntity.badRequest().build();
            }

            for (Participant newParticipant : expense.getParticipants()) {
                Participant p = du.participantFindById(newParticipant.getId()).orElse(null);
                if (p != null && !Objects.equals(p.getId(), payer.getId())) {
                    p.setNetDebt(p.getNetDebt() - individualAmt);
                    payer.setNetDebt(payer.getNetDebt() + individualAmt);
                    du.participantSave(p);
                    du.participantSave(payer);
                }
            }
            Expense ex = event.getExpList().stream()
                    .filter(x -> Objects.equals(x.getId(), id))
                    .findFirst()
                    .orElse(null);
            if (ex != null) {
                event.getExpList().remove(ex);
            }
            du.eventSave(event);
            du.expenseDeleteById(id);
            return ResponseEntity.ok(expense);
        }

        return ResponseEntity.badRequest().build();
    }


}
