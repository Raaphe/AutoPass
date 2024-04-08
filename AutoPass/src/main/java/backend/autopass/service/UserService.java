package backend.autopass.service;

import backend.autopass.model.entities.Pass;
import backend.autopass.model.entities.User;
import backend.autopass.model.entities.UserWallet;
import backend.autopass.model.enums.Role;
import backend.autopass.model.repositories.PassRepository;
import backend.autopass.model.repositories.UserRepository;
import backend.autopass.model.repositories.UserWalletRepository;
import backend.autopass.payload.dto.*;
import backend.autopass.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * UserService - 2024-03-30
 * Raph, Moncef
 * User service method implementation.
 * AutoPass
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PassRepository passRepository;
    private final UserWalletRepository walletRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Value("${AWS.bucket_name}")
    private String bucketName;

    @Value("${AWS.access_key}")
    private String awsAccessKey;

    @Value("${AWS.secret_key}")
    private String awsSecretKey;

    @Override
    public User createUser(SignUpDTO signUpDTO) {

        try {
            if (userRepository.existsByEmail(signUpDTO.getEmail())) {
                throw new Exception("Email already in use");
            }

            User user = this.buildUser(signUpDTO);
            user.setRole(Role.USER);


            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User createAdmin(SignUpDTO signUpDTO) {

        try {
            if (userRepository.existsByEmail(signUpDTO.getEmail())) {
                throw new Exception("Email already in use");
            }

        User user = this.buildUser(signUpDTO);
        user.setRole(Role.ADMIN);

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteUser(Long userId) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("User not found");
        }
        User user = userOptional.get();
        if (!user.isDeleted()) {
            user.setDeleted(true);
            userRepository.save(user);
        } else {
            throw new Exception("User is already marked as deleted");
        }
    }

    public boolean updateUser(UpdateUserDTO updateDto){
        Optional<User> userOptional = userRepository.findByEmail(updateDto.getEmail());
        try {
            if (userOptional.isEmpty()) {
                return false;
            } else {
                User user = userOptional.get();
                user.setEmail(updateDto.getEmail());
                user.setFirstName(updateDto.getFirstName());
                user.setLastName(updateDto.getLastName());
                user.setPassword(updateDto.getPassword());
                userRepository.save(user);
                return true;
            }
        }catch (Exception userMess){
            return false;
        }
    }

    @Override
    public User getUserById(Long userId) {
        Optional<User> user = userRepository.getUserById(Math.toIntExact(userId));
        return user.orElse(null);
    }

    @Override
    public Boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Boolean changePassword(ChangePasswordDTO dto) {
        String email = jwtService.extractUserName(dto.getToken());

        if (this.userExists(email)) {
            Optional<User> user = this.userRepository.findByEmail(email);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (user.isPresent() && jwtService.isTokenValid(dto.getToken(), userDetails)) {
                User concreteUser = user.get();
                concreteUser.setPassword(passwordEncoder.encode(dto.getPassword()));
                concreteUser = userRepository.save(concreteUser);
                return passwordEncoder.matches(dto.getPassword(), concreteUser.getPassword());
            }
        }
        return false;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public String saveImageToUser(ChangeImageDTO dto) throws AwsServiceException, SdkClientException {
        String fileName = "pfp_" + System.currentTimeMillis();

        try {
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);

            S3Client client = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .region(Region.US_EAST_2)
                    .build();

            PutObjectRequest request = PutObjectRequest
                    .builder()
                    .bucket(this.bucketName)
                    .key(fileName)
                    .acl("public-read")
                    .build();

            client.putObject(request,
                    RequestBody.fromInputStream(dto.getImage().getInputStream(), dto.getImage().getInputStream().available()));
        } catch (AwsServiceException | SdkClientException | IOException e) {
            throw new RuntimeException(e);
        }

        String imageUrl =  "https://photosforraph.s3.us-east-2.amazonaws.com/" + fileName;
        Optional<User> user = userRepository.getUserById(dto.getUserId());
        if (user.isPresent()) {
            User concreteUser = user.get();
            concreteUser.setProfileImageUrl(imageUrl);
            concreteUser.setIsProfileImageChanged(true);
            this.userRepository.save(concreteUser);
        }
        return imageUrl;
    }

    @Override
    public String getImageFromUser(int userId) {
        String url = userRepository.findProfileImageUrlById(userId);
        return url.isBlank() ? "" : url;
    }

    @Override
    public User saveBasicUserInfo(BasicUserInfoDTO info) {
        assert info.getFirstName() != null && info.getFirstName().length() <= 30;
        assert info.getLastName() != null && info.getLastName().length() <= 30;

        Optional<User> user = userRepository.getUserById(info.getId());
        if (user.isPresent()) {
            User userConcrete = user.get();
            userConcrete.setFirstName(info.getFirstName());
            userConcrete.setLastName(info.getLastName());

            if (!Objects.equals(info.getEmail(), "") && !userRepository.existsByEmail(info.getEmail())) {
                userConcrete.setEmail(info.getEmail());
            }


            return userRepository.save(userConcrete);
        } else {
            return new User();
        }
    }


    private User buildUser(SignUpDTO signUpDTO) {

        Pass pass = passRepository.save(new Pass());
        UserWallet wallet = walletRepository.save(
                UserWallet
                        .builder()
                        .membershipActive(false)
                        .ticketAmount(0)
                        .memberShipEnds(System.currentTimeMillis())
                        .build()
        );

        Optional<User> user = userRepository.findByEmail(signUpDTO.getEmail());
        String imageUrl = "";
        if (user.isEmpty()) {
            imageUrl = "https://photosforraph.s3.us-east-2.amazonaws.com/photos/profileImage.png";
        } else if (user.get().getProfileImageUrl() != null) {
            imageUrl = user.get().getProfileImageUrl();
        }

        User concreteUser = User.builder()
                .email(signUpDTO.getEmail())
                .firstName(signUpDTO.getFirstname())
                .lastName(signUpDTO.getLastname())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .pass(pass)
                .isProfileImageChanged(false)
                .profileImageUrl(imageUrl)
                .wallet(wallet)
                .build();

        return userRepository.save(concreteUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        Role role = user.getRole();
        var authorities = role.getAuthorities();

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
