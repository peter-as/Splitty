package server.database;

import commons.Participant;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    /**
     * Finds a participant by their name
     * @param name the name of the participant that is to be found
     * @return the found participant
     */
    Participant findByName(String name);

    /**
     * Deletes a participant by name
     * @param name the name of the participant
     */
    void deleteByName(String name);

    /**
     * Checks if a participant exists by looking for their name
     * @param name the name of the participant
     * @return true if the participant exists, false otherwise
     */
    boolean existsByName(String name);
}
