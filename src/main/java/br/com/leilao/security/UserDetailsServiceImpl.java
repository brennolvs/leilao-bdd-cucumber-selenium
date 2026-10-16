package br.com.leilao.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.leilao.model.Usuario;
import br.com.leilao.repositories.UsuarioRepository;

public class UserDetailsServiceImpl implements UserDetailsService {
	 
    @Autowired
    private UsuarioRepository userRepository;
     
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Usuario user = userRepository.getUserByUsername(username);
         
        if (user == null) {
            throw new UsernameNotFoundException("Usuario nao encontrado");
        }
         
        return new LeilaoUserDetails(user);
    }
 
}
