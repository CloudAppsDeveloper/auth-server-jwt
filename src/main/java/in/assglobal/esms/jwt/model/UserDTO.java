package in.assglobal.esms.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDTO {
	private String id;
	private String email;
	private String password;
	private String role;
	private String username;

	
}