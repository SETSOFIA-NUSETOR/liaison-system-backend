package com.backend.liaison_system.user_details;

import com.backend.liaison_system.users.lecturer.Lecturer;
import com.backend.liaison_system.users.lecturer.LecturerRepository;
import com.backend.liaison_system.users.liaison.LiaisonOperative;
import com.backend.liaison_system.users.liaison.LiaisonOperativeRepository;
import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LiaisonUserDetailsService implements UserDetailsService {
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final LiaisonOperativeRepository liaisonOperativeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return new LiaisonUserDetails(student.get());
        }

        Optional<Lecturer> lecturer = lecturerRepository.findByEmail(email);
        if (lecturer.isPresent()) {
            return new LiaisonUserDetails(lecturer.get());
        }

        Optional<LiaisonOperative> liaisonOperative = liaisonOperativeRepository.findByEmail(email);
        if (liaisonOperative.isPresent()) {
            return new LiaisonUserDetails(liaisonOperative.get());
        }

        throw new UsernameNotFoundException("user email not found");
    }
}