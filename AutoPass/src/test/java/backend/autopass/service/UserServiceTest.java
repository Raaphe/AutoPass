package backend.autopass.service;

import backend.autopass.model.repositories.PassRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.repositories.UserWalletRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private PassRepository mockPassRepository;
    @Mock
    private UserWalletRepository mockUserWalletRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private UserService mockUserService;

    @Test
    public void createUser() {

//        // Given
//        SignUpDTO dto = SignUpDTO.builder()
//                .email("user@hotmail.com")
//                .firstname("Swiss")
//                .lastname("Nguyen")
//                .password("passwd")
//                .role(Role.USER)
//                .build();
//
//        Pass pass = new Pass();
//        UserWallet wallet = UserWallet.builder().membershipActive(false).ticketAmount(0).build();
//
//        User builtUser = User.builder()
//                .email("user@hotmail.com")
//                .role(Role.USER)
//                .firstName("Swiss")
//                .lastName("Nguyen")
//                .password(mockPasswordEncoder.encode("passwd"))
//                .wallet(wallet)
//                .pass(pass)
//                .build();
//
//        User expectedUser = User.builder()
//                .email("user@hotmail.com")
//                .id(1)
//                .isDeleted(false)
//                .role(Role.USER)
//                .firstName("Swiss")
//                .lastName("Nguyen")
//                .password(mockPasswordEncoder.encode("passwd"))
//                .wallet(wallet)
//                .pass(pass)
//                .build();
//
//        given(mockUserRepository.save(any(User.class))).willReturn(expectedUser);
//        given(mockPassRepository.save(any(Pass.class))).willReturn(pass);
//        given(mockUserWalletRepository.save(any(UserWallet.class))).willReturn(wallet);
////        given(mockPasswordEncoder.encode(anyString())).willReturn(mockPasswordEncoder.encode("passwd"));

//
//        // When
//        User resultUser = mockUserService.createUser(dto);
//
//        // Then
//        verify(mockUserRepository).save(eq(builtUser));
//        verify(mockPassRepository).save(any(Pass.class));
//        verify(mockUserWalletRepository).save(any(UserWallet.class));
//        verify(mockPasswordEncoder).encode(eq("passwd"));
//
//        Assertions.assertEquals(expectedUser, resultUser); // Simplified assertion, adjust as needed.
    }

//
//    @Test
//    void createAdmin() {
//
//
//        // Given
//
//
//        // When
//
//
//        // Then
//    }
//
//    @Test
//    void getUserById() {
//
//
//        // Given
//
//
//        // When
//
//
//        // Then
//
//    }
//
//    @Test
//    void userExists() {
//
//
//        // Given
//
//
//        // When
//
//
//        // Then
//
//    }
//
//    @Test
//    void testUserExists() {
//
//        // Given
//
//
//        // When
//
//
//        // Then
//
//    }
}