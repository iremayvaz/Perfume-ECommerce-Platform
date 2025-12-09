package com.iremayvaz.model.userDetails;

import com.iremayvaz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // AppUserDetailsService bean'i olarak kaydedilecek. Ve her AppUserDetailsService @AutoWired edildiğinde bu enjekte edilecek!
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    // AppUserDetailsService : Spring Security'nin kullanıcı yükleme interface'i

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + email));

        return new AppUserDetails(user);
    }
}
