package com.proje.web.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUser{

	
	 private String firstname;
	 private String lastname;
	 @NotBlank private String username;
	 @NotBlank private String password;
	 @NotBlank private String repassword ;

}
