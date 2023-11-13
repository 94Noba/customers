package sn.optimizer.amigosFullStackCourse.customer.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sn.optimizer.amigosFullStackCourse.customer.repository.CustomerJpaRepository;


@Service
public class CustomerDetailsService implements UserDetailsService {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerDetailsService(CustomerJpaRepository customerJpaRepository){
        this.customerJpaRepository=customerJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return customerJpaRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("Please provide a valid email"));
    }
}
