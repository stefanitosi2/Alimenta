package com.Alimenta.Alimenta.service;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.Alimenta.Alimenta.model.UserLogin;
import com.Alimenta.Alimenta.model.Usuario;
import com.Alimenta.Alimenta.repository.UsuarioRepository;

public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	public List<Usuario> listarUsuarios() {

		return repository.findAll();

	}
	
	public Optional<Usuario> CadastrarUsuario(Usuario usuario) {
		Optional<Usuario> user = repository.findByUsuario(usuario.getUsuario());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (user.isPresent()) {
			return Optional.ofNullable(null);
		}

		String senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);

		return Optional.of(repository.save(usuario));
	}
	
		public Optional<UserLogin> Logar (Optional <UserLogin> user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional <Usuario> usuario = repository.findByUsuario(user.get().getUsuario());
        
        if(usuario.isPresent()) {
            if(encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {
                
                String auth = user.get().getUsuario() + ":" + user.get().getSenha();
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                
                user.get().setToken(authHeader);
                user.get().setNome(usuario.get().getNome());
                
                return user;
            }
        }
        return null;
    }
}
