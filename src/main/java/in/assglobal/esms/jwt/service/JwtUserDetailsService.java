package in.assglobal.esms.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.assglobal.esms.jwt.dao.UserDao;
import in.assglobal.esms.jwt.model.DAOUser;
import in.assglobal.esms.jwt.model.UserDTO;

@Service
public class JwtUserDetailsService {
	
	@Autowired
	private UserDao userDao;

//	@Autowired
//	PasswordEncoder bcryptEncoder;

	
	public DAOUser loadUserByUsername(String username) throws Exception {
		DAOUser user = userDao.findByUsername(username);
		if (user == null) {
			throw new Exception("User not found with username: " + username);
		}
		return user;
	}
	
	public DAOUser save(UserDTO user) {
		DAOUser newUser = new DAOUser();
		newUser.setUsername(user.getUsername());
		//newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setPassword(user.getPassword());
		newUser.setEmail(user.getEmail());
		newUser.setRole(user.getRole());
		return userDao.save(newUser);
	}
}