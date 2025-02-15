package com.example.skillsync.controller;

import com.example.skillsync.model.Image;
import com.example.skillsync.model.User;
import com.example.skillsync.repo.ImageRepository;
import com.example.skillsync.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// https://www.baeldung.com/java-db-storing-files
@RestController
public class ImageController {

    ImageRepository imageRepository;
    UserService userService; // Needed to assign images the user ID

    @Autowired
    public ImageController(ImageRepository imageRepository, UserService userService) {
        this.imageRepository = imageRepository; // Needed for findBy
        this.userService = userService;
    }

    @PostMapping("/upload")
    @Transactional
    ResponseEntity<Void> uploadImage (@RequestParam MultipartFile image) throws Exception {
        User user = userService.getLoggedInUser(); // Getting user that's uploading image
        Image image_db = new Image();
        String imageName = String.valueOf(user.getId());
        if (imageRepository.findByName(imageName) != null) { // Delete old profile pictures if they exist - each user can only have one profile picture
            imageRepository.deleteAllByName(imageName);
        }
        image_db.setName(imageName);
        image_db.setContent(image.getBytes()); // Actual image file content
        imageRepository.save(image_db); // User images are saved at /images/x where x is the user's ID
        return ResponseEntity.status(HttpStatus.FOUND).header("Location","/settings").build(); // Response entity so I can redirect from a rest controller
    }

    @GetMapping("/images/{image}") // To be used for profile picture
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable String image) {
        Image image_db = imageRepository.findByName(image);
        if (image_db == null) {
            System.out.println("Image not found"); // Debug info
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok() // Converts to PNG for display. Tested to work with JPEG
                .contentType(MediaType.parseMediaType(MediaType.IMAGE_PNG_VALUE)).body(new ByteArrayResource(image_db.getContent()));
    }
}
