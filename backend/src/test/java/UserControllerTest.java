import com.fasterxml.jackson.databind.ObjectMapper;
import com.proje.ProjeApplication;
import com.proje.web.dto.CreateTicket;
import com.proje.web.dto.CreateUser;
import com.proje.web.model.TicketCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



@ContextConfiguration(classes = ProjeApplication.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void CreateUser_ReturnCreated() throws Exception {

        String randomUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);

        CreateUser createUser = CreateUser.builder()
                .firstname("Veli")
                .lastname("Yaşar")
                .username(randomUsername)
                .password("12345678")
                .repassword("12345678")
                .build();

        String json = objectMapper.writeValueAsString(createUser);

        // Kullanıcıyı kaydet
        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.username").value(randomUsername));
    }


    @Test
    public void role_accessApi_withJWT() throws Exception {

        String adminJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0Njc5OTQ5NSwiZXhwIjoxNzQ3MTU5NDk1fQ.PsHrbciQIlz63KcVCcZjYTVzsWlQ-A87OOhjvYydH5o";

        String userJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGkxOSIsImlhdCI6MTc0NjgzMDEyNCwiZXhwIjoxNzQ3MTkwMTI0fQ.PE6mrOs1VjOln73u2-ckhfSL-z4ziUZ2IX7uZEipjsM";

        // admin token'ı ile, admin api'ye istek at, ok al
        mockMvc.perform(get("/api/user")
                        .header("Authorization", "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());


        // user token'ı ile, admin api'ye istek at, forbidden al
        mockMvc.perform(get("/api/user")
                        .header("Authorization", "Bearer " + userJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isForbidden());


        CreateTicket ticket = new CreateTicket();
        ticket.setTicketCategory(TicketCategory.FEEDBACK);
        ticket.setUserId(1L);
        ticket.setMessage("Messega");
        ticket.setThread("Thread");

        String json = objectMapper.writeValueAsString(ticket);

        // Hem Admin hem User'ın erişebileceği ama diğerlerinin (yanlış token, authentication olmadan erişim) erişmeyeceği API'ye erişim, erişim olacak ama badrequest verecek, parametre göndermiyoruz
        mockMvc.perform(post("/api/user/createTicket")
                        .header("Authorization", "Bearer " + "3213123")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());


        // Hem Admin hem User'ın erişebileceği ama diğerlerinin (yanlış token, authentication olmadan erişim) erişmeyeceği API'ye erişim, erişim olacak ama badrequest verecek, parametre göndermiyoruz
        mockMvc.perform(post("/api/user/createTicket")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isUnauthorized());



    }




}



