package com.diplomathesis.mMusic_Backend.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.diplomathesis.mMusic_Backend.model.ApiResponseSong;
import com.diplomathesis.mMusic_Backend.model.FullSong;
import com.diplomathesis.mMusic_Backend.model.Song;
import com.diplomathesis.mMusic_Backend.service.SongService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.protocol.exceptions.TransactionException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
@RequestMapping("/mMusic-api/songs")
@Tag(name = "Songs", description = "CRUD Operations for Songs")
public class SongController {

	// Initialize User Controller
	public SongService songService;

	public SongController(SongService songService) {
		this.songService = songService;
	}

	// Get Song By IPFS hashCode
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Get Song By IPFS hashCode")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Song successfully found", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FullSong.class))) }),
			@ApiResponse(responseCode = "404", description = "Song NOT successfully found", content = @Content) })
	@GetMapping("/getSongByIPFSHash")
	public FullSong getSong(@RequestParam String hashCode) throws InterruptedException, ExecutionException {
		return songService.getSongByIPFSHashCode(hashCode);
	}

	// Get All Songs
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Get All Songs")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of songs successfully found", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Song.class))) }),
			@ApiResponse(responseCode = "404", description = "List of songs NOT successfully found", content = @Content) })
	@GetMapping("/getAllSongs")
	public List<Song> getAllSongs() throws InterruptedException, ExecutionException {
		return songService.getSongs();
	}

	// Get All Full Songs
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Get All Full Songs")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of songs successfully found", content = @Content),
			@ApiResponse(responseCode = "404", description = "List of songs NOT successfully found", content = @Content) })
	@GetMapping("/getAllFullSongs")
	public List<FullSong> getAllFullSongs() throws InterruptedException, ExecutionException {
		return songService.getFullSongs();
	}

	// UPLOAD SONG ENDPOINT
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Add Song")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Song successfully added", content = @Content),
			@ApiResponse(responseCode = "404", description = "Song NOT successfully added", content = @Content) })
	@PostMapping("/addSong")
	public FullSong addSongToDB(@RequestPart(value = "data") FullSong song, @RequestPart(value = "file") MultipartFile file)
			throws TransactionException, Exception {
		return songService.addSong(song, file);
	}

	// PLAY SONG ENDPOINT
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Play Song")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "URL successfully created", content = @Content),
			@ApiResponse(responseCode = "404", description = "URL NOT successfully created", content = @Content) })
	@GetMapping("/playSong")
	public Map<String, String> playSong(@RequestParam String ipfsHash)
			throws TransactionException, Exception {
		return songService.getSongUrlFromIpfsToPlayIt(ipfsHash);
	}

	// update song details
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Update Song Details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Song details updated successfully", content = @Content),
			@ApiResponse(responseCode = "404", description = "Song details NOT updated successfully", content = @Content) })
	@PostMapping("/updateSongDetails")
	public FullSong updateSong(@RequestPart(value = "ipfsHash") String ipfsHash, @RequestPart(value = "title") String title, @RequestPart(value = "stageName") String stageName, @RequestPart(value = "genre") String genre)
			throws InterruptedException, ExecutionException {
		return songService.updateSongDetails(ipfsHash, title, stageName, genre);
	}

	//add song to playlist
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Add Song to Playlist")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Song successfully added to playlist", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FullSong.class))) }),
			@ApiResponse(responseCode = "404", description = "Song NOT successfully added to playlist", content = @Content) })
	@PostMapping("/addSongToPlaylist")
	public ApiResponseSong addSongToPlaylist(@RequestPart(value = "userEmail") String userEmail, @RequestPart(value = "songHash") String songHash)
			throws Exception {
		return songService.addSongToUserPlaylist(userEmail, songHash);
	}

	//delete song from playlist
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Delete Song From Playlist")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Song successfully deleted from playlist", content = {@Content }),
			@ApiResponse(responseCode = "404", description = "Song NOT successfully deleted from playlist", content = @Content) })
	@PostMapping("/deleteSongFromPlaylist")
	public ApiResponseSong deleteSongPlaylist(@RequestPart(value = "userEmail") String userEmail, @RequestPart(value = "songHash") String songHash)
			throws Exception {
		return songService.deleteSongFromUserPlaylist(userEmail, songHash);
	}


	// Get All Songs From Playlist
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Get All Songs From Playlist")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of songs from playlist successfully found", content = @Content),
			@ApiResponse(responseCode = "404", description = "List of songs from playlist NOT successfully found", content = @Content) })
	@GetMapping("/getSongsFromUserPlaylist")
	public List<FullSong> getAllSongsFromPlaylist(String userEmail) throws InterruptedException, ExecutionException {
		return songService.getAllSongsFromUserPlaylist(userEmail);
	}

	// Get All Songs From Uploads
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Get All Songs From Uploads")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of songs from uploads successfully found", content = @Content),
			@ApiResponse(responseCode = "404", description = "List of songs from uploads NOT successfully found", content = @Content) })
	@GetMapping("/getAllSongsUploadedByUser")
	public List<FullSong> getAllSongsFromUploads(String userEmail) throws InterruptedException, ExecutionException {
		return songService.getAllSongsFromUserUploads(userEmail);
	}

	// Get Recommandations
	@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201", "http://localhost:4202" })
	@Operation(summary = "Get User Recommended Playlist")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of recommended songs successfully found", content = @Content),
			@ApiResponse(responseCode = "404", description = "List of recommended songs NOT successfully found", content = @Content) })
	@GetMapping("/getRecommendations")
	public List<FullSong> getRecommendations(String userEmail) throws InterruptedException, ExecutionException {
		return songService.getRecommendationsPlaylist(userEmail);
	}
}