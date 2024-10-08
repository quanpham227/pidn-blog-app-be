package com.pivinadanang.blog.controller;
import com.pivinadanang.blog.components.LocalizationUtils;
import com.pivinadanang.blog.dtos.SlideDTO;
import com.pivinadanang.blog.models.SlideEntity;
import com.pivinadanang.blog.responses.ResponseObject;
import com.pivinadanang.blog.responses.slide.SlideResponse;
import com.pivinadanang.blog.services.slide.ISlideService;
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
@RequestMapping("api/v1/slides")
@Validated
@RequiredArgsConstructor
public class SlideController {
    private final ISlideService slideService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllSlides() {
        List<SlideResponse> slides = slideService.getAllSlides();
        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.GET_SLIDE_SUCCESSFULLY))
                .status(HttpStatus.OK)
                .data(slides)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getSlideById(@PathVariable("id") Long slideId) throws Exception {
        SlideResponse slide = slideService.findById(slideId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(slide)
                .message("Get slide information successfully")
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> insertSlide(@Valid @RequestBody SlideDTO slideDTO, BindingResult result) throws Exception {
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
        if (slideService.existsByTitle(slideDTO.getTitle())) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.SLIDE_ALREADY_EXISTS))
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
        SlideResponse slide = slideService.createSlide(slideDTO);
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.INSERT_SLIDE_SUCCESSFULLY))
                .status(HttpStatus.OK)
                .data(slide)
                .build());
    }

    @PutMapping(value = "{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> updateSlide(@Valid @RequestBody SlideDTO slideDTO,
                                                      @PathVariable Long id,
                                                      BindingResult result) throws Exception {
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
        SlideResponse slide = slideService.updateSlide(id, slideDTO);
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_SLIDE_SUCCESSFULLY, id))
                .status(HttpStatus.OK)
                .data(slide)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> deleteSlide(@PathVariable Long id) throws Exception {
        slideService.deleteSlide(id);
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_SLIDE_SUCCESSFULLY, id))
                .status(HttpStatus.OK)
                .data(null)
                .build());
    }
}
