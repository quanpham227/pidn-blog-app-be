package com.pivinadanang.blog.controller;

import com.pivinadanang.blog.components.LocalizationUtils;
import com.pivinadanang.blog.dtos.AboutDTO;
import com.pivinadanang.blog.responses.ResponseObject;
import com.pivinadanang.blog.responses.about.AboutResponse;
import com.pivinadanang.blog.services.about.IAboutService;
import com.pivinadanang.blog.ultils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/about")
@Validated
@RequiredArgsConstructor
public class AboutController {
    private final IAboutService aboutService;


    // Lấy thông tin trang About
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAbout() throws Exception {
        AboutResponse aboutResponse = aboutService.getAbout();
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .data(aboutResponse)
                .message("Get about information successfully")
                .build());

    }

    // Cập nhật thông tin trang About
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> updateAbout(
            @PathVariable Long id,
            @Valid @RequestBody AboutDTO aboutDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .message(errorMessages.toString())
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .build());
        }

        AboutResponse aboutResponse = aboutService.updateAbout(id, aboutDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .data(aboutResponse)
                .message("Update about information successfully")
                .build());
    }
}