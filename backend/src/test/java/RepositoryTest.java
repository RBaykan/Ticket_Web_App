import com.proje.ProjeApplication;
import com.proje.security.exceptions.TicketNotFound;
import com.proje.security.exceptions.UserAlReady;
import com.proje.security.exceptions.UserNotFound;
import com.proje.web.model.*;
import com.proje.web.repository.TicketRepository;
import com.proje.web.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ContextConfiguration(classes = ProjeApplication.class)
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;


    private PasswordEncoder encoder;

    @BeforeEach
    public void setup() {
        encoder = new BCryptPasswordEncoder();
    }


    @Test
    public void saveTicket(){

        // Arrange
        User user = new User();
        user.setFirstname("Ahmet");
        user.setLastname("Uzun");
        user.setUsername("beyaz");
        user.setPassword("ahmet123");


        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setRole("ROLE_USER");
        roles.add(role);

        user.setRoles(roles);


        List<Ticket> tickets = new ArrayList<>();

        Ticket ticket = new Ticket();
        ticket.setThread("New Thread");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setTicketCategory(TicketCategory.GENERAL);
        ticket.setUser(user);


        List<Message> messages = new ArrayList<>();
        Message message = new Message();
        message.setMessage("Hello, this is My new Thread");
        message.setUser(user.getUsername());
        messages.add(message);
        message.setTicket(ticket);

        ticket.setMessages(messages);
        tickets.add(ticket);
        user.setTickets(tickets);



        // ACT
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        List<Ticket> getSavedUserTickets = ticketRepository.findListByUser(savedUser).orElseThrow(TicketNotFound::new);

        // Add new Message
        Ticket getSavedUserTicket = getSavedUserTickets.getFirst();
        Message message1 = new Message();
        message1.setMessage("I neeed help");
        message1.setUser(savedUser.getUsername());
        message1.setTicket(getSavedUserTicket);

        getSavedUserTicket.getMessages().add(message1);


        Ticket savedTicket = ticketRepository.save(getSavedUserTicket);

        // Add new ticket
        Ticket newTicket = new Ticket();
        newTicket.setUser(user);
        newTicket.setThread("Help Thread");
        newTicket.setStatus(TicketStatus.OPEN);
        newTicket.setTicketCategory(TicketCategory.Technical_Support);
        getSavedUserTickets.add(newTicket);


        List<Message> messages2 = new ArrayList<>();
        Message message2 = new Message();
        message2.setTicket(newTicket);
        message2.setUser(user.getUsername());
        message2.setMessage("Hi please, help me");

        messages2.add(message2);
        newTicket.setMessages(messages2);

        Ticket savedNewTicket = ticketRepository.save(newTicket);
        List<Ticket> updateTickets = ticketRepository.findListByUser(savedUser).orElseThrow(TicketNotFound::new);


        Assertions.assertNotNull(getSavedUserTickets); // VT'ye kaydedilen kullanıcının biletleri de kaydedildi
        Assertions.assertEquals(2, updateTickets.size()); // Kullanıcı biletlerinde 2 bilet mi var
        Assertions.assertNotEquals(savedNewTicket, getSavedUserTicket); // Biletlere kaydedilen biletler, farklı
        Assertions.assertEquals("Hi please, help me", savedNewTicket.getMessages().getFirst().getMessage()); // Yeni biletin ilk mesajı
        // Yeni kaydedilen bilet, son bilet olduğu için güncellenen biletler listesindeki son bilet ile mesaşları karşılaştırma
        Assertions.assertEquals(savedNewTicket.getMessages().getFirst().getMessage(), updateTickets.getLast().getMessages().getLast().getMessage());









    }








}
