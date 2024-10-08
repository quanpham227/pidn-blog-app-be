package com.pivinadanang.blog.responses.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class PostListResponse {
    private List<PostResponse> posts;
    private int totalPages;
    private HttpStatus status;
}
