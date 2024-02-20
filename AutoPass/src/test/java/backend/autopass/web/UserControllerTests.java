package backend.autopass.web;

import backend.autopass.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

//    User USER_1 = User
//            .builder()
//            .firstName("William")
//            .id(1)
//            .wallet(UserWallet.builder().membershipActive(false).ticketAmount(0).build())
//            .email("aubergine@gmail.com")
//            .pass(new Pass(1, null))
//            .password(userService.)
//            .lastName("Aubergine")
//            .build();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();


    }

    @Test
    public void getUser_success() {

    }


}
