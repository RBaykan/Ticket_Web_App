import com.proje.web.dto.*;
import com.proje.web.mapper.MessegaMapper;
import com.proje.web.mapper.TicketMapper;
import com.proje.web.mapper.UserMapper;
import com.proje.web.model.*;
import com.proje.web.repository.MessageRepository;
import com.proje.web.repository.TicketRepository;
import com.proje.web.repository.UserRepository;
import com.proje.web.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessegaMapper messegaMapper;


    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp(){
    }

    @Test
    public void createUser_returnDTO_flow(){


        User user = User.builder()
                .firstname("Ayşe")
                .lastname("Kedi")
                .username("kedi")
                .password("ayse1234")
                .build();

        CreateUser createUser = CreateUser.builder()
                .firstname("Ayşe")
                .lastname("Kedi")
                .username("kedi")
                .password("ayse1234")
                .repassword("ayse1234")
                .build();



        UserDTO userDTO = UserDTO.builder()
                .firstname("Ayşe")
                .lastname("Kedi")
                .username("kedi")
                .password("ayse1234")
                .build();



        // ACT
        when(encoder.encode(anyString())).thenReturn("12345678");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(userMapper.modelToDTO(Mockito.any())).thenReturn(userDTO);

        UserDTO savedDTO = userService.registerNewUser(createUser);

        Assertions.assertNotNull(savedDTO); // UserDTO var


    }


    @Test
    public void saveTicket_returnDTO_flow(){


        // Arrange
        User user = Mockito.mock(User.class);
        TicketDTO ticketDTO = Mockito.mock(TicketDTO.class);

        CreateTicket createTicket = new CreateTicket();
        createTicket.setUserId(1L);
        createTicket.setThread("New Thread");
        createTicket.setMessage("Hi, how are you Admin");



        // Act
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(ticketMapper.modelToDTO(Mockito.any())).thenReturn(ticketDTO);

        TicketDTO savedTicketDTO = userService.createTicket(createTicket);

        // Assert
        verify(userRepository).findById(1L);
        verify(ticketMapper).modelToDTO(any());
        Assertions.assertNotNull(savedTicketDTO);

    }


    @Test
    public void sendMessage_returnDTO_flow(){

        Ticket ticket = Mockito.mock(Ticket.class);
        User user = Mockito.mock(User.class);
        MessageDTO messageDTO = Mockito.mock(MessageDTO.class);
        SendMessage sendMessage = Mockito.mock();

        ticket.setStatus(TicketStatus.OPEN);


        when(ticketRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ticket));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(messegaMapper.modelToDTO(Mockito.any())).thenReturn(messageDTO);


        MessageDTO savedMessageDTO = userService.sendMessage(sendMessage);



        // Assert
        verify(ticketRepository).findById(anyLong());
        verify(userRepository).findById(anyLong());
        verify(messegaMapper).modelToDTO(any());
        Assertions.assertNotNull(savedMessageDTO);





    }




}
