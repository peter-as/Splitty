/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

import client.scenes.OverviewCtrl;
import com.fasterxml.jackson.databind.JsonNode;
import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javafx.application.Platform;
import org.glassfish.jersey.client.ClientConfig;

public class ServerUtils {

    private final String server;

    /**
     * Constructor
     */
    public ServerUtils() {
        String s = "";
        try (BufferedReader br =
                     Files.newBufferedReader(
                             Paths.get("client/src/main/resources/client/email/config.txt"))) {
            br.readLine();
            br.readLine();
            br.readLine();
            s = br.readLine().replace("splitty ", "");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        server = s;
    }

    /**
     * Get events method
     * @return the events
     */
    public List<Event> getEvents() {
        List<Event> ans =  ClientBuilder.newClient(new ClientConfig())
                .target(server).path("events")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<>() {
                });
        return ans;
    }


    /**
     * Get a list of participants from a certain event.
     * @param eventID id of the event to get participants from.
     * @return the list of participants
     */
    public List<Participant> getParticipants(String eventID) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("events")
                .path(eventID)
                .path("participants")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<>() {});
        } else {
            // Handle error cases
            System.err.println("Failed to retrieve participants for event with id:" + eventID);
            return Collections.emptyList();
        }
    }

    /**
     * Method to get a list of expenses from a certain event.
     * @param eventID the id of the event to get the expenses from.
     * @return the list of expenses.
     */
    public List<Expense> getExpenses(String eventID) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("events")
                .path(eventID)
                .path("expenses")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<Expense> expenses = response.readEntity(new GenericType<>() {});
            return expenses;
        } else {
            // Handle error cases
            System.err.println("Failed to retrieve expenses for event with id:" + eventID);
            return Collections.emptyList();
        }
    }

    /**
     * Method to get a single event.
     * @param eventID id of the event to get.
     * @return the event.
     */
    public Event getEvent(String eventID) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("events")
                .path(eventID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Event ev = response.readEntity(new GenericType<>() {});
            return ev;
        } else {
            // Handle error cases
            System.err.println("Failed to retrieve event with id:" + eventID);
            return null;
        }
    }

    /**
     * Post event method
     * Posts event to server utils
     * @param ev Event to post
     */
    public Event postEvent(Event ev) {
        ev.setId((long) 3);
        Event e = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("events")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(ev, APPLICATION_JSON), Event.class);
        return e;
    }

    /**
     * Post expense method
     * posts expense to server utils
     * @param ex - expense to add to server
     * @return expense
     */
    public Expense postExpense(Expense ex, String eventId) {
        ex.setId((long) 3);
        if (ex.getTag().getId() == null) {
            ex.getTag().setId((long) 3);
        }
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("events")
                .path(eventId)
                .path("expenses")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(ex, APPLICATION_JSON), Expense.class);

    }


    /**
     * Edits an expense
     * @param expense the expense to be edited
     * @return the response expense
     */
    public Expense editExpense(Expense expense, String id, String eventID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("events")
                .path(eventID)
                .path("expenses")
                .path("{id}")
                .resolveTemplate("id", id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON) //what response type is accepted
                .put(Entity.entity(expense, APPLICATION_JSON), Expense.class);
    }

    /**
     * Deletes an expense
     * @param expense the expense to be deleted
     * @param eventID the id of the respective event
     */
    public void deleteExpense(Expense expense, String eventID) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("events")
                .path("{eid}").resolveTemplate("eid", eventID)
                .path("expenses")
                .path("{id}")
                .resolveTemplate("id", expense.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON) //what response type is accepted
                .delete();
    }

    /**
     * Adds a new participant
     * @param participant the participant to be added
     * @param eventID the id of the respective event
     * @return the response string
     */
    public String addParticipant(Participant participant, String eventID) {
        participant.setId((long) 3);
        String p = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("events")
                .path("{eid}").resolveTemplate("eid", eventID)
                .path("participants")
                .request(APPLICATION_JSON)
                .accept(TEXT_PLAIN) //what response type is accepted
                .post(Entity.entity(participant, APPLICATION_JSON), String.class);
        return p;
    }

    /**
     * Edits a new participant
     * @param participant the participant to be edited
     * @return the response participant
     */
    public Participant editParticipant(Participant participant) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("participants/{id}")
                .resolveTemplate("id", participant.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON) //what response type is accepted
                .put(Entity.entity(participant, APPLICATION_JSON), Participant.class);
    }

    /**
     * Removes a participant
     * @param participant the participant to be removed
     * @param eventID the id of the respective event
     * @return the response string
     */
    public String removeParticipant(Participant participant, String eventID) {

        String p = ClientBuilder.newClient(new ClientConfig())
                .target(server).path("events")
                .path("{eid}").resolveTemplate("eid", eventID)
                .path("participants")
                .path("{id}")
                .resolveTemplate("id", participant.getId())
                .request(APPLICATION_JSON)
                .accept(TEXT_PLAIN) //what response type is accepted
                .delete()
                .toString();
        return p;
    }

    /**
     * Get debts method
     * @param eventID event whose debts we want to get
     * @return the list of debts for the event
     */
    public List<Debt> getDebts(String eventID) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("debts")
                .path("{id}").resolveTemplate("id", eventID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<Debt>>() {});
    }

    /**
     * Remove debt
     * @param eventID Event the debt which we want to remove is in
     * @param debt The debt we want to remove
     */
    public void removeDebt(String eventID, Debt debt) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("debts")
                .path("{id}").resolveTemplate("id", eventID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(debt, APPLICATION_JSON), Debt.class);
    }

    /**
     * Gets all tags for an event
     * @param eventID - event id
     * @return - list of tags
     */
    public List<Tag> getTags(String eventID) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("events")
                .path(eventID)
                .path("tags")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<Tag> r =  response.readEntity(new GenericType<>() {});
            return r;
        } else {
            // Handle error cases
            System.err.println("Failed to retrieve expenses for event with id:" + eventID);
            return Collections.emptyList();
        }
    }

    /**
     * Adds a new tag
     * @param tag the tag to be added
     * @param eventId the id of the respective event
     * @return the response string
     */
    public Tag addTag(Tag tag, String eventId) {
        Tag t = ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("events")
                .path(eventId)
                .path("tags")
                .request(APPLICATION_JSON)
                .accept(TEXT_PLAIN) //what response type is accepted
                .post(Entity.entity(tag, APPLICATION_JSON), Tag.class);
        System.out.println("success!");
        return t;
    }

    /**
     * Call upon the server to remove the given Tag
     * @param eventID ID of the event the tag is linked to.
     * @param tag the tag to remove.
     * @return String of info on the deleted tag.
     */
    public String removeTag(String eventID, Tag tag) {
        String t = ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("events")
                .path("{eid}").resolveTemplate("eid", eventID)
                .path("tags")
                .path("{id}")
                .resolveTemplate("id", tag.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete()
                .toString();
        return t;
    }

    /**
     * Call upon the server to edit the given Tag
     * @param eventID ID of the event the tag is linked to.
     * @param tag the tag to edit.
     * @return The edited tag.
     */
    public Tag editTag(String eventID, Tag tag) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("events")
                .path("{eid}").resolveTemplate("eid", eventID)
                .path("tags")
                .path("{id}")
                .resolveTemplate("id", tag.getId())
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON) //what response type is accepted
                .put(Entity.entity(tag, APPLICATION_JSON), Tag.class);
    }
    
    /**
     * Edits event in server
     * @param eventID - id of expense to edit
     * @param event - new version of event
     * @return - editted event.
     */
    public Event editEvent(String eventID, Event event) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("events")
                .path("{id}").resolveTemplate("id", eventID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .put(Entity.entity(event, APPLICATION_JSON), Event.class);
    }

    /**
     * Call upon the server to get exchange rates for a day
     * @param date the date of the day
     * @return JsonNode made of the exchange rates
     */
    public JsonNode getRates(String date) {
        Response response = ClientBuilder.newClient(new ClientConfig())
                .target(server)
                .path("exchange")
                .path(date)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<>() {});
        } else {
            System.err.println("Failed getting events");
            return null;
        }
    }

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    public static Map<Long, Map<Object, Consumer<Event>>> listeners = new HashMap<>();

    /**
     * Register for updates
     * @param oc place to get the event id
     * @param consumer function to execute
     */
    public void registerForUpdates(OverviewCtrl oc, Consumer<Event> consumer) {
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                if (oc.getEventID() == null) {
                    continue;
                }
                Response response = ClientBuilder.newClient(new ClientConfig())
                        .target(server)
                        .path("events/updates")
                        .path(oc.getEventID())
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);
                try {
                    Event ev = response.readEntity(Event.class);
                    Platform.runLater(() -> consumer.accept(ev));
                } catch (Exception ex) {
                    continue;
                }
            }
        });
    }

    /**
     * Interrupt thread
     */
    public void stop() {
        EXEC.shutdownNow();
    }
}