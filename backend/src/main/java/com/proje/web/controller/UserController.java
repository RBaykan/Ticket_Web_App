package com.proje.web.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proje.security.exceptions.CustomJWTException;
import com.proje.security.exceptions.UserAlReady;
import com.proje.security.service.JwtUtils;
import com.proje.web.dto.*;
import com.proje.web.model.Message;
import com.proje.web.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.proje.web.service.UserService;

import jakarta.validation.Valid;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/api/user")
public class UserController {


	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;


	public UserController(UserService service, AuthenticationManager authenticationManager, JwtUtils jwtUtils){

		this.userService = service;
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;


	}



	@GetMapping()
	@Operation(
			summary = "Kayıtlı kullanıcıları getir",
			description = "Veritabanındaki tüm kullanıcıları getirme olanağı tanır. Admin yetkisi gerektirir"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Kullanıcılar getirildi",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = UserDTO.class, type = "array")
					)
			),
			@ApiResponse(responseCode = "401", description = "Kimlik doğrulanmadı", content = @Content),
			@ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
			@ApiResponse(responseCode = "500", description = "Sunucu hatası", content = @Content)
	})
	public List<UserDTO> allUser() {
		return userService.getAllUser();
	}


	@PostMapping("/register")
	@Operation(
			summary = "Kullanıcıyı kaydet",
			description = "Veri tabanına kullanıcıyı kaydeder"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Kullanıcı oluşturuldu",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = UserDTO.class)
					)

			),
			@ApiResponse(responseCode = "400", description = "Kullanıcı bilgisi girilmedi/eksik.", content = @Content),
			@ApiResponse(responseCode = "500", description = "Sunucu hatası", content = @Content)
	})
	public ResponseEntity<?> register(@RequestBody @Valid  CreateUser user) {

		if (user == null) {
			return ResponseEntity.badRequest().body("User info is null");
		}
			UserDTO userDTO = userService.registerNewUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);

	}


	@PostMapping("/login")
	@Operation(
			summary = "Token almak için giriş yap",
			description = "Kullanıcı giriş yaparak JWT token alır ve bu token ile birçok API'ye erişim hakkı olur"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Giriş yapıldı ve token verildi",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = String.class)
					)
			),
			@ApiResponse(responseCode = "400", description = "Kullanıcı bilgisi girilmedi/eksik.", content = @Content),
			@ApiResponse(responseCode = "500", description = "Sunucu hatası", content = @Content)
	})
	public ResponseEntity<?> authenticateUser(@RequestBody(required = false) LoginUser loginUser){

		if (loginUser == null) {
			return ResponseEntity.badRequest().body("User info is null");
		}

		Authentication authentication;
		{
			try {
				authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword())
				);
			} catch (AuthenticationException e) {
				Map<String, Object> map = new HashMap<>();
				map.put("message", "Bad Creditials");
				map.put("status", false);
				return  new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
			}
		}


		UserDetails details = (UserDetails)  authentication.getPrincipal();

		String jwtToken = jwtUtils.generateTokenFromUsername(details);


		return  ResponseEntity.status(HttpStatus.CREATED).body(jwtToken);
	}


	@GetMapping("/loginJWT")
	@Operation(
			summary = "JWT Token ile kullanıcı bilgilerine eriş",
			description = "JWT Token ile kullanıcı bilgilerine erişilir"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Kullanıcı bilgileri verildi",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = UserDTO.class)
					)
			),
			@ApiResponse(responseCode = "401", description = "Yetki verilmedi", content = @Content),
			@ApiResponse(responseCode = "500", description = "Sunucu hatası", content = @Content)
	})
	public ResponseEntity<?> loginJWT(HttpServletRequest request){

		String jwt = jwtUtils.getJwtFromHeader(request);



		try {
			boolean isValidate = jwtUtils.validateToken(jwt);
			if(!isValidate){
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
			}
		} catch (CustomJWTException e){
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		}

		String username = jwtUtils.getUsernameFromToken(jwt);
		UserDTO user = userService.getUserWithName(username);
		return ResponseEntity.ok(user);


	}



	@PostMapping("/createTicket")
	@Operation(
			summary = "Veri tabanına bilet kaydet",
			description = "JWT Token ile authenticate olduktan sonra, CreateTicket nesnesindeki bilgilerle bilet oluşturulur"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "Bilet Oluşturuldu",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = CreateTicket.class)
					)
			),
			@ApiResponse(responseCode = "400", description = "Eksik/Girilmemiş CreateTicket bilgisi", content = @Content),
			@ApiResponse(responseCode = "401", description = "Yetki verilmedi", content = @Content),
			@ApiResponse(responseCode = "500", description = "Sunucu hatası", content = @Content)
	})
	public ResponseEntity<?> createTick(@RequestBody CreateTicket createTicket) {



		TicketDTO ticket = userService.createTicket(createTicket);
		return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
	}


	@GetMapping("/tickets")
	@Operation(
			summary = "Veri tabanındaki biletleri getir",
			description = "JWT Token ile authenticate olduktan sonra, rolü Admin olan kullanıcılar, biletlere erişir"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Bilet bilgileri verildi",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = TicketDTO.class , type = "array")
					)
			),
			@ApiResponse(responseCode = "401", description = "Yetki verilmedi", content = @Content),
			@ApiResponse(responseCode = "500", description = "Sunucu hatası", content = @Content)
	})
	public ResponseEntity<?> getTickets() {


		List<TicketDTO> tickets = userService.allTickets();

		return ResponseEntity.status(HttpStatus.OK).body(tickets);
	}


	@PutMapping("/sendMessage")
	@Operation(
			summary = "Bilete yanıt vermek",
			description = "JWT Token ile authenticate olduktan sonra, SendMessage nesnesindeki bilgilerle, bilete yanıt verilir." +
					"USER rolü olan kullanıcılar, biletleri kapalı değilse, kendi biletlerine yanıt vermeye devam edebilir." +
					"ADMIN rolündeki kullanıcılar, her türlü bilete yanıt verebilir."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Bilete yanıt verildi",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = MessageDTO.class)
					)
			),
			@ApiResponse(responseCode = "400", description = "Eksik/Girilmemiş SendMessage bilgisi", content = @Content),
			@ApiResponse(responseCode = "401", description = "Yetki verilmedi", content = @Content),
			@ApiResponse(responseCode = "500", description = "Sunucu hatası", content = @Content)
	})
	public ResponseEntity<?> sendMessage(@RequestBody SendMessage message) {


		MessageDTO messageDTO = userService.sendMessage(message);

		return ResponseEntity.ok(messageDTO);
	}


	@PutMapping("/closeTicket")
	@Operation(
			summary = "Veri tabanındaki biletlerin yanıtlanmasını durdur.",
			description = "JWT Token ile authenticate olduktan sonra, rolü Admin olan kullanıcılar, biletleri kapatabilir"
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Bilet kapatıldı",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = Boolean.class)
					)
			),
			@ApiResponse(responseCode = "401", description = "Yetki verilmedi", content = @Content),
			@ApiResponse(responseCode = "500", description = "Sunucu hatası", content = @Content)
	})
	public ResponseEntity<?> closeTicket(@RequestBody CloseTicket closeTicket) {



		return ResponseEntity.ok(userService.closeTicket(closeTicket));
	}





}
