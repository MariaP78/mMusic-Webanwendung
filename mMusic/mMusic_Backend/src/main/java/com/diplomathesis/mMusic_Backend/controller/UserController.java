package com.diplomathesis.mMusic_Backend.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import com.diplomathesis.mMusic_Backend.model.FullUser;
import com.diplomathesis.mMusic_Backend.model.User;
import com.diplomathesis.mMusic_Backend.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
@RequestMapping("/mMusic-api/users")
@Tag(name = "Users", description = "CRUD Operations for Users")
public class UserController {

	// Initialize User Controller
	public UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// Get User By Document Id
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Get User By its Document Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User successfully found", content = @Content),
			@ApiResponse(responseCode = "404", description = "User NOT successfully found", content = @Content) })
	@GetMapping("/getUser")
	public User getUser(@RequestParam String userDocumentId) throws InterruptedException, ExecutionException {
		return userService.getUserByUserDocumentId(userDocumentId);
	}

	// Get All Users
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Get All Users")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of users successfully found", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))) }),
			@ApiResponse(responseCode = "404", description = "List of users NOT successfully found", content = @Content) })
	@GetMapping("/getAllUsers")
	public List<User> getAllUsers() throws InterruptedException, ExecutionException {
		return userService.getUsers();
	}

	// Add User
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Add User")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User added successfully", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FullUser.class))) }),
			@ApiResponse(responseCode = "404", description = "User NOT added successfully", content = @Content) })
	@PostMapping("/addUser")
	public FullUser addUser(@RequestBody FullUser user)
			throws InterruptedException, ExecutionException {
		return userService.addNewUser(user);
	}


	// Update User
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Update User")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User updated successfully", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FullUser.class))) }),
			@ApiResponse(responseCode = "404", description = "User NOT updated successfully", content = @Content) })
	@PostMapping("/updateUser")
	public FullUser updateUser(@RequestPart(value = "email") String email, @RequestPart(value = "birthday") String birthday, @RequestPart(value = "firstname") String firstname, @RequestPart(value = "lastname") String lastname, @RequestPart(value = "phone") String phone, @RequestPart(value = "stageName") String stageName)
			throws InterruptedException, ExecutionException {
		return userService.updateUserDetails(email, birthday, firstname, lastname, phone, stageName);
	}

	// Delete User
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Delete User by its Document Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User deleted successfully", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))) }),
			@ApiResponse(responseCode = "404", description = "User NOT deleted successfully", content = @Content) })
	@DeleteMapping("/deleteUser")
	public String deleteUser(@RequestParam String userDocumentId) throws InterruptedException, ExecutionException {
		return userService.deleteUserByUserDocumentId(userDocumentId);
	}

}
