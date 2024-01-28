package BooksVille.services.implementation;

import BooksVille.entities.enums.Roles;
import BooksVille.entities.model.UserEntity;
import BooksVille.infrastructure.events.publisher.EventPublisher;
import BooksVille.infrastructure.exceptions.ApplicationException;
import BooksVille.infrastructure.security.JWTGenerator;
import BooksVille.payload.request.authRequest.ForgotPasswordResetRequest;
import BooksVille.payload.request.authRequest.LoginRequest;
import BooksVille.payload.request.authRequest.UserSignUpRequest;
import BooksVille.payload.response.ApiResponse;
import BooksVille.payload.response.authResponse.JwtAuthResponse;
import BooksVille.payload.response.authResponse.UserSignUpResponse;
import BooksVille.repositories.UserEntityRepository;
import BooksVille.services.AuthService;
import BooksVille.utils.SecurityConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final ModelMapper modelMapper;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher publisher;
    private final HttpServletRequest request;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;

    @Override
    public ResponseEntity<ApiResponse<UserSignUpResponse>> registerUser(UserSignUpRequest userSignUpRequest) {
        // Checks if a user's email is already in the database
        boolean isPresent = userEntityRepository.existsByEmail(userSignUpRequest.getEmail());

        // Throws and error if the email already exists
        if (isPresent) {
            throw new ApplicationException("User with this e-mail already exist");
        }

        // Maps the UserSignUpRequest dto to a User entity, so it can be saved
        UserEntity newUser = modelMapper.map(userSignUpRequest, UserEntity.class);

        // Assigning the role and isVerified gotten to the newUser to be saved to the database
        newUser.setRoles(Roles.USER);

        // Encrypt the password using Bcrypt password encoder
        newUser.setPassword(passwordEncoder.encode(userSignUpRequest.getPassword()));

        // Save the user to the database
        UserEntity savedUser = userEntityRepository.save(newUser);

        // Publish and event to verify Email
        publisher.completeRegistrationEventPublisher(savedUser.getEmail(), savedUser.getFirstName(), request);

        UserSignUpResponse signupResponse = modelMapper.map(savedUser, UserSignUpResponse.class);

        // Return a ResponseEntity of a success message
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>("Account created successfully", signupResponse)
        );
    }

    @Override
    public ResponseEntity<ApiResponse<UserSignUpResponse>> registerAdmin(UserSignUpRequest userSignUpRequest) {
        // Checks if a user's email is already in the database
        boolean isPresent = userEntityRepository.existsByEmail(userSignUpRequest.getEmail());

        // Throws and error if the email already exists
        if (isPresent) {
            throw new ApplicationException("User with this e-mail already exist");
        }

        // Maps the UserSignUpRequest dto to a User entity, so it can be saved
        UserEntity newAdmin = modelMapper.map(userSignUpRequest, UserEntity.class);

        // Assigning the role and isVerified gotten to the newAdmin to be saved to the database
        newAdmin.setRoles(Roles.ADMIN);

        // Encrypt the password using Bcrypt password encoder
        newAdmin.setPassword(passwordEncoder.encode(userSignUpRequest.getPassword()));

        // Save the user to the database
        UserEntity savedAdmin = userEntityRepository.save(newAdmin);

        // Publish and event to verify Email
        publisher.completeRegistrationEventPublisher(savedAdmin.getEmail(), savedAdmin.getFirstName(), request);

        UserSignUpResponse signupResponse = modelMapper.map(savedAdmin, UserSignUpResponse.class);

        // Return a ResponseEntity of a success message
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>("Account created successfully", signupResponse)
        );

    }

    @Override
    public ResponseEntity<ApiResponse<String>> forgotPassword(String email) {
        if (!userEntityRepository.existsByEmail(email)) {
            throw new ApplicationException("Invalid email provided, please check and try again.");
        }

        publisher.forgotPasswordEventPublisher(email, request);

        return ResponseEntity.ok(new ApiResponse<>("A link has been sent to your email to reset your password"));
    }


    public ResponseEntity<ApiResponse<String>> resetForgotPassword(ForgotPasswordResetRequest forgotPasswordResetRequest) {
        if (!jwtGenerator.validateToken(forgotPasswordResetRequest.getToken())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Token expired, please request for a new one"));
        }

        String email = jwtGenerator.getEmailFromJWT(forgotPasswordResetRequest.getToken());

        Optional<UserEntity> userOptional = userEntityRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            user.setPassword(passwordEncoder.encode(forgotPasswordResetRequest.getNewPassword()));

            userEntityRepository.save(user);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>("Password Changed Successfully"));
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>("Invalid"));
    }

    @Override
    public ResponseEntity<ApiResponse<JwtAuthResponse>> login(LoginRequest loginRequest) {

        // Authentication manager to authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        Optional<UserEntity> userEntityOptional = userEntityRepository.findByEmail(loginRequest.getEmail());


        if (userEntityOptional.isPresent() && !userEntityOptional.get().isVerified()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>("notVerified")
            );
        }

        // Saving authentication in security context so user won't have to login everytime the network is called
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        // Generate jwt token
        String token = jwtGenerator.generateToken(authentication, SecurityConstants.JWT_EXPIRATION);

        // Generate jwt refresh token
        String refreshToken = jwtGenerator.generateToken(authentication, SecurityConstants.JWT_REFRESH_TOKEN_EXPIRATION);

        UserEntity userEntity = userEntityOptional.get();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                "Login Successful",
                                JwtAuthResponse.builder()
                                        .accessToken(token)
                                        .refreshToken(refreshToken)
                                        .tokenType("Bearer")
                                        .id(userEntity.getId())
                                        .profilePicture(userEntity.getProfilePicture())
                                        .email(userEntity.getEmail())
                                        .phoneNumber(userEntity.getPhoneNumber())
                                        .firstName(userEntity.getFirstName())
                                        .lastName(userEntity.getLastName())
                                        .role(userEntity.getRoles())
                                        .build()
                        )
                );
        }

    @Override
    public ResponseEntity<ApiResponse<JwtAuthResponse>> adminLogin(LoginRequest loginRequest) {
        Optional<UserEntity> userEntityOptional = userEntityRepository.findByEmail(loginRequest.getEmail());

        if (userEntityOptional.isPresent()) {
            Roles role = userEntityOptional.get().getRoles();

            if (role != Roles.ADMIN) {
                throw new ApplicationException("Unauthorized, Not an Admin");
            }
        }

        // Authentication manager to authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );


        if (userEntityOptional.isPresent() && !userEntityOptional.get().isVerified()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse<>("notVerified")
            );
        }

        // Saving authentication in security context so user won't have to login everytime the network is called
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        // Generate jwt token
        String token = jwtGenerator.generateToken(authentication, SecurityConstants.JWT_EXPIRATION);

        // Generate jwt refresh token
        String refreshToken = jwtGenerator.generateToken(authentication, SecurityConstants.JWT_REFRESH_TOKEN_EXPIRATION);

        UserEntity userEntity = userEntityOptional.get();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                "Login Successful",
                                JwtAuthResponse.builder()
                                        .accessToken(token)
                                        .refreshToken(refreshToken)
                                        .tokenType("Bearer")
                                        .id(userEntity.getId())
                                        .email(userEntity.getEmail())
                                        .phoneNumber(userEntity.getPhoneNumber())
                                        .firstName(userEntity.getFirstName())
                                        .lastName(userEntity.getLastName())
                                        .role(userEntity.getRoles())
                                        .build()
                        )
                );
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public ResponseEntity<ApiResponse<String>> verifyToken(String receivedToken) {
        if(!jwtGenerator.validateToken(receivedToken)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("Token expired, do request for new token", "expired"));
        }

        String email = jwtGenerator.getEmailFromJWT(receivedToken);

        Optional<UserEntity> userEntityOptional = userEntityRepository.findByEmail(email);

        if (userEntityOptional.isPresent()){

            UserEntity userEntity = userEntityOptional.get();

            if (userEntity.isVerified()){
                return ResponseEntity
                        .status(HttpStatus.ALREADY_REPORTED)
                        .body(new ApiResponse<>("This account has been verified, do proceed to  login", "account verified"));
            }

            userEntity.setVerified(true);
            userEntityRepository.save(userEntity);

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(new ApiResponse<>("Verification Successful", "valid"));
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>("Invalid Token", "invalid"));
    }
}
