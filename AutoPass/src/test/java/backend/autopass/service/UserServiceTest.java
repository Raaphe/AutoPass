package backend.autopass.service;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ActiveProfiles("test")
//@DisplayName("User Service Class")
//@ExtendWith(MockitoExtension.class)
class UserServiceTest {

//    @Mock
//    private UserRepository mockUserRepository;
//    @Mock
//    private PassRepository mockPassRepository;
//    @Mock
//    private UserWalletRepository mockUserWalletRepository;
//    @Mock
//    private PasswordEncoder mockPasswordEncoder;
//
//    @InjectMocks
//    private UserService mockUserService;
//
//    @BeforeEach
//    void setUp() {
//        ReflectionTestUtils.setField(mockUserService, "userRepository", mockUserRepository);
//        ReflectionTestUtils.setField(mockUserService, "passRepository", mockPassRepository);
//        ReflectionTestUtils.setField(mockUserService, "userWalletRepository", mockUserWalletRepository);
//        ReflectionTestUtils.setField(mockUserService, "passwordEncoder", mockPasswordEncoder);
//    }
//
//    @Test
//    void createUser() {
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
//                .id(1L)
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
//        given(mockPasswordEncoder.encode(anyString())).willReturn(mockPasswordEncoder.encode("passwd"));
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
//        assertEquals(expectedUser, resultUser); // Simplified assertion, adjust as needed.
//    }
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