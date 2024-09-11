package com.backend.liaison_system.users.lecturer;

import com.backend.liaison_system.dao.Response;
import com.backend.liaison_system.enums.UserRoles;
import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.users.lecturer.dto.NewLecturerRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LecturerServiceImplTest {

    private AutoCloseable autoCloseable;

    @InjectMocks
    LecturerServiceImpl lecturerService;

    @Mock
    LecturerRepository lecturerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception{
        if (autoCloseable != null) {
            autoCloseable.close();
        }
        Mockito.reset(lecturerRepository);
    }

    // create a dummy lecturer data
    private Lecturer lecturer() {
        Lecturer lecturer = new Lecturer();

        lecturer.setId(UUID.randomUUID().toString());
        lecturer.setCreatedAt(LocalDateTime.now());
        lecturer.setUpdatedAt(LocalDateTime.now());

        lecturer.setFirstName("Lecturer");
        lecturer.setOtherName("Other");
        lecturer.setLastName("New");
        lecturer.setEmail("lecturer@email.com");
        lecturer.setProfile("example-profile-image.com");

        lecturer.setPhone("+233-55-159-963");
        lecturer.setCompany("Lecturer's Company");
        lecturer.setPassword("password");

        lecturer.setRole(UserRoles.LECTURER);
        return lecturer;
    }

    // create a dummy lecturer request data
    private NewLecturerRequest request() {
        NewLecturerRequest request = new NewLecturerRequest();

        request.setFirstName("Lecturer");
        request.setOtherName("Other");
        request.setLastName("New");
        request.setEmail("request@email.com");
        request.setProfile("example-profile-image.com");

        request.setPhone("+233-55-159-963");
        request.setCompany("Lecturer's Company");
        request.setPassword("password");

        request.setRole(UserRoles.LECTURER);
        return request;
    }


    @Test
    void createNewLecturer() {

        Lecturer lecturer = lecturer();

        List<Lecturer> lecturers = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Lecturer lec = lecturer();

            lec.setEmail("request" + i + "@email.com");
            lecturers.add(lec);
        }


        List<NewLecturerRequest> requests = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            NewLecturerRequest req = request();
            req.setEmail("request" + i + "@email.com");
            requests.add(req);
        }

        // mock the save behaviour:
        when(lecturerRepository.saveAll(any(List.class))).thenReturn(lecturers);
        when(lecturerRepository.findByEmail("anyexisting@email.com")).thenReturn(Optional.of(lecturer));

        // operation and assertions:
        ResponseEntity<Response<List<Lecturer>>> response = lecturerService.createNewLecturer(requests);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().getData().size());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void ifAnEmailAlreadyExist_throw_EMAIL_ALREADY_EXISTS_exception() {
        Lecturer lecturer = lecturer();

        List<Lecturer> lecturers = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Lecturer lec = lecturer();

            lec.setEmail("request" + i + "@email.com");
            lecturers.add(lec);
        }


        List<NewLecturerRequest> requests = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            NewLecturerRequest req = request();
            req.setEmail("request" + i + "@email.com");
            requests.add(req);
        }

        // mock the save behaviour:
        when(lecturerRepository.saveAll(any(List.class))).thenReturn(lecturers);
        when(lecturerRepository.findByEmail(requests.iterator().next().getEmail())).thenReturn(Optional.of(lecturer));


        // operations:
        LiaisonException exception = assertThrows(LiaisonException.class, () -> lecturerService.createNewLecturer(requests));

        assertEquals(Error.EMAIL_ALREADY_EXISTS.label, exception.getMessage());
    }
}