package com.server.application.service;

import com.server.application.dto.RoleDTO;
import com.server.application.dto.UserDTO;
import com.server.application.dto.UserInsertDTO;
import com.server.application.dto.UserUpdateDTO;
import com.server.application.email.EmailService;
import com.server.application.model.Role;
import com.server.application.model.User;
import com.server.application.repo.RoleRepository;
import com.server.application.repo.UserRepository;
import com.server.application.service.exceptions.DatabaseException;
import com.server.application.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private VerificationTokenService verificationTokenService;

	@Autowired
	private EmailService emailService;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new UserDTO(entity);
	}

	@Transactional
	public String insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		if(entity.getRoles().isEmpty()) {
			entity.setRoles(roleRepository.findByAuthority("ROLE_CLIENT"));
		}
		String token = UUID.randomUUID().toString();
		verificationTokenService.createVerificationToken(entity, token);

		String link = "http://localhost:8080/management/confirmUser?token=" + token;

		String email = emailService.send(entity, "Confirm your registration", "Click the link to confirm your account " + link);

		return email;
	}

	@Transactional
	public UserDTO update(Long id, UserDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}		
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		
		entity.getRoles().clear();
		for (RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getReferenceById(roleDto.getId());
			entity.getRoles().add(role);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

		User user = repository.findByEmail(s).get();
		if(user == null){
			throw  new UsernameNotFoundException("Email not found");
		}
		return user;
	}

	public String passwordRecoveryCode(String email){
		User user = repository.findByEmail(email).get();
		user.setRecoveryCode(getPasswordRecoveryCode(user.getId()));
		user.setDateRecoveryCode(new Date());
		repository.save(user);

		emailService.send(user, "Password recovery code", "Here is your code \n" + user.getRecoveryCode());

		return "Código Enviado";
	}

	public String getPasswordRecoveryCode(Long id){
		DateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
		return format.format(new Date()) + id;
	}

	public String alterPassword(UserUpdateDTO user){
		User entity = repository.findByEmail(user.getEmail()).get();
		if(entity != null){
			Date differenceDate = new Date(new Date().getTime() - entity.getDateRecoveryCode().getTime());
			if(differenceDate.getTime()/1000 < 900 && entity.getRecoveryCode() != null){
				entity.setPassword(passwordEncoder.encode(user.getPassword()));
				entity.setRecoveryCode(null);
				repository.save(entity);
				return "Senha alterada com sucesso";
			}else{
				return "Tempo expirado ou código ínvalido";
			}
		}else{
			return "Email não encontrado";
		}
	}

}
