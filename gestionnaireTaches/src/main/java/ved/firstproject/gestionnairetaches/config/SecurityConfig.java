package ved.firstproject.gestionnairetaches.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    String[] endPoints = {"/api/signup",
            "/api/createtask",
            "/api/tasks",
            "/api/updatetask",
            "/api/completetask/**",
            "/api/completedtasks",
            "/api/tasks/filter",
            "/api/group/create",
            "/api/group/addTask",
            "/api/group/findGroupById",
            "/api/group/addUserToGroup",
            "/api/user/findUserByUsername",
            "/api/group/findGroupByTitle",
            "/api/group/removeUserFromGroup",
            "/api/group/completeTaskFromGroup",
            "/api/group/getTasksOfGroup",
            "/api/group/getGroupsFromUserId",
            "/api/group/removeTaskFromGroup",
            "/api/group/updateTaskForGroup",
            "/api/group/filterByCategoryGroup",
            "/api/group/assignTaskForGrTo",
            "/api/group/completedtasksForGroup",
            "/api/user/findAllUserFromGroup",
            "/api/login"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(endPoints).permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(endPoints);
    }
}
