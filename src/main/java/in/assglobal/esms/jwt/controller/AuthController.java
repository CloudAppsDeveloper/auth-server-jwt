package in.assglobal.esms.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.assglobal.esms.jwt.model.DAOUser;
import in.assglobal.esms.jwt.model.JwtRequest;
import in.assglobal.esms.jwt.model.JwtResponse;
import in.assglobal.esms.jwt.model.UserDTO;
import in.assglobal.esms.jwt.service.JwtUserDetailsService;
import in.assglobal.esms.jwt.util.JwtTokenUtil;

@RestController
@CrossOrigin
public class AuthController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		System.out.println("JwtRequest details :: " + authenticationRequest);

		DAOUser userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		System.out.println("userDetails :: " + userDetails);

		if (authenticate(userDetails, authenticationRequest)) {

			UserDTO user = new UserDTO();
			user.setUsername(userDetails.getUsername());
			user.setPassword(userDetails.getPassword());

			final String token = jwtTokenUtil.generateToken(user, "ACCESS");
			final String refreshToken = jwtTokenUtil.generateToken(user, "REFRESH");
			System.out.println("Jwt Token: " + token);

			return ResponseEntity.ok(new JwtResponse(token, refreshToken));
		} else {
			return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}

	private boolean authenticate(DAOUser userDetails, JwtRequest request) throws Exception {
		try {

			// String encryptedDbPwd = BCrypt.hashpw(userDetails.getPassword(),
			// BCrypt.gensalt());
			// String encryptedRequestPwd = BCrypt.hashpw(request.getPassword(),
			// BCrypt.gensalt());

			String encryptedDbPwd = userDetails.getPassword();
			String encryptedRequestPwd = request.getPassword();

			System.out.println("encryptedRequestPwd:" + encryptedRequestPwd);
			System.out.println("encryptedDbPwd:" + encryptedDbPwd);

			if (encryptedDbPwd.equals(encryptedRequestPwd)) {
				return true;
			}
			return false;

		} catch (Exception e) {
			throw new Exception("USER_DISABLED", e);
		} 
	}
}